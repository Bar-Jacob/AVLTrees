package il.ac.tau.cs.dast.ex1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * WAVLTree
 * <p>
 * An implementation of a WAVL Tree.
 * Implemented by: alona1 and nadavcordova
 */

public class WAVLTree {

    private WAVLNode root = createExternalLeaf(null);
    private WAVLNode min;
    private WAVLNode max;

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */
    public boolean empty() {
        return !root.isInnerNode();
    }

    /**
     * public String search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public String search(int k) {
        WAVLNode node = findPosition(k);
        return node.getValue();
    }

    /**
     * public int insert(int k, String i)
     * <p>
     * inserts an item with key k and info i to the WAVL tree.
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) {
        WAVLNode posToInsert = findPosition(k);

        if (posToInsert.isInnerNode())
            return -1;

        WAVLNode node = new WAVLNode(posToInsert.parent, k, i);

        if (empty()) {
            root = node;
            max = node;
            min = node;
            return 0;
        } else {
            if (posToInsert.parent.key > node.key) {
                node.parent.left = node;
                if (node.parent.getKey() == min.getKey()) {
                    min = node;
                }
            } else {
                node.parent.right = node;
                if (node.parent.getKey() == max.getKey()) {
                    max = node;
                }
            }
            return insertRebalance(node.parent);
        }
    }

    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of re-balancing operations, or 0 if no rebalancing operations were needed.
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        WAVLNode node = findPosition(k);

        // Check if node is in the tree
        if (!node.isInnerNode()) {
            return -1;
        }

        // if the node is minimum
        if (node.getKey() == min.getKey()) {
            min = node.successor();
        }

        // if the node is maximum
        if (node.getKey() == max.getKey()) {
            // if the node has a child - find the max in his subtree
            if (node.getLeft().isInnerNode()) {
                WAVLNode node_2 = node.getLeft();
                while (node_2.getRight().isInnerNode()) {
                    node_2 = node_2.getRight();
                }
                max = node_2;
            // if the node is a leaf
            } else {
                max = node.parent;
            }
        }


        if (node.isLeaf()) {                                    // Case leaf - only remove
            node.replaceWith(createExternalLeaf(null));

        } else if (!node.getLeft().isInnerNode()) {             // Case has only right child -
            node.replaceWith(node.right);                       // remove and put right child in place

        } else if (!node.getRight().isInnerNode()) {              // Case has only left child -
            node.replaceWith(node.left);                        // remove and put left child in place

        } else {                                                // Case has two children - replace with successor
            WAVLNode successor = node.successor();

            /*
              Successor of a node with 2 children must either be a leaf or have only a right child.
              Remove the successor by putting its right child (might be external) as the successor's parent's child.
             */
            successor.replaceWith(successor.right);

            // Put successor instead of node
            node.key = successor.key;
            node.value = successor.value;

