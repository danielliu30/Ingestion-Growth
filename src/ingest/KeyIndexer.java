package ingest;

import store.SubIndex;

/**
 * Index/dedup stage: registers the normalized record at the input offset
 * in the shard's hot-tier {@link SubIndex}.
 *
 * Reads {@code keyLen} from the {@link store.RecordLayout} header and calls
 * {@code SubIndex.put(offset + HEADER_SIZE, keyLen, offset)}. The arena is
 * the index's bound key region, so the key is never copied. Handling of
 * duplicate keys is decided by the {@link store.DedupMode} the sub-index
 * was constructed with, which is the shard's mode
 * ({@link store.DedupMode#FIRST_WRITE_WINS} or
 * {@link store.DedupMode#SEQ_NAMESPACED}). This stage adds no policy of
 * its own.
 *
 * Pass-through: returns the input offset unchanged and appends nothing.
 * One instance per shard, bound to that shard's sub-index.
 */
public final class KeyIndexer implements IngestionStage {

    /** The shard's hot-tier index; applies the shard's dedup policy on {@code put}. */
    private final SubIndex index;

    /**
     * Bind the stage to one shard's sub-index.
     *
     * @param index hot-tier index, constructed with the shard's dedup mode
     */
    public KeyIndexer(SubIndex index) {
        this.index = index;
    }

    /**
     * Insert the record's key into the sub-index.
     *
     * @param offset arena offset of a normalized record header
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
