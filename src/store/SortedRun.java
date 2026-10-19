package store;

/**
 * Cold-tier sorted run: a read-only, binary-searchable view over a frozen
 * {@link ByteArena} and its {@link OffsetIndex} after
 * {@link RadixSorter#sort} has permuted the index.
 *
 * Rank {@code r} is slot {@code r} of the permuted index. Ranks run from
 * {@code 0} to {@code count() - 1} in non-decreasing key order, as
 * produced by the sorter using the same {@link RadixSorter.KeyAt} bound
 * here (the 32-bit {@code ts} tick of {@link RecordLayout}). Records with
 * equal keys keep their arrival order, because LSD radix passes are stable.
 *
 * Built once per segment by the flusher ({@code SegmentFlusher}) and never
 * modified after that. The view copies nothing, so the arena and index
 * must stay frozen (no {@link ByteArena#clear}, no {@link OffsetIndex#reset})
 * for as long as readers can reach the run. Accessors are scalar; no
 * per-record objects.
 */
public final class SortedRun {

    /** Frozen arena holding the record bytes; {@link #offsetAt} values point into it. */
    private final ByteArena arena;

    /** Permuted offset index; slot {@code r} holds the arena offset of rank {@code r}. */
    private final OffsetIndex index;

    /** Sort-key extractor the index was sorted with; defines rank order. */
    private final RadixSorter.KeyAt key;

    /**
     * Bind the run to its frozen arena and sorted index. Copies nothing.
     *
     * @param arena frozen arena; must receive no further appends
     * @param index offset index over {@code arena}, already sorted with {@code key}
     * @param key   the extractor {@code index} was sorted with
     */
    public SortedRun(ByteArena arena, OffsetIndex index, RadixSorter.KeyAt key) {
        this.arena = arena;
        this.index = index;
        this.key = key;
    }

    /**
     * Sort key of the record at {@code rank}: {@code key.keyAt(offsetAt(rank))}.
     *
     * @param rank position in sorted order, {@code 0 <= rank < count()}
     * @return its 32-bit sort key
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    public int keyAt(int rank) {
        throw new UnsupportedOperationException("skeleton");
    }

    /**
     * Arena offset of the record at {@code rank}.
     *
     * @param rank position in sorted order, {@code 0 <= rank < count()}
     * @return offset of the record header in the frozen arena
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    public long offsetAt(int rank) {
        throw new UnsupportedOperationException("skeleton");
    }

    /**
     * @return number of records in the run, fixed when the run was built
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    public int count() {
        throw new UnsupportedOperationException("skeleton");
    }

    /**
     * Binary search for the first record whose key is not less than
     * {@code tsKey}, using the same key order {@link RadixSorter#sort}
     * produces. Takes O(log count) {@link #keyAt} probes and allocates
     * nothing. When several records share {@code tsKey}, returns the
     * lowest of their ranks, so a scan from the result visits all of them
     * in arrival order.
     *
     * @param tsKey 32-bit sort key to search for
     * @return lowest rank {@code r} with {@code keyAt(r) >= tsKey}, or
     *         {@code count()} if every key is smaller
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    public int lowerBound(int tsKey) {
        throw new UnsupportedOperationException("skeleton");
    }
}
