package ingest;

/**
 * Normalization stage: turns the kind-stamped header from
 * {@link TypeDetector} into a complete normalized record appended to the
 * arena.
 *
 * Appends a {@link store.RecordLayout} record: a header carrying
 * {@code seq}, {@code ts}, {@code kind}, {@code keyLen} and
 * {@code payloadLen}, followed by the key and payload bytes. Returns the
 * record's offset. Append-only: the detector's header is read and never
 * rewritten, and the normalized record is a new append.
 *
 * Labels eagerly, enforces nothing. {@code seq} is the per-shard counter
 * held by this stage. {@code ts} is the shard clock's 32-bit tick at
 * normalization and the cold-run sort key. Header fields are written with
 * scalar VarHandle stores; no per-record objects.
 *
 * One instance per shard, because it owns that shard's {@code seq}
 * counter. Not shareable across writers.
 */
public final class RecordWriter implements IngestionStage {

    /** Next {@code seq} to stamp. Per-shard; the single writer advances it with a plain increment. */
    private long nextSeq;

    /**
     * Append the normalized record for the detector header at
     * {@code offset}, stamping the next {@code seq} and the current
     * {@code ts}.
     *
     * @param offset arena offset of the kind-stamped header from {@link TypeDetector}
     * @return arena offset of the appended normalized record
     * @throws IngestionException if the arena is full or the input block is malformed
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    @Override
    public long process(long offset) throws IngestionException {
        throw new UnsupportedOperationException("skeleton");
    }

    /** @return stage name used in logs and errors */
    @Override
    public String name() {
        return "record-writer";
    }
}