            // Change the pointer so that the re-balancing starts at this node
            node = successor.right;
        }
        return removeRebalance(node.parent);
    }

    /**
     * public String min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public String min() {
        if (empty()){return null;}
        return min.getValue();
    }

    /**
     * public String max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public String max() {
        if (empty()) {return null;}
        return max.getValue();
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        List<WAVLNode> nodeList = getInOrder(root);
        int[] keys = new int[nodeList.size()];

        int i = 0;
        for (WAVLNode node : nodeList) {
            keys[i] = node.key;
            i++;
        }
        return keys;
    }

    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() {
        List<WAVLNode> nodeList = getInOrder(root);
        String[] values = new String[nodeList.size()];

        int i = 0;
        for (WAVLNode node : nodeList) {
            values[i] = node.value;
            i++;
        }
        return values;
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    public int size() {
        return root.getSubtreeSize();
    }

    /**
     * public WAVLNode getRoot()
     * <p>
     * Returns the root WAVL node, or null if the tree is empty
     */
    public WAVLNode getRoot() {
        return root.isInnerNode() ? root : null;
    }

    /**
     * public int select(int i)
     * <p>
     * Returns the value of the i'th smallest key (return -1 if tree is empty)
     * Example 1: select(1) returns the value of the node with minimal key
     * Example 2: select(size()) returns the value of the node with maximal key
     * Example 3: select(2) returns the value 2nd smallest minimal node, i.e the value of the node minimal node's successor
     */
    public String select(int i) {
        if (empty()){return null;}
        WAVLNode node = min;
        for (int j = 1; j < i; j++) {
            node = node.successor();
        }
        return node.getValue();

    }

    /**
     * Recursively get an ordered list of all nodes in the tree.
     * Use (Linked)Lists to do this.
     *
     * @param node Get the subtree of node in-order. First call is root.
     * @return Ordered (by key) list of all nodes in the tree.
     */
    private List<WAVLNode> getInOrder(WAVLNode node) {
        if (!node.isInnerNode()) {
            return new ArrayList<>();
        }
        List<WAVLNode> list = new LinkedList<>();
        list.addAll(getInOrder(node.left));
        list.add(node);
        list.addAll(getInOrder(node.right));
        return list;
    }

    /**
     * Balance the tree by demoting and rotating.
     *
     * @param node The WAVLNode from which to start balancing up until root.
     * @return Number of rebalancing operations, or 0 if no rebalancing operations were necessary.
     */
    private int removeRebalance(WAVLNode node) {
        int counter = 0;
        while (node != null) {
            counter += node.calculateSize();

            // Check if problem is on the left
            if (node.leftDiff() == 3) {
                if (node.rightDiff() == 2) {                    // Case 1
                    node.rank--;
                    counter++;

                } else if ((node.right.leftDiff() == 2)
                        && (node.right.rightDiff() == 2)) {     // Case 2
                    node.rank--;
                    node.right.rank--;
                    counter += 2;

                } else if (node.right.rightDiff() == 1) {       // Case 3
                    node.rank--;
                    node.right.rank++;
                    counter += rotateLeft(node);
                    counter += 3;

                } else {                                        // Case 4
                    node.rank -= 2;
                    node.right.rank--;
                    node.right.left.rank += 2;
                    counter += rotateRight(node.right);
                    counter += rotateLeft(node);
                    counter += 3;
                }

                // Problem is on the right
            } else if (node.rightDiff() == 3) {
                if (node.rank - node.left.rank == 2) {          // Case 1
                    node.rank--;
                    counter++;

                } else if ((node.left.leftDiff() == 2)
                        && (node.left.rightDiff() == 2)) {      // Case 2
                    node.rank--;
                    node.left.rank--;
                    counter += 2;

                } else if (node.left.leftDiff() == 1) {         // Case 3
                    node.rank--;
                    node.left.rank++;
                    counter += rotateRight(node);
                    counter += 3;

                } else {                                        // Case 4
                    node.rank -= 2;
                    node.left.rank--;
                    node.left.right.rank += 2;
                    counter += rotateLeft(node.left);
                    counter += rotateRight(node);
                    counter += 5;
                }
            }

            node = node.parent;
        }
        return counter;
    }

    /**
     * Balance the tree by promoting and rotating.
     * Balance bottom-up and re-calculate sizes as we go up the branch.
     *
     * @param node The WAVLNode from which to start balancing up until root.
     * @return Number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     */
    private int insertRebalance(WAVLNode node) {
        int counter = 0;
        while (node != null) {
            counter += node.calculateSize();
            if (node.leftDiff() == 0) {                             // Problem on left side
                if (node.rightDiff() == 1) {                            // Case 1 - not terminal
                    node.rank++;
                    counter++;
                } else {
                    if (node.left.rightDiff() == 2) {                   // Case 2
                        node.rank--;
                        counter += rotateRight(node);

                        counter += 2;
                    } else {                                            // Case 3
                        WAVLNode left = node.left;
                        left.right.rank++;
                        left.rank--;
                        node.rank--;
                        counter += rotateLeft(left);
                        counter += rotateRight(node);

                        counter += 5;
                    }
                }
            } else if (node.rightDiff() == 0) {                     // Problem on right side
                if (node.leftDiff() == 1) {                             // Case 1
                    node.rank++;
                    counter++;
                } else {
                    if (node.right.leftDiff() == 2) {                   // Case 2
                        node.rank--;
                        counter += rotateLeft(node);

                        counter += 2;
                    } else {                                            // Case 3
                        WAVLNode right = node.right;
                        right.left.rank++;
                        right.rank--;
                        node.rank--;
                        counter += rotateRight(right);
                        counter += rotateLeft(node);

                        counter += 5;
                    }
                }
            }
            node = node.parent;
        }
        return counter;
    }

    /**
     * Find node with key k, or if does not exist return the external leaf where a node with that key will be inserted.
     *
     * @param k The key to search in the tree.
     * @return found node (might be external)
     */
    private WAVLNode findPosition(int k) {
        WAVLNode node = root;
        while (node.isInnerNode()) {
            if (node.getKey() == k)
                return node;
            else if (node.getKey() > k)
                node = node.left;
            else
                node = node.right;
        }

        return node;
    }

    /**
     * Rotate right on the given node.
     * Rotate on node >y< will produce the following:
     *            y                 x
     *     ______/ \__    =>     __/ \______
     *    x           c   =>    a           y
     * __/ \__            =>             __/ \__
     * a       b                         b       c
     *
     * @param node Node to be rotated
     * @return Number of extra demotions performed during the rotation (leaf rank change)
     */
    private int rotateRight(WAVLNode node) {
        WAVLNode left = node.left;

        // modify parent
        if (node.parent == null) {
            root = left;
        } else if (node.isLeftChild()) {
            node.parent.left = left;
        } else {
            node.parent.right = left;
        }

        // Update parents
        left.parent = node.parent;
        left.right.parent = node;
        node.parent = left;

        // rotate
        node.left = left.right;
        left.right = node;

        // update sub-tree sizes
        return node.calculateSize() + left.calculateSize();
    }

    /**
     * Rotate left on the given node.
     * Rotate on node >x< will produce the following:
     *        x                              y
     *     __/ \______       =>       ______/ \__
     *    a           y      =>      x           c
     *             __/ \__   =>   __/ \__
     *            b       c      a       b
     *
     * @param node Node to be rotated
     * @return Number of extra demotions performed during the rotation (leaf rank change)
     */
    private int rotateLeft(WAVLNode node) {
        WAVLNode right = node.right;

        // modify parent
        if (node.parent == null) {
            root = right;
        } else if (node.isLeftChild()) {
            node.parent.left = right;
        } else {
            node.parent.right = right;
        }

        // Update parents
        right.parent = node.parent;
        right.left.parent = node;
        node.parent = node.right;

        // rotate
        node.right = right.left;
        right.left = node;

        // update sub-tree sizes
        return node.calculateSize() + right.calculateSize();
    }

    /**
     * Create a new external leaf.
     *
     * @param parent Parent of the created leaf.
     * @return The created external leaf.
     */
    public WAVLNode createExternalLeaf(WAVLNode parent) {
        return new WAVLNode(parent, -1, null, true);
    }

    /**
     * public class WAVLNode
     */
    public class WAVLNode {
        private int key;
        private String value;
        private int rank;

        private WAVLNode left, right = null, parent = null;
        private int subTreeSize;

        /**
         * Create a new inner-node with given parameters.
         *
         * @param parent Parent on the created node
         * @param key    Key in the tree
         * @param value  Value of the node (info)
         */
        public WAVLNode(WAVLNode parent, int key, String value) {
            this(parent, key, value, false);
        }

        /**
         * Create a new node with given parameters
         *
         * @param parent   Parent on the created node
         * @param key      Key in the tree
         * @param value    Value of the node (info)
         * @param external Is this node an external node?
         */
        private WAVLNode(WAVLNode parent, int key, String value, boolean external) {
            this.key = key;
            this.value = value;
            this.rank = -1;
            this.parent = parent;
            this.subTreeSize = 0;

            // If not external set ranks and subtree size, and add 2 external leaves
            if (!external) {
                this.rank = 0;
                this.subTreeSize = 1;
                this.left = createExternalLeaf(this);
                this.right = createExternalLeaf(this);
            }
        }

        public int getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public WAVLNode getLeft() {
            return left;
        }

        public WAVLNode getRight() {
            return right;
        }

        public boolean isInnerNode() {
            return rank != -1;
        }

        public int getSubtreeSize() {
            return subTreeSize;
        }

        /**
         * @return whether the node is a leaf on an inner junction
         */
        public boolean isLeaf() {
            return ((!right.isInnerNode()) && (!left.isInnerNode()));
        }

        /**
         * @return whether the node is a left child or a right child
         */
        public boolean isLeftChild() {
            return (parent != null && parent.key > key);
        }

        /**
         * Get the successor of this node
         *
         * @return The successor node
         */
        public WAVLNode successor() {
            WAVLNode node;
            if (right.isInnerNode()) {
                node = right;
                while (node.left.isInnerNode())
                    node = node.left;
            } else {
                node = this;
                while (node.parent != null && !node.isLeftChild())
                    node = node.parent;
                node = node.parent;
            }
            return node;
        }

        /**
         * Replace the current node (this) with another node.
         * Update the parent pointer and the new node's parent pointer.
         * Doe's not redirect children!
         *
         * @param node The node to put instead of this
         */
        public void replaceWith(WAVLNode node) {
            if (parent != null) {
                if (isLeftChild()) {
                    parent.left = node;
                } else {
                    parent.right = node;
                }
            } else {
                WAVLTree.this.root = node;
            }
            node.parent = parent;
        }

        /**
         * @return Rank difference with right child
         */
        public int rightDiff() {
            return rank - right.rank;
        }

        /**
         * @return Rank difference with left child
         */
        public int leftDiff() {
            return rank - left.rank;
        }

        /**
         * Calculate the node's subTreeSize based on children's sizes.
         * Also fix problem: when node is leaf, make sure its rank is 0.
         *
         * @return Number to increase the demotions counter by (1 if rank changed and else 0)
         */
        public int calculateSize() {
            subTreeSize = (right != null ? right.subTreeSize : 0) + (left != null ? left.subTreeSize : 0) + 1;
            if (isLeaf() && rank != 0) {
                rank = 0;
                return 1;
            }
            return 0;
        }

        public int getRank() {
            return rank;
        }
    }

}