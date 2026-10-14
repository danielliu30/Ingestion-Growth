package store;

/**
 * User-selected policy for what writers do while the out-of-order
 * sorter owns an arena. Trade-off is memory vs. ingest stalls.
 */
public enum FillMode {

    /**
     * Writers fill a second arena while the sorter works on the first;
     * arenas swap on completion. Ingest never stalls; costs 2x arena memory.
     */
    TWIN_BUFFER,

    /**
     * Writers pause until sort + flush complete, then resume on the same
     * arena. Single arena footprint; creates a latency spike every flush.
     */
    PAUSE_RESUME
}
