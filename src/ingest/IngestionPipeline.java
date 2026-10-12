package ingest;

import java.util.List;

/**
 * Ordered chain of {@link IngestionStage}s.
 *
 * Output of stage n is handed to stage n+1 unchanged. The pipeline owns
 * sequencing only — it holds no data and makes no decisions about the
 * values flowing through it.
 */
public final class IngestionPipeline {

    /** Stages in execution order; fixed at construction. */
    private final List<IngestionStage<?, ?>> stages;

    /**
     * @param stages stages in execution order; must form a type-adjacent
     *               chain (each stage's output type matches the next
     *               stage's input type). Adjacency is the caller's wiring
     *               responsibility in the skeleton phase.
     */
    public IngestionPipeline(List<IngestionStage<?, ?>> stages) {
        this.stages = stages;
    }

    /**
     * Thread {@code input} through every stage in order.
     *
     * @param <I>   input type of the first stage
     * @param <O>   output type of the last stage
     * @param input value fed to the first stage
     * @return value produced by the last stage
     * @throws IngestionException the first failure encountered; the stage
     *                            that threw is identifiable by {@link
     *                            IngestionStage#name()}
     * @throws UnsupportedOperationException skeleton phase — not yet implemented
     */
    public <I, O> O run(I input) throws IngestionException {
        throw new UnsupportedOperationException("skeleton");
    }

    /** @return number of stages in the chain */
    public int size() {
        return stages.size();
    }
}
