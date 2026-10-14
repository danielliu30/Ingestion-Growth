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
     * Identity is {@code (key, seq)}: duplicate keys coexist as separate
     * versions; readers choose which version to observe.
     */
    SEQ_NAMESPACED
}
