package org.insa.graph.io;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class BinaryReader {
	
	// Map version and magic number targeted for this reader.
	private int version;
	private int magicNumber;
	
	// InputStream
	protected DataInputStream dis;
	
	protected BinaryReader(int magicNumber, int version, DataInputStream dis) {
		this.magicNumber = magicNumber;
		this.version = version;
		this.dis = dis;
	}
	
	/**
	 * @param version
	 * @throws BadVersionException 
	 */
	public void checkVersionOrThrow(int version) throws BadVersionException {
		if (this.version != version) {
			throw new BadVersionException(version, this.version);
		}
	}
	
	/**
	 * @param magicNumber
	 * @throws BadMagicNumberException 
	 */
	public void checkMagicNumberOrThrow(int magicNumber) throws BadMagicNumberException {
		if (this.magicNumber != magicNumber) {
			throw new BadMagicNumberException(magicNumber, this.magicNumber);
		}
	}
	
	/**
	 * Check if the next byte in the input stream correspond to the
	 * given byte. This function consumes the next byte in the input
	 * stream.
	 * 
	 * @param i Byte to check against.
	 * 
	 * @throws IOException 
	 */
	public void checkByteOrThrow(int i) throws IOException {
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
		int x = dis.readUnsignedShort() ;
		return (x << 8) | dis.readUnsignedByte() ;
	}
}
