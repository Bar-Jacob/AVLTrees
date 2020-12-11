import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import AVLTree.AVLNode;
import AVLTree.IAVLNode;

/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with distinct integer keys and info
 *
 */

public class AVLTree {

	private IAVLNode root = null;
	private IAVLNode min;
	private IAVLNode max;
	private IAVLNode virtualLeaf = new AVLNode();

	/**
	 * public boolean empty()
	 *
	 * returns true if and only if the tree is empty
	 *
	 */
	public boolean empty() {
		return (this.root == null);
	}

	/**
	 * public String search(int k)
	 *
	 * returns the info of an item with key k if it exists in the tree otherwise,
	 * returns null
	 */
	public String search(int k) {
		IAVLNode node = SearchNode(k);
		if (node.isRealNode()) {
			return node.getValue();
		}
		return null;
	}

	/**
	 * public int insert(int k, String i)
	 *
	 * inserts an item with key k and info i to the AVL tree. the tree must remain
	 * valid (keep its invariants). returns the number of rebalancing operations, or
	 * 0 if no rebalancing operations were necessary. promotion/rotation - counted
	 * as one rebalnce operation, double-rotation is counted as 2. returns -1 if an
	 * item with key k already exists in the tree.
	 */
	public int insert(int k, String i) {

		IAVLNode newNode = new AVLNode(k, i);
		if (this.empty()) { // insert root
			this.root = (AVLNode) newNode;
			this.max = (AVLNode) newNode;
			this.min = (AVLNode) newNode;

		} else { // tree is not empty
			IAVLNode position = this.findPosition(this.root, k);
			if (position.getKey() == k) { // key already exists
				return -1;

			} else if (position.getKey() > k) { // insert as a left child
				position.setLeft(newNode);
				if (newNode.getKey() <= this.min.getKey()) {
					this.min = newNode;
				}

			} else { // insert as a right child
				position.setRight(newNode);
				if (newNode.getKey() >= this.max.getKey()) {
					this.max = newNode;
				}
			}
			newNode.setParent(position);
		}
		newNode.updatePath();
		return rebalance(newNode);

	}

