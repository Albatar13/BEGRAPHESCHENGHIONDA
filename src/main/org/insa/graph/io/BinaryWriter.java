package org.insa.graph.io;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class BinaryWriter {

    // Output stream.
    protected DataOutputStream dos;

    /**
     * @param dos
     */
    protected BinaryWriter(DataOutputStream dos) {
        this.dos = dos;
    }

    /**
     * Write a 24-bits integer in BigEndian to the output stream.
     * 
     * @param value
     * 
     * @throws IOException
     */
    protected void write24bits(int value) throws IOException {
        dos.writeShort(value >> 8);
        dos.writeByte(value & 0xff);
    }

}
