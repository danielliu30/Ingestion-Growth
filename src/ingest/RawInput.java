package ingest;

/**
 * Raw bytes as they arrive at the pipeline's input edge.
 *
 * The pipeline never mutates this array; stages downstream treat it
 * as read-only.
 */
public final class RawInput {

    /** Payload bytes, read-only by convention across all stages. */
    private final byte[] bytes;

    /** Opaque provenance tag (file path, socket id, producer name); never interpreted. */
    private final String source;

    /**
     * @param bytes  raw payload as received
     * @param source provenance tag for logging and error context
     */
    public RawInput(byte[] bytes, String source) {
        this.bytes = bytes;
        this.source = source;
    }

    /** @return raw payload bytes */
    public byte[] bytes() {
        return bytes;
    }

    /** @return provenance tag, may be null if the producer did not supply one */
    public String source() {
        return source;
    }
}
