package store;

/**
 * User-selected ingest path for a source: whether its data is trusted
 * to arrive in order or must be sorted after the fact.
 */
public enum IngestMode {

    /**
     * In-order ingestion: each writer fills its own byte stream and
     * sub-index, so segments are born ordered. No sorting ever runs;
     * the sorter is never on the path.
     */
    ORDERED,

    /**
     * Out-of-order ingestion: the arena accepts records unordered;
     * at threshold a background {@code RadixSorter} sorts the offset
     * index and a sorted run is flushed to cold storage. Writer
     * behavior during sort is governed by {@link FillMode}.
     */
    SORT_LATER
}
