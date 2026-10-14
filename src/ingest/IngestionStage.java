package ingest;

/**
 * A single stage in the ingestion pipeline.
 *
 * Bare-metal contract: stages hand off a {@code long} — an arena
 * offset pointing at the record/bytes the next stage consumes.
 * Primitive in, primitive out: no boxing, no per-record objects,
 * nothing on the Java heap. The interpretation of the offset is
 * stage-specific (raw block, normalized record header, wire bytes).
 *
 * Stages own no shared state and are composable in any order that
 * makes sense; each writer runs its own pipeline instance.
 */
public interface IngestionStage {

    /**
     * Run this stage's work on the input at {@code offset}.
     *
     * @param offset arena offset handed off from the previous stage
     * @return arena offset to hand to the next stage
     * @throws IngestionException if this stage cannot process the input
     */
    long process(long offset) throws IngestionException;

    /**
     * Human-readable name of this stage, used in logs and errors.
     */
    String name();
}
