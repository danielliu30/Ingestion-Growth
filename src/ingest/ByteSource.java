package ingest;

/**
 * First pipeline stage: copies one record frame out of a
 * {@code FrameSource}-bound source region into the arena as a raw block,
 * returning its offset.
 *
 * Input is a source region the transport adapter has bound to the
 * shard's {@code FrameSource}: complete varint-length-delimited record
 * frames, one shard per source. The writer walks it with
 * {@code FrameSource.nextFrame()} and hands each frame here; frame
 * walking, reassembly and shard selection all happen before this stage.
 *
 * Bare-metal contract: bytes go straight from the bound source region
 * into the arena ({@code ByteArena.append} reads the source offset
 * directly) — no intermediate buffer, no per-record objects. The {@code source} tag is edge
 * metadata for logging only; it is not carried on the hot path or into
 * the arena. One instance per writer; holds no shared state.
 */
public final class ByteSource implements IngestionStage {

    /**
     * Append the current frame's record bytes to the arena as
     * {@code [len][bytes]}. {@code offset} is {@code FrameSource.frameOffset()}
     * in the bound source region and the length is
     * {@code FrameSource.frameLength()} of the same, current frame.
     *
     * @param offset source-region offset of the current frame's record bytes
     * @return arena offset of the appended raw block
     * @throws IngestionException if the arena is full
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
