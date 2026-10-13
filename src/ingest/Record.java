package ingest;

/**
 * Canonical normalized form produced by {@code Normalizer} and consumed
 * by {@code ProtoEncoder}.
 *
 * {@code payload} is the normalized body, still raw bytes at this point —
 * encoding to the protobuf wire format is the encoder's job.
 */
public final class Record {

    /** Store identity; the key the index hashes on. */
    private final byte[] key;

    /** Detected payload kind, preserved from TypeDetector. */
    private final DataKind kind;

    /** Normalized body, still raw bytes; wire encoding happens downstream. */
    private final byte[] payload;

    /** Per-shard ordering label assigned at ingest; materialized at read/flush. */
    private final long seq;

    /** 32-bit time tick; primary sort key for cold runs (radix-friendly). */
    private final int ts;

    /**
     * @param key     store identity for this record
     * @param kind    detected payload kind
     * @param payload normalized body bytes
     * @param seq     per-shard ordering label
     * @param ts      32-bit time tick used as the cold-run sort key
     */
    public Record(byte[] key, DataKind kind, byte[] payload, long seq, int ts) {
        this.key = key;
        this.kind = kind;
        this.payload = payload;
        this.seq = seq;
        this.ts = ts;
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

    /** @return per-shard ordering label */
    public long seq() {
        return seq;
    }

    /** @return 32-bit time tick, the cold-run sort key */
    public int ts() {
        return ts;
    }
}
