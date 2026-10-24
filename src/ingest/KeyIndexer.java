package ingest;

import store.ActiveSegment;

/**
 * Index/dedup stage: registers the normalized record at the input offset
 * in the active segment's hot-tier {@link store.SubIndex}.
 *
 * The stage is bound to the shard's {@link ActiveSegment} holder, not to one
 * sub-index. The sub-index is rebuilt per segment and swapped at every
 * freeze or resume, so a construction-time binding would go stale. On each
 * call the stage loads {@link ActiveSegment#current()} and uses that pair's
 * sub-index, with that pair's arena as the key region, so key and index
 * always come from the same segment.
 *
 * Reads {@code keyLen} from the {@link store.RecordLayout} header and calls
 * {@code SubIndex.put(offset + HEADER_SIZE, keyLen, offset)}. The active
 * arena is the index's bound key region, so the key is never copied. Handling of
 * duplicate keys is decided by the {@link store.DedupMode} the sub-index
 * was constructed with, which is the shard's mode
 * ({@link store.DedupMode#FIRST_WRITE_WINS} or
 * {@link store.DedupMode#SEQ_NAMESPACED}). This stage adds no policy of
 * its own.
 *
 * Pass-through: returns the input offset unchanged and appends nothing.
 * One instance per shard, bound to that shard's active-segment holder.
 */
public final class KeyIndexer implements IngestionStage {

    /** The shard's holder for the active arena and sub-index; read with {@code getAcquire} on every call. */
    private final ActiveSegment segment;

    /**
     * Bind the stage to one shard's active-segment holder.
     *
     * @param segment the shard's active-segment holder; each published
     *                sub-index is constructed with the shard's dedup mode
     */
    public KeyIndexer(ActiveSegment segment) {
        this.segment = segment;
    }

    /**
     * Insert the record's key into the active segment's sub-index.
     *
     * @param offset offset of a normalized record header in the active arena
     * @return {@code offset}, unchanged
     * @throws IngestionException if the record header is malformed
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    @Override
    public long process(long offset) throws IngestionException {
        throw new UnsupportedOperationException("skeleton");
    }

    /** @return stage name used in logs and errors */
    @Override
    public String name() {
        return "key-indexer";
    }
}
