import java.util.Arrays;
import java.util.Comparator;

/*
 Notes:
 
 - Constructor with no parameters. Only one constructor.		DONE
 
 
 - insert(x) method 			Implementation DONE -> Preliminary Testing DONE -> Deep Testing TODO
 	- Discard duplicates
 	- Return true if element is added, false if not added (if duplicate then would return false)
 	- Use tree structure itself, nothing else.
 	
 	
 - split(Node root)				TODO
 	- split nodes. Might bubble upwards and cause chain of splitting.
 
 
 - find(root, val) 					Implementation DONE -> Preliminary Testing DONE -> Deep Testing TODO
 	- finds and returns the node containing that value. Returns null if not found, else returns node w/ value. Private method.
 	
 	
 - size() method 				DONE
 	- returns the number of values in the tree. Includes values found in the same node.
 	
 	
 - size(int x) method			Implementation DONE -> Preliminary Testing DONE -> Deep Testing TODO
 	- will return the int size of the subtree rooted at the node that contains integer x. (The size is the number of keys in that subtree.) If x is not in the tree, it should return 0.
 	
 	
 - get(x) method				TODO
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

		return get(this.root, index, 0);
	}

	// TODO: Figure out if this works
	// In order for BST: left, visit, right. For 23 Tree I think it will be left,
	// visit, mid, visit, right ?
	// Assumes nodes do not exceed maxiumum (2). Assumes index exists in tree.
	private Integer get(Node root, int targetIndex, int currIndex) {
		// "I will not call it with values that are supposed to be out of bounds."

		if (root == null) {
			return null;
		}

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

		/*
		 * 
		 * Integer leftValIndex = currIndex + size(root.lfChild); Integer rightValIndex
		 * = null;
		 * 
		 * if(root.getVal(1) != null) {//ie we have a right val in our node
		 * rightValIndex = leftValIndex + 1; if(root.midChild != null) { // ie we have a
		 * middle child. rightValIndex+= size(root.midChild); } }
		 * 
		 * 
		 * if(targetIndex < leftValIndex) { get(root.lfChild, targetIndex, 0); //
		 * Travelling left, no need to account for trees in right subtree }
		 * 
		 */

		return null;
	}

	// TODO: WIP... ...... :(
	private void split(Node root) {

		if (root == null) { // Base case #0
			return;
		}

		// Are we below maximum capacity? Return. We're done.
		if (root.numVals() != 3) { // Base case #1
			return;
		}

		// Are we at the root of our tree?
		if (root.parent == null) {// Base case #2
			Node parent = new Node(root.getVal(1));

			Node temp = root;

			// parent.children.set(0, new Node(root.getVal(0), parent));// sets left child
			// of new parent
			// parent.children.set(1, new Node(root.getVal(2), parent)); // sets right child
			// of new parent
		}
	}

	public boolean insert(int x) {
		int prevSize = this.size;
		root.insert(x);
		return prevSize != this.size;
	}

	

	private class Node {

		private static final int MAX_NUM_OF_VALS_PER_NODE = 3; // i.e., therefore capacity is 2 and once we reach 3 we
																// must split.
		
		private static final int MAX_CHILDREN = 4;

		public Integer[] vals; // Keeps track of values in a node (referred to as 'keys' in video). // 0 =
								// leftmost, 1 = rightmost, etc

		public Node parent; // I will need a reference to the parent to split and maybe get
		public Node lfChild;
		public Node midChild;
		public Node rtChild;

		// public ArrayList<Node> children; // 0 = right, 1 = mid, 2 = right

		public Node(Integer x, Node parent) {
			vals = new Integer[MAX_NUM_OF_VALS_PER_NODE];
			// children = new ArrayList<Node>();
			addVal(x);
			resetChildren();
			this.parent = parent;
		}

		public Node(Integer x) {
			vals = new Integer[MAX_NUM_OF_VALS_PER_NODE];
			// children = new ArrayList<Node>();
			addVal(x);
			resetChildren();
			this.parent = null;
		}

		public Node() {
			vals = new Integer[MAX_NUM_OF_VALS_PER_NODE];
			// children = new ArrayList<Node>();
			resetChildren();
			parent = null;
		}

		private void resetChildren() {
			lfChild = null;
			rtChild = null;
			midChild = null;
		}

		public int numVals() {
			int i = 0;
			for (; i < MAX_NUM_OF_VALS_PER_NODE; i++) {
				if (vals[i] == null) {
					return i;
				}
			}
			return i;
		}

		public void addVal(int val) { // Assumes there is at least one null value in our array. Which there should
										// because our node's capacity is 2, but we split once we reach 3. O(nlogn)
			int i = 0;
			for (; i < MAX_NUM_OF_VALS_PER_NODE; i++) {
				if (vals[i] == null) {
					vals[i] = val;
					break; // We found a null? Then we add our digit there.
				}
			}
			order();
		}

		// nlogn
		private void order() {// Makes sure our vals within node are ordered properly. TODO: Maybe make it
								// faster?

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
			if (index < MAX_NUM_OF_VALS_PER_NODE) {
				vals[index] = null;
			}
			order();
		}

		// Get corresponding values from our node. We pass in an index and we get
		// corresponding val from node. 0 = first val, etc.
		public Integer getVal(int index) {
			if (index < MAX_NUM_OF_VALS_PER_NODE) {
				return vals[index];
			}
			return -1; // Returns -1 if out of bounds
		}
		
		
		public void insert(int val) { // I am essentially using DFS

			if (getVal(0) == null) { // First insertion into tree. // BASE CASE #1
				addVal(val);
				size++;
				return;
			}

			// Is our val equal to leftmost val OR right most val if it exists? Then it
			// exists and we don't add duplicates.
			if (getVal(0) == val || ((getVal(1) != null && getVal(1) == val))) { // BASE CASE #2
				return;
			}

			// We have reached a leaf. Therefore add. I don't care if there are 2 already in
			// a node, split function will handle splitting.
			if (lfChild == null && rtChild == null && midChild == null) { // BASE CASE #3
				addVal(val);
				size++;
				split(this); // TODO: Find a way to split. Create method and call it here.
				return;
			}

			// Three recursive cases. 1) Value being added is either less than leftmost val
			// OR 2) Value is inbetween leftmost and rightmost OR 3) Value is greater than
			// rightmost

			// Case 1: Value is less than leftmost val in a node.
			// I can assume that there is a leftmost value. Otherwise it wouldn't even be a
			// node at all.
			if (val < getVal(0)) {
				this.lfChild.insert(val);
			}

			// Case 2: Value is inbetween leftmost and rightmost.
			// We can assume leftmost value exists.
			if (val > getVal(0) && getVal(1) != null && val < getVal(1)) { // is val > leftmost, does
																							// rightmost exist, AND is val <
																							// rightMost?
				this.midChild.insert(val);
			}

			// Case 3: Value is greater than rightmost (if it exists)
			if (root.getVal(2) != null && val > root.getVal(2)) {
				this.rtChild.insert(val);
			}
		}
		
		// Assumes nodes have been properly split (i.e., max vals per node is 2) and no
		// duplicates
		public Node find(Integer target) { // Traverses tree until it finds a node w/ a matching value. If
														// nothing is

			Integer leftMost = this.getVal(0);
			Integer rightMost = this.getVal(1);

			if (leftMost == null) {// Tree/node is empty base case
				return null;
			}

			// Three cases. 1) Val is less than leftmost OR 2) Val is inbetween leftmost and
			// rightmost OR 3) val is greater than rightmost OR
			// 4) rightval doesn't exist but target is greater than leftmost, threfore go
			// down right subtree.

			// leftmost val is bigger than target ie case 1
			if (leftMost > target && this.lfChild != null) {
				return this.lfChild.find(target);
			}

			// Target is inbetween leftmost and rightmost aka case 2
			if (target > leftMost && rightMost != null && target < rightMost && this.midChild != null) {
				return this.midChild.find(target);
			}

			// Target is greater than rightmost. case 3
			if (rightMost != null && target > rightMost && this.rtChild != null) {
				return this.rtChild.find(target);
			}

			// node only contains one value
			if (rightMost == null && target > leftMost && this.rtChild != null) {
				return this.rtChild.find(target);
			}

			// We found our value
			if (leftMost == target || rightMost == target) {
				return this;
			}
			return null; // not found
		}

		private int size() {
			int currNodeCount = this.numVals();

			if (this.lfChild != null) {
				currNodeCount += this.lfChild.size();
			}
			if (this.rtChild != null) {
				currNodeCount += this.rtChild.size();
			}
			if (this.midChild != null) {
				currNodeCount += this.midChild.size();
			}

			return currNodeCount;
		}
	}
}