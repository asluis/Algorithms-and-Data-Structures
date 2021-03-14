import java.util.Arrays;
import java.util.Comparator;

/*
 Notes:
 
 - Constructor with no parameters. Only one constructor.		DONE
 
 
 - insert(x) method 			Implementation DONE -> Moved within Node class DONE -> Preliminary Testing DONE -> Deep Testing TODO
 	- Discard duplicates
 	- Return true if element is added, false if not added (if duplicate then would return false)
 	- Use tree structure itself, nothing else.
 	
 	
 - split(Node root)				Implementation DONE -> Preliminary Testing DONE -> Deep Testing TODO make sure that parent is assigned correctly. Fix cc bug
 	- split nodes. Might bubble upwards and cause chain of splitting.
 
 
 - find(root, val) 					Implementation DONE -> Moved within Node class DONE -> Preliminary Testing DONE -> Deep Testing TODO
 	- finds and returns the node containing that value. Returns null if not found, else returns node w/ value. Private method.
 	
 	
 - size() method 				Implementation DONE -> Moved within Node class DONE -> Preliminary Testing DONE -> TODO
 	- returns the number of values in the tree. Includes values found in the same node.
 	
 	
 - size(int x) method			Implementation DONE -> Moved within Node class DONE -> Preliminary Testing DONE -> Deep Testing TODO
 	- will return the int size of the subtree rooted at the node that contains integer x. (The size is the number of keys in that subtree.) If x is not in the tree, it should return 0.
 	
 	
 - get(x) method				Implementation DONE -> Moved within Node class TODO
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
			if(numChildren == 0) {
				children[0] = new Node(value, this);
				numChildren++;
				return;
			}
			if(numChildren < MAX_CHILDREN) {
				children[numChildren - 1] = new Node(value, this);
				children[numChildren - 1].parent = this;
				numChildren++;	
				orderChildren();
			}
		}
		
		public void addChild(Node child) {
			if(numChildren < MAX_CHILDREN) {
				child.parent = this;
				children[numChildren - 1] = child;
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
			Arrays.sort(children, new Comparator<Node>() {
				@Override
				public int compare(Node a, Node b) {
					Node aChild = a.children[0];
					Node bChild = b.children[0];

					if(aChild != null && bChild != null) {
						return Integer.compare(aChild.vals[0], bChild.vals[0]);
					} else if(aChild != null && bChild == null) {
						return 1;
					}else if(aChild == null && bChild != null) {
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
			}else {// Means we have a parent.
				
				parent.addVal(vals[(numVals - 1) / 2]); // Pushing middle value upwards
				removeVal((numVals - 1) / 2);
				while(numVals > 0) { // Converting all values to children. Done so in order.
					parent.addChild(new Node(vals[0]));
					removeVal(0);
				}
				adjustSplitNodeChildren();
			}
			if(parent.numVals >= MAX_VALS) parent.split(); // Recursively calls split if necessary.
		}
		
		// might be slightly specific to 23 trees. Will not be general enough for a B tree.
		private void adjustSplitNodeChildren() {
			for(int i = 1; i < parent.numChildren; i++) { // Starts at 1 
				for(int j = 0 + ((i-1) * MAX_CHILDREN / 2); j < MAX_CHILDREN / 2; j++) {
					parent.children[i].children[j] = children[j];
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
		
		
		// TODO: Figure out which of these approaches works... WIP RIP
		/*
		 * Observation: index of leftmost val in root is size of left subtree. Index of rightmost val is size of leftmost + size of midsubtree + 1
		 */
		// In order for BST: left, visit, right. For 23 Tree I think it will be left,
		// visit, mid, visit, right ?
		// Assumes nodes do not exceed maxiumum (2). Assumes index exists in tree.
		public Integer get(int targetIndex) {
			// "I will not call it with values that are supposed to be out of bounds."
			// Index of val a is size of left subtree. Index of right val is size of leftsubtree + size of Mid subtree + 1.
			
			
			if (targetIndex < MAX_VALS) {
				return vals[targetIndex];
			}
			int nodeSkipped = 0;
			for(int i = 0; i < MAX_VALS - 1; i++) {
				if(vals[i] != null) {
					for(int j = 0; j <= i; j++) {
						if(children[j] != null) {
							nodeSkipped+= children[j].size();
						}
					}
					if(targetIndex == 0 || targetIndex == nodeSkipped) {
						return vals[i];
					}
					if(targetIndex < nodeSkipped) {
						return children[i].get(targetIndex - nodeSkipped);
					}
				}else { // We got to a null val, check if our value is in corresponding children index
					return children[i].get(targetIndex - nodeSkipped);
				}
			}
/*
			if (root.lfChild == null && root.midChild == null && root.rtChild == null) { // Are we in a leaf? BASE CASE
				if (targetIndex == currIndex) { // Does the firstVal in leaf contain the index of item we look for?
					return root.getVal(0);
				} else if (targetIndex == ++currIndex && root.getVal(1) != null) { // Does the 2nd val in leaf contain the
																					// index of item we look for?
					return root.getVal(1);
				}
			}
			get(root.lfChild, targetIndex, ++currIndex); // Go as far down the left subtree as possible.
			if (targetIndex == currIndex) { // If we get here it's because we didn't find anything in left subtrees. Check
											// if maybe this node has our val
				return root.getVal(0);
			} else if (targetIndex == ++currIndex) { // TODO: Will this line work? I will think about it when it's not 2:20
														// am
				return root.getVal(1);
			}
			get(root.midChild, targetIndex, ++currIndex); // Means we didnt find val in left subtree. Go back to where we
															// started going down left subtree and
															// go down to midChild, then start going down left subtree
															// again.
			if (targetIndex == currIndex) {
				return root.getVal(0);
			} else if (targetIndex == ++currIndex) { // TODO: Will this line work? Probably not. Index is not linear
				return root.getVal(1);
			}
			get(root.rtChild, targetIndex, ++currIndex);
*/
			return null;
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
			for(int child = 0; child < numChildren; child++) {
				currNodeCount += children[child].size();
			}
			return currNodeCount;
		}
	}
}