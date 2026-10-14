package store;

/**
 * Background sort over an arena's offset index — the ordering
 * materialization step for the out-of-order path.
 *
 * Never invoked on the ingest hot path: the caller runs this on a
 * dedicated worker against a frozen arena while writers continue on
 * the twin (or pause, per {@link FillMode}).
 *
 * Sorts the index, not the records: {@code offsetIndex} is permuted
 * in place; {@link ByteArena} contents are untouched, keeping the
 * arena append-only even through flush.
 */
public interface RadixSorter {

    /**
     * Extracts the sort key for one indexed record.
     * The caller supplies this so the sorter stays agnostic of record
     * layout — typically reads the 32-bit {@code ts} field out of the
     * arena at the given offset.
     */
    @FunctionalInterface
    interface KeyAt {
        /**
         * @param offset arena offset of a record
         * @return its 32-bit sort key
         */
        int keyAt(long offset);
    }

    /**
     * Reorder the offset index at {@code indexOffset} so
     * {@code key.keyAt(offset)} is non-decreasing. LSD radix: 4 stable
     * passes over 8-bit digits. The index and the counting buffer live
     * in off-heap memory ({@code MemorySegment} + {@code ValueLayout}
     * VarHandles) — scalar ops only, no heap arrays, no per-record
     * objects.
     *
     * @param indexOffset offset of the {@code count}-long index into
     *                    the frozen arena, permuted in place
     * @param count       number of index entries
     * @param key         sort-key extractor supplied by the caller
     */
    void sort(long indexOffset, int count, KeyAt key);
}
