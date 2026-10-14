package ingest;

/**
 * Second pipeline stage: sniffs the first bytes of the raw block at the
 * given arena offset and classifies it.
 *
 * Bare-metal contract: pure inspection over the raw byte region —
 * magic-signature checks only, no parsing, no decoding, no allocation.
 * The detected kind is stamped into a record header
 * ({@code store.RecordLayout}) appended to the arena; the returned
 * offset points at that header. Unknown input is a valid outcome, not
 * an error. Stateless; safe to share across writers.
 */
public final class TypeDetector implements IngestionStage {

    /**
     * Classify the payload at {@code offset} and append a record header
     * carrying the detected kind.
     *
     * @param offset arena offset of the raw block
     * @return arena offset of the record header with kind stamped
     * @throws IngestionException never thrown for unknown payloads —
     *                            reserved for unreadable input
     * @throws UnsupportedOperationException skeleton phase — not yet implemented
     */
    @Override
    public long process(long offset) throws IngestionException {
        throw new UnsupportedOperationException("skeleton");
    }

    /** @return stage name used in logs and errors */
    @Override
    public String name() {
        return "type-detector";
    }
}
