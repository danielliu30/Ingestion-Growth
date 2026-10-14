# Ingestion-Growth — agent context

High-performance ingestion tool. Plain Java 11, no build tools, no dependencies.
Goal: minimal Java-heap usage, bare-metal techniques throughout.

## Build / verify

```sh
javac -d out $(find src -name "*.java")   # compile everything
```

## Architecture

Stage-based pipeline (`ingest/`), off-heap storage (`store/`),
hand-rolled protobuf wire format (`proto/`). No protobuf library.

- `ingest/` — `IngestionStage<I,O>` contract, `IngestionPipeline`, data
  types (`RawInput`, `DataKind`, `Record`), stage classes
- `store/` — `ByteArena` (off-heap append region), `SubIndex` (custom
  open-addressed hashmap: key -> arena offset), `RadixSorter`, policy
  enums (`DedupMode`, `FillMode`)
- `proto/` — `WireWriter` / `WireReader` over caller-owned regions

## Design decisions (locked)

- **Append-only.** Records are never modified or erased. Concurrency is
  a *publication* problem, not mutual exclusion — release/acquire on the
  occupied marker/watermark.
- **Ordering: label eagerly, materialize lazily.** `Record.seq` is a
  per-shard counter stamped at ingest; `Record.ts` is a 32-bit time tick
  and the cold-run sort key. No ordering enforcement on the hot path;
  order materializes at flush/read. Order contract is per-source.
- **Two ingest paths.** Sequential: per-thread byte stream + sub-index,
  born ordered. Out-of-order: arena + background `RadixSorter` over the
  offset index (never sorts records in place), flush to cold runs.
- **One writer per shard** — multi-writer = N single-writer shards.
  Hot sources scale via stage-level SPSC pipeline depth, not more writers.
- **User switches.** `DedupMode` (FIRST_WRITE_WINS / SEQ_NAMESPACED) and
  `FillMode` (TWIN_BUFFER / PAUSE_RESUME) — both modes implemented.
- **Cold tier = sorted runs** (binary-searchable); the hashmap is
  hot-tier index only and is rebuilt per segment — no resize under readers.
- **Bare metal by directive.** Byte streams and VarHandles, not
  objects and locks: arena bump = `VarHandle.getAndAdd` (LOCK XADD),
  slot publish = `setRelease`, reads = `getAcquire`, wire codec scalar
  access = `byteArrayViewVarHandle` (single MOV). Zero per-record
  objects on the hot path.
- **Off-heap constraint (Java 11).** No Foreign Memory API — off-heap
  means direct `ByteBuffer` + `byteBufferViewVarHandle`, with
  `sun.misc.Unsafe` as the escape hatch for ops VarHandle can't express.
- **Performance is a provable claim.** All speed claims must be
  JMH-verified against a fair POJO baseline
  (`java.util.HashMap` + object records + protobuf-java). Metrics:
  throughput, bytes/op allocation, p99/p999 latency, GC pauses.
  JMH and the baseline live in `bench/` — bench dependencies never
  enter `src/` (JMH works with plain javac, no build tool).

## CR / PR conventions

- **One logically-smallest component per CR.** No mixed contexts.
  Stack branches only on true dependencies.
- **Framing: infra -> service -> tests.** Branch + title prefixes
  (`infra/`, `service/`, `test/`). Current phase is infra skeletons only.
- **PR body:** always `## Context` (project phase), `## Why this change`
  (motivation), `## Intention` (design intent). Conditional: `Fixes` only
  when closing an issue; semantic-change notes only when behavior changes.
- **Comments on all fields, methods, enum constants** — house style.
- Skeleton phase: classes/interfaces with documented signatures; method
  bodies may `throw new UnsupportedOperationException("skeleton")`.
- Never push to `main` directly; every change is a reviewed PR.
