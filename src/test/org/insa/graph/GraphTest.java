package org.insa.graph;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.insa.graph.RoadInformation.RoadType;
import org.junit.BeforeClass;
import org.junit.Test;

public class GraphTest {

    // Small graph use for tests
    private static Graph graph;

    // List of nodes
    private static Node[] nodes;

    @BeforeClass
    public static void initAll() throws IOException {

        // Create nodes
        nodes = new Node[5];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = new Node(i, null);
        }

        Node.linkNodes(nodes[0], nodes[1], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, false, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[0], nodes[2], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, false, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[0], nodes[4], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null), new ArrayList<>());
        Node.linkNodes(nodes[1], nodes[2], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, false, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[2], nodes[3], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null), new ArrayList<>());
        Node.linkNodes(nodes[2], nodes[3], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null), new ArrayList<>());
        Node.linkNodes(nodes[2], nodes[3], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null), new ArrayList<>());
        Node.linkNodes(nodes[3], nodes[0], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, false, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[3], nodes[4], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null), new ArrayList<>());
        Node.linkNodes(nodes[4], nodes[0], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null), new ArrayList<>());

        graph = new Graph("ID", "", Arrays.asList(nodes), null);

    }

    /**
     * @return List of arcs between a and b.
     */
    private List<Arc> getArcsBetween(Node a, Node b) {
        List<Arc> arcs = new ArrayList<>();
        for (Arc arc: a) {
            if (arc.getDestination().equals(b)) {
                arcs.add(arc);
            }
        }
        return arcs;
    }

    @Test
    public void testTranspose() {
        Graph transpose = graph.transpose();

        // Basic asserts...
        assertEquals("R/" + graph.getMapId(), transpose.getMapId());
        assertEquals(graph.size(), transpose.size());

        final int expNbSucc[] = { 4, 2, 2, 4, 2 };
        for (int i = 0; i < expNbSucc.length; ++i) {
            assertEquals(expNbSucc[i], transpose.get(i).getNumberOfSuccessors());
        }

        assertEquals(getArcsBetween(transpose.get(0), transpose.get(1)).size(), 1);
        assertEquals(getArcsBetween(transpose.get(0), transpose.get(2)).size(), 1);
        assertEquals(getArcsBetween(transpose.get(0), transpose.get(3)).size(), 1);
        assertEquals(getArcsBetween(transpose.get(0), transpose.get(4)).size(), 1);
        assertEquals(getArcsBetween(transpose.get(1), transpose.get(0)).size(), 1);
        assertEquals(getArcsBetween(transpose.get(1), transpose.get(2)).size(), 1);
        assertEquals(getArcsBetween(transpose.get(1), transpose.get(3)).size(), 0);
        assertEquals(getArcsBetween(transpose.get(1), transpose.get(4)).size(), 0);
        assertEquals(getArcsBetween(transpose.get(2), transpose.get(0)).size(), 1);
        assertEquals(getArcsBetween(transpose.get(2), transpose.get(1)).size(), 1);
        assertEquals(getArcsBetween(transpose.get(2), transpose.get(3)).size(), 0);
        assertEquals(getArcsBetween(transpose.get(2), transpose.get(4)).size(), 0);
        assertEquals(getArcsBetween(transpose.get(3), transpose.get(0)).size(), 1);
        assertEquals(getArcsBetween(transpose.get(3), transpose.get(1)).size(), 0);
        assertEquals(getArcsBetween(transpose.get(3), transpose.get(2)).size(), 3);
        assertEquals(getArcsBetween(transpose.get(3), transpose.get(4)).size(), 0);
        assertEquals(getArcsBetween(transpose.get(4), transpose.get(0)).size(), 1);
        assertEquals(getArcsBetween(transpose.get(4), transpose.get(1)).size(), 0);
        assertEquals(getArcsBetween(transpose.get(4), transpose.get(2)).size(), 0);
        assertEquals(getArcsBetween(transpose.get(4), transpose.get(3)).size(), 1);

    }
}
