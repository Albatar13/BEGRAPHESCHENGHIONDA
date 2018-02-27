package org.insa.graph.io;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Point;
import org.junit.BeforeClass;
import org.junit.Test;

public class BinaryGraphReaderTest {

    // Epsilon for latitude and longitude.
    private static final double EPS = 1e-5;

    private static Graph midip;

    @BeforeClass
    public static void initAll() throws IOException {
        BinaryGraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream("Maps/midip.map"))));
        midip = reader.read();
    }

    public void assertPointAt(Point p1, double longitude, double latitude) {
        assertEquals(p1.getLongitude(), longitude, EPS);
        assertEquals(p1.getLatitude(), latitude, EPS);
    }

    @Test
    public void testMidipNodes() {
        List<Node> nodes = midip.getNodes();

        assertEquals(nodes.size(), 150827);

        // Check the locations of some nodes.
        assertPointAt(nodes.get(58411).getPoint(), 1.799864, 43.92864);
        assertPointAt(nodes.get(133312).getPoint(), 0.539752, 43.317505);
        assertPointAt(nodes.get(113688).getPoint(), 1.682739, 44.799774);
        assertPointAt(nodes.get(118141).getPoint(), 0.274857, 43.47475);
        assertPointAt(nodes.get(146918).getPoint(), 0.116148, 43.811386);

    }

    @Test
    public void testMidipArcs() {
        // TODO: Check the number of edges.
        // TODO: Check information for some edges.
    }

}
