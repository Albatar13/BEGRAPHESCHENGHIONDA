package org.insa.graph.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class BinaryReader {

    // Map version and magic number targeted for this reader.
    private int minVersion;
    private int curVersion;
    private int magicNumber;

    // InputStream
    protected DataInputStream dis;

    // List of observers
    protected List<GraphReaderObserver> observers = new ArrayList<>();

    protected BinaryReader(int magicNumber, int minVersion, DataInputStream dis) {
        this.magicNumber = magicNumber;
        this.minVersion = minVersion;
        this.dis = dis;
    }

    /**
     * {@inheritDoc}
     */
    public void addObserver(GraphReaderObserver observer) {
        observers.add(observer);
    }

    /**
     * Check if the given version is greater than the minimum version, and update
     * the current version if it is.
     * 
     * @param version
     * @throws BadVersionException
     */
    protected void checkVersionOrThrow(int version) throws BadVersionException {
        if (version < this.minVersion) {
            throw new BadVersionException(version, this.minVersion);
        }
        this.curVersion = version;
    }

    /**
     * @return the current version.
     */
    protected int getCurrentVersion() {
        return this.curVersion;
    }

    /**
     * @param magicNumber
     * @throws BadMagicNumberException
     */
    protected void checkMagicNumberOrThrow(int magicNumber) throws BadMagicNumberException {
        if (this.magicNumber != magicNumber) {
            throw new BadMagicNumberException(magicNumber, this.magicNumber);
        }
    }

    /**
     * Check if the next byte in the input stream correspond to the given byte. This
     * function consumes the next byte in the input stream.
     * 
     * @param i Byte to check against.
     * 
     * @throws IOException
     */
    protected void checkByteOrThrow(int i) throws IOException {
        if (dis.readUnsignedByte() != i) {
            throw new BadFormatException();
        }
    }

    /**
     * Read 24 bits from the stream and return the corresponding integer value.
     * 
     * @return Integer value read from the next 24 bits of the stream.
     * 
     * @throws IOException
     */
    protected int read24bits() throws IOException {
        int x = dis.readUnsignedShort();
        return (x << 8) | dis.readUnsignedByte();
    }
}
