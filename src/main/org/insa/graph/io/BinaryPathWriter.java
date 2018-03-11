package org.insa.graph.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.insa.graph.Arc;
import org.insa.graph.Path;

/**
 * Implementation of {@link PathWriter} to write paths in binary format.
 *
 */
public class BinaryPathWriter extends BinaryWriter implements PathWriter {

    /**
     * Create a new BinaryPathWriter that writes to the given output stream.
     * 
     * @param dos Output stream to write to.
     */
    public BinaryPathWriter(DataOutputStream dos) {
        super(dos);
    }

    @Override
    public void writePath(Path path) throws IOException {

        // Write magic number and version.
        dos.writeInt(BinaryPathReader.MAGIC_NUMBER);
        dos.writeInt(BinaryPathReader.VERSION);

        // Write map id.
        byte[] bytes = Arrays.copyOf(path.getGraph().getMapId().getBytes("UTF-8"),
                BinaryGraphReader.MAP_ID_FIELD_LENGTH);
        dos.write(bytes);

        // Write number of arcs
        dos.writeInt(path.getArcs().size() + 1);

        // Write origin / destination.
        dos.writeInt(path.getOrigin().getId());
        dos.writeInt(path.getDestination().getId());

        // Write nodes.
        dos.writeInt(path.getOrigin().getId());
        for (Arc arc: path.getArcs()) {
            dos.writeInt(arc.getDestination().getId());
        }

        dos.flush();
        dos.close();
    }

}
