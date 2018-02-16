package org.insa.graph.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;

import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Point;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class BinaryGraphReaderTest {
	
	// Epsilon for latitude and longitude.
	private static final double EPS = 1e-5;
	
	private static Graph midip;

	@BeforeAll
    static void initAll() throws IOException {
		BinaryGraphReader reader = new BinaryGraphReader(Openfile.open("midip"));
		midip = reader.read();
    }
	
	void assertPointAt(Point p1, double longitude, double latitude) {
		assertEquals(p1.getLongitude(), longitude, EPS);
		assertEquals(p1.getLatitude(), latitude, EPS);
	}

    @Test
    void testMidipNodes() {
    		ArrayList<Node> nodes = midip.getNodes();
    	
    		assertEquals(nodes.size(), 150827);
    		
    		// Check the locations of some nodes.
    		assertPointAt(nodes.get(58411).getPoint(), 1.799864, 43.92864);
    		assertPointAt(nodes.get(133312).getPoint(), 0.539752, 43.317505);
    		assertPointAt(nodes.get(113688).getPoint(), 1.682739, 44.799774);
    		assertPointAt(nodes.get(118141).getPoint(), 0.274857, 43.47475);
    		assertPointAt(nodes.get(146918).getPoint(), 0.116148, 43.811386);

    }
    
    @Test
    void testMidipArcs() {
    		// TODO: Check the number of edges.
    		// TODO: Check information for some edges.
    }
    
}
