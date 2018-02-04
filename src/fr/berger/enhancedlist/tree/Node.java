package fr.berger.enhancedlist.tree;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Node<T> implements Serializable, Iterable<Node<T>> {
	
	@Nullable
	private T data;
	@Nullable
	private Node<T> parent;
	@NotNull
	private ArrayList<Node<T>> children;
	
	/* CONSTRUCTORS */
	
	public Node(@Nullable T data, @Nullable Node<T> parent, @NotNull ArrayList<Node<T>> children) {
		setData(data);
		setParent(parent);
		setChildren(children);
	}
	public Node(@Nullable T data, @NotNull ArrayList<Node<T>> children) {
		setData(data);
		setParent(null);
		setChildren(children);
	}
	public Node(@Nullable T data, @Nullable Node<T> parent) {
		setData(data);
		setParent(parent);
		setChildren(new ArrayList<>());
	}
	public Node(@Nullable T data) {
		setData(data);
		setParent(null);
		setChildren(new ArrayList<>());
	}
	public Node() {
		setData(null);
		setParent(null);
		setChildren(new ArrayList<>());
	}
	
	/* OTHER METHODS */
	
	public void addChild(@Nullable Node<T> child) {
		if (child != null)
			child.setParent(this);
		
		getChildren().add(child);
	}
	public void addChild(@Nullable T data) {
		Node<T> n = new Node<>(data, this);
		getChildren().add(n);
	}
	
	public static <T> boolean isLeaf(Node<T> node) {
		if (node == null)
			return true;
		
		if (node.getChildren().size() == 0)
			return true;
		
		// Search if all children of node are null. If it is the case, then it is a leaf
		boolean notNullFound = false;
		for (int i = 0; i < node.getChildren().size() && !notNullFound; i++)
			if (node.getChildren().get(i) != null)
				notNullFound = true;
		
		return !notNullFound;
	}
	public boolean isLeaf() {
		return isLeaf(this);
	}
	
	public int computeHeight() {
		return computeHeight(this);
	}
	public int computeHeight(Node<T> node) {
		if (node == null)
			return 0;
		
		if (isLeaf(node))
			return 1;
		
		ArrayList<Integer> depths = new ArrayList<>(node.getChildren().size());
		
		// For all child, compute the depths
		for (Node<T> child : node) {
			depths.add(computeHeight(child));
		}
		
		// Get the maximum depths
		int i, max = 0;
		for (i = 0; i < depths.size(); i++)
			if (depths.get(i) > depths.get(max))
				max = i;
		
		// 'max' is the index to the maximum depth
		return 1 + depths.get(max);
	}
	
	/* GETTERS & SETTERS */
	
	public @Nullable T getData() {
		return data;
	}
	
	public void setData(@Nullable T data) {
		this.data = data;
	}
	
	public @Nullable Node<T> getParent() {
		return parent;
	}
	
	public void setParent(@Nullable Node<T> parent) {
		this.parent = parent;
	}
	
	public @NotNull ArrayList<Node<T>> getChildren() {
		if (this.children == null)
			this.children = new ArrayList<>();
		
		return this.children;
	}
	
	public void setChildren(@NotNull ArrayList<Node<T>> children) {
		if (children == null)
			throw new NullPointerException();
		
		this.children = children;
	}
	
	/* OVERRIDES */
	
	@Override
	public Iterator<Node<T>> iterator() {
		return new Iterator<Node<T>>() {
			
			private int index = 0;
			
			@Override
			public boolean hasNext() {
				return children != null && index < children.size();
			}
			
			@Override
			public Node<T> next() {
				if  (children == null)
					return null;
				
				if (0 <= index && index < children.size())
					return children.get(index++);
				else
					return null;
			}
		};
	}
	
	@Override
	public String toString() {
		return "Node{" +
				"data=" + data +
				", parent=" + parent +
				", children=" + children +
				'}';
	}
	
	@Override
	public boolean equals(@NotNull Object o) {
		if (o == null)
			return false;
		
		if (this == o)
			return true;
		
		if (!(o instanceof Node))
			return false;
		
		Node<?> node = (Node<?>) o;
		
		return Objects.equals(data, node.data) &&
				Objects.equals(parent, node.parent) &&
				Objects.equals(children, node.children);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(data, parent, children);
	}
}
