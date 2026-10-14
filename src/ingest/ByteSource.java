package ingest;

/**
 * First pipeline stage: pulls raw bytes from a source and appends them
 * to the arena as a framed raw block, returning its offset.
 *
 * Bare-metal contract: bytes go straight into the arena/off-heap
 * region — the stage copies into storage, never wraps them in
 * per-record objects. The {@code source} tag is edge metadata for
 * logging only; it is not carried on the hot path. One instance per
 * writer; holds no shared state.
 */
public final class ByteSource implements IngestionStage {

    /**
     * Read the source descriptor block at {@code offset} (its format is
     * edge-specific), pull the payload bytes, and append them to the
     * arena as {@code [len][bytes]}.
     *
     * @param offset arena offset of the source-descriptor block
     * @return arena offset of the appended raw block
     * @throws IngestionException if the source cannot be read
     * @throws UnsupportedOperationException skeleton phase — not yet implemented
     */
    @Override
    public long process(long offset) throws IngestionException {
        throw new UnsupportedOperationException("skeleton");
    }

    /** @return stage name used in logs and errors */
    @Override
    public String name() {
        return "byte-source";
    }
}
