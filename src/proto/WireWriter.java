package proto;

/**
 * Hand-rolled protobuf wire-format encoder.
 *
 * Writes into a caller-owned off-heap region bound at construction —
 * the implementation owns no growable buffers, so encoding allocates
 * nothing on the hot path. Protobuf wire types: varint (0),
 * fixed64 (1), length-delimited (2), fixed32 (5). All addresses are
 * {@code long} offsets; no heap arrays in the contract.
 */
public interface WireWriter {

    /**
     * Write a field key: {@code (fieldNumber << 3) | wireType} as a varint.
     *
     * @param fieldNumber protobuf field number, 1-based
     * @param wireType    wire type (0, 1, 2, or 5)
     */
    void writeTag(int fieldNumber, int wireType);

    /**
     * Write an LEB128 unsigned varint — the building block for all
     * varint-typed fields.
     *
     * @param value value to encode, low 7 bits per byte, MSB = continuation
     */
    void writeVarint(long value);

    /**
     * Write a length-delimited field: varint length followed by
     * {@code len} bytes copied from {@code srcOffset} in the bound
     * source region.
     *
     * @param srcOffset offset of the source bytes
     * @param len       bytes to frame
     */
    void writeLengthDelimited(long srcOffset, int len);

    /**
     * Write 4 bytes little-endian (fixed32 wire type).
     *
     * @param value value to encode
     */
    void writeFixed32(int value);

    /**
     * Write 8 bytes little-endian (fixed64 wire type).
     *
     * @param value value to encode
     */
    void writeFixed64(long value);

    /** @return bytes written so far into the backing region */
    int position();
}
