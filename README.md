# Ingestion-Growth

Plain Java (11), no build tools, no dependencies.

Two systems built with low-level techniques:

- **`store/`** — a custom HashMap data store (open addressing, manual hashing, byte[] keys/values)
- **`ingest/`** — a stage-based pipeline that ingests raw data, encodes it to the
  protobuf wire format by hand (`proto/`), and stores it in the custom HashMap

## Build & run

```sh
javac -d out $(find src -name "*.java")
java -cp out Main
```
