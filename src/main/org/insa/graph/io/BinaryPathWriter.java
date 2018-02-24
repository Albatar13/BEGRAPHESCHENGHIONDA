package org.insa.graph.io;

import java.io.DataOutputStream;
import java.io.IOException;

import org.insa.graph.Arc;
import org.insa.graph.Path;

public class BinaryPathWriter extends BinaryWriter implements AbstractPathWriter {

    /**
     * @param dos
     */
    protected BinaryPathWriter(DataOutputStream dos) {
        super(dos);
    }

    @Override
    public void writePath(Path path) throws IOException {

        // Write magic number and version.
        dos.writeInt(BinaryPathReader.MAGIC_NUMBER);
        dos.writeInt(BinaryPathReader.VERSION);

        // Write map id.
        dos.writeInt(path.getGraph().getMapId());

        // Write number of racs
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
    }

}
