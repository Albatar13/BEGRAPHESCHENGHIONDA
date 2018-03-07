package org.insa.graph.io;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Base class for writing binary file.
 *
 */
public abstract class BinaryReader {

    // Map version and magic number targeted for this reader.
    private int minVersion;
    private int curVersion;
    private int magicNumber;

    // InputStream
    protected DataInputStream dis;

    /**
     * Create a new BinaryReader that reads from the given stream and that expected
     * the given magic number and at least the given minimum version.
     * 
     * @param magicNumber Magic number of files to be read.
     * @param minVersion Minimum version of files to be read.
     * @param dis Input stream from which to read.
     */
    protected BinaryReader(int magicNumber, int minVersion, DataInputStream dis) {
        this.magicNumber = magicNumber;
        this.minVersion = minVersion;
        this.dis = dis;
    }

    /**
     * Check if the given version is greater than the minimum version, and update
     * the current version if it is.
     * 
     * @param version Version to check.
     * 
     * @throws BadVersionException if the given version is not greater than the
     * minimum version.
     */
    protected void checkVersionOrThrow(int version) throws BadVersionException {
        if (version < this.minVersion) {
            throw new BadVersionException(version, this.minVersion);
        }
        this.curVersion = version;
    }

    /**
     * @return The current version.
     */
    protected int getCurrentVersion() {
        return this.curVersion;
    }

    /**
     * Check if the given number matches the expected magic number.
     * 
     * @param magicNumber The magic number to check.
     * 
     * @throws BadMagicNumberException If the two magic numbers are not equal.
     */
    protected void checkMagicNumberOrThrow(int magicNumber) throws BadMagicNumberException {
        if (this.magicNumber != magicNumber) {
            throw new BadMagicNumberException(magicNumber, this.magicNumber);
        }
    }

    /**
     * Check if the next byte in the input stream correspond to the given byte.
     * 
     * This function consumes the next byte in the input stream.
     * 
     * @param b Byte to check.
     * 
     * @throws IOException if an error occurs while reading the byte.
     * @throws BadFormatException if the byte read is not the expected one.
     */
    protected void checkByteOrThrow(int b) throws IOException {
        if (dis.readUnsignedByte() != b) {
            throw new BadFormatException();
        }
    }

    /**
     * Read a byte array of fixed length from the input and convert it to a string
     * using the given charset, removing any trailing '\0'.
     * 
     * @param length Number of bytes to read.
     * @param charset Charset to use to convert the bytes into a string.
     * 
     * @return The convert string.
     * 
     * @throws IOException if an error occurs while reading or converting.
     */
    protected String readFixedLengthString(int length, String charset) throws IOException {
        byte[] bytes = new byte[length];
        this.dis.read(bytes);
        return new String(bytes, "UTF-8").trim();
    }

    /**
     * Read 24 bits in BigEndian order from the stream and return the corresponding
     * integer value.
     * 
     * @return Integer value read from the next 24 bits of the stream.
     * 
     * @throws IOException if an error occurs while reading from the stream.
     */
    protected int read24bits() throws IOException {
        int x = dis.readUnsignedShort();
        return (x << 8) | dis.readUnsignedByte();
    }
}
