package store;

/**
 * User-selected policy for how the store treats a key that already
 * exists. Records are never erased — the policy decides identity,
 * not retention.
 */
public enum DedupMode {

    /**
     * Identity is {@code key} alone: the first write of a key wins;
     * later writes with the same key are ignored.
     */
    FIRST_WRITE_WINS,

    /**
     * Identity is {@code (key, seq)}: every write of a key is kept as its
     * own record, stamped with its per-shard {@code seq}. The hot-tier
     * {@link SubIndex} stays key-only and resolves a key to a single
     * offset; the other versions are not reachable through it and remain
     * in the arena and cold runs, distinguished by {@code seq}.
     */
    SEQ_NAMESPACED
}
