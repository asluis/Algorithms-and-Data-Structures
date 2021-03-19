import java.util.Arrays;
import java.util.Comparator;

/*
 Notes:
 
 - Constructor with no parameters. Only one constructor.		DONE
 
 
 - insert(x) method 			Implementation DONE -> Moved within Node class DONE -> Preliminary Testing DONE -> Deep Testing DONE
 	- Discard duplicates
 	- Return true if element is added, false if not added (if duplicate then would return false)
 	- Use tree structure itself, nothing else.
 	
 	
 - split(Node root)				Implementation DONE -> Preliminary Testing DONE -> Deep Testing TODO
 	- split nodes. Might bubble upwards and cause chain of splitting.
 
 
 - find(root, val) 					Implementation DONE -> Moved within Node class DONE -> Preliminary Testing DONE -> Deep Testing DONE
 	- finds and returns the node containing that value. Returns null if not found, else returns node w/ value. Private method.
 	
 	
 - size() method 				Implementation DONE -> Moved within Node class DONE -> Preliminary Testing DONE -> Deep Testing DONE
 	- returns the number of values in the tree. Includes values found in the same node.
 	
 	
 - size(int x) method			Implementation DONE -> Moved within Node class DONE -> Preliminary Testing DONE -> Deep Testing TODO
 	- will return the int size of the subtree rooted at the node that contains integer x. (The size is the number of keys in that subtree.) If x is not in the tree, it should return 0.
 	
 	
 - get(x) method				Implementation DONE -> Moved within Node class DONE -> Preliminary Testing DONE -> Deep Testing TODO
 	- returns the item that would be at location x if the values in tree were sorted in ascending order (from least to largest, t.get(0) returns least item while t.get(t.size() - 1) returns largest item)
 	- Don't use any other structure, just use the 23 tree
 	
 	
 - Create own JUnit tests		TODO

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
	

	public int size(Integer val) {

		Node node = root.find(val);
		if (node == null)
			return 0;

		return node.size();
	}

	public int get(int index) {

		return root.get(index);
	}

	public boolean insert(int x) {
		int prevSize = this.size;
		root.insert(x);
		return prevSize != this.size;
	}

	private class Node {
		private static final int MAX_VALS = 3; // i.e., therefore capacity is 2 and once we reach 3 we
												// must split.
		private static final int MAX_CHILDREN = 4;
		public Integer[] vals; // Keeps track of values in a node (referred to as 'keys' in video). // 0 =
								// leftmost, 1 = rightmost, etc
		public Node parent; // I will need a reference to the parent to split and maybe get
		public Node[] children;
		
		private int numVals = 0;
		private int numChildren = 0;

		public Node(Integer x, Node parent) {
			vals = new Integer[MAX_VALS];
			// children = new ArrayList<Node>();
			addVal(x);
			resetChildren();
			this.parent = parent;
		}

		public Node(Integer x) {
			vals = new Integer[MAX_VALS];
			addVal(x);
			resetChildren();
			this.parent = null;
		}

		public Node() {
			vals = new Integer[MAX_VALS];
			// children = new ArrayList<Node>();
			resetChildren();
			parent = null;
			
		}

		private void resetChildren() {
			children = new Node[MAX_CHILDREN];
			for (int i = 0; i < MAX_CHILDREN; i++) {
				children[i] = null;
			}
			numChildren = 0;
		}
		
		public void addChild(Integer value) {

			if(numChildren < MAX_CHILDREN) {
				children[numChildren] = new Node(value, this);
				numChildren++;	
				//orderChildren();
			}
		}
		
		public void addChild(Node child) {			
			if(numChildren < MAX_CHILDREN) {
				child.parent = this;
				children[numChildren] = child;
				numChildren++;
				orderChildren();
			}
		}
		
		public void removeChild(int index) {
			if (index < MAX_CHILDREN && index < numChildren) {
				children[index] = null;
				numChildren--;
				orderChildren();
			}
		}

		public void addVal(int val) { // Assumes there is at least one null value in our array. Which there should
										// because our node's capacity is 2, but we split once we reach 3. O(nlogn)
			if(numVals < MAX_VALS) {
				vals[numVals] = val;
				numVals++;
				size++;
				order();
			}
		}

		private void orderChildren() {
			
			
			Arrays.sort(parent.children, new Comparator<Node>() {
				@Override
				public int compare(Node a, Node b) {
					if(a != null && b != null ) {
						return Integer.compare(a.getVal(0), b.getVal(0));
					} else if(a == null && b != null) {
						return 1;
					}else if(a != null && b == null ) {
						return -1;
					}
					return 0; // both null, therefore same weight
				}
			});
		}

		// nlogn. Orders values
		private void order() {// Makes sure our vals within node are ordered properly. TODO: Maybe make it faster?
			// Custom sort that treats null as negative infinity
			Arrays.sort(vals, new Comparator<Integer>() {
				@Override
				public int compare(Integer num1, Integer num2) {
					if (num1 != null && num2 != null) { // both nums
						return Integer.compare(num1, num2);
					} else if (num1 == null && num2 != null) { // left operand is negative infinity
						return 1;
					} else if (num1 != null && num2 == null) { // right operand is negative infinity
						return -1;
					}
					return 0; // both operands are negative infinity
				}

			}); // nlogn
		}

		// Resets a particular value in our node to null;
		public void removeVal(int index) {
			if (index < MAX_VALS && index < numVals) {
				vals[index] = null;
				numVals--;
				size--;
				order();
			}
		}

		// Get corresponding values from our node. We pass in an index and we get
		// corresponding val from node. 0 = first val, etc.
		public Integer getVal(int index) {
			if (index < MAX_VALS) {
				return vals[index];
			}
			return -1; // Returns -1 if out of bounds
		}
		
		// Splits node. FINALLY WORKS :)
		private void split() {
			// Two cases. One where parent is null, and the other when it isn't
			if(parent == null) {
				parent = new Node(vals[(numVals - 1) / 2], null); // creates a new parent
				removeVal((numVals - 1) / 2);
				while(numVals != 0) {// Converting current values to children of parent.
					parent.addChild(vals[0]);
					removeVal(0);
				}
				// Now we have a bunch of unassigned children. Assign them to parent's new children
				adjustSplitNodeChildren();
				root = parent;
				children = parent.children;
				parent = null;
			}else {// Means we have a parent.
				parent.addVal(vals[(numVals - 1) / 2]); // Pushing middle value upwards
				removeVal((numVals - 1) / 2);
				while(numVals > 1) { // Converting all values to children. Done so in order.
					parent.addChild(vals[1]);
					removeVal(1);
				}
				orderChildren();
				adjustSplitNodeChildren();
				if(parent.numVals >= MAX_VALS) parent.split(); // Recursively calls split if necessary.
			}
		}
		
		// might be slightly specific to 23 trees. Will not be general enough for a B tree.
		private void adjustSplitNodeChildren() {
			int outer = -1;
			int childChild = 0;
			
			for(int i = 0; i < MAX_CHILDREN; i++) {
				if(i % 2 == 0) {
					outer++;
					childChild = 0;
				}else {
					childChild = 1;
				}
				if(parent.children[outer].children[childChild] == null) {
					//parent.children[outer].children[childChild] = children[i];
					if(children[i] != null) parent.children[outer].addChild(children[i]);
					
					//parent.children[outer].numChildren++;
				}
			}
		}
		
		// We are working with a valid tree.
		public void insert(int val) { // I am essentially using DFS
			// Checks all vals except rightmost val.
			if(numVals == 0) addVal(val); // first val to be added to this node. Just do it.
			for(int i = 0; i < numVals; i++) {
				if(val == vals[i]) return; // As soon as we find an equal value, just end it.
				if(val < vals[i]) {
					if(children[i] != null) {// Do we have more room to recurse? Do it
						children[i].insert(val);
						return;
					}else if(children[i] == null) { // No more room to recurse. We have reached a leaf, add val to our current node.
						addVal(val);
						if(numVals >= MAX_VALS) split();
						return;
					}
				}
			}
			// Checking if our value is greater than the rightmost val in our node.
			if(val > vals[numVals - 1]) { // -1 to account for 0 based indexing
				if(children[numVals] != null) children[numVals].insert(val);
				if(children[numVals] == null) {
					addVal(val); // at a leaf, just add the value to our node.
					if(numVals >= MAX_VALS) split();
				}
			}
			return; // At the end, nothing to do.
		}
		
		
		/*
		 * Observation: index of leftmost val in root is size of left subtree. Index of rightmost val is size of leftmost + size of midsubtree + 1
		 */
		public Integer get(int targetIndex) {
			// "I will not call it with values that are supposed to be out of bounds."
			// Index of val a is size of left subtree. Index of right val is size of leftsubtree + size of Mid subtree + 1.
			
			// Case for when targetIndex is less than the current value's index
			int currentSize = 0;
			for(int index = 0; index < numVals; index++) {
				if(index < numChildren) currentSize += children[index].size(); // Add the size of the subtree we're skipping over
				if(targetIndex == currentSize) return vals[index];
				if(targetIndex < currentSize) { 
					if(index < numChildren) return children[index].get(targetIndex); // No need to subtract anything because we aren't skipping anything.
				}
				currentSize++; // Add the value we're skipping over.
			}
			
			// Case for when the target index is greater than last value's index
			if(targetIndex >= currentSize) {
				return children[numChildren - 1].get(targetIndex - currentSize); // Subtract size of currentIndex because we're skipping that many nodes
			}
			return null; // ie not found
		}
		
		// Assumes nodes have been properly split (i.e., max vals per node is 2) and no
		// duplicates
		public Node find(Integer target) { // Traverses tree until it finds a node w/ a matching value. If
											// nothing is matching then return null
			// ONLY checking if < than curr val in node, not greater than. This leaves the rightmost node not checked 
			for(int i = 0; i < numVals; i++) {
				if(target < vals[i] && children[i] != null) {
					return children[i].find(target);
				}else if(target == vals[i]) {
					return this;
				}
			}
			// Haven't checked if target is greater than rightmost value. IF so recurse downwards
			if(target > vals[numVals - 1] && children[numVals] != null) { // -1 to account for 0 based indexing
				return children[numVals].find(target);
			}
			return null; // means not found
		}

		public int size() {// Iterates thru all children and adds their size to the sum. Then we return that sum.
			int currNodeCount = numVals;
			// Essentially iterating over children array and adding each to our count. 
			for(int child = 0; child < MAX_CHILDREN; child++) {
				if(children[child] != null) currNodeCount += children[child].size();
			}
			return currNodeCount;
		}
	}
}