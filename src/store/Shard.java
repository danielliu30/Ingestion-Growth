package store;

import ingest.IngestionException;
import ingest.IngestionPipeline;

/**
 * The single-writer unit: one writer thread, one {@link ByteArena}, one
 * {@link SubIndex}, one {@link OffsetIndex}, one {@link IngestionPipeline}.
 *
 * Scaling rule: one writer per shard. Multi-writer ingest means N
 * independent single-writer shards, never shared components or locks.
 * A hot source scales through stage-level SPSC pipeline depth, not more
 * writers. Because only one thread appends, the arena cursor, offset-index
 * count and per-shard {@code seq} advance with plain stores. Readers see
 * data only through release/acquire publication of watermarks and
 * occupied markers.
 *
 * Freeze contract between the hot arena and the flusher, selected by
 * {@link FillMode}: {@link #freeze} stops appends to the hot arena and
 * offset index and hands them to a {@link SegmentFlusher}.
 * <ul>
 *   <li>{@link FillMode#TWIN_BUFFER}: the writer switches to the twin arena
 *       with a fresh sub-index and offset index (the hot-tier hashmap is
 *       rebuilt per segment), so {@link #ingest} never stalls. Costs 2x
 *       arena memory.</li>
 *   <li>{@link FillMode#PAUSE_RESUME}: {@link #ingest} blocks until
 *       {@link #resume}, which reuses the same arena for the next segment.
 *       Single-arena footprint, with a latency spike on every flush.</li>
 * </ul>
 *
 * Active-segment holder: the shard owns one per-writer holder cell,
 * {@code store.ActiveSegment}, which holds the active {@link ByteArena} and
 * its {@link SubIndex} as a single pair. Pipeline stages are bound to that
 * holder, not to one arena or sub-index, because under
 * {@link FillMode#TWIN_BUFFER} (and the {@link IngestMode#SORT_LATER} path
 * generally) the active arena is swapped out at every freeze and a
 * construction-time binding would go stale. The holder publishes the pair
 * with {@code setRelease}. Stages, and the writer thread on each record,
 * load it with {@code getAcquire}. Arena and sub-index swap in lockstep as
 * one unit and never field by field. One writer per shard makes the holder
 * a single-owner cell: the swap is a plain release store with no CAS or
 * other atomic read-modify-write. Release/acquire is needed only so the
 * sorter and readers on other threads see a fully built pair. The
 * {@link RadixSorter} worker started at {@link #freeze} stays bound to the
 * frozen arena and is never bound to the holder; only the active pointer
 * moves.
 */
public final class Shard {

    /**
     * First segment's arena, published as the holder's initial pair. After
     * construction the active arena is reached only through the holder;
     * frozen arenas are handed off directly on {@link #freeze}.
     */
    private final ByteArena arena;

    /**
     * First segment's sub-index, published together with {@link #arena}.
     * Later segments' sub-indexes are reached only through the holder.
     */
    private final SubIndex subIndex;

    /** Arrival-order index of record offsets; the input to the SORT_LATER flush. */
    private final OffsetIndex offsets;

    /** This shard's own stage chain; never shared with another shard. */
    private final IngestionPipeline pipeline;

    /** What the writer does while a frozen segment is being flushed. */
    private final FillMode fillMode;

    /**
     * Bind one writer's components. None of them may be shared with
     * another shard. {@code arena} and {@code subIndex} become the
     * active-segment holder's initial pair.
     *
     * @param arena    hot arena for the first segment
     * @param subIndex hot-tier index over {@code arena}, constructed with
     *                 this shard's {@link DedupMode}; it applies the dedup
     *                 policy itself
     * @param offsets  offset index over {@code arena}
     * @param pipeline stage chain whose stages resolve the active arena and
     *                 sub-index through this shard's active-segment holder
     * @param fillMode writer behavior while a segment is flushed
     */
    public Shard(ByteArena arena, SubIndex subIndex, OffsetIndex offsets,
                 IngestionPipeline pipeline, FillMode fillMode) {
        this.arena = arena;
        this.subIndex = subIndex;
        this.offsets = offsets;
        this.pipeline = pipeline;
        this.fillMode = fillMode;
    }

    /**
     * Run {@code offset} through the pipeline and store the resulting record
     * offset in the offset index. Writer thread only. Loads the active pair
     * from the holder with {@code getAcquire} once per record; every stage
     * of that record sees the same pair. Under
     * {@link FillMode#PAUSE_RESUME}, blocks while a segment is frozen.
     *
     * @param offset arena offset fed to the first stage
     * @return arena offset of the ingested record
     * @throws IngestionException the first stage failure, as raised by
     *                            {@link IngestionPipeline#run}
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    public long ingest(long offset) throws IngestionException {
        throw new UnsupportedOperationException("skeleton");
    }

    /**
     * Freeze the hot arena and its offset index (no further appends) and
     * return a flusher bound to them, for a background worker to run.
     * Called by the writer thread when the arena or offset index is full.
     * What the writer does next follows {@link FillMode}. Under
     * {@link FillMode#TWIN_BUFFER}, publishes the twin arena and a fresh
     * sub-index into the holder as one pair before returning, so the next
     * {@link #ingest} goes to the twin. {@code sorter} stays bound to the
     * frozen arena, never to the holder.
     *
     * @param sorter sorter bound to the offset index's region
     * @param key    sort-key extractor, typically the record's {@code ts}
     * @return flusher over the frozen segment
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    public SegmentFlusher freeze(RadixSorter sorter, RadixSorter.KeyAt key) {
        throw new UnsupportedOperationException("skeleton");
    }

    /**
     * Mark the frozen segment's run as released. Under
     * {@link FillMode#PAUSE_RESUME}, clear the arena, reset the offset index
     * and unblock {@link #ingest} on the same arena. Under
     * {@link FillMode#TWIN_BUFFER}, the released arena becomes the next
     * twin. Never called while readers may still reach the run.
     *
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    public void resume() {
        throw new UnsupportedOperationException("skeleton");
    }
}
