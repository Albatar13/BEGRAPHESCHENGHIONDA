package org.insa.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.insa.graph.RoadInformation.RoadType;
import org.junit.BeforeClass;
import org.junit.Test;

public class PathTest {

    // Small graph use for tests
    private static Graph graph;

    // List of nodes
    private static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d;

    // Some paths...
    private static Path emptyPath, singleNodePath, shortPath, longPath, loopPath, longLoopPath,
            invalidPath;

    @BeforeClass
    public static void initAll() throws IOException {

        // 10 and 20 meters per seconds
        RoadInformation speed10 = new RoadInformation(RoadType.MOTORWAY, null, true, 36, ""),
                speed20 = new RoadInformation(RoadType.MOTORWAY, null, true, 72, "");

        // Create nodes
        nodes = new Node[5];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = new Node(i, null);
        }

        // Add arcs...
        a2b = Node.linkNodes(nodes[0], nodes[1], 10, speed10, null);
        a2c = Node.linkNodes(nodes[0], nodes[2], 15, speed10, null);
        a2e = Node.linkNodes(nodes[0], nodes[4], 15, speed20, null);
        b2c = Node.linkNodes(nodes[1], nodes[2], 10, speed10, null);
        c2d_1 = Node.linkNodes(nodes[2], nodes[3], 20, speed10, null);
        c2d_2 = Node.linkNodes(nodes[2], nodes[3], 10, speed10, null);
        c2d_3 = Node.linkNodes(nodes[2], nodes[3], 15, speed20, null);
        d2a = Node.linkNodes(nodes[3], nodes[0], 15, speed10, null);
        d2e = Node.linkNodes(nodes[3], nodes[4], 22.8f, speed20, null);
        e2d = Node.linkNodes(nodes[4], nodes[0], 10, speed10, null);

        graph = new Graph("ID", "", Arrays.asList(nodes), null);

