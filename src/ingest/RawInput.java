package ingest;

/**
 * Raw bytes as they arrive at the pipeline's input edge.
 *
 * The pipeline never mutates this array; stages downstream treat it
 * as read-only. {@code source} is an opaque tag (file path, socket id,
 * producer name) carried for logging and error context only.
 */
public final class RawInput {

    private final byte[] bytes;
    private final String source;

    public RawInput(byte[] bytes, String source) {
        this.bytes = bytes;
        this.source = source;
    }

    public byte[] bytes() {
        return bytes;
    }

    public String source() {
        return source;
    }
}
