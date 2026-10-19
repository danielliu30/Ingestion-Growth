package ingest;

import proto.WireWriter;

/**
 * Wire-encode stage: frames the normalized record at the input offset as
 * protobuf wire bytes and appends the frame to the writer's arena.
 *
 * The record's header fields ({@link store.RecordLayout}) and its key and
 * payload are encoded with the bound {@link WireWriter} into that writer's
 * off-heap scratch region. The encoded bytes are then appended with
 * {@code ByteArena.append}, so watermark publication remains the only way
 * bytes enter the arena. Field numbers are fixed by the service phase.
 * Scalar writes only; no per-record objects.
 *
 * One instance per writer, because the scratch region is writer-private.
 */
public final class WireEncoder implements IngestionStage {

    /** Encoder over this writer's scratch region, which is also the arena's append source. */
    private final WireWriter writer;

    /**
     * Bind the stage to one writer's encoder.
     *
     * @param writer encoder bound to a writer-private off-heap scratch region
     */
    public WireEncoder(WireWriter writer) {
        this.writer = writer;
    }

    /**
     * Encode the record at {@code offset} and append the wire frame.
     *
     * @param offset arena offset of a normalized record header
     * @return arena offset of the appended wire frame
     * @throws IngestionException if the arena is full or the record is malformed
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    @Override
    public long process(long offset) throws IngestionException {
        throw new UnsupportedOperationException("skeleton");
    }

    /** @return stage name used in logs and errors */
    @Override
    public String name() {
        return "wire-encoder";
    }
}
