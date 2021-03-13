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
			if(numChildren < MAX_CHILDREN) {
				children[numChildren] = new Node(value, this);
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
			if(numVals() == MAX_VALS) {// ie we have more values than should be allowed.
				if(parent == null) { // We are splitting and we are at the top of the tree. Guaranteed to have 4 illegal children.
					parent = new Node(vals[MAX_VALS / 2]); // 3/2 = 1.5 = 1
					// Assigning parent's left children with node's left values.
					for(int i = 0; i < MAX_VALS / 2; i++) { // [0,1) 
						parent.children[i] = new Node(vals[i], this);
						// Sets the leftmost 2 children to be the two only children of the recently split leftmost value.
						for(int j = 0; j < MAX_CHILDREN / 2; j++) { // [0, 2) Ensures that 0 is leftmost child and 2 is right most child.
							// Makes sure we only look at ever other child. Ensures we place our original children in positions 0 and 2
							parent.children[i].children[(j == 0) ? j : j + 1] = this.children[j]; // Ensures we place children in position 2 from original node's 1 index child
							if(this.children[j] != null) { // TODO: Fix bug in this area
								this.children[(j == 0) ? j : j + 1].parent = parent.children[i]; // Sets parent of old children to be our parent's children
							}
							// NOTE: Why (j==0)?j:j + 1 Because I am setting children[2] to be the rightmost child and otherwise it 
							// would place what is supposed to be the rightmost child into children[1] which is where the middle child is supposed to go.
						}
						removeVal(i);// Removes value from current node because we have created a new node for it that will become a child.
					}
					// Assigning parent's right children with node's right values.
					for(int i = MAX_VALS / 2 + 1; i < MAX_VALS; i++) { // [2, 3)
						parent.children[i] = new Node(vals[i], this); // creates new node and places it as child of new parent.
						// Sets the leftmost 2 children to be the two only children of the recently split leftmost value.
						for(int j = 0; j < MAX_CHILDREN / 2; j++) { // [0, 2) Ensures that 0 is leftmost child and 2 is right most child.
							//Places 3rd child in leftmost child's position in new node, and places 4th child in rightmost child's position in new node.
							parent.children[i].children[(j == 0) ? j : j + 1] = this.children[j + 2]; 
							if(this.children[j + 2] != null) { // TODO: fix bug in this area
								this.children[j + 2].parent = parent.children[i]; // Sets parent of old children to be our parent's children
							}
						}
						removeVal(i); // Removes value from current node becase we have created a new node for it that will become a child.
					}
					order(); // Orders values accordingly to how I specified in order.
					root = this; // Sets our current node to be the topmost root. 
					children = parent.children; // Sets our new node's children equal to the new children.
					parent = null;// Null since our topmost node will not have a parent
				}else {
					parent.addVal(vals[MAX_VALS / 2]);
					removeVal(MAX_VALS / 2);
					//order(); // Moves remaining vals in node to positions 0,1
					// Converrt the values in the node into children. The children will belong to this node's parent.
					if(parent.numVals() == 2) { // Guarantees 2 children already exist. We added one.
						for(int i = parent.numChildren(); i < MAX_CHILDREN - 1; i++) {
							parent.children[i - 1] = new Node(vals[0], parent);
							removeVal(0);
						}
					}else { // means our parent now has 3 values. Create the 4 children then recurse. 
						for(int i = parent.numChildren() - 1; i < MAX_CHILDREN - 1; i++) {
							parent.children[i + 1] = new Node(vals[i]);
							removeVal(i);
						}
						parent.split();
					}
				}
			}
		}
		
		// We are working with a valid tree.
		public void insert(int val) { // I am essentially using DFS
			// Checks all vals except rightmost val.
			
			if(numVals == 0) {
				addVal(val); // first insertion into this node.
				return;
			}
			for(int i = 0; i < numVals; i++) {
				
			}
			
			for(int i = 0; i < MAX_VALS - 1; i++) {
				if(vals[0] == null) { // Is our first value null? Then we are in an empty node. Add it.
					addVal(val);
					return;
				}
				if(vals[i] == null) continue; // Do we actually have an nth value? Or is it null?
				// Checks everything except rightmost value
				if(val < vals[i] && val != vals[i]) {
					if(children[i] == null) { // We are at a leaf, therefore just add val to node
						this.addVal(val);
						
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
						split();
					}
				}
			}else if(val > vals[0] && val != vals[0]) {
					if(children[MAX_VALS - 1] != null) {
						children[MAX_VALS - 1].insert(val);
					} else if (children[MAX_VALS - 1] == null) {
						addVal(val);
						split();
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
				if(target < vals[i]) {
					return children[i].find(target);
				}else if(target == vals[i]) {
					return this;
				}
			}
			// Haven't checked if target is greater than rightmost value. IF so recurse downwards
			if(target > vals[numVals - 1]) { // -1 to account for 0 based indexing
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