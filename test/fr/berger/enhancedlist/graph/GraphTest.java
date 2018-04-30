package fr.berger.enhancedlist.graph;

import fr.berger.arrow.Ref;
import fr.berger.enhancedlist.graph.builder.VertexBuilder;
import fr.berger.enhancedlist.lexicon.Lexicon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
	
	// g1
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
	
	// gSym
	Vertex<Object> vs1;
	Vertex<Object> vs2;
	Vertex<Object> vs3;
	Vertex<Object> vs4;
	Vertex<Object> vs5;
	
	Edge<Object> es1;
	Edge<Object> es2;
	
	Edge<Object> es3;
	Edge<Object> es4;
	
	Edge<Object> es5;
	Edge<Object> es6;
	
	Edge<Object> es7;
	Edge<Object> es8;
	Graph<Object, Object> gSym;
	
	// gAntiSym
	Graph<Object, Object> gAntiSym;
	
	// gTransitive
	Vertex<Void> vt1;
	Vertex<Void> vt2;
	Vertex<Void> vt3;
	
	Edge<Void> et1;
	Edge<Void> et2;
	Edge<Void> et3;
	
	Graph<Void, Void> gTransitive;
	
	// gComplete
	Vertex<Void> vc1;
	Vertex<Void> vc2;
	Vertex<Void> vc3;
	Vertex<Void> vc4;
	
	Edge<Void> ec1;
	Edge<Void> ec2;
	Edge<Void> ec3;
	Edge<Void> ec4;
	Edge<Void> ec5;
	Edge<Void> ec6;
	
	Graph<Void, Void> gComplete;
	
	// gNotComplete
	Graph<Void, Void> gNotComplete;
	
	// gReflexive
	Vertex<Void> vr1;
	
	Edge<Void> er1;
	
	Graph<Void, Void> gReflexive;
	
	// gNCo
	Vertex<Void> vnco1;
	Vertex<Void> vnco2;
	Vertex<Void> vnco3;
	Vertex<Void> vnco4;
	Vertex<Void> vnco5;
	
	Edge<Void> enco1;
	Edge<Void> enco2;
	Edge<Void> enco3;
	Edge<Void> enco4;
	
	Graph<Void, Void> gNCo;
	
	@BeforeEach
	void setup() {
		// g1
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
		
		// gSym
		vs1 = new VertexBuilder<>()
				.setLabel(1)
				.createVertex();
		vs2 = new VertexBuilder<>()
				.setLabel(2)
				.createVertex();
		vs3 = new VertexBuilder<>()
				.setLabel(3)
				.createVertex();
		vs4 = new VertexBuilder<>()
				.setLabel(4)
				.createVertex();
		vs5 = new VertexBuilder<>()
				.setLabel(5)
				.createVertex();
		
		es1 = new Edge<>(vs1, vs2);
		es2 = new Edge<>(vs2, vs1);
		
		es3 = new Edge<>(vs2, vs3);
		es4 = new Edge<>(vs3, vs2);
		
		es5 = new Edge<>(vs2, vs4);
		es6 = new Edge<>(vs4, vs2);
		
		es7 = new Edge<>(vs4, vs5);
		es8 = new Edge<>(vs5, vs4);
		
		gSym = new Graph<>(true, new Lexicon<>(
				vs1, vs2, vs3, vs4, vs5
		), new Lexicon<>(
				es1, es2, es3, es4, es5, es6, es7, es8
		));
		
		// gAntiSym
		gAntiSym = new Graph<>(true, new Lexicon<>(
				vs1, vs2, vs3, vs4, vs5
		), new Lexicon<>(
				es1, es3, es5, es7
		));
		
		// gTransitive
		vt1 = new VertexBuilder<Void>()
				.setLabel(1)
				.createVertex();
		vt2 = new VertexBuilder<Void>()
				.setLabel(2)
				.createVertex();
		vt3 = new VertexBuilder<Void>()
				.setLabel(3)
				.createVertex();
		
		et1 = new Edge<>(vt2, vt1);
		et2 = new Edge<>(vt1, vt3);
		et3 = new Edge<>(vt2, vt3);
		
		gTransitive = new Graph<>(false, new Lexicon<>(
				vt1, vt2, vt3
		), new Lexicon<>(
				et1, et2, et3
		));
		
		// gComplete
		vc1 = new VertexBuilder<Void>()
				.setLabel(1)
				.createVertex();
		vc2 = new VertexBuilder<Void>()
				.setLabel(2)
				.createVertex();
		vc3 = new VertexBuilder<Void>()
				.setLabel(3)
				.createVertex();
		vc4 = new VertexBuilder<Void>()
				.setLabel(4)
				.createVertex();
		
		ec1 = new Edge<>(vc1, vc2);
		ec2 = new Edge<>(vc2, vc4);
		ec3 = new Edge<>(vc3, vc1);
		ec4 = new Edge<>(vc4, vc3);
		ec5 = new Edge<>(vc2, vc3);
		ec6 = new Edge<>(vc1, vc4);
		
		gComplete = new Graph<>(true, new Lexicon<>(
				vc1, vc2, vc3, vc4
		), new Lexicon<>(
				ec1, ec2, ec3, ec4, ec5, ec6
		));
		
		// gNotComplete
		gNotComplete = new Graph<>(true, new Lexicon<>(
				vc1, vc2, vc3, vc4
		), new Lexicon<>(
				ec1, ec5, ec4
		));
		
		// gReflexive
		vr1 = new VertexBuilder<Void>()
				.setLabel(1)
				.createVertex();
		
		er1 = new Edge<>(vr1, vr1);
		
		gReflexive = new Graph<>(true, new Lexicon<>(
				vr1
		), new Lexicon<>(
				er1
		));
		
		// gNotConnected
		vnco1 = new VertexBuilder<Void>()
				.setLabel(1)
				.createVertex();
		vnco2 = new VertexBuilder<Void>()
				.setLabel(1)
				.createVertex();
		vnco3 = new VertexBuilder<Void>()
				.setLabel(3)
				.createVertex();
		vnco4 = new VertexBuilder<Void>()
				.setLabel(4)
				.createVertex();
		vnco5 = new VertexBuilder<Void>()
				.setLabel(5)
				.createVertex();
		
		 enco1 = new Edge<>(vnco1, vnco2);
		 enco2 = new Edge<>(vnco2, vnco3);
		 enco3 = new Edge<>(vnco1, vnco3);
		 enco4 = new Edge<>(vnco5, vnco4);
		
		 gNCo = new Graph<>(true, new Lexicon<>(
				vnco1, vnco2, vnco3, vnco4, vnco5
		), new Lexicon<>(
				enco1, enco2, enco3, enco4
		));
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
	void test_searchEdge() {
		assertEquals(e11, g1.searchEdge(v11, v12));
		assertEquals(e12, g1.searchEdge(v11, v13));
		assertEquals(e13, g1.searchEdge(v12, v14));
		assertEquals(e14, g1.searchEdge(v14, v13));
		assertEquals(e15, g1.searchEdge(v13, v12));
		assertEquals(e17, g1.searchEdge(v14, v14));
		
		assertNull(g1.searchEdge(v11, v14));
		assertNull(g1.searchEdge(v14, v11));
		assertNull(g1.searchEdge(v12, v13));
		
		g1.setOriented(false);
		
		assertEquals(e11, g1.searchEdge(v12, v11));
		assertEquals(e12, g1.searchEdge(v13, v11));
		assertEquals(e13, g1.searchEdge(v14, v12));
		assertEquals(e14, g1.searchEdge(v13, v14));
		assertEquals(e15, g1.searchEdge(v12, v13));
		assertEquals(e17, g1.searchEdge(v14, v14));
		
		assertNull(g1.searchEdge(v11, v14));
		assertNull(g1.searchEdge(v14, v11));
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
	
	@Test
	void test_isSymmetrical() {
		assertTrue(gSym.isSymmetrical());
		assertFalse(gSym.isAntisymmetric());
		
		assertTrue(gAntiSym.isAntisymmetric());
		assertFalse(gAntiSym.isSymmetrical());
	}
	
	@Test
	void test_isTransitive() {
		assertTrue(gTransitive.isTransitive());
		assertFalse(g1.isTransitive());
	}
	
	@Test
	void test_isComplete() {
		assertTrue(gComplete.isComplete());
		assertFalse(gNotComplete.isComplete());
	}
	
	@Test
	void test_isReflexive() {
		assertTrue(gReflexive.isReflexive());
		assertFalse(g1.isReflexive());
	}
	
	@Test
	void test_isAntiReflexive() {
		assertTrue(gSym.isAntiReflexive());
		assertTrue(gAntiSym.isAntiReflexive());
		assertTrue(gComplete.isAntiReflexive());
		assertTrue(gNotComplete.isAntiReflexive());
		assertTrue(gTransitive.isAntiReflexive());
		assertFalse(g1.isAntiReflexive());
		assertFalse(gReflexive.isAntiReflexive());
	}
	
	@Test
	void test_isConnected() {
		assertTrue(gSym.isConnected());
		assertTrue(gAntiSym.isConnected());
		assertTrue(gComplete.isConnected());
		assertTrue(gNotComplete.isConnected());
		assertTrue(gTransitive.isConnected());
		assertTrue(g1.isConnected());
		assertTrue(gReflexive.isConnected());
		
		assertFalse(gNCo.isConnected());
	}
}