package ingest;

import proto.WireWriter;
import store.ActiveSegment;

/**
 * Wire-encode stage: frames the normalized record at the input offset as
 * protobuf wire bytes and appends the frame to the active arena.
 *
 * The stage reaches the arena through the shard's {@link ActiveSegment}
 * holder, never through a {@code ByteArena} bound at construction. Under
 * {@link store.FillMode#TWIN_BUFFER} the active arena is swapped at every
 * freeze, so a fixed binding would go stale. On each call the stage loads
 * {@link ActiveSegment#current()} and uses that segment's arena for both the
 * input read and the frame append.
 *
 * Scratch region: the writer owns one private off-heap scratch region,
 * split into two windows.
 * <ul>
 *   <li>Read window: the bound destination region for
 *       {@code ByteArena.readAt}. The record at {@code offset}
 *       ({@link store.RecordLayout} header, then key, then payload) is copied
 *       here, and header fields are read from it with scalar VarHandle
 *       loads.</li>
 *   <li>Encode window: the region the bound {@link WireWriter} writes into.
 *       Key and payload are framed from the read window with
 *       {@code writeLengthDelimited}, and header fields with scalar
 *       writes. The encode window is also the bound source region for
 *       {@code ByteArena.append}: the finished frame (its length is the
 *       advance of {@link WireWriter#position()}) is appended from it, so
 *       watermark publication remains the only way bytes enter the
 *       arena.</li>
 * </ul>
 * Every arena that can become active for this writer (both twins under
 * {@code TWIN_BUFFER}) is bound to this same scratch region, so a swap
 * changes only which arena is used, not where the stage reads and encodes.
 * Field numbers are fixed by the service phase. Scalar writes only; no
 * per-record objects.
 *
 * One instance per writer, because the scratch region is writer-private.
 */
public final class WireEncoder implements IngestionStage {

    /** Encoder over the encode window of this writer's scratch region. */
    private final WireWriter writer;

    /** The shard's holder for the active arena; read with {@code getAcquire} on every call. */
    private final ActiveSegment segment;

    /**
     * Bind the stage to one writer's encoder and its shard's active-segment
     * holder.
     *
     * @param writer  encoder bound to the encode window of a writer-private
     *                off-heap scratch region
     * @param segment the shard's active-segment holder; the stage never
     *                caches the arena it resolves
     */
    public WireEncoder(WireWriter writer, ActiveSegment segment) {
        this.writer = writer;
        this.segment = segment;
    }

    /**
     * Read the record at {@code offset} from the active arena into the read
     * window, encode it into the encode window, and append the wire frame
     * to the same arena.
     *
     * @param offset offset of a normalized record header in the active arena
     * @return offset of the appended wire frame in the active arena
     * @throws IngestionException if the arena is full or the record is malformed
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    @Override
    public long process(long offset) throws IngestionException {
        throw new UnsupportedOperationException("skeleton");
    }

    /** @return stage name used in logs and errors */
    @Override
    public String name() {
        return "wire-encoder";
    }
}
