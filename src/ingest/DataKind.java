package ingest;

/**
 * The kind of payload a {@link RawInput} is detected to carry.
 *
 * Detection is best-effort sniffing; {@link #UNKNOWN} is the honest
 * fallback when no signature matches.
 */
public enum DataKind {
    TEXT,
    JSON,
    IMAGE,
    BINARY,
    UNKNOWN
}
