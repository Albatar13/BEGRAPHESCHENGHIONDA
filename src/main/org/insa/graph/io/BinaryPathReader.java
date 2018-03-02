package org.insa.graph.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class BinaryPathReader extends BinaryReader implements PathReader {

    // Map version and magic number targeted for this reader.
    protected static final int VERSION = 1;
    protected static final int MAGIC_NUMBER = 0xdecafe;

    public BinaryPathReader(DataInputStream dis) {
        super(MAGIC_NUMBER, VERSION, dis);
    }

    @Override
    public Path readPath(Graph graph) throws IOException {

        // Read and check magic number and version.
        checkMagicNumberOrThrow(dis.readInt());
        checkVersionOrThrow(dis.readInt());

        // Read map ID and check against graph.
        byte[] mapIdBytes = new byte[BinaryGraphReaderInsa2018.MAP_ID_FIELD_LENGTH];
        dis.read(mapIdBytes);

        String mapId = new String(mapIdBytes, "UTF-8");

        if (!mapId.equals(graph.getMapId())) {
            throw new MapMismatchException(mapId, graph.getMapId());
        }

        // Number of nodes in the path (without first and last).
        int nbNodes = dis.readInt();

        // Skip (duplicate) first and last node
        readNode(graph);
        readNode(graph);

        // Read intermediate nodes:
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (int i = 0; i < nbNodes; ++i) {
            nodes.add(readNode(graph));
        }

        return Path.createFastestPathFromNodes(graph, nodes);
    }

    /**
     * Read a node from the stream and returns id.
     * 
     * @return
     * @throws IOException
     */
    protected Node readNode(Graph graph) throws IOException {
        // Discard zone.
        dis.readUnsignedByte();

        return graph.getNodes().get(read24bits());
    }

}
