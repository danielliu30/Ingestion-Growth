# Ingestion-Growth

Plain Java (11), no build tools, no dependencies.

Two systems built with low-level techniques:

- **`store/`** — off-heap, append-only storage: `ByteArena` holds raw record
  bytes, `SubIndex` is a custom open-addressed hash map (key -> arena offset);
  no heap arrays in the contracts
- **`ingest/`** — a stage-based pipeline that ingests raw data, encodes it to the
  protobuf wire format by hand (`proto/`), and appends it to the store

## Build

```sh
javac -d out $(find src -name "*.java")
```
