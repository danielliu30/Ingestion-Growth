package ingest;

/**
 * The kind of payload a {@link RawInput} is detected to carry.
 *
 * Detection is best-effort sniffing; {@link #UNKNOWN} is the honest
 * fallback when no signature matches.
 */
public enum DataKind {

    /** Character data (UTF-8 / ASCII text). */
    TEXT,

    /** JSON-structured payload. */
    JSON,

    /** Recognized image bytes (PNG/JPEG signature). */
    IMAGE,

    /** Other recognized binary payload. */
    BINARY,

    /** No signature matched; valid outcome, not a failure. */
    UNKNOWN
}
