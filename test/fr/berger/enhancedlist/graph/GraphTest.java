package fr.berger.enhancedlist.graph;

import fr.berger.arrow.Ref;
import fr.berger.enhancedlist.graph.builder.VertexBuilder;
import fr.berger.enhancedlist.lexicon.Lexicon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
	
	Vertex<Object> v11;
	Vertex<Object> v12;
	Vertex<Object> v13;
	Vertex<Object> v14;
	Edge<Object> e11;
	Edge<Object> e12;
	Edge<Object> e13;
	Edge<Object> e14;
	Edge<Object> e15;
	Edge<Object> e16;
	Edge<Object> e17;
	Graph<Object, Object> g1; // See course page 1
	
	@BeforeEach
	void setup() {
		v11 = new VertexBuilder<>()
				.setLabel(1)
				.createVertex();
		v12 = new VertexBuilder<>()
				.setLabel(2)
				.createVertex();
		v13 = new VertexBuilder<>()
				.setLabel(3)
				.createVertex();
		v14 = new VertexBuilder<>()
				.setLabel(4)
				.createVertex();
		
		e11 = new Edge<>(v11, v12);
		e12 = new Edge<>(v11, v13);
		e13 = new Edge<>(v12, v14);
		e14 = new Edge<>(v14, v13);
		e15 = new Edge<>(v13, v12);
		e16 = new Edge<>(v13, v12);
		e17 = new Edge<>(v14, v14);
		
		g1 = new Graph<>(true, new Lexicon<>(
				v11, v12, v13, v14
		), new Lexicon<>(
				e11, e12, e13, e14, e15, e16, e17
		));
		
		System.out.println("GraphTest.setup> g1 created: " + g1.toString());
	}
	
	@Test
	void test_getSuccessors() {
		assertEquals(new Lexicon<>(v12, v13), g1.getSuccessors(v11));
		assertEquals(new Lexicon<>(v14), g1.getSuccessors(v12));
		assertEquals(new Lexicon<>(v12, v12), g1.getSuccessors(v13));
		assertNotEquals(new Lexicon<>(v12), g1.getSuccessors(v13));
		assertEquals(new Lexicon<>(v13, v14), g1.getSuccessors(v14));
		assertNotEquals(new Lexicon<>(v14), g1.getSuccessors(v14));
	}
	
	@Test
	void test_getPredecessors() {
		assertEquals(new Lexicon<>(), g1.getPredecessors(v11));
		assertEquals(new Lexicon<>(v11, v13, v13), g1.getPredecessors(v12));
		assertNotEquals(new Lexicon<>(v11, v13), g1.getPredecessors(v12));
		assertEquals(new Lexicon<>(v11, v14), g1.getPredecessors(v13));
		assertEquals(new Lexicon<>(v12, v14), g1.getPredecessors(v14));
		assertNotEquals(new Lexicon<>(v12), g1.getPredecessors(v14));
		assertNotEquals(new Lexicon<>(v14), g1.getPredecessors(v14));
	}
	
	@Test
	void test_getSources() {
		assertEquals(new Lexicon<>(v11), g1.getSources());
	}
	
	@Test
	void test_getSinks() {
		assertEquals(new Lexicon<>(), g1.getSinks());
	}
	
	@Test
	void test_getInDegree() {
		assertEquals(0, g1.getInDegree(v11));
		assertEquals(3, g1.getInDegree(v12));
		assertEquals(2, g1.getInDegree(v13));
		assertEquals(2, g1.getInDegree(v14));
	}
	
	@Test
	void test_getOutDegree() {
		assertEquals(2, g1.getOutDegree(v11));
		assertEquals(1, g1.getOutDegree(v12));
		assertEquals(2, g1.getOutDegree(v13));
		assertEquals(2, g1.getOutDegree(v14));
	}
	
	@Test
	void test_getDegree() {
		assertEquals(2, g1.getDegree(v11));
		assertEquals(4, g1.getDegree(v12));
		assertEquals(4, g1.getDegree(v13));
		assertEquals(4, g1.getDegree(v14));
	}
	
	@Test
	void areAdjacent() {
		/* VERTICES */
		assertTrue(g1.areAdjacent(v11, v12));
		assertTrue(g1.areAdjacent(v12, v11));
		
		assertTrue(g1.areAdjacent(v12, v13));
		assertTrue(g1.areAdjacent(v13, v12));
		
		assertTrue(g1.areAdjacent(v12, v14));
		assertTrue(g1.areAdjacent(v14, v12));
		
		assertTrue(g1.areAdjacent(v11, v13));
		assertTrue(g1.areAdjacent(v13, v11));
		
		assertTrue(g1.areAdjacent(v14, v13));
		assertTrue(g1.areAdjacent(v13, v14));
		
		assertTrue(g1.areAdjacent(v14, v14));
		
		
		assertFalse(g1.areAdjacent(v11, v14));
		assertFalse(g1.areAdjacent(v14, v11));
		
		assertFalse(g1.areAdjacent(v11, v11));
		assertFalse(g1.areAdjacent(v12, v12));
		assertFalse(g1.areAdjacent(v13, v13));
		
		/* EDGES */
		assertTrue(g1.areAdjacent(e11, e13));
		assertTrue(g1.areAdjacent(e13, e11));
		
		assertTrue(g1.areAdjacent(e11, e15));
		assertTrue(g1.areAdjacent(e15, e11));
		
		assertTrue(g1.areAdjacent(e11, e16));
		assertTrue(g1.areAdjacent(e16, e11));
		
		assertTrue(g1.areAdjacent(e12, e15));
		assertTrue(g1.areAdjacent(e15, e12));
		
		assertTrue(g1.areAdjacent(e12, e16));
		assertTrue(g1.areAdjacent(e16, e12));
		
		assertTrue(g1.areAdjacent(e12, e14));
		assertTrue(g1.areAdjacent(e14, e12));
		
		assertTrue(g1.areAdjacent(e15, e16));
		assertTrue(g1.areAdjacent(e16, e15));
		
		assertTrue(g1.areAdjacent(e13, e15));
		assertTrue(g1.areAdjacent(e15, e13));
		
		assertTrue(g1.areAdjacent(e13, e16));
		assertTrue(g1.areAdjacent(e16, e13));
		
		assertTrue(g1.areAdjacent(e13, e17));
		assertTrue(g1.areAdjacent(e17, e13));
		
		assertTrue(g1.areAdjacent(e14, e15));
		assertTrue(g1.areAdjacent(e15, e14));
		
		assertTrue(g1.areAdjacent(e14, e16));
		assertTrue(g1.areAdjacent(e16, e14));
		
		assertTrue(g1.areAdjacent(e17, e17));
		
		
		assertFalse(g1.areAdjacent(e11, e14));
		assertFalse(g1.areAdjacent(e14, e11));
		
		assertFalse(g1.areAdjacent(e12, e13));
		assertFalse(g1.areAdjacent(e13, e12));
		
		assertFalse(g1.areAdjacent(e15, e17));
		assertFalse(g1.areAdjacent(e17, e15));
		
		assertFalse(g1.areAdjacent(e16, e17));
		assertFalse(g1.areAdjacent(e17, e16));
	}
}