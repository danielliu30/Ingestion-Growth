package store;

/**
 * A pre-allocated, append-only region of memory holding raw record bytes.
 *
 * Implementations back the region with off-heap memory (e.g. a direct
 * {@code ByteBuffer} or a {@code sun.misc.Unsafe} allocation, both
 * available on Java 11) and perform machine-width scalar reads/writes
 * against it — nothing on the Java heap. An arena never resizes: when
 * full it is frozen and swapped out — growth happens by appending a new
 * arena, never by rehash.
 *
 * All addresses are {@code long} offsets into off-heap regions bound at
 * construction; no heap arrays appear in the contract.
 */
public interface ByteArena {

    /**
     * Copy {@code len} bytes from {@code srcOffset} (in the caller's
     * bound source region) to the arena tail and return the offset.
     * Called only by the shard's single writer, so the write cursor is
     * advanced with a plain add — no atomic read-modify-write. After the
     * copy the new watermark is published with {@code setRelease};
     * readers load it with {@code getAcquire} and only read below it.
     *
     * @param srcOffset offset of the source bytes in the bound source region
     * @param len       bytes to append
     * @return offset at which the record begins, or -1 if the arena is full
     */
    long append(long srcOffset, int len);

    /**
     * Copy {@code len} bytes starting at {@code offset} into
     * {@code dstOffset} in the caller's bound destination region.
     *
     * @param offset    position previously returned by {@link #append}
     * @param dstOffset offset in the bound destination region
     * @param len       bytes to copy
     */
    void readAt(long offset, long dstOffset, int len);

    /** @return bytes currently committed to this arena */
    long used();

    /** @return total capacity in bytes, fixed at construction */
    long capacity();

    /**
     * Reset the write cursor to zero, discarding all contents.
     * Only called after the arena has been sorted and flushed — never
     * while readers may hold offsets into it.
     */
    void clear();
}
