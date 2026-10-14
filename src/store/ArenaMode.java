package store;

/**
 * Selects who owns the native memory backing a {@link ByteArena}.
 *
 * The access path is identical either way — {@code MemorySegment} +
 * {@code ValueLayout} VarHandles, scalar machine-width ops — so the
 * mode is a pure construction-time choice; no downstream code changes.
 *
 * Both modes keep record bytes off the Java heap. They differ in
 * whether the JVM knows the allocation exists at all.
 */
public enum ArenaMode {

    /**
     * JVM-managed native memory: {@code Arena.ofConfined/ofShared} via
     * the FFM API. The JVM tracks the allocation — bounds-checked
     * access, deterministic {@code close()}, visible in native-memory
     * accounting ({@code NMT}). Safe default.
     */
    MANAGED,

    /**
     * Completely outside the JVM: raw native memory obtained via
     * {@code sun.misc.Unsafe.allocateMemory} (or {@code malloc} through
     * JNI), then wrapped as a {@code MemorySegment} view purely for
     * VarHandle access. No bounds checks, no accounting, no auto-free —
     * the caller owns the lifetime and must release it explicitly.
     * Maximum throughput; use where the access pattern is proven.
     */
    UNMANAGED
}
