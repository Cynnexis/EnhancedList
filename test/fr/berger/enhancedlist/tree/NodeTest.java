package fr.berger.enhancedlist.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {
	
	Node<Integer> root;
	
	Node<Integer> n7;
	Node<Integer> n2;
	Node<Integer> n6;
	Node<Integer> n5;
	Node<Integer> n11;
	Node<Integer> n5a;
	Node<Integer> n9;
	Node<Integer> n4;
	
	@BeforeEach
	void setUp() {
		// Example: https://upload.wikimedia.org/wikipedia/commons/thumb/f/f7/Binary_tree.svg/220px-Binary_tree.svg.png
		root = new Node<>(2);
		
		n7 = new Node<>(7);
		n2 = new Node<>(2);
		n6 = new Node<>(6);
		n5 = new Node<>(5);
		n11 = new Node<>(11);
		n5a = new Node<>(5);
		n9 = new Node<>(9);
		n4 = new Node<>(4);
		
		n6.addChild(n5);
		n6.addChild(n11);
		
		n9.addChild(n4);
		
		n7.addChild(n2);
		n7.addChild(n6);
		
		n5a.addChild(n9);
		
		root.addChild(n7);
		root.addChild(n5a);
	}
	
	@Test
	void isLeaf() {
		boolean result;
		
		result = root.isLeaf();
		System.out.println("isLeaf> root : " + result);
		Assertions.assertFalse(result);
		
		result = n7.isLeaf();
		System.out.println("isLeaf> n7 : " + result);
		Assertions.assertFalse(result);
		
		result = n2.isLeaf();
		System.out.println("isLeaf> n2 : " + result);
		Assertions.assertTrue(result);
		
		result = n5.isLeaf();
		System.out.println("isLeaf> n5 : " + result);
		Assertions.assertTrue(result);
		
		result = n11.isLeaf();
		System.out.println("isLeaf> n11 : " + result);
		Assertions.assertTrue(result);
		
		result = n4.isLeaf();
		System.out.println("isLeaf> n4: " + result);
		Assertions.assertTrue(result);
		
		result = n9.isLeaf();
		System.out.println("isLeaf> n9: " + result);
		Assertions.assertFalse(result);
	}
	
	@Test
	void computeHeight() {
		long beginning = System.currentTimeMillis();
		int result = root.computeHeight();
		long end = System.currentTimeMillis();
		
		System.out.println("computeHeight> Height: " + result);
		System.out.println("computeHeight> Execution time: " + (end - beginning) + "ms");
	}
}