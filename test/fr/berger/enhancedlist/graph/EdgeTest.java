package fr.berger.enhancedlist.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {
	
	Vertex<Void> v1;
	Vertex<Void> v2;
	Vertex<Void> v3;
	Vertex<Void> v4;
	
	Edge e11;
	Edge e12;
	Edge e13;
	Edge e14;
	Edge e21;
	Edge e22;
	Edge e23;
	Edge e24;
	Edge e31;
	Edge e32;
	Edge e33;
	Edge e34;
	Edge e41;
	Edge e42;
	Edge e43;
	Edge e44;
	
	@BeforeEach
	void setup() {
		v1 = new Vertex<>(1);
		v2 = new Vertex<>(2);
		v3 = new Vertex<>(3);
		v4 = new Vertex<>(4);
		
		e11 = new Edge<>(v1, v1);
		e12 = new Edge<>(v1, v2);
		e13 = new Edge<>(v1, v3);
		e14 = new Edge<>(v1, v4);
		e21 = new Edge<>(v2, v1);
		e22 = new Edge<>(v2, v2);
		e23 = new Edge<>(v2, v3);
		e24 = new Edge<>(v2, v4);
		e31 = new Edge<>(v3, v1);
		e32 = new Edge<>(v3, v2);
		e33 = new Edge<>(v3, v3);
		e34 = new Edge<>(v3, v4);
		e41 = new Edge<>(v4, v1);
		e42 = new Edge<>(v4, v2);
		e43 = new Edge<>(v4, v3);
		e44 = new Edge<>(v4, v4);
	}
	
	@Test
	void getSymmetry() {
		assertEquals(e11.getX(), e11.getSymmetry().getX());
		assertEquals(e11.getY(), e11.getSymmetry().getY());
		assertEquals(e21.getX(), e12.getSymmetry().getX());
		assertEquals(e21.getY(), e12.getSymmetry().getY());
		assertEquals(e31.getX(), e13.getSymmetry().getX());
		assertEquals(e31.getY(), e13.getSymmetry().getY());
		assertEquals(e41.getX(), e14.getSymmetry().getX());
		assertEquals(e41.getY(), e14.getSymmetry().getY());
		
		assertEquals(e22.getX(), e22.getSymmetry().getX());
		assertEquals(e22.getY(), e22.getSymmetry().getY());
		assertEquals(e32.getX(), e23.getSymmetry().getX());
		assertEquals(e32.getY(), e23.getSymmetry().getY());
		assertEquals(e42.getX(), e24.getSymmetry().getX());
		assertEquals(e42.getY(), e24.getSymmetry().getY());
		
		assertEquals(e33.getX(), e33.getSymmetry().getX());
		assertEquals(e33.getY(), e33.getSymmetry().getY());
		assertEquals(e43.getX(), e34.getSymmetry().getX());
		assertEquals(e43.getY(), e34.getSymmetry().getY());
		
		assertEquals(e44.getX(), e44.getSymmetry().getX());
		assertEquals(e44.getY(), e44.getSymmetry().getY());
	}
}