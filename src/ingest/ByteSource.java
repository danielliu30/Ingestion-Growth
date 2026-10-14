package ingest;

/**
 * First pipeline stage: pulls raw bytes from a source and wraps them
 * in a {@link RawInput} for downstream stages.
 *
 * Bare-metal contract: bytes are read straight into the destination
 * arena/off-heap region — the stage copies into storage, never wraps
 * them in per-record objects. One instance per writer; holds no
 * shared state.
 */
public final class ByteSource implements IngestionStage<String, RawInput> {

    /**
     * Read all bytes produced by {@code source} into storage and return
     * the resulting input.
     *
     * @param source source tag (file path, socket id, producer name)
     * @return raw input handed to {@code TypeDetector}
     * @throws IngestionException if the source cannot be read
     * @throws UnsupportedOperationException skeleton phase — not yet implemented
     */
    @Override
    public RawInput process(String source) throws IngestionException {
        throw new UnsupportedOperationException("skeleton");
    }

    /** @return stage name used in logs and errors */
    @Override
    public String name() {
        return "byte-source";
    }
}