	public int rebalance(IAVLNode node) {
		int cnt = 0;
		while (node != null) {
			
			AVLNode newNode = (AVLNode) node;

			if (newNode.rankDiffLeft() == 0) { // problem with left subtree
				if (newNode.rankDiffRight() == 1) { // case 1: node-01, not terminal
					newNode.promote(); // sol: promote
					cnt++;
				} else {
					if (newNode.getLeft().rankDiffLeft() == 1 && // case 2: node-02 with child-12, terminal
							newNode.getLeft().rankDiffRight() == 2) {

						newNode.demote(); // sol: demote + right rotate
						node = this.rotateRight(newNode);
						cnt += 2;

					} else { // case3: node-02 with child-21, terminal
						newNode.demote(); // sol: demote + left rotation + right rotation
						newNode.getLeft().demote();
						node = this.rotateLeft(newNode.getLeft());
						node.promote();
						node = this.rotateRight(newNode);
						cnt += 5;
					}
				}
			}

			else if (newNode.rankDiffRight() == 0) { // problem with right subtree
				if (newNode.rankDiffLeft() == 1) { // case 1: node-01, not terminal
					newNode.promote(); // sol: promote
					cnt++;
				} else {
					if (newNode.getRight().rankDiffLeft() == 2 && // case2: node-20 with child-12, terminal
							newNode.getRight().rankDiffRight() == 1) {

						newNode.demote(); // sol: demote + left rotate
						node = this.rotateLeft(newNode);
						cnt += 2;

					} else { // case3: node-02 with child-21, terminal
						newNode.demote(); // sol: demote + right rotation + left rotation
						newNode.getRight().demote();
						node = this.rotateRight(newNode.getRight());
						node.promote();
						node = this.rotateLeft(newNode);
						cnt += 5;
					}

				}

			}
			node = node.getParent();
		}
		return cnt;
	}


	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there; the tree
	 * must remain valid (keep its invariants). returns the number of rebalancing
	 * operations, or 0 if no rebalancing operations were needed. demotion/rotation
	 * - counted as one rebalnce operation, double-rotation is counted as 2. returns
	 * -1 if an item with key k was not found in the tree.
	 */
	public int delete(int k) {
		int binarymax=0;
		int binarymin=0;
		if (this.max.getKey()==k) {
			binarymax =1;
		}
		if (this.min.getKey()==k) {
			binarymin =1;
		}
		int counter =0;
		if (this.search(k)==null) {
			return -1;}
		DelRec(k, this.root, counter);
		if (binarymax==1) {
			updatemax();}
		if (binarymin==1) {
			updatemin();}
		return counter;
		}
	// a recursive deletion function
	private IAVLNode DelRec(int k,IAVLNode root, int counter) {
		// First we find the node recursively
		if (!root.isRealNode()) {
			return root;} 			// recursion end
		if (root.getKey()>k) {		
			// we need to turn left
			root.setLeft(DelRec(k, root.getLeft(), counter));}
		if (root.getKey()<k) {		
			// we need to turn right
			root.setRight(DelRec(k, root.getRight(), counter));}
		if (root.getKey()==k) {		
			// we found the node to delete!
			
			if (!root.getLeft().isRealNode() || !root.getRight().isRealNode()) {
				// node has one or zero sons
				IAVLNode temp = null;
				if (!root.getLeft().isRealNode()) { // we need to find if the node has a real son
					temp = root.getRight();
				}else {
					temp = root.getLeft();
				}
				if (!temp.isRealNode()) {
					// root doesn't have kids
					temp = root;
					root = virtualLeaf;				 //**********************************virtual
				}else {
					root = temp; // root is switched with it's son
				}
			}else {
				//node has two sons
				// first we'll find it's successor
				IAVLNode successor = successor(root);
				// now lets switch them
				IAVLNode temp = root.getLeft();
				root.setLeft(successor.getLeft());
				successor.setLeft(temp);
				temp = root.getRight();
				root.setRight(successor.getRight());
				successor.setRight(temp);
				if (root.getParent().getKey() >root.getKey()) {
					//root is it's parent's left son
					root.getParent().setLeft(successor);
				}else {
					//root is it's parent's right son
					root.getParent().setRight(successor);
				}
				temp = successor;
				successor= root;
				root = temp;
				// now the root and it's successor are switched. lets delete the temp (=root) node from the right subtree recursively
				root.setRight(DelRec(temp.getKey(), root.getRight(), counter));
				
			}
			
			if(!root.isRealNode()) {		// this means our tree only had one node
				return root;
			}
			
			// update the height and size
			root.setHeight(Math.max(root.getLeft().getHeight(), root.getRight().getHeight())+1);
			root.setSize(root.getLeft().getSize()+ root.getRight().getSize()+1);
			
			// calc balance factor
			int bala = Bfactor(root);
			
			// now to check if we need to re balance:
			if (bala>1) {
				if (Bfactor(root.getLeft())<0) {
					// Left Right rotation
					counter+= 2;
					root.setLeft(rotateLeft(root.getLeft()));
					return(rotateRight(root.getRight()));
				}else {
					// Left Left rotation
					counter++;
					return(rotateRight(root));
				}
			}
			if (bala<-1) {
				if (Bfactor(root.getRight())<=0) {
					// Right Right rotation
					counter++;
					return rotateLeft(root);
				}else {
					// Right Left rotation
					counter+= 2;
					root.setRight(rotateRight(root.getRight()));
					return rotateLeft(root);
				}
			}
			// no need for rotations :)
			return root;
		}
	}

		private IAVLNode successor(IAVLNode node) { // returns the successor of node
		while (node.getLeft().isRealNode()) {
			node = node.getLeft();
		}
		return node;
	}
		
		private int Bfactor(IAVLNode node){  // returns the balance factor between node.left to node.right
	        if (!node.isRealNode()) { 
	            return 0;}
	        return (node.getLeft().getHeight() - node.getRight().getHeight());
	    }  
 
	

	/**
	 * SearchNode(int k):
	 * @param int k
	 * @return node with key == k or null if there is no such node in the tree
	 */
	private IAVLNode SearchNode(int k) {
		IAVLNode node = this.getRoot();
		while (node.isRealNode()) {
			if (node.getKey() == k) {
				return node;
			}
			if (node.getKey() > k) {
				node = node.getLeft();
			} else {
				node = node.getRight();
			}
		}
		return null;
	}

