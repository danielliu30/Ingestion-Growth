package store;

/**
 * Per-writer holder for the shard's active segment: the
 * {@link ByteArena} the writer appends to and the {@link SubIndex} over it.
 *
 * Under {@link FillMode#TWIN_BUFFER} (and the {@link IngestMode#SORT_LATER}
 * path generally) the active arena is swapped out at every freeze, so a
 * stage bound to one arena or sub-index at construction goes stale. Stages
 * are bound to this holder instead and resolve the current pair from it.
 *
 * The arena and its sub-index are published together, as one immutable
 * {@link Segment}, through a single reference: {@code setRelease} on
 * publish, {@code getAcquire} on read. They swap in lockstep and never
 * field by field, so a reader can never pair one segment's arena with
 * another segment's sub-index.
 *
 * Single owner: one writer per shard, and the shard is the only publisher.
 * It publishes at freeze, or on resume while the writer is paused, and never
 * concurrently. The swap is therefore a plain release store with no CAS or
 * other atomic read-modify-write. Release/acquire is needed only so that
 * other threads (readers, and the sorter handing a segment back) see a fully
 * built pair. A {@link RadixSorter} worker is never bound to this holder:
 * it stays pinned to the frozen arena it was given, and only the active
 * pointer moves.
 *
 * Owned by the {@code Shard}. One instance per shard, never shared.
 */
public final class ActiveSegment {

    /**
     * One segment's arena and sub-index, published as a single unit.
     * Immutable, and allocated once per segment, never per record.
     */
    public static final class Segment {

        /** Arena the writer appends this segment's records to. */
        private final ByteArena arena;

        /** Hot-tier key index over {@link #arena}, fresh for this segment. */
        private final SubIndex subIndex;

        /**
         * Pair an arena with the sub-index built for it.
         *
         * @param arena    arena for this segment
         * @param subIndex fresh sub-index over {@code arena}, constructed with
         *                 the shard's {@link DedupMode}
         */
        public Segment(ByteArena arena, SubIndex subIndex) {
            this.arena = arena;
            this.subIndex = subIndex;
        }

        /**
         * @return this segment's arena
         * @throws UnsupportedOperationException skeleton phase, not yet implemented
         */
        public ByteArena arena() {
            throw new UnsupportedOperationException("skeleton");
        }

        /**
         * @return this segment's sub-index, paired with {@link #arena()}
         * @throws UnsupportedOperationException skeleton phase, not yet implemented
         */
        public SubIndex subIndex() {
            throw new UnsupportedOperationException("skeleton");
        }
    }

    /**
     * The currently published segment. Accessed only through a
     * {@code VarHandle}: {@code setRelease} in {@link #publish},
     * {@code getAcquire} in {@link #current}.
     */
    private Segment current;

    /**
     * Create the holder and publish the shard's first segment.
     *
     * @param arena    arena for the first segment
     * @param subIndex fresh sub-index over {@code arena}
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    public ActiveSegment(ByteArena arena, SubIndex subIndex) {
        throw new UnsupportedOperationException("skeleton");
    }

    /**
     * Load the active segment with {@code getAcquire}. The writer loads it
     * once per record and every stage of that record uses the same pair.
     * Because only the shard publishes, between records, the pair cannot
     * change while a record is mid-pipeline.
     *
     * @return the active arena and sub-index, as one unit
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    public Segment current() {
        throw new UnsupportedOperationException("skeleton");
    }

    /**
     * Install the next segment: build a new {@link Segment} from
     * {@code arena} and {@code subIndex} and publish it with
     * {@code setRelease}, replacing both at once. Called only by the owning
     * shard, at freeze or on resume, never concurrently, so no CAS is used.
     * The previous segment is not touched; whoever froze it (the sorter and
     * flusher) still holds it directly.
     *
     * @param arena    arena for the next segment
     * @param subIndex fresh sub-index over {@code arena}; never the previous
     *                 segment's
     * @throws UnsupportedOperationException skeleton phase, not yet implemented
     */
    public void publish(ByteArena arena, SubIndex subIndex) {
        throw new UnsupportedOperationException("skeleton");
    }
}