        emptyPath = new Path(graph, new ArrayList<Arc>());
        singleNodePath = new Path(graph, nodes[1]);
        shortPath = new Path(graph, Arrays.asList(new Arc[] { a2b, b2c, c2d_1 }));
        longPath = new Path(graph, Arrays.asList(new Arc[] { a2b, b2c, c2d_1, d2e }));
        loopPath = new Path(graph, Arrays.asList(new Arc[] { a2b, b2c, c2d_1, d2a }));
        longLoopPath = new Path(graph,
                Arrays.asList(new Arc[] { a2b, b2c, c2d_1, d2a, a2c, c2d_3, d2a, a2b, b2c }));
        invalidPath = new Path(graph, Arrays.asList(new Arc[] { a2b, c2d_1, d2e }));

    }

    @Test
    public void testConstructor() {
        assertEquals(graph, emptyPath.getGraph());
        assertEquals(graph, singleNodePath.getGraph());
        assertEquals(graph, shortPath.getGraph());
        assertEquals(graph, longPath.getGraph());
        assertEquals(graph, loopPath.getGraph());
        assertEquals(graph, longLoopPath.getGraph());
        assertEquals(graph, invalidPath.getGraph());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testImmutability() {
        emptyPath.getArcs().add(a2b);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(emptyPath.isEmpty());

        assertFalse(singleNodePath.isEmpty());
        assertFalse(shortPath.isEmpty());
        assertFalse(longPath.isEmpty());
        assertFalse(loopPath.isEmpty());
        assertFalse(longLoopPath.isEmpty());
        assertFalse(invalidPath.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(0, emptyPath.size());
        assertEquals(1, singleNodePath.size());
        assertEquals(4, shortPath.size());
        assertEquals(5, longPath.size());
        assertEquals(5, loopPath.size());
        assertEquals(10, longLoopPath.size());
    }

    @Test
    public void testIsValid() {
        assertTrue(emptyPath.isValid());
        assertTrue(singleNodePath.isValid());
        assertTrue(shortPath.isValid());
        assertTrue(longPath.isValid());
        assertTrue(loopPath.isValid());
        assertTrue(longLoopPath.isValid());

        assertFalse(invalidPath.isValid());
    }

    @Test
    public void testGetLength() {
        assertEquals(0, emptyPath.getLength(), 1e-6);
        assertEquals(0, singleNodePath.getLength(), 1e-6);
        assertEquals(40, shortPath.getLength(), 1e-6);
        assertEquals(62.8, longPath.getLength(), 1e-6);
        assertEquals(55, loopPath.getLength(), 1e-6);
        assertEquals(120, longLoopPath.getLength(), 1e-6);
    }

    @Test
    public void testGetTravelTime() {
        // Note: 18 km/h = 5m/s
        assertEquals(0, emptyPath.getTravelTime(18), 1e-6);
        assertEquals(0, singleNodePath.getTravelTime(18), 1e-6);
        assertEquals(8, shortPath.getTravelTime(18), 1e-6);
        assertEquals(12.56, longPath.getTravelTime(18), 1e-6);
        assertEquals(11, loopPath.getTravelTime(18), 1e-6);
        assertEquals(24, longLoopPath.getTravelTime(18), 1e-6);

        // Note: 28.8 km/h = 8m/s
        assertEquals(0, emptyPath.getTravelTime(28.8), 1e-6);
        assertEquals(0, singleNodePath.getTravelTime(28.8), 1e-6);
        assertEquals(5, shortPath.getTravelTime(28.8), 1e-6);
        assertEquals(7.85, longPath.getTravelTime(28.8), 1e-6);
        assertEquals(6.875, loopPath.getTravelTime(28.8), 1e-6);
        assertEquals(15, longLoopPath.getTravelTime(28.8), 1e-6);
    }

    @Test
    public void testGetMinimumTravelTime() {
        assertEquals(0, emptyPath.getMinimumTravelTime(), 1e-4);
        assertEquals(0, singleNodePath.getLength(), 1e-4);
        assertEquals(4, shortPath.getMinimumTravelTime(), 1e-4);
        assertEquals(5.14, longPath.getMinimumTravelTime(), 1e-4);
        assertEquals(5.5, loopPath.getMinimumTravelTime(), 1e-4);
        assertEquals(11.25, longLoopPath.getMinimumTravelTime(), 1e-4);
    }

    @Test
    public void testCreateFastestPathFromNodes() {
        Path path;
        Arc[] expected;

        // Simple construction
        path = Path.createFastestPathFromNodes(graph,
                Arrays.asList(new Node[] { nodes[0], nodes[1], nodes[2] }));
        expected = new Arc[] { a2b, b2c };
        assertEquals(expected.length, path.getArcs().size());
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], path.getArcs().get(i));
        }

        // Not so simple construction
        path = Path.createFastestPathFromNodes(graph,
                Arrays.asList(new Node[] { nodes[0], nodes[1], nodes[2], nodes[3] }));
        expected = new Arc[] { a2b, b2c, c2d_3 };
        assertEquals(expected.length, path.getArcs().size());
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], path.getArcs().get(i));
        }

        // Trap construction!
        path = Path.createFastestPathFromNodes(graph, Arrays.asList(new Node[] { nodes[1] }));
        assertEquals(nodes[1], path.getOrigin());
        assertEquals(0, path.getArcs().size());

        // Trap construction - The return!
        path = Path.createFastestPathFromNodes(graph, Arrays.asList(new Node[0]));
        assertEquals(null, path.getOrigin());
        assertEquals(0, path.getArcs().size());
        assertTrue(path.isEmpty());
    }

    @Test
    public void testCreateShortestPathFromNodes() {
        Path path;
        Arc[] expected;

        // Simple construction
        path = Path.createShortestPathFromNodes(graph,
                Arrays.asList(new Node[] { nodes[0], nodes[1], nodes[2] }));
        expected = new Arc[] { a2b, b2c };
        assertEquals(expected.length, path.getArcs().size());
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], path.getArcs().get(i));
        }

        // Not so simple construction
        path = Path.createShortestPathFromNodes(graph,
                Arrays.asList(new Node[] { nodes[0], nodes[1], nodes[2], nodes[3] }));
        expected = new Arc[] { a2b, b2c, c2d_2 };
        assertEquals(expected.length, path.getArcs().size());
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], path.getArcs().get(i));
        }

        // Trap construction!
        path = Path.createShortestPathFromNodes(graph, Arrays.asList(new Node[] { nodes[1] }));
        assertEquals(nodes[1], path.getOrigin());
        assertEquals(0, path.getArcs().size());

        // Trap construction - The return!
        path = Path.createShortestPathFromNodes(graph, Arrays.asList(new Node[0]));
        assertEquals(null, path.getOrigin());
        assertEquals(0, path.getArcs().size());
        assertTrue(path.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFastestPathFromNodesException() {
        Path.createFastestPathFromNodes(graph, Arrays.asList(new Node[] { nodes[1], nodes[0] }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateShortestPathFromNodesException() {
        Path.createShortestPathFromNodes(graph, Arrays.asList(new Node[] { nodes[1], nodes[0] }));
    }

}
