package store;

/**
 * Mutable off-heap index of {@code long} arena offsets: the region that
 * {@link RadixSorter#sort} permutes in place on the out-of-order path.
 *
 * Index metadata only, never record bytes. Each slot holds an offset
 * returned by {@link ByteArena#append}; the arena those offsets point
 * into stays append-only, so all reordering for a sorted run happens
 * here and never in the arena.
 *
 * Storage is a fixed-capacity region of 8-byte slots in off-heap memory
 * ({@code MemorySegment}, or a direct {@code ByteBuffer} as the fallback)
 * bound at construction and accessed with scalar {@code ValueLayout}
 * VarHandle ops. No heap arrays, no boxing.
 *
 * Single-writer contract: only the shard's writer calls {@link #append}
 * and {@link #reset}. {@link #set} is called only by the sorter, and only
 * after the arena and this index have been frozen for flush.
 */
public interface OffsetIndex {

    /**
     * Read the arena offset stored in {@code slot}.
     *
     * @param slot slot number, {@code 0 <= slot < count()}
     * @return arena offset held in that slot
     */
    long get(int slot);

    /**
     * Overwrite {@code slot} with {@code offset}. Used by the sorter's
     * in-place permutation of a frozen index, never by the hot-path writer.
     *
     * @param slot   slot number, {@code 0 <= slot < count()}
     * @param offset arena offset previously returned by {@link ByteArena#append}
     */
    void set(int slot, long offset);

    /**
     * Store {@code offset} in the next free slot: the single writer's grow
     * step, called once per record appended to the arena. The slot is
     * written first, then the new count is published with
     * {@code setRelease}.
     *
     * @param offset arena offset previously returned by {@link ByteArena#append}
     * @return slot written, or -1 if the index is full
     */
    int append(long offset);

    /**
     * Number of filled slots, loaded with {@code getAcquire}. Slots below
     * it are safe to read.
     *
     * @return filled slot count
     */
    int count();

    /** @return total number of slots, fixed at construction */
    int capacity();

    /**
     * Offset of slot 0 within the bound index region. This is the
     * {@code indexOffset} argument to {@link RadixSorter#sort}; the sorter
     * must be bound to the same region as this index.
     *
     * @return region offset of the first slot
     */
    long regionOffset();

    /**
     * Set the count back to zero so the next segment can refill the slots.
     * Called only by the single writer, and only after the sorted run
     * built over this index has been released. Never called while readers
     * may still hold ranks into it. Clears index metadata only: the arena
     * records are untouched.
     */
    void reset();
}
