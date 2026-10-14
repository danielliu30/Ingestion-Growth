package store;

/**
 * Binary layout of a normalized record inside a {@link ByteArena}.
 *
 * The record is bytes, never a Java object: each field lives at a
 * fixed offset from the record base and is read/written with
 * {@code ValueLayout} VarHandles. Layout (little-endian):
 *
 * <pre>
 *   off  0  seq        long   8B  per-shard ordering label
 *   off  8  ts         int    4B  32-bit time tick, cold-run sort key
 *   off 12  kind       byte   1B  {@code DataKind} ordinal
 *   off 13  keyLen     short  2B  key length in bytes
 *   off 15  payloadLen int    4B  payload length in bytes
 *   off 19  key        var    keyLen bytes
 *   off 19+ key + payload     payloadLen bytes
 * </pre>
 *
 * Constants only — no instances, no allocation.
 */
public final class RecordLayout {

    /** Byte offset of {@code seq} (long, per-shard ordering label). */
    public static final int SEQ_OFF = 0;

    /** Byte offset of {@code ts} (int, cold-run sort key). */
    public static final int TS_OFF = 8;

    /** Byte offset of {@code kind} (byte, DataKind ordinal). */
    public static final int KIND_OFF = 12;

    /** Byte offset of {@code keyLen} (short). */
    public static final int KEY_LEN_OFF = 13;

    /** Byte offset of {@code payloadLen} (int). */
    public static final int PAYLOAD_LEN_OFF = 15;

    /** Fixed header size in bytes; variable key/payload follow. */
    public static final int HEADER_SIZE = 19;

    private RecordLayout() {
    }
}
