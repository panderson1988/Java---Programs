package program4;
import stdlib.*;
import algs13.Queue;
/* ***********************************************************************
 *
 *  A symbol table implemented with a binary search tree.
 *
 *
 *************************************************************************/
public class Program4BST<K extends Comparable<? super K>, V> {
	private Node<K,V> root;             // root of BST

	private static class Node<K extends Comparable<? super K>,V> {
		public K key;       // sorted by key
		public V val;             // associated data
		public Node<K,V> left, right;  // left and right subtrees

		public Node(K key, V val) {
			this.key = key;
			this.val = val;
		}
	}

	// is the symbol table empty?
	public boolean isEmpty() { return root == null; }

	/* *********************************************************************
	 *  Search BST for given key, and return associated value if found,
	 *  return null if not found
	 ***********************************************************************/
	// does there enodeist a key-value pair with given key?
	public boolean contains(K key) {
		return get(key) != null;
	}

	// return value associated with the given key, or null if no such key enodeists
	public V get(K key) { return get(root, key); }

	private V get(Node<K,V> node, K key) {
		if (node == null) return null;
		int cmp = key.compareTo(node.key);
		if      (cmp < 0) return get(node.left, key);
		else if (cmp > 0) return get(node.right, key);
		else              return node.val;
	}

	/* *********************************************************************
	 *  Insert key-value pair into BST
	 *  If key already enodeists, update with new value
	 ***********************************************************************/
	public void put(K key, V val) {
		if (val == null) { delete(key); return; }
		root = put(root, key, val);
	}

	private Node<K,V> put(Node<K,V> node, K key, V val) {
		if (node == null) return new Node<>(key, val);
		int cmp = key.compareTo(node.key);
		if      (cmp < 0)
			node.left  = put(node.left,  key, val);
		else if (cmp > 0)
			node.right = put(node.right, key, val);
		else
			node.val   = val;
		return node;
	}

	public void delete(K key) {
		root = delete(root, key);
	}

	private Node<K,V> delete(Node<K,V> node, K key) {
		if (node == null) return null;
		int cmp = key.compareTo(node.key);
		if (cmp < 0) {
			// Key to delete is to the left of node.
			node.left  = delete(node.left,  key);
			return node;
		}
		else if (cmp > 0) {
			// Key to delete is to the right of node.
			node.right = delete(node.right, key);
			return node;
		}
		else {
			// node contains the key we wish to delete.
			if (node.left == null && node.right == null) {
				// node is a leaf.
				return null;
			}
			else if (node.left == null) {
				// node has only a right child.
				return node.right;
			}
			else if (node.right == null) {
				// node has only a left child.
				return node.left;
			}
			else {
				// node has two children.
				Node<K,V> leftTreeManodeNode = node.left;
				// Find the node with the largest key to the left.
				while (leftTreeManodeNode.right != null) {
					leftTreeManodeNode = leftTreeManodeNode.right;
				}
				// Copy that largest key and its value to node.
				K leftTreeManodeKey = leftTreeManodeNode.key;
				node.key = leftTreeManodeNode.key;
				node.val = leftTreeManodeNode.val;
				// Delete the node copied from.
				node.right = delete(node.right, leftTreeManodeKey);
				return node;
			}
		}
	}
}
