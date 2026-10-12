package ingest;

/**
 * Canonical normalized form produced by {@code Normalizer} and consumed
 * by {@code ProtoEncoder}.
 *
 * {@code payload} is the normalized body, still raw bytes at this point —
 * encoding to the protobuf wire format is the encoder's job.
 */
public final class Record {

    /** Store identity; the key the custom HashMap indexes on. */
    private final byte[] key;

    /** Detected payload kind, preserved from TypeDetector. */
    private final DataKind kind;

    /** Normalized body, still raw bytes; wire encoding happens downstream. */
    private final byte[] payload;

    /**
     * @param key     store identity for this record
     * @param kind    detected payload kind
     * @param payload normalized body bytes
     */
    public Record(byte[] key, DataKind kind, byte[] payload) {
        this.key = key;
        this.kind = kind;
        this.payload = payload;
    }

    /** @return store identity */
    public byte[] key() {
        return key;
    }

    /** @return detected payload kind */
    public DataKind kind() {
        return kind;
    }

    /** @return normalized body bytes */
    public byte[] payload() {
        return payload;
    }
}
