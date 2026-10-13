package proto;

/**
 * Hand-rolled protobuf wire-format decoder.
 *
 * Reads forward over a caller-owned byte region; no copies, no objects
 * per field. Unknown fields are skipped by wire type — forward
 * compatibility comes free with the format.
 */
public interface WireReader {

    /**
     * Read the next field key.
     *
     * @return {@code (fieldNumber << 3) | wireType}, or -1 at end of input
     */
    int readTag();

    /**
     * Read an LEB128 varint.
     *
     * @return decoded value; up to 10 bytes consumed
     */
    long readVarint();

    /**
     * Read a length-delimited field into {@code dst}.
     *
     * @param dst destination buffer, must be at least the field length
     * @return number of bytes copied
     */
    int readBytes(byte[] dst);

    /** @return 4 bytes little-endian */
    int readFixed32();

    /** @return 8 bytes little-endian */
    long readFixed64();

    /**
     * Skip a field of the given wire type without materializing it.
     *
     * @param wireType wire type from {@link #readTag}
     */
    void skip(int wireType);
}
