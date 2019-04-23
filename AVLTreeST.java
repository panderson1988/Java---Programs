/******************************************************************************
 *  This is a modified version of the AVLTreeST class found on the web and
 *  written by the authors of the Algorithms textbook.  It has been adapted
 *  by John Rogers for the Fall, 2017, offering of CSC 403.
 *
 *  The class represents a symbol table implemented using an AVL tree, which
 *  is a self-balancing binary search tree (BST).  Although not presented
 *  in the current edition of the textbook, it is a good first example of a
 *  self-balancing BST.
 *  
 *  Some terms to keep in mind:
 *  
 *  - BST property: This is also called the symmetric order property.  A binary
 *  tree has this if, at every node, the keys in nodes in its left sub-tree
 *  are less than node's key and the keys in the nodes in its right sub-tree
 *  are greater.
 *  
 *  - AVL property: A BST has this property if, at every node, the height of
 *  the sub-tree to the left and the height of the sub-tree to the right
 *  differ by at most 1.
 *  
 *  - node size: The size of a node is the number of nodes in the sub-tree
 *  rooted at the node.  The size of an empty tree is 0.
 *  
 *  - node height: The height of a node is the length of the longest path
 *  (counting edges) from this node to each leaf below it.
 *  
 ******************************************************************************/

package avltree;

import java.util.NoSuchElementException;

import algs13.*;
import stdlib.*;


public class AVLTreeST<Key extends Comparable<Key>, Value> {

    /**
     * The root node.
     */
    private Node root;

    /**
     * This class represents a node of the AVL tree.
     */
    private class Node {
        private final Key key;   // the key
        private Value val;       // the associated value
        private int height;      // height of the subtree
        private int size;        // number of nodes in subtree
        private Node left;       // left subtree
        private Node right;      // right subtree

        public Node(Key key, Value val, int height, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
            this.height = height;
        }
    }

    /**
     * Initializes an empty symbol table.
     */
    public AVLTreeST() {
    	root = null;
    }

    /**
     * Checks whether the symbol table is empty.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Returns the number key-value pairs in the symbol table.
     */
    public int size() {
        return size(root);
    }

    /**
     * Returns the number of nodes in the subtree.
     */
    private int size(Node node) {
        if (node == null) return 0;
        return node.size;
    }

    /**
     * Returns the height of the internal AVL tree. It is assumed that the
     * height of an empty tree is -1 and the height of a tree with just one node
     * is 0.
     */
    public int height() {
        return height(root);
    }

    /**
     * Returns the height of the subtree.
     */
    private int height(Node node) {
        if (node == null) return -1;
        return node.height;
    }

