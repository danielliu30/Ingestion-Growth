package store;

/**
 * Custom open-addressed hash map: key bytes at an arena offset
 * ({@code keyOffset, keyLen}) {@literal ->} long record offset.
 *
 * Serves as the hot-tier sub-index — one per live segment. Record
 * bytes never live here, only the lookup entry; the arena owns the
 * payload. Rebuilt per segment on flush, so it never resizes under
 * concurrent readers.
 *
 * Storage is raw bytes in off-heap memory
 * ({@code java.lang.foreign.MemorySegment}): slot metadata and the
 * offset table accessed via {@code ValueLayout} VarHandles — scalar
 * machine-width ops, no boxed entries, no heap arrays.
 *
 * Concurrency contract: append-only semantics apply — entries are
 * published (not mutated), so correctness reduces to release/acquire
 * ordering on the slot's occupied marker ({@code setRelease} on write,
 * {@code getAcquire} on read).
 */
public interface SubIndex {

    /**
     * Look up the record offset for the key at {@code keyOffset}.
     *
     * @param keyOffset offset of the key bytes in the bound key region
     * @param keyLen    key length in bytes
     * @return record offset, or -1 if absent
     */
    long get(long keyOffset, int keyLen);

    /**
     * Insert {@code (keyOffset, keyLen) -> offset}. Behavior when the
     * key already exists is governed by the {@link DedupMode} the index
     * was constructed with.
     *
     * @param keyOffset offset of the key bytes in the bound key region
     * @param keyLen    key length in bytes
     * @param offset    record offset previously returned by
     *                  {@link ByteArena#append}
     */
    void put(long keyOffset, int keyLen, long offset);

    /** @return number of live entries */
    int size();
}
