package fr.berger.enhancedlist.tree;

import java.io.Serializable;
import java.util.ArrayList;

public class Tree<T> extends Node<T> implements Serializable, Iterable<Node<T>> {
	
	public Tree(T data, ArrayList<Node<T>> children) {
		super(data, children);
	}
	public Tree(T data) {
		super(data);
	}
	public Tree() {
		super();
	}
	
	/* OVERRIDE */
	
	@Override
	public String toString() {
		return super.toString();
	}
}
