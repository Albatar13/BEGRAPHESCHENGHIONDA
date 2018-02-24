package org.insa.graph;

import java.io.IOException;
import java.util.Arrays;

import org.insa.graph.RoadInformation.RoadType;
import org.junit.jupiter.api.BeforeAll;

public class PathTest {

    // Small graph use for tests
    private static Graph graph;

    @BeforeAll
    static void initAll() throws IOException {

        RoadInformation speed10 = new RoadInformation(RoadType.ROAD, true, 10, ""),
                speed20 = new RoadInformation(RoadType.ROAD, true, 20, "");

        // Create nodes
        Node[] nodes = new Node[5];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = new Node(i, null);
        }

        // Add arcs...
        new Arc(nodes[0], nodes[1], 10, speed10, null);
        new Arc(nodes[0], nodes[3], 15, speed10, null);
        new Arc(nodes[0], nodes[4], 15, speed20, null);
        new Arc(nodes[1], nodes[2], 10, speed10, null);
        new Arc(nodes[2], nodes[3], 20, speed10, null);
        new Arc(nodes[2], nodes[3], 10, speed10, null);
        new Arc(nodes[2], nodes[3], 15, speed20, null);
        new Arc(nodes[3], nodes[0], 15, speed10, null);
        new Arc(nodes[3], nodes[4], 20, speed20, null);
        new Arc(nodes[0], nodes[1], 10, speed10, null);

        graph = new Graph(0, Arrays.asList(nodes));
    }

}
