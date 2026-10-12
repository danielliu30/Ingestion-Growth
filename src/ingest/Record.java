package ingest;

/**
 * Canonical normalized form produced by {@code Normalizer} and consumed
 * by {@code ProtoEncoder}.
 *
 * {@code key} identifies the record in the store. {@code payload} is the
 * normalized body, still raw bytes at this point — encoding to the
 * protobuf wire format is the encoder's job.
 */
public final class Record {

    private final byte[] key;
    private final DataKind kind;
    private final byte[] payload;

    public Record(byte[] key, DataKind kind, byte[] payload) {
        this.key = key;
        this.kind = kind;
        this.payload = payload;
    }

    public byte[] key() {
        return key;
    }

    public DataKind kind() {
        return kind;
    }

    public byte[] payload() {
        return payload;
    }
}
