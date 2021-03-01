import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
 Notes:
 
 - Constructor with no parameters. Only one constructor.
 
 - insert(x) method 			Implementation -> Testing
 	- Discard duplicates
 	- Return true if element is added, false if not added (if duplicate then would return false)
 	- Use tree structure itself, nothing else.
 	
 - size() method 				DONE
 	- returns the number of values in the tree. Includes values found in the same node.
 	
 - get(x) method				Implementation
 	- returns the item that would be at location x if the values in tree were sorted in ascending order (from least to largest, t.get(0) returns least item while t.get(t.size() - 1) returns largest item)
 	- Don't use any other structure, just use the 23 tree
 	
 - Create own JUnit tests

 */

// All code is created by Luis Alvarez Sanchez for CS 146.


public class Tree {
	
	private Node root;
	private int size;
	
	
	public Tree() {
		root = new Node();
		size = 0;
	}
	
	
	
	public int size() {
		return this.size;
	}
	
	public boolean insert(int x) {
		int prevSize = this.size;
		insert(root, x);
		return prevSize != this.size;
	}
	
	
	private void insert(Node root, int val) { // I am essentially using DFS
		
		
		if(root.getVal(0) == null) { // First insertion into tree. // BASE CASE #1
			root.addVal(val);
			size++;
			return;
		}
		
		// We have reached a leaf. Therefore add. I don't care if there are 2 already in a node, split function will handle splitting.
		if(root.lfChild == null && root.rtChild == null && root.midChild == null) { // BASE CASE #2
			root.addVal(val);
			size++;
			// TODO: Find a way to split. Create method and call it here.
			return;
		}
		
		// Is our val equal to leftmost val OR right most val if it exists?
		if(root.getVal(0) == val || ((root.getVal(1) != null && root.getVal(1) == val))) { // BASE CASE #3
			return;
		}
		
		
		// Three recursive cases. 1) Value being added is either less than leftmost val OR 2) Value is inbetween leftmost and rightmost OR 3) Value is greater than rightmost
		
		
		//Case 1: Value is less than leftmost val in a node. 
		// I can assume that there is a leftmost value. Otherwise it wouldn't even be a node at all.
		if(val < root.getVal(0)) {
			insert(root.lfChild, val);
		}
		
		// Case 2: Value is inbetween leftmost and rightmost.
		// We can assume leftmost value exists. 
		if(val > root.getVal(0) && root.getVal(1) != null && val < root.getVal(1)){ // is val > leftmost, does rightmost exist, AND is val < rightMost?
			insert(root.midChild, val);
		}
		
		
		// Case 3: Value is greater than rightmost (if it exists)
		if(root.getVal(2) != null && val > root.getVal(2)) {
			insert(root.rtChild, val);
		}
	}

	public int get(int ithBiggest) {
		
		
		return get(ithBiggest, size);
	}

	// Again using DFS
	private int get(int ithBiggest, int size) {
		
		
		
		
		
		
	}
	
	
	private class Node{
		
		private static final int MAX_NUM_OF_VALS_PER_NODE = 3; // i.e., therefore capacity is 2 and once we reach 3 we must split.
		
		public Integer[] vals; // Keeps track of values in a node (referred to as 'keys' in video).
		public Node lfChild;		// Not scalable.
		public Node rtChild;
		public Node midChild;
		
		//private ArrayList<Node> children;  Scalable solution to # of children. But I will leave this out since I'm only doing 23 Trees.
		
		public Node(Integer x) {
			vals = new Integer[MAX_NUM_OF_VALS_PER_NODE];
			addVal(x);
			lfChild = null;
			rtChild = null;
			midChild = null;
		}
		
		public Node() {
			vals = new Integer[MAX_NUM_OF_VALS_PER_NODE];
			lfChild = null;
			rtChild = null;
			midChild = null;
		}
		
		public void addVal(int val) { // Assumes there is at least one null value in our array. Which there should because our node's capacity is 2, but we split once we reach 3.   O(nlogn)
			for(int i = 0; i < MAX_NUM_OF_VALS_PER_NODE; i++) {
				if(vals[i] == null) {
					vals[i] = val;
				}
			}
			Arrays.sort(vals); // Makes sure our nodes are ordered properly. TODO: Maybe make it faster? Counting sort maybe.
		}
		
		
		// Resets a particular value in our node to null;
		public void removeVal(int index) {
			if(index < MAX_NUM_OF_VALS_PER_NODE) {
				vals[index] = null;
			}
		}
		
		
		// Get corresponding values from our node. We pass in an index and we get corresponding val from node. 0 = first val, etc.
		public Integer getVal(int index) {
			if(index < MAX_NUM_OF_VALS_PER_NODE) {
				return vals[index];
			}
			return -1; // Returns -1 if out of bounds
		}
	}
}