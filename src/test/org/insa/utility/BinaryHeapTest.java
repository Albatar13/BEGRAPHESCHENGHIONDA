package org.insa.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class BinaryHeapTest {
	
	private BinaryHeap<Integer> rangeHeap1;
	
	static IntStream dataRange1() {
		return IntStream.range(0, 20);
	}
	
	@BeforeAll
    static void initAll() {
		
    }

    @BeforeEach
    void init() {
    		// Create the range heap
    		this.rangeHeap1 = new BinaryHeap<Integer>();
    		dataRange1().forEach((int x) -> rangeHeap1.insert(x));
    }

    @Test
    void testInsert() {
    		BinaryHeap<Integer> heap = new BinaryHeap<Integer>();
    		int size = 0;
    		for (int x: dataRange1().toArray()) {
    			heap.insert(x);
    			size += 1;
    			assertEquals(heap.size(), size);
    		}
    }
    
    @Test
    void testDeleteMin() {
		int[] range1 = dataRange1().toArray();
		int size = range1.length;
		assertEquals(rangeHeap1.size(), size);
		for (int x: range1) {
			assertEquals(rangeHeap1.deleteMin().intValue(), x);
			size -= 1;
			assertEquals(rangeHeap1.size(), size);
		}
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }
    
}
