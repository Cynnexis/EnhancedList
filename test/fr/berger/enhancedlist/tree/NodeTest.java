package fr.berger.enhancedlist.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
	
	@Test
	void computeNumberOfNodes() {
		int result = root.computeNumberOfNodes();
		System.out.println("computeNumberOfNodes> number for root: " + result);
		
		Assertions.assertEquals(9, result);
	}
	
	@Test
	void getAllNodesAtDepth() {
		System.out.println("getAllNodesAtDepth> 0: " + root.getAllNodesAtDepth(0));
		System.out.println("getAllNodesAtDepth> 1: " + root.getAllNodesAtDepth(1));
		System.out.println("getAllNodesAtDepth> 2: " + root.getAllNodesAtDepth(2));
		System.out.println("getAllNodesAtDepth> 3: " + root.getAllNodesAtDepth(3));
		System.out.println("getAllNodesAtDepth> 4: " + root.getAllNodesAtDepth(4));
		System.out.println("getAllNodesAtDepth> 5: " + root.getAllNodesAtDepth(5));
		
		ArrayList<Node<Integer>> a1 = new ArrayList<>(1);
		ArrayList<Node<Integer>> a2 = new ArrayList<>(2);
		ArrayList<Node<Integer>> a3 = new ArrayList<>(3);
		ArrayList<Node<Integer>> a4 = new ArrayList<>(3);
		ArrayList<Node<Integer>> a5 = new ArrayList<>(0);
		
		a1.add(root);
		
		a2.add(n7);
		a2.add(n5a);
		
		a3.add(n2);
		a3.add(n6);
		a3.add(n9);
		
		a4.add(n5);
		a4.add(n11);
		a4.add(n4);
		
		Assertions.assertEquals(a1, root.getAllNodesAtDepth(0));
		Assertions.assertEquals(a1, root.getAllNodesAtDepth(1));
		Assertions.assertEquals(a2, root.getAllNodesAtDepth(2));
		Assertions.assertEquals(a3, root.getAllNodesAtDepth(3));
		Assertions.assertEquals(a4, root.getAllNodesAtDepth(4));
		Assertions.assertEquals(a5, root.getAllNodesAtDepth(5));
	}
}