package ingest;

/**
 * Ordered chain of {@link IngestionStage}s.
 *
 * Each stage's output offset is handed to the next stage unchanged.
 * The pipeline owns sequencing only — it holds no data and makes no
 * decisions about the offsets flowing through it. One instance per
 * writer, bound to that writer's arena.
 */
public final class IngestionPipeline {

    /** Stages in execution order; fixed at construction. Plain array — no collections. */
    private final IngestionStage[] stages;

    /**
     * @param stages stages in execution order; must form a coherent
     *               offset contract (each stage's output offset is a
     *               valid input for the next). Adjacency is the
     *               caller's wiring responsibility. The array is
     *               copied, so later writes to it do not alter the chain.
     */
    public IngestionPipeline(IngestionStage... stages) {
        this.stages = stages.clone();
    }

    /**
     * Thread {@code offset} through every stage in order.
     *
     * {@code offset} is in whatever region the first stage reads: for a
     * chain headed by {@link ByteSource}, the source-region offset from
     * {@code FrameSource.frameOffset()}; otherwise an arena offset. Every
     * later stage receives an arena offset.
     *
     * @param offset input offset for the first stage, in that stage's input region
     * @return arena offset produced by the last stage
     * @throws IngestionException the first failure encountered; the stage
     *                            that threw is identifiable by {@link
     *                            IngestionStage#name()}
     * @throws UnsupportedOperationException skeleton phase — not yet implemented
     */
    public long run(long offset) throws IngestionException {
        throw new UnsupportedOperationException("skeleton");
    }

    /** @return number of stages in the chain */
    public int size() {
        return stages.length;
    }
}