    /**
     * Returns the value associated with the given key.
     */
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        Node node = get(root, key);
        if (node == null) return null;
        return node.val;
    }

    /**
     * Returns value associated with the given key in the subtree or
     */
    private Node get(Node node, Key key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) return get(node.left, key);
        else if (cmp > 0) return get(node.right, key);
        else return node;
    }

    /**
     * Checks whether the symbol table contains the given key.
     */
    public boolean contains(Key key) {
        return get(key) != null;
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting
     * the old value with the new value if the symbol table already contains the
     * specified key. Deletes the specified key (and its associated value) from
     * this symbol table if the specified value is {@code null}.
     */
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        if (val == null) {
            delete(key);
            return;
        }
        root = put(root, key, val);
        assert check();
    }

    /**
     * Inserts the key-value pair in the subtree. It overrides the old value
     * with the new value if the symbol table already contains the specified key
     * and deletes the specified key (and its associated value) from this symbol
     * table if the specified value is {@code null}.
     */
    private Node put(Node node, Key key, Value val) {
        if (node == null) return new Node(key, val, 0, 1);
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, val);
        }
        else if (cmp > 0) {
            node.right = put(node.right, key, val);
        }
        else {
            node.val = val;
            return node;
        }
        node.size = 1 + size(node.left) + size(node.right);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    /**
     * Restores the AVL tree property of the subtree.
     */
    private Node balance(Node node) {
        if (balanceFactor(node) < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            node = rotateLeft(node);
        }
        else if (balanceFactor(node) > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            node = rotateRight(node);
        }
        return node;
    }

    /**
     * Returns the balance factor of the subtree. The balance factor is defined
     * as the difference in height of the left subtree and right subtree, in
     * this order. Therefore, a subtree with a balance factor of -1, 0 or 1 has
     * the AVL property since the heights of the two child subtrees differ by at
     * most one.
     */
    private int balanceFactor(Node node) {
        return height(node.left) - height(node.right);
    }

    /**
     * Rotates the given subtree to the right.
     */
    private Node rotateRight(Node node) {
        Node child = node.left;
        node.left = child.right;
        child.right = node;
        child.size = node.size;
        node.size = 1 + size(node.left) + size(node.right);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        child.height = 1 + Math.max(height(child.left), height(child.right));
        return child;
    }

    /**
     * Rotates the given subtree to the left.
     *
     */
    private Node rotateLeft(Node node) {
        Node child = node.right;
        node.right = child.left;
        child.left = node;
        child.size = node.size;
        node.size = 1 + size(node.left) + size(node.right);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        child.height = 1 + Math.max(height(child.left), height(child.right));
        return child;
    }

    /**
     * Removes the specified key and its associated value from the symbol table
     * (if the key is in the symbol table).
     */
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        if (!contains(key)) return;
        root = delete(root, key);
        assert check();
    }

    /**
     * Removes the specified key and its associated value from the given
     * subtree.
     */
    private Node delete(Node node, Key key) {
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = delete(node.left, key);
        }
        else if (cmp > 0) {
            node.right = delete(node.right, key);
        }
        else {
            if (node.left == null) {
                return node.right;
            }
            else if (node.right == null) {
                return node.left;
            }
            else {
                Node hold = node;
                node = min(hold.right);
                node.right = deleteMin(hold.right);
                node.left = hold.left;
            }
        }
        node.size = 1 + size(node.left) + size(node.right);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    /**
     * Removes the smallest key and associated value from the symbol table.
     */
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("called deleteMin() with empty symbol table");
        root = deleteMin(root);
        assert check();
    }

    /**
     * Removes the smallest key and associated value from the given subtree.
     */
    private Node deleteMin(Node node) {
        if (node.left == null) return node.right;
        node.left = deleteMin(node.left);
        node.size = 1 + size(node.left) + size(node.right);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    /**
     * Removes the largest key and associated value from the symbol table.
     */
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("called deleteMax() with empty symbol table");
        root = deleteMax(root);
        assert check();
    }

    /**
     * Removes the largest key and associated value from the given subtree.
     */
    private Node deleteMax(Node node) {
        if (node.right == null) return node.left;
        node.right = deleteMax(node.right);
        node.size = 1 + size(node.left) + size(node.right);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    /**
     * Returns the smallest key in the symbol table.
     */
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("called min() with empty symbol table");
        return min(root).key;
    }

    /**
     * Returns the node with the smallest key in the subtree.
     */
    private Node min(Node node) {
        if (node.left == null) return node;
        return min(node.left);
    }

    /**
     * Returns the largest key in the symbol table.
     */
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("called max() with empty symbol table");
        return max(root).key;
    }

    /**
     * Returns the node with the largest key in the subtree.
     */
    private Node max(Node node) {
        if (node.right == null) return node;
        return max(node.right);
    }

    /**
     * Returns the number of keys in the symbol table strictly less than
     * {@code key}.
     */
    public int rank(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to rank() is null");
        return rank(key, root);
    }

    /**
     * Returns the number of keys in the subtree less than key.
     */
    private int rank(Key key, Node node) {
        if (node == null) return 0;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) return rank(key, node.left);
        else if (cmp > 0) return 1 + size(node.left) + rank(key, node.right);
        else return size(node.left);
    }

    /**
     * Returns all keys in the symbol table.
     */
    public Iterable<Key> keys() {
        return keysInOrder();
    }

    /**
     * Returns all keys in the symbol table following an in-order traversal.
     */
    public Iterable<Key> keysInOrder() {
        Queue<Key> queue = new Queue<Key>();
        keysInOrder(root, queue);
        return queue;
    }

    /**
     * Adds the keys in the subtree to queue following an in-order traversal.
     */
    private void keysInOrder(Node node, Queue<Key> queue) {
        if (node == null) return;
        keysInOrder(node.left, queue);
        queue.enqueue(node.key);
        keysInOrder(node.right, queue);
    }

    /**
     * Returns all keys in the symbol table following a level-order traversal.
     */
    public Iterable<Key> keysLevelOrder() {
        Queue<Key> queue = new Queue<Key>();
        if (!isEmpty()) {
            Queue<Node> queue2 = new Queue<Node>();
            queue2.enqueue(root);
            while (!queue2.isEmpty()) {
                Node node = queue2.dequeue();
                queue.enqueue(node.key);
                if (node.left != null) {
                    queue2.enqueue(node.left);
                }
                if (node.right != null) {
                    queue2.enqueue(node.right);
                }
            }
        }
        return queue;
    }

    /**
     * Returns all keys in the symbol table in the given range.
     */
    public Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");
        Queue<Key> queue = new Queue<Key>();
        keys(root, queue, lo, hi);
        return queue;
    }

    /**
     * Adds the keys between {@code lo} and {@code hi} in the subtree
     * to the {@code queue}.
     */
    private void keys(Node node, Queue<Key> queue, Key lo, Key hi) {
        if (node == null) return;
        int cmplo = lo.compareTo(node.key);
        int cmphi = hi.compareTo(node.key);
        if (cmplo < 0) keys(node.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(node.key);
        if (cmphi > 0) keys(node.right, queue, lo, hi);
    }

    /**
     * Returns the number of keys in the symbol table in the given range.
     */
    public int size(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to size() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to size() is null");
        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else return rank(hi) - rank(lo);
    }

    /**
     * Checks if the AVL tree invariants are fine.
     */
    private boolean check() {
        if (!isBST()) StdOut.println("Symmetric order not consistent");
        if (!isAVL()) StdOut.println("AVL property not consistent");
        if (!isSizeConsistent()) StdOut.println("Subtree counts not consistent");
        return isBST() && isAVL() && isSizeConsistent();
    }

    /**
     * Checks if AVL property is consistent.
     */
    private boolean isAVL() {
        return isAVL(root);
    }

    /**
     * Checks if AVL property is consistent in the subtree.
     */
    private boolean isAVL(Node node) {
        if (node == null) return true;
        int bf = balanceFactor(node);
        if (bf > 1 || bf < -1) return false;
        return isAVL(node.left) && isAVL(node.right);
    }

    /**
     * Checks if the symmetric order is consistent.
     */
    private boolean isBST() {
        return isBST(root, null, null);
    }

    /**
     * Checks if the tree rooted at node is a BST with all keys strictly between
     * min and max (if min or max is null, treat as empty constraint) Credit:
     * Bob Dondero's elegant solution
     */
    private boolean isBST(Node node, Key min, Key max) {
        if (node == null) return true;
        if (min != null && node.key.compareTo(min) <= 0) return false;
        if (max != null && node.key.compareTo(max) >= 0) return false;
        return isBST(node.left, min, node.key) && isBST(node.right, node.key, max);
    }

    /**
     * Checks if size is consistent.
     */
    private boolean isSizeConsistent() {
        return isSizeConsistent(root);
    }

    /**
     * Checks if the size of the subtree is consistent.
     */
    private boolean isSizeConsistent(Node node) {
        if (node == null) return true;
        if (node.size != size(node.left) + size(node.right) + 1) return false;
        return isSizeConsistent(node.left) && isSizeConsistent(node.right);
    }

    /*
     * This draws the tree in the StdDraw canvas.
     */
	public void drawTree() {
		if (root != null) {
			StdDraw.setPenColor (StdDraw.BLACK);
			StdDraw.setCanvasSize(2000,700);
			drawTree(root, .5, 1, .15, 0);
		}
	}

	/*
	 * This draws the tree from the node n down to the leaves below it.
	 */
	private void drawTree (Node node, double x, double y, double range, int depth) {
		int CUTOFF = 10;
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.text(x, y, node.key+"/"+node.height+"/"+node.size);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius (.007);
		if (node.left != null && depth != CUTOFF) {
			StdDraw.line (x-range, y-.08, x-.01, y-.01);
			drawTree (node.left, x-range, y-.1, range*.5, depth+1);
		}
		if (node.right != null && depth != CUTOFF) {
			StdDraw.line (x+range, y-.08, x+.01, y-.01);
			drawTree (node.right, x+range, y-.1, range*.5, depth+1);
		}
	}
}
