package org.insa.graph.io;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Base class for writing binary file.
 *
 */
public abstract class BinaryWriter {

    // Output stream.
    protected DataOutputStream dos;

    /**
     * Create a new BinaryWriter that writes to the given output stream.
     * 
     * @param dos Stream to write to.
     */
    protected BinaryWriter(DataOutputStream dos) {
        this.dos = dos;
    }

    /**
     * Write a 24-bits integer in BigEndian order to the output stream.
     * 
     * @param value Value to write.
     * 
     * @throws IOException if an error occurs while writing to the stream.
     */
    protected void write24bits(int value) throws IOException {
        dos.writeShort(value >> 8);
        dos.writeByte(value & 0xff);
    }

}
