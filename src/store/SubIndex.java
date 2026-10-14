package store;

/**
 * Custom open-addressed hash map: {@code byte[] key -> long offset}
 * into a {@link ByteArena}.
 *
 * Serves as the hot-tier sub-index — one per live segment. Record
 * bytes never live here, only the lookup entry; the arena owns the
 * payload. Rebuilt per segment on flush, so it never resizes under
 * concurrent readers.
 *
 * Storage is raw bytes in off-heap memory
 * ({@code java.lang.foreign.MemorySegment}): slot metadata and the
 * offset table accessed via {@code ValueLayout} VarHandles — scalar
 * machine-width ops, no boxed entries.
 *
 * Concurrency contract: append-only semantics apply — entries are
 * published (not mutated), so correctness reduces to release/acquire
 * ordering on the slot's occupied marker ({@code setRelease} on write,
 * {@code getAcquire} on read).
 */
public interface SubIndex {

    /**
     * Look up the arena offset for {@code key}.
     *
     * @param key record identity
     * @return offset into the owning {@link ByteArena}, or -1 if absent
     */
    long get(byte[] key);

    /**
     * Insert {@code key -> offset}. Behavior when the key already exists
     * is governed by the {@link DedupMode} the index was constructed with.
     *
     * @param key    record identity
     * @param offset position returned by {@link ByteArena#append}
     */
    void put(byte[] key, long offset);

    /** @return number of live entries */
    int size();
}
