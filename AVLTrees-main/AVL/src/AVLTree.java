import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
		AVLNode node = SearchNode(k);
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
		System.out.println("***I'm here!***");
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
							newNode.getRight().rankDiffRight() == 2) {

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
					if (newNode.getLeft().rankDiffLeft() == 1 && // case2: node-02 with child-12, terminal
							newNode.getRight().rankDiffRight() == 2) {

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
		if (this.max.getKey() == k) { // if we're trying to delete the maximal node, it doesn't have a right son
			AVLNode node = this.max;
			if (node.left.isRealNode()) {
				node.parent.right = (AVLNode) node.getLeft();
				node.parent.right.parent = node.parent;
			} else {
				this.max.parent.right = null;
			}
			this.calcmax(); // we need to update the current maximal node
			return 0;
		}
		if (this.min.getKey() == k) { // if we're trying to delete the minimal node, it doesn't have a left son
			AVLNode node = this.min;
			if (node.right.isRealNode()) {
				node.parent.left = (AVLNode) node.getRight();
				node.parent.left.parent = node.parent;
			} else {
				this.min.parent.left = null;
			}
			this.calcmin(); // we need to update the current minimal node
			return 0;
		}
		AVLNode node2delete = this.SearchNode(k);
		if (node2delete == null) { // the tree doesn't contain a node with key k... nothing to delete
			return -1;
		}

		return 42; // to be replaced by student code
	}

	private AVLNode SearchNode(int k) {
		AVLNode node = (AVLNode) this.getRoot();
		while (node.isRealNode()) {
			if (node.getKey() == k) {
				return node;
			}
			if (node.getKey() > k) {
				node = (AVLNode) node.getLeft();
			} else {
				node = (AVLNode) node.getRight();
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
		return this.min.val;
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree, or null if the
	 * tree is empty
	 */
	public String max() {
		return this.max(); // this is a saved field in our data structure :)
	}

	/**
	 * public void calcmax()
	 * 
	 * calculates the max key out of our tree and sets the corresponding field if
	 * the tree is empty, it sets it to null
	 */
	public void calcmax() {
		if (this.empty()) {
			this.max = null;
			this.min = null;
			return;
		}
		AVLNode node = (AVLNode) this.getRoot();
		AVLNode max = null;
		while (node.isRealNode()) {
			max = node;
			node = (AVLNode) node.getRight();
		}
		this.max = max;
	}

	/**
	 * public void calcmin()
	 * 
	 * calculates the minimal key out of our tree and sets the corresponding field
	 * if the tree is empty, it sets it to null
	 */

	public void calcmin() {
		if (this.empty()) {
			this.min = null;
			this.max = null;
			return;
		}
		AVLNode node = (AVLNode) this.getRoot();
		AVLNode min = null;
		while (node.isRealNode()) {
			min = node;
			node = (AVLNode) node.getLeft();
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

	public void inOrderList(IAVLNode node, List<IAVLNode> inOrderNodeList) {

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

	/**
	 * public join(IAVLNode x, AVLTree t)
	 *
	 * joins t and x with the tree. Returns the complexity of the operation
	 * (|tree.rank - t.rank| + 1). precondition: keys(x,t) < keys() or keys(x,t) >
	 * keys(). t/tree might be empty (rank = -1). postcondition: none
	 */
	public int join(IAVLNode x, AVLTree t) {
		return 0;
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
		public int getKey(); // returns node's key (for virtuval node return -1)
		public String getValue(); // returns node's value [info] (for virtuval node return null)
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
