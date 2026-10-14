package ingest;

/**
 * Second pipeline stage: sniffs the first bytes of a {@link RawInput}
 * and classifies it as a {@link DataKind}.
 *
 * Bare-metal contract: pure inspection over the raw byte region —
 * magic-signature checks only, no parsing, no decoding, no allocation.
 * Detection is best-effort; {@link DataKind#UNKNOWN} is a valid
 * outcome, not an error. Stateless; safe to share across writers.
 */
public final class TypeDetector implements IngestionStage<RawInput, DataKind> {

    /**
     * Classify the payload carried by {@code input}.
     *
     * @param input raw bytes from {@code ByteSource}
     * @return detected {@link DataKind}; {@link DataKind#UNKNOWN} when
     *         no signature matches
     * @throws IngestionException never thrown for unknown payloads —
     *                            reserved for unreadable input
     * @throws UnsupportedOperationException skeleton phase — not yet implemented
     */
    @Override
    public DataKind process(RawInput input) throws IngestionException {
        throw new UnsupportedOperationException("skeleton");
    }

    /** @return stage name used in logs and errors */
    @Override
    public String name() {
        return "type-detector";
    }
}
