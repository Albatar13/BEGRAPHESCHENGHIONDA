package org.insa.graph;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;

public class PathTest {

    // Small graph use for tests
    private Graph graph;

    @BeforeAll
    static void initAll() throws IOException {
        Node[] nodes = new Node[] {};

        graph = new Graph(0, Arrays.asList(nodes));
    }

}
