package fr.berger.enhancedlist.graph;

import fr.berger.arrow.Ref;
import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.algorithm.Dijkstra;
import fr.berger.enhancedlist.graph.builder.VertexBuilder;
import fr.berger.enhancedlist.lexicon.Lexicon;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

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
	
	// g2
	Vertex<Void> v2a;
	Vertex<Void> v2b;
	Vertex<Void> v2c;
	Vertex<Void> v2d;
	Vertex<Void> v2e;
	Vertex<Void> v2f;
	Vertex<Void> v2g;
	Vertex<Void> v2h;
	Vertex<Void> v2i;
	Vertex<Void> v2j;
	Vertex<Void> v2k;
	Vertex<Void> v2l;
	Edge<Void> e21;
	Edge<Void> e22;
	Edge<Void> e23;
	Edge<Void> e24;
	Edge<Void> e25;
	Edge<Void> e26;
	Edge<Void> e27;
	Edge<Void> e28;
	Edge<Void> e29;
	Edge<Void> e210;
	Edge<Void> e211;
	Edge<Void> e212;
	Edge<Void> e213;
	Edge<Void> e214;
	Edge<Void> e215;
	Edge<Void> e216;
	Edge<Void> e217;
	Edge<Void> e218;
	Graph<Void, Void> g2; // See TD1
	
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
		
		// g2
		v2a = new Vertex<>("a");
		v2b = new Vertex<>("b");
		v2c = new Vertex<>("c");
		v2d = new Vertex<>("d");
		v2e = new Vertex<>("e");
		v2f = new Vertex<>("f");
		v2g = new Vertex<>("g");
		v2h = new Vertex<>("h");
		v2i = new Vertex<>("i");
		v2j = new Vertex<>("j");
		v2k = new Vertex<>("k");
		v2l = new Vertex<>("l");
		e21 = new Edge<>(v2a, v2b);
		e22 = new Edge<>(v2b, v2c);
		e23 = new Edge<>(v2c, v2d);
		e24 = new Edge<>(v2d, v2e);
		e25 = new Edge<>(v2e, v2f);
		e26 = new Edge<>(v2f, v2g);
		e27 = new Edge<>(v2g, v2c);
		e28 = new Edge<>(v2d, v2f);
		e29 = new Edge<>(v2a, v2c);
		e210 = new Edge<>(v2a, v2g);
		e211 = new Edge<>(v2e, v2l);
		e212 = new Edge<>(v2a, v2h);
		e213 = new Edge<>(v2h, v2g);
		e214 = new Edge<>(v2h, v2e);
		e215 = new Edge<>(v2k, v2i);
		e216 = new Edge<>(v2i, v2k);
		e217 = new Edge<>(v2i, v2j);
		e218 = new Edge<>(v2k, v2j);
		g2 = new Graph<>(true, new Lexicon<>(
				v2a, v2b, v2c, v2d, v2e, v2f, v2g, v2h, v2i, v2j, v2k, v2l
		), new Lexicon<>(
				e21, e22, e23, e24, e25, e26, e27, e28, e29, e210, e211, e212, e213, e214, e215, e216, e217, e218
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
	
	@AfterEach
	void tearDown() {
		System.out.println();
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
		assertEquals(new Lexicon<>(v2a), g2.getSources());
	}
	
	@Test
	void test_getSinks() {
		assertEquals(new Lexicon<>(), g1.getSinks());
		assertEquals(new Lexicon<>(v2j, v2l), g2.getSinks());
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
		assertFalse(g2.isReflexive());
	}
	
	@Test
	void test_isAntiReflexive() {
		assertTrue(gSym.isAntiReflexive());
		assertTrue(gAntiSym.isAntiReflexive());
		assertTrue(gComplete.isAntiReflexive());
		assertTrue(gNotComplete.isAntiReflexive());
		assertTrue(gTransitive.isAntiReflexive());
		assertFalse(g1.isAntiReflexive());
		assertTrue(g2.isAntiReflexive());
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
		assertFalse(g2.isConnected());
	}
	
	// Algorithm
	
	@Test
	void test_mapDistanceFrom() {
		LinkedHashMap<Vertex<Void>, Long> map = g2.mapDistanceFrom(v2a);
		
		try {
			g2.mapDistanceFrom(vnco1); // vnco1 ∉ g2.V
			fail("Should have thrown an exception.");
		} catch (IllegalArgumentException ignored) { }
		
		System.out.println("GraphTest.test_mapDistanceFrom> map(g2) = {");
		for (Map.Entry<Vertex<Void>, Long> entry : map.entrySet())
			System.out.println("\t" + entry.getKey().getLabel() + " is " +
					(entry.getValue() != Long.MAX_VALUE ? entry.getValue() : "+∞") + " away from " + v2a.getLabel());
		
		System.out.println("}");
		
		assertEquals(0, map.get(v2a).longValue());
		assertEquals(1, map.get(v2b).longValue());
		assertEquals(1, map.get(v2c).longValue());
		assertEquals(1, map.get(v2g).longValue());
		assertEquals(1, map.get(v2h).longValue());
		assertEquals(2, map.get(v2d).longValue());
		assertEquals(2, map.get(v2e).longValue());
		assertEquals(3, map.get(v2f).longValue());
		assertEquals(3, map.get(v2l).longValue());
		assertEquals(Long.MAX_VALUE, map.get(v2i).longValue());
		assertEquals(Long.MAX_VALUE, map.get(v2j).longValue());
		assertEquals(Long.MAX_VALUE, map.get(v2k).longValue());
	}
	
	@Test
	void test_getShortestDistanceBetween() {
		assertEquals(0, g2.getShortestDistanceBetween(v2a, v2a));
		assertEquals(1, g2.getShortestDistanceBetween(v2a, v2b));
		assertEquals(1, g2.getShortestDistanceBetween(v2a, v2c));
		assertEquals(1, g2.getShortestDistanceBetween(v2a, v2g));
		assertEquals(1, g2.getShortestDistanceBetween(v2a, v2h));
		assertEquals(2, g2.getShortestDistanceBetween(v2a, v2d));
		assertEquals(2, g2.getShortestDistanceBetween(v2a, v2e));
		assertEquals(3, g2.getShortestDistanceBetween(v2a, v2f));
		assertEquals(3, g2.getShortestDistanceBetween(v2a, v2l));
		assertEquals(Long.MAX_VALUE, g2.getShortestDistanceBetween(v2a, v2i));
		assertEquals(Long.MAX_VALUE, g2.getShortestDistanceBetween(v2a, v2j));
		assertEquals(Long.MAX_VALUE, g2.getShortestDistanceBetween(v2a, v2k));
		
		assertEquals(0, g1.getShortestDistanceBetween(v14, v14));
	}
	
	@Test
	void test_Dijkstra_map() {
		Couple<LinkedHashMap<Vertex<Void>, Long>, LinkedHashMap<Vertex<Void>, Vertex<Void>>> map = Dijkstra.map(g2, v2a);
		
		System.out.println("GraphTest.test_Dijkstra_map> map(g2) = {");
		for (Vertex<Void> v2 : g2.getVertices()) {
			Vertex<Void> prev = map.getY().getOrDefault(v2, null);
			
			System.out.println("\t" + v2.getLabel() +
					": dist(a)=" + map.getX().getOrDefault(v2, Long.MAX_VALUE) +
					" ; prev=" + (prev != null ? prev.getLabel() : "(null)"));
		}
		
		System.out.println("}");
		
		assertEquals(Long.MAX_VALUE, map.getX().get(v2i).longValue());
		assertEquals(Long.MAX_VALUE, map.getX().get(v2j).longValue());
		assertEquals(Long.MAX_VALUE, map.getX().get(v2k).longValue());
	}
	
	@Test
	void test_Dijkstra_getPath() {
		Lexicon<Vertex<Void>> vertices = Dijkstra.getPath(g2, v2a, v2l);
		
		System.out.print("GraphTest.test_Dijkstra_getPath> path(g2) = {\n\t");
		for (Vertex<Void> vertex : vertices)
			System.out.print(vertex.getLabel() + " -> ");
		System.out.println("END\n}");
		
		assertEquals(new Lexicon<>(
				v2a, v2h, v2e, v2l
		), vertices);
		
		
		assertNull(Dijkstra.getPath(g2, v2a, v2i));
		assertNull(Dijkstra.getPath(g2, v2a, v2j));
		assertNull(Dijkstra.getPath(g2, v2a, v2k));
		
		try {
			Dijkstra.getPath(g2, v2a, vr1); // vr1 ∉ g2.V
			fail("Sould have thrown exception.");
		} catch (IllegalArgumentException ignored) { }
	}
}