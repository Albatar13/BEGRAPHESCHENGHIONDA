package org.insa.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.insa.graph.RoadInformation.RoadType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class PathTest {

    // Small graph use for tests
    private static Graph graph;

    // List of nodes
    private static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d;

    // Some paths...
    private static Path emptyPath, shortPath, longPath, loopPath, longLoopPath, invalidPath;

    @BeforeAll
    static void initAll() throws IOException {

        // 10 and 20 meters per seconds
        RoadInformation speed10 = new RoadInformation(RoadType.ROAD, true, 36, ""),
                speed20 = new RoadInformation(RoadType.ROAD, true, 72, "");

        // Create nodes
        nodes = new Node[5];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = new Node(i, null);
        }

        // Add arcs...
        a2b = new Arc(nodes[0], nodes[1], 10, speed10, null);
        a2c = new Arc(nodes[0], nodes[2], 15, speed10, null);
        a2e = new Arc(nodes[0], nodes[4], 15, speed20, null);
        b2c = new Arc(nodes[1], nodes[2], 10, speed10, null);
        c2d_1 = new Arc(nodes[2], nodes[3], 20, speed10, null);
        c2d_2 = new Arc(nodes[2], nodes[3], 10, speed10, null);
        c2d_3 = new Arc(nodes[2], nodes[3], 15, speed20, null);
        d2a = new Arc(nodes[3], nodes[0], 15, speed10, null);
        d2e = new Arc(nodes[3], nodes[4], 20, speed20, null);
        e2d = new Arc(nodes[4], nodes[0], 10, speed10, null);

        graph = new Graph(0, Arrays.asList(nodes));

        emptyPath = new Path(graph, new ArrayList<Arc>());
        shortPath = new Path(graph, Arrays.asList(new Arc[] { a2b, b2c, c2d_1 }));
        longPath = new Path(graph, Arrays.asList(new Arc[] { a2b, b2c, c2d_1, d2e }));
        loopPath = new Path(graph, Arrays.asList(new Arc[] { a2b, b2c, c2d_1, d2a }));
        longLoopPath = new Path(graph, Arrays.asList(new Arc[] { a2b, b2c, c2d_1, d2a, a2c, c2d_3, d2a, a2b, b2c }));
        invalidPath = new Path(graph, Arrays.asList(new Arc[] { a2b, c2d_1, d2e }));

    }

    @Test
    void testConstructor() {
        assertEquals(graph, emptyPath.getGraph());
        assertEquals(graph, shortPath.getGraph());
        assertEquals(graph, longPath.getGraph());
        assertEquals(graph, loopPath.getGraph());
        assertEquals(graph, longLoopPath.getGraph());
        assertEquals(graph, invalidPath.getGraph());
    }

    @Test
    void testImmutability() {
        assertThrows(UnsupportedOperationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                emptyPath.getArcs().add(a2b);
            }
        });
        assertThrows(UnsupportedOperationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                shortPath.getArcs().add(d2e);
            }
        });
    }

    @Test
    void testIsEmpty() {
        assertTrue(emptyPath.isEmpty());

        assertFalse(shortPath.isEmpty());
        assertFalse(longPath.isEmpty());
        assertFalse(loopPath.isEmpty());
        assertFalse(longLoopPath.isEmpty());
        assertFalse(invalidPath.isEmpty());
    }

    @Test
    void testIsValid() {
        assertTrue(emptyPath.isValid());
        assertTrue(shortPath.isValid());
        assertTrue(longPath.isValid());
        assertTrue(loopPath.isValid());
        assertTrue(longLoopPath.isValid());

        assertFalse(invalidPath.isValid());
    }

    @Test
    void testGetLength() {
        assertEquals(emptyPath.getLength(), 0);
        assertEquals(shortPath.getLength(), 40);
        assertEquals(longPath.getLength(), 60);
        assertEquals(loopPath.getLength(), 55);
        assertEquals(longLoopPath.getLength(), 120);
    }

    @Test
    void testGetMinimumTravelTime() {
        assertEquals(emptyPath.getMinimumTravelTime(), 0, 1e-4);
        assertEquals(shortPath.getMinimumTravelTime(), 4, 1e-4);
        assertEquals(longPath.getMinimumTravelTime(), 5, 1e-4);
        assertEquals(loopPath.getMinimumTravelTime(), 5.5, 1e-4);
        assertEquals(longLoopPath.getMinimumTravelTime(), 11.25, 1e-4);
    }

    @Test
    void testCreateFastestPathFromNodes() {
        Path path;
        Arc[] expected;

        // Simple construction
        path = Path.createFastestPathFromNodes(graph, Arrays.asList(new Node[] { nodes[0], nodes[1], nodes[2] }));
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

        // Wrong construction
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Path.createFastestPathFromNodes(graph, Arrays.asList(new Node[] { nodes[1], nodes[0] }));
            }
        });
    }

    @Test
    void testCreateShortestPathFromNodes() {
        Path path;
        Arc[] expected;

        // Simple construction
        path = Path.createShortestPathFromNodes(graph, Arrays.asList(new Node[] { nodes[0], nodes[1], nodes[2] }));
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

        // Wrong construction
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Path.createShortestPathFromNodes(graph, Arrays.asList(new Node[] { nodes[1], nodes[0] }));
            }
        });
    }
}