	/**
	 * public String min()
	 *
	 * Returns the info of the item with the smallest key in the tree, or null if
	 * the tree is empty
	 */
	public String min() {
		if (this.empty()) { // tree is empty
			return null;
		}
		return this.min.getValue();
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree, or null if the
	 * tree is empty
	 */
	public String max() {
		if (this.empty()) { // tree is empty
			return null;
		}
		return this.max.getValue(); // this is a saved field in our data structure :)
	}
	/**
	 * public void updatemax()
	 * 
	 * calculates the max key out of our tree and sets the corresponding field if
	 * the tree is empty, it sets it to null
	 * @return 
	 */
	public void updatemax() {
		if (this.empty()) {
			this.max = virtualLeaf;
			return;

		}
		IAVLNode node = this.getRoot();
		IAVLNode max = null;
		while (node.isRealNode()) {
			max = node;
			node = node.getRight();
		}
		this.max = max;
	}

	/**
	 * public void updatemin()
	 * 
	 * calculates the minimal key out of our tree and sets the corresponding field
	 * if the tree is empty, it sets it to null
	 */

	public void updatemin() {
		if (this.empty()) {
			this.min = virtualLeaf;
			return;
		}
		IAVLNode node = this.getRoot();
		IAVLNode min = null;
		while (node.isRealNode()) {
			min = node;
			node = node.getLeft();
		}
		this.min = min;
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree, or an empty array
	 * if the tree is empty.
	 */
	public int[] keysToArray() {
		int[] keysArray = new int[this.getRoot().getSize()];

		if (this.empty() == true) {
			return keysArray;
		}

		List<IAVLNode> inOrderNodeList = new LinkedList<IAVLNode>();
		this.inOrderList(this.getRoot(), inOrderNodeList);

		int i = 0;
		for (IAVLNode node : inOrderNodeList) {
			keysArray[i] = node.getKey();
			i++;
		}
		return keysArray;
	}

	private void inOrderList(IAVLNode node, List<IAVLNode> inOrderNodeList) {

		if (!node.isRealNode()) {
			return;
		}
		this.inOrderList(node.getLeft(), inOrderNodeList);
		inOrderNodeList.add(node);
		this.inOrderList(node.getRight(), inOrderNodeList);

	}

	/**
	 * public String[] infoToArray()
	 *
	 * Returns an array which contains all info in the tree, sorted by their
	 * respective keys, or an empty array if the tree is empty.
	 */
	public String[] infoToArray() {
		String[] infoArray = new String[this.getRoot().getSize()];
		if (this.empty() == true) {
			return infoArray;
		}

		List<IAVLNode> inOrderNodeList = new LinkedList<IAVLNode>();
		this.inOrderList(this.getRoot(), inOrderNodeList);

		int i = 0;
		for (IAVLNode node : inOrderNodeList) {
			infoArray[i] = node.getValue();
			i++;
		}
		return infoArray;
	}

	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 *
	 * precondition: none postcondition: none
	 */
	public int size() {
		return this.root.getSize();
	}

	/**
	 * public int getRoot()
	 *
	 * Returns the root AVL node, or null if the tree is empty
	 *
	 * precondition: none postcondition: none
	 */
	public IAVLNode getRoot() {
		return this.root;
	}

	// returns the height of the tree, 0 if it's empty
	public int getHeight() {
		if (this.empty()) {
			return 0;
		}
		return this.getRoot().getHeight();
	}

	/**
	 * public string split(int x)
	 *
	 * splits the tree into 2 trees according to the key x. Returns an array [t1,
	 * t2] with two AVL trees. keys(t1) < x < keys(t2). precondition: search(x) !=
	 * null (i.e. you can also assume that the tree is not empty) postcondition:
	 * none
	 */
	public AVLTree[] split(int x) {
		return null;
	}
	
	private AVLTree[] split(int x, IAVLNode node) {
		
		IAVLNode pNode = node;
		AVLTree[] result = new AVLTree[2];

		result[0] = new AVLTree();	//smaller than x
		result[1] = new AVLTree();	//bigger than x
		
		result[0].root = node.getLeft();
		result[1].root = node.getRight();
		pNode = node.getParent();
		
		while(node != null) {
			if(pNode.getKey() < x) {
				
				result[0] = this.join(pNode, result[0]);
			}
		}
			
		
	}

	/**
	 * public join(IAVLNode x, AVLTree t)
	 *
	 * joins t and x with the tree. Returns the complexity of the operation
	 * (|tree.rank - t.rank| + 1). precondition: keys(x,t) < keys() or keys(x,t) >
	 * keys(). t/tree might be empty (rank = -1). postcondition: none
	 */
		public int join(IAVLNode x, AVLTree t) {
		int valtoreturn = Math.abs(this.getHeight()-t.getHeight()) +1;
		if (t.empty()) {
			// t is empty. we can just insert x to this
			this.insert(x.getKey(), x.getValue());
			return valtoreturn;
		}
		if (this.empty()) {
			// tree is empty. we can just insert x to t and set tree.root <--- t.root
			t.insert(x.getKey(), x.getValue());
			this.root = t.root;
			return valtoreturn;
		}

		// t && tree are not empty
		
		// lets check witch tree should be on which side
		AVLTree Rtree;
		AVLTree Ltree;
		if (this.getRoot().getKey()>x.getKey()) {
			Rtree = this;
			Ltree = t;
		}else {
			Ltree = this;
			Rtree = t;
		}
		// now lets check which tree is higher
		int heightdiff = Rtree.getRoot().getHeight() - Ltree.getRoot().getHeight();
		if (heightdiff==0) {
			//trees are equal in height
			x.setRight(Rtree.getRoot());
			x.setRight(Ltree.getRoot());
			this.root=x;
			return 1;
		}
		IAVLNode temp = null;
		if (heightdiff> 0) {
			//Rtree is taller than Ltree
			temp = Rtree.root;
			while (temp.getHeight()>Ltree.getRoot().getHeight()) {
				temp = temp.getLeft();
			}
			/* set x to be:
			 *  	         temp.parent
			 *	     		/
			 *      	   x
			 *     		 /   \
			 * Ltree.root     temp
			 */
			x.setParent(temp.getParent());
			x.setLeft(Ltree.getRoot());
			x.setRight(temp);
			x.getParent().setLeft(x);
			x.getLeft().setParent(x);
			x.getRight().setParent(x);
			x.getLeft().updatePath();
			
		}else {
			//Ltree is taller than Rtree
			temp = Ltree.root;
			while (temp.getHeight()>Rtree.getRoot().getHeight()) {
				temp = temp.getRight();
			}
			/* set x to be:
			 * temp.parent
			 *	     \
			 *        x
			 *      /   \
			 *  temp      Rtree.root
			 */
			x.setParent(temp.getParent());
			x.setRight(Rtree.getRoot());
			x.setLeft(temp);
			x.getParent().setRight(x);
			x.getRight().setParent(x);
			x.getLeft().setParent(x);
			x.getRight().updatePath();
		}
		return valtoreturn;
	}

	public IAVLNode rotateRight(IAVLNode node) {

		IAVLNode leftNode = node.getLeft();
		IAVLNode parentNode = node.getParent();

		if (parentNode == null) { // node is root
			this.root = (AVLNode) leftNode;

		} else if (parentNode.getKey() > node.getKey()) { // node is left child
			parentNode.setLeft(leftNode); // updating parentNode's new child

		} else { // node is right child
			parentNode.setRight(leftNode);
		}

		leftNode.setParent(parentNode); // updating leftNode's new parent

		// rotate
		node.setLeft(leftNode.getRight());
		leftNode.setRight(node);

		// sizes and heights update
		node.update();
		leftNode.update();

		return leftNode;
	}

	public IAVLNode rotateLeft(IAVLNode node) {
		IAVLNode rightNode = node.getRight();
		IAVLNode parentNode = node.getParent();

		if (parentNode == null) { // node is root
			this.root = (AVLNode) rightNode;

		} else if (parentNode.getKey() > node.getKey()) { // node is left child
			parentNode.setLeft(rightNode); // updating parentNode's new child

		} else { // node is right child
			parentNode.setRight(rightNode);
		}

		rightNode.setParent(parentNode); // updating rightNode's new parent

		// rotate
		node.setRight(rightNode.getLeft());
		rightNode.setLeft(node);

		// sizes and heights update
		node.update();
		rightNode.update();

		return rightNode;
	}

	public IAVLNode findPosition(IAVLNode currNode, int k) {
		IAVLNode tmp = null; // always a step before the currNode

		while (currNode.isRealNode()) {
			tmp = currNode;
			if (k < currNode.getKey()) {
				currNode = currNode.getLeft();
			} else if (k > currNode.getKey()) {
				currNode = currNode.getRight();
			} else { // k == currNode.key
				return currNode; // return an inner node
			}
		}
		return tmp; // return a leaf or null if tree is empty
	}

	/**
	 * public interface IAVLNode ! Do not delete or modify this - otherwise all
	 * tests will fail !
	 */
	public interface IAVLNode {
		public int getKey(); // returns node's key (for virtual node return -1)
		public String getValue(); // returns node's value [info] (for virtual node return null)
		public void setLeft(IAVLNode node); // sets left child
		public IAVLNode getLeft(); // returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); // sets right child
		public IAVLNode getRight(); // returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); // sets parent
		public IAVLNode getParent(); // returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
		public void setHeight(int height); // sets the height of the node
		public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
		public int getSize(); // returns the size of the node's subtree including the node itself
		public void promote(); // promote rank
		public void demote(); // demote rank
		public int getRank(); // returns node's rank
		public int rankDiffRight(); // returns rank difference between the node and it's right child
		public int rankDiffLeft(); // returns rank difference between the node and it's left child
		public void update(); // update node's height and size
		public void updatePath(); // update the eight and size of the path from the the giving node to the root
	}

	/**
	 * public class AVLNode
	 *
	 * If you wish to implement classes other than AVLTree (for example AVLNode), do
	 * it in this file, not in another file. This class can and must be modified.
	 * (It must implement IAVLNode)
	 */
	public class AVLNode implements IAVLNode {
		private int key;
		private String val;
		private AVLNode left = null;
		private AVLNode right = null;
		private AVLNode parent = null;
		private int height;
		private int size;
		private int rank;

		public AVLNode(int Key, String Val) {
			this.key = Key;
			this.val = Val;
			this.left = new AVLNode();
			this.right = new AVLNode();
			this.height = 0;
			this.size = 1;
			this.rank = 0;
		}

		public AVLNode() {
			this.key = -1;
			this.val = null;
			this.left = null;
			this.right = null;
			this.height = -1;
			this.size = 0;
			this.rank = -1;
		}

		public int getKey() {
			return this.key; // returns key
		}

		public String getValue() {
			return this.val; // returns value
		}

		public void setLeft(IAVLNode node) {
			this.left = (AVLNode) node; // to be replaced by student code
		}

		public IAVLNode getLeft() {
			if (this.left == null || this.left.getValue() == "-1") {
				return null;
			}
			return this.left; // returns the left child of the node if it has one, or null otherwise
		}

		public void setRight(IAVLNode node) {
			this.right = (AVLNode) node; // to be replaced by student code
		}

		public IAVLNode getRight() {
			if (this.right == null || this.right.getKey() == -1) {
				return null;
			}
			return this.right; // returns the right child of the node if it has one, or null otherwise
		}

		public void setParent(IAVLNode node) {
			this.parent = (AVLNode) node; // sets the given node as the parent of the current node
		}

		public IAVLNode getParent() {
			return this.parent; // to be replaced by student code
		}

		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode() {
			return ((this.getKey() != -1) ? true : false); // returns true if the key is positive else returns false
		}

		public void setHeight(int height) {
			this.height = height; // to be replaced by student code
		}

		public int getHeight() {
			return this.height; // to be replaced by student code
		}

		public int getSize() {
			return this.size;
		}
		
		public void setSize(int i) {
			this.size=(i);
		}

		public void promote() {
			this.rank++;
		}

		public void demote() {
			this.rank--;
		}

		public int getRank() {
			return this.rank;
		}

		public int rankDiffRight() {
			if (this.isRealNode() == false) {
				return 0;
			}
			System.out.println(this.rank);
			System.out.println(this.right.rank);
			System.out.println(this.getRank() - this.getRight().getRank());
			return (this.getRank() - this.getRight().getRank());
		}

		public int rankDiffLeft() {
			if (this.isRealNode() == false) {
				return 0;
			}
			return this.rank - this.getLeft().getRank();
		}

		public void update() {
			this.size = 1; // the node itself

			if (this.right.isRealNode()) { // adding right subtree size
				this.size += this.right.size;
			}
			if (this.left.isRealNode()) { // adding left subtree size
				this.size += this.left.size;
			}

			this.height = 1 + Math.max(this.left.height, this.right.height);
		}

		public void updatePath() {
			IAVLNode curr = this;
			while (curr != null) {
				curr.update();
				curr = curr.getParent();
			}
		}

	}

}
