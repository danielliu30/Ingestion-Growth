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
 */
public final class Shard {

    /** Hot arena this shard's writer appends to; frozen and handed off on {@link #freeze}. */
    private final ByteArena arena;

    /** Hot-tier key index for the current segment. */
    private final SubIndex subIndex;

    /** Arrival-order index of record offsets; the input to the SORT_LATER flush. */
    private final OffsetIndex offsets;

    /** This shard's own stage chain; never shared with another shard. */
    private final IngestionPipeline pipeline;

    /** What the writer does while a frozen segment is being flushed. */
    private final FillMode fillMode;

    /**
     * Bind one writer's components. None of them may be shared with
     * another shard.
     *
     * @param arena    hot arena for the first segment
     * @param subIndex hot-tier index, constructed with this shard's
     *                 {@link DedupMode}; it applies the dedup policy itself
     * @param offsets  offset index over {@code arena}
     * @param pipeline stage chain wired to this shard's arena and sub-index
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
     * offset in the offset index. Writer thread only. Under
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
     * What the writer does next follows {@link FillMode}.
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
