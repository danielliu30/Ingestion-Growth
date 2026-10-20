package store;

/**
 * The SORT_LATER flush step: turns one frozen arena segment into a
 * {@link SortedRun}.
 *
 * Bound at construction to the frozen {@link ByteArena}, its
 * {@link OffsetIndex}, and the {@link RadixSorter} and
 * {@link RadixSorter.KeyAt} to sort with. {@link #flush} sorts the offset
 * index in place and wraps arena and index as a run. The arena's bytes are
 * never written, so the arena stays append-only through flush.
 *
 * Runs on a dedicated background worker, never on the ingest hot path.
 * Precondition: no writer appends to the bound arena or index while the
 * flush runs. The shard's {@link FillMode} is what guarantees this:
 * <ul>
 *   <li>{@link FillMode#TWIN_BUFFER}: the writer has already moved to the
 *       twin arena, so ingest continues while this flusher works.</li>
 *   <li>{@link FillMode#PAUSE_RESUME}: the writer is blocked until the
 *       flush completes and the run is released. It then resumes on the
 *       same arena after {@link ByteArena#clear} and
 *       {@link OffsetIndex#reset}, which end the run's view.</li>
 * </ul>
 * One flusher per frozen segment; not reused across segments.
 */
public final class SegmentFlusher {

    /** Frozen arena whose records the run will reference; never written here. */
    private final ByteArena arena;

    /** Offset index over {@link #arena}; permuted in place by {@link #sorter}. */
    private final OffsetIndex index;

    /** Background sorter, bound to the same region as {@link #index}. */
    private final RadixSorter sorter;

    /** Sort-key extractor, typically the record's 32-bit {@code ts}. */
    private final RadixSorter.KeyAt key;

    /**
     * Bind the flusher to one frozen segment.
     *
     * @param arena  frozen arena; must receive no appends during flush
     * @param index  that arena's offset index, frozen with it
     * @param sorter sorter bound to {@code index}'s region
     * @param key    sort-key extractor reading records out of {@code arena}
     */
    public SegmentFlusher(ByteArena arena, OffsetIndex index, RadixSorter sorter,
                          RadixSorter.KeyAt key) {
        this.arena = arena;
        this.index = index;
        this.sorter = sorter;
        this.key = key;
    }

    /**
     * Sort the bound index with
     * {@code sorter.sort(index.regionOffset(), index.count(), key)} and
     * return the run over the frozen arena and the permuted index. Call
     * once per flusher.
     *
     * @return the segment's sorted run
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    public SortedRun flush() {
        throw new UnsupportedOperationException("skeleton");
    }
}
