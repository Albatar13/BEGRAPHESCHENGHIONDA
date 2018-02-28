package org.insa.graph.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Point;
import org.insa.graph.RoadInformation;
import org.insa.graph.RoadInformation.RoadType;

public class BinaryGraphReaderInsa2016 extends BinaryReader implements GraphReader {

    // Map version and magic number targeted for this reader.
    private static final int VERSION = 4;
    private static final int MAGIC_NUMBER = 0xbacaff;

    /**
     * Convert a character to its corresponding road type.
     * 
     * @param ch Character to convert.
     * 
     * @return Road type corresponding to ch.
     * 
     * @see http://wiki.openstreetmap.org/wiki/Highway_tag_usage.
     */
    public static RoadType toRoadType(char ch) {
        switch (ch) {
        case 'a':
            return RoadType.MOTORWAY;
        case 'b':
            return RoadType.TRUNK;
        case 'c':
            return RoadType.PRIMARY;
        case 'd':
            return RoadType.SECONDARY;
        case 'e':
            return RoadType.MOTORWAY_LINK;
        case 'f':
            return RoadType.TRUNK_LINK;
        case 'g':
            return RoadType.PRIMARY_LINK;
        case 'h':
            return RoadType.SECONDARY_LINK;
        case 'i':
            return RoadType.TERTIARY;
        case 'j':
            return RoadType.RESIDENTIAL;
        case 'k':
            return RoadType.UNCLASSIFIED;
        case 'l':
            return RoadType.ROAD;
        case 'm':
            return RoadType.LIVING_STREET;
        case 'n':
            return RoadType.SERVICE;
        case 'o':
            return RoadType.ROUNDABOUT;
        case 'z':
            return RoadType.COASTLINE;
        }
        return RoadType.UNCLASSIFIED;
    }

    /**
     * Create a new BinaryGraphReader using the given DataInputStream.
     * 
     * @param dis
     */
    public BinaryGraphReaderInsa2016(DataInputStream dis) {
        super(MAGIC_NUMBER, VERSION, dis);
    }

    @Override
    public Graph read() throws IOException {

        // Read and check magic number and file version.
        checkMagicNumberOrThrow(dis.readInt());
        checkVersionOrThrow(dis.readInt());

        // Read map id.
        int mapId = dis.readInt();

        // Read zone.
        int graphZone = dis.readInt();

        // Number of descriptors and nodes.
        int nbDesc = dis.readInt();
        int nbNodes = dis.readInt();

        // Number of successors for each nodes.
        int[] nbSuccessors = new int[nbNodes];

        // Construct an array list with initial capacity of nbNodes.
        ArrayList<Node> nodes = new ArrayList<Node>(nbNodes);

        // Read nodes.
        for (int node = 0; node < nbNodes; ++node) {
            float longitude = ((float) dis.readInt()) / 1E6f;
            float latitude = ((float) dis.readInt()) / 1E6f;
            nbSuccessors[node] = dis.readUnsignedByte();
            nodes.add(new Node(node, new Point(longitude, latitude)));
        }

        // Check format.
        checkByteOrThrow(255);

        // Read descriptors.
        RoadInformation[] descs = new RoadInformation[nbDesc];

        // Read
        for (int descr = 0; descr < nbDesc; ++descr) {
            descs[descr] = readRoadInformation();
        }

        // Check format.
        checkByteOrThrow(254);

        // Read successors and convert to arcs.
        for (int node = 0; node < nbNodes; ++node) {
            for (int succ = 0; succ < nbSuccessors[node]; ++succ) {

                // Read destination zone.
                int destZone = dis.readUnsignedByte();

                // Read target node number.
                int destNode = this.read24bits();

                // Read information number.
                int descrNum = this.read24bits();

                // Length of the arc.
                int length = dis.readUnsignedShort();

                // Number of segments.
                int nbSegments = dis.readUnsignedShort();

                // Chain of points corresponding to the segments.
                ArrayList<Point> points = new ArrayList<Point>(nbSegments + 2);
                points.add(nodes.get(node).getPoint());

                for (int seg = 0; seg < nbSegments; ++seg) {
                    Point lastPoint = points.get(points.size() - 1);

                    float dlon = (dis.readShort()) / 2.0e5f;
                    float dlat = (dis.readShort()) / 2.0e5f;

                    points.add(new Point(lastPoint.getLongitude() + dlon, lastPoint.getLatitude() + dlat));
                }

                points.add(nodes.get(destNode).getPoint());

                if (graphZone == destZone) {

                    RoadInformation info = descs[descrNum];
                    Node orig = nodes.get(node);
                    Node dest = nodes.get(destNode);

                    // Add successor to initial arc.
                    new Arc(orig, dest, length, info, points);

                    // And reverse arc if its a two-way road.
                    if (!info.isOneWay()) {
                        // Add without segments.
                        ArrayList<Point> rPoints = new ArrayList<Point>(points);
                        Collections.reverse(rPoints);
                        new Arc(dest, orig, length, info, rPoints);
                    }

                }
            }
        }

        // Check format.
        checkByteOrThrow(253);

        return new Graph(mapId, nodes);
    }

    /**
     * Read the next road information from the stream.
     * 
     * @throws IOException
     */
    private RoadInformation readRoadInformation() throws IOException {
        char type = (char) dis.readUnsignedByte();
        int x = dis.readUnsignedByte();
        return new RoadInformation(toRoadType(type), (x & 0x80) > 0, (x & 0x7F) * 5, dis.readUTF());
    }

}
