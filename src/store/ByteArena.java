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
     * Implementations claim space with {@code VarHandle.getAndAdd} on the
     * write cursor — a single LOCK XADD — so each caller owns a private
     * region: wait-free, no slot contention.
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
     * Reset the write cursor to zero so the arena can be refilled.
     * Records are never erased: only called after the arena has been
     * sorted and flushed to a cold run, which retains them — never
     * while readers may hold offsets into it.
     */
    void clear();
}
