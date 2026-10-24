package ingest;

import java.nio.ByteBuffer;

/**
 * Edge ingress contract: walks complete, varint-length-delimited record
 * frames in place over a caller-bound source region.
 *
 * Frame format, identical across every transport:
 *
 * <pre>
 *   [len varint][len record bytes][len varint][len record bytes]...
 * </pre>
 *
 * {@code len} is an unsigned LEB128 varint of at most 5 bytes (the
 * protobuf length-delimited prefix), and the record bytes follow it
 * directly. The record format carries no transport detail, so a frame
 * is the same bytes whatever delivered it.
 *
 * Transport rules:
 * <ul>
 *   <li>Complete frames only. The bound region holds whole frames; a frame
 *       split across partial reads is reassembled by the transport adapter
 *       (in its carry-over buffer) before it is bound here. This contract
 *       never buffers or stitches bytes.</li>
 *   <li>No sharding metadata inside record frames. Shard selection is made
 *       by connection or channel, or by a transport-level header that the
 *       adapter strips before binding. One source feeds one shard.</li>
 * </ul>
 *
 * Bare-metal contract: zero copies. The source never moves bytes; it only
 * decodes each length prefix and reports where the record bytes sit in the
 * bound region, so {@link ByteSource} can copy them straight into the
 * arena. Scalar absolute reads only; the region's position and limit are
 * not modified, and no per-frame objects are created. One instance per
 * shard writer; not thread-safe.
 */
public final class FrameSource {

    /**
     * Bound source region, an off-heap direct buffer owned by the transport
     * adapter. Only the bytes between its position and limit at
     * {@link #bind} time are walked.
     */
    private ByteBuffer region;

    /** Region offset of the next frame's length prefix; advanced by {@link #nextFrame}. */
    private long cursor;

    /** Region offset at which the walk stops: the bound region's limit. */
    private long end;

    /** Region offset of the current frame's record bytes (after its prefix). */
    private long frameOffset;

    /** Length in bytes of the current frame's record bytes, as decoded from its prefix. */
    private int frameLength;

    /**
     * Bind a source region and reset the walk to its first frame. The bytes
     * between the region's position and limit must be complete frames. The
     * region is not copied, and the adapter must not reuse it until this
     * source is rebound or every frame has been consumed.
     *
     * @param region direct buffer holding complete record frames
     * @throws IllegalArgumentException if {@code region} is not direct
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    public void bind(ByteBuffer region) {
        throw new UnsupportedOperationException("skeleton");
    }

    /**
     * Advance to the next frame: decode its varint length prefix at the
     * cursor and set {@link #frameOffset} and {@link #frameLength}. Moves
     * the cursor past the record bytes; copies nothing.
     *
     * @return {@code true} if a frame is current, {@code false} once the
     *         bound region is exhausted
     * @throws IngestionException if a prefix is longer than 5 bytes, or a
     *                            frame runs past the region's limit. Both
     *                            break the complete-frames contract.
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    public boolean nextFrame() throws IngestionException {
        throw new UnsupportedOperationException("skeleton");
    }

    /**
     * Offset of the current frame's record bytes in the bound region,
     * after the length prefix. Valid only after {@link #nextFrame}
     * returned {@code true}. Passed as the source offset to
     * {@code ByteArena.append}.
     *
     * @return region offset of the record bytes
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    public long frameOffset() {
        throw new UnsupportedOperationException("skeleton");
    }

    /**
     * Length of the current frame's record bytes, excluding the prefix.
     * Valid only after {@link #nextFrame} returned {@code true}.
     *
     * @return record length in bytes
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    public int frameLength() {
        throw new UnsupportedOperationException("skeleton");
    }
}
