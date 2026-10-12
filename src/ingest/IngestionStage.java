package ingest;

/**
 * A single stage in the ingestion pipeline.
 *
 * Each stage takes an input of type I, does its work, and hands off
 * an output of type O to the next stage. Stages own no shared state
 * and are composable in any order that makes sense.
 *
 * @param <I> input type accepted by this stage
 * @param <O> output type produced by this stage
 */
public interface IngestionStage<I, O> {

    /**
     * Run this stage's work on the given input.
     *
     * @param input value handed off from the previous stage
     * @return value to hand to the next stage
     * @throws IngestionException if this stage cannot process the input
     */
    O process(I input) throws IngestionException;

    /**
     * Human-readable name of this stage, used in logs and errors.
     */
    String name();
}
