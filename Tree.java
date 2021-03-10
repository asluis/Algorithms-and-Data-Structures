import java.util.Arrays;
import java.util.Comparator;

/*
 Notes:
 
 - Constructor with no parameters. Only one constructor.		DONE
 
 
 - insert(x) method 			Implementation DONE -> Moved within Node class DONE -> Preliminary Testing DONE -> Deep Testing TODO
 	- Discard duplicates
 	- Return true if element is added, false if not added (if duplicate then would return false)
 	- Use tree structure itself, nothing else.
 	
 	
 - split(Node root)				TODO
 	- split nodes. Might bubble upwards and cause chain of splitting.
 
 
 - find(root, val) 					Implementation DONE -> Moved within Node class DONE -> Preliminary Testing DONE -> Deep Testing TODO
 	- finds and returns the node containing that value. Returns null if not found, else returns node w/ value. Private method.
 	
 	
 - size() method 				Implementation DONE -> Moved within Node class DONE -> Preliminary Testing DONE -> TODO
 	- returns the number of values in the tree. Includes values found in the same node.
 	
 	
 - size(int x) method			Implementation DONE -> Moved within Node class DONE -> Preliminary Testing DONE -> Deep Testing TODO
 	- will return the int size of the subtree rooted at the node that contains integer x. (The size is the number of keys in that subtree.) If x is not in the tree, it should return 0.
 	
 	
 - get(x) method				Implementation DONE -> Moved within Node class DONE -> Preliminary Testing TODO
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

	class Node {

		private static final int MAX_VALS = 3; // i.e., therefore capacity is 2 and once we reach 3 we
												// must split.

		private static final int MAX_CHILDREN = 4;

		public Integer[] vals; // Keeps track of values in a node (referred to as 'keys' in video). // 0 =
								// leftmost, 1 = rightmost, etc

		public Node parent; // I will need a reference to the parent to split and maybe get

		public Node[] children;

		public Node(Integer x, Node parent) {
			vals = new Integer[MAX_VALS];
			// children = new ArrayList<Node>();
			addVal(x);
			resetChildren();
			this.parent = parent;
		}

		public Node(Integer x) {
			vals = new Integer[MAX_VALS];
			// children = new ArrayList<Node>();
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
		}

		public int numVals() {
			int i = 0;
			for (; i < MAX_VALS; i++) {
				if (vals[i] == null) {
					return i;
				}
			}
			return i;
		}
		
		public int numChildren() {
			int counter = 0;
			for(int i = 0; i < children.length; i++) {
				if(children[i] != null) {
					counter++;
				}
			}
			return counter;
		}

		public void addVal(int val) { // Assumes there is at least one null value in our array. Which there should
										// because our node's capacity is 2, but we split once we reach 3. O(nlogn)
			int i = 0;
			for (; i < MAX_VALS; i++) {
				if (vals[i] == null) {
					vals[i] = val;
					break; // We found a null? Then we add our digit there.
				}
				if(vals[i] == val) { // aka don't add it. // Extra layer of safety
					size--;
					return;
				}
			}
			order();
		}

		// nlogn. Orders values
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
			if (index < MAX_VALS) {
				vals[index] = null;
			}
			order();
		}

		// Get corresponding values from our node. We pass in an index and we get
		// corresponding val from node. 0 = first val, etc.
		public Integer getVal(int index) {
			if (index < MAX_VALS) {
				return vals[index];
			}
			return -1; // Returns -1 if out of bounds
		}
		
		private void split() {
			if(numVals() == MAX_VALS) {// ie we have more values than should be allowed.
				
				if(parent == null) { // We are splitting and we are at the top of the tree. Guaranteed to have 4 illegal children.
					
					
					
					parent = new Node(vals[MAX_VALS / 2]); // 3/2 = 1.5 = 1
					root = parent;
					
					
					// Assigning parent's left children with node's left values.
					for(int i = 0; i < MAX_VALS / 2; i++) { // [0,1) 
						parent.children[i] = new Node(vals[i], parent);
					}
					// Assigning parent's right children with node's right values.
					for(int i = MAX_VALS / 2 + 1; i < MAX_VALS; i++) { // [2, 3)
						parent.children[i] = new Node(vals[i], parent);
					}
					
					// At this point we have two trees. The smaller new one and the old one with children having children. Do something about that.
					// I will merge these two trees.
					
					
					
					

				}
			}
		}

		// We are working with a valid tree.
		public void insert(int val) { // I am essentially using DFS
			// Checks all vals except rightmost val.
			for(int i = 0; i < MAX_VALS - 1; i++) {
				
				if(vals[0] == null) { // Is our first value null? Then we are in an empty node. Add it.
					addVal(val);
					size++;
					return;
				}
				if(vals[i] == null) continue; // Do we actually have an nth value? Or is it null?
				// Checks everything except rightmost value
				if(val < vals[i] && val != vals[i]) {
					if(children[i] == null) { // We are at a leaf, therefore just add val to node
						this.addVal(val);
						size++;
						split(); //TODO: IMPLEMENT
					}else { // Still space to move down
						children[i].insert(val);
					}
					return; // We're done.
				}
			}
			// Checking right edge
			//Just check rightmost. If rightmost val exists, then we go in and check if val > rightmost.
			// If val > rightmost, check if rightmost child exists then recurse. Else just add to current node and call split
			if(vals[MAX_VALS - 2] != null) { 
				if(val > vals[MAX_VALS - 2] && val != vals[MAX_VALS - 2]) {
					if(children[MAX_VALS - 1] != null) {
						children[MAX_VALS - 1].insert(val);
					}else {
						this.addVal(val);
						size++;
						split(); //TODO: IMPLEMENT
					}	
				}
			}else { // else we only have one value in our node (23 tree)
				if(val > vals[0]) {
					if(children[MAX_VALS - 1] != null) {
						children[MAX_VALS - 1].insert(val);
					} else if (children[MAX_VALS - 1] == null) {
						addVal(val);
						size++;
						split();
					}
				}
			}
			return; // At the end, nothing to do.
		}
		
		
		// TODO: Figure out which of these works
		/*
		 * Observation: index of leftmost val in root is size of left subtree. Index of rightmost val is size of leftmost + size of midsubtree + 1
		 */
		// In order for BST: left, visit, right. For 23 Tree I think it will be left,
		// visit, mid, visit, right ?
		// Assumes nodes do not exceed maxiumum (2). Assumes index exists in tree.
		public Integer get(int targetIndex) {
			// "I will not call it with values that are supposed to be out of bounds."
			
			
			// Index of val a is size of left subtree. Index of right val is size of leftsubtree + size of Mid subtree + 1.
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
			// ONLY checking if < than curr val in node, not greater than. This leaves the rightmost node not 
			for(int i = 0; i < vals.length; i++) {
				if(vals[i] != null) { // Value exists?
					if(target < vals[i]) { // Value lessthan our target?
						if(children[i] != null) { // Recurse downwards
							return children[i].find(target);
						}
					}else if(vals[i] == target) { // We found the node! Return it
						return this;
					}
				}
			}
			
			// When do we go to the right child? Never. So we must manually check it by making a manual visit to the right child.\
			if(vals[MAX_VALS - 2] != null) { // Do we have a rigthmost val?
				if(target > vals[MAX_VALS - 2]) {
					if(children[MAX_VALS - 1] != null) { // Space to move down
						return children[MAX_VALS - 1].find(target);
					}
				}else if(target == vals[MAX_VALS - 2]) {
					return this;
				}
			}else if(target > vals[0] && children[MAX_VALS - 1] != null) {// Do we have a left most value who is less than our target? We will find it. Recurse.
					return children[MAX_VALS - 1].find(target);
			}
			return null;
		}

		public int size() {// Iterates thru all children and adds their size to the sum. Then we return that sum.
			int currNodeCount = this.numVals();
			// Essentially iterating over children array and adding each to our count. 
			for(int child = 0; child < children.length; child++) {
				if(children[child] != null) {
					currNodeCount += children[child].size();
				}
			}
			return currNodeCount;
		}
	}
}