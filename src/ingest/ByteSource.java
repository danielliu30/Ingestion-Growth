package ingest;

import store.ActiveSegment;

/**
 * First pipeline stage: pulls raw bytes from a source and appends them
 * to the active arena as a framed raw block, returning its offset.
 *
 * The stage is bound to the shard's {@link ActiveSegment} holder, not to one
 * arena. Under {@link store.FillMode#TWIN_BUFFER} the active arena is
 * swapped at every freeze, so a construction-time binding would go stale.
 * On each call the stage loads {@link ActiveSegment#current()} and reads
 * and appends through that pair's arena.
 *
 * Bare-metal contract: bytes go straight into the arena/off-heap
 * region — the stage copies into storage, never wraps them in
 * per-record objects. The {@code source} tag is edge metadata for
 * logging only; it is not carried on the hot path. One instance per
 * writer; holds no shared state.
 */
public final class ByteSource implements IngestionStage {

    /** The shard's holder for the active arena; read with {@code getAcquire} on every call. */
    private final ActiveSegment segment;

    /**
     * Bind the stage to one writer's active-segment holder.
     *
     * @param segment the shard's active-segment holder; the stage never
     *                caches the arena it resolves
     */
    public ByteSource(ActiveSegment segment) {
        this.segment = segment;
    }

    /**
     * Read the source descriptor block at {@code offset} (its format is
     * edge-specific), pull the payload bytes, and append them to the
     * active arena as {@code [len][bytes]}.
     *
     * @param offset offset of the source-descriptor block in the active arena
     * @return offset of the appended raw block in the active arena
     * @throws IngestionException if the source cannot be read
     * @throws UnsupportedOperationException skeleton phase — not yet implemented
     */
    @Override
    public long process(long offset) throws IngestionException {
        throw new UnsupportedOperationException("skeleton");
    }

    /** @return stage name used in logs and errors */
    @Override
    public String name() {
        return "byte-source";
    }
}
