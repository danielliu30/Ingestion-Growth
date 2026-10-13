package store;

/**
 * A pre-allocated, append-only region of memory holding raw record bytes.
 *
 * Implementations are expected to live off-heap (direct buffers or
 * Unsafe-allocated memory) so the hot ingest path allocates nothing.
 * An arena never resizes: when full it is frozen and swapped out —
 * growth happens by appending a new arena, never by rehash.
 */
public interface ByteArena {

    /**
     * Append {@code data} at the tail and return its offset.
     * Under multi-writer use, implementations claim space with a single
     * atomic bump so each caller owns a private region — wait-free,
     * no slot contention.
     *
     * @param data record bytes to append
     * @return offset at which the record begins, or -1 if the arena is full
     */
    long append(byte[] data);

    /**
     * Copy {@code len} bytes starting at {@code offset} into {@code dst}.
     *
     * @param offset position previously returned by {@link #append}
     * @param dst    destination buffer
     * @param len    bytes to copy
     */
    void readAt(long offset, byte[] dst, int len);

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
