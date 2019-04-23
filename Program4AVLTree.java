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

package program4;

import java.util.NoSuchElementException;

import algs13.*;
import stdlib.*;


public class Program4AVLTree<Key extends Comparable<Key>, Value> {

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
    public Program4AVLTree() {
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
        	// The new node was inserted to the right
            if (balanceFactor(node.right) > 0) {
            	// The new node was inserted to the left of the right child
                node.right = rotateRight(node.right);
            } // Otherwise it was inserted to the right of the right child
            node = rotateLeft(node);
        }
        else if (balanceFactor(node) > 1) {
        	// The new node was inserted to the left
            if (balanceFactor(node.left) < 0) {
            	// The new node wsa inserted to the right of the left child
                node.left = rotateLeft(node.left);
            } // Otherwise it was inserted to the left of the left child
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

}
