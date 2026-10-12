package ingest;

/**
 * Thrown when a pipeline stage cannot process its input.
 *
 * Checked on purpose: ingestion deals with untrusted external bytes,
 * so callers must decide explicitly whether to retry, skip, or abort.
 */
public class IngestionException extends Exception {

    public IngestionException(String message) {
        super(message);
    }

    public IngestionException(String message, Throwable cause) {
        super(message, cause);
    }
}
