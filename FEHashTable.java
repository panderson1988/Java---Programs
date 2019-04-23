package finalexam;

import java.util.*;

public class FEHashTable<K extends Comparable<? super K>, V> {
	private ArrayList<K> keys;
	private ArrayList<V> values;
	private boolean[] neverUsed;
	private int count;
	private int capacity;
	
	public FEHashTable(int capacity) {
		keys = new ArrayList<K>(capacity);
		values = new ArrayList<V>(capacity);
		for (int i = 0; i < capacity; i++) {
			keys.add(null);
			values.add(null);
		}
		neverUsed = new boolean[capacity];
		for (int i = 0; i < capacity; i++) {
			neverUsed[i] = true;
		}
		count = 0;
		this.capacity = capacity;
	}
		
	public int size() {
		return count;
	}
	
	public boolean isEmpty() {
		return count == 0;
	}
	
	private int hash(K key) {
		return (key.hashCode() & 0x7fffffff) % capacity;
	}

	public boolean contains(K key) {
		int hash = hash(key);
		int startHash = hash;
		while (! key.equals(keys.get(hash))) {
			if (keys.get(hash) == null) {
				if (neverUsed[hash]) {
					return false;
				}
			}
			hash = (hash + 1) % capacity;
			if (hash == startHash) {
				return false;
			}
		}
		return true;
	}
	
	public V get(K key) {
		if (!contains(key)) return null;
		int hash = hash(key);
		int startHash = hash;
		while (! key.equals(keys.get(hash))) {
			if (keys.get(hash) == null) {
				if (neverUsed[hash]) {
					return null;
				}
			}
			hash = (hash + 1) % capacity;
			if (hash == startHash) {
				return null;
			}
		}
		return values.get(hash);
	}
	
	public void put(K key, V value) {
		if (count >= capacity) {
			throw new RuntimeException("Tried to insert into a full table");
		}
		
		if (contains(key)) {
			return;
		}
		int hash = hash(key);
		while (keys.get(hash) != null) {
			hash = (hash + 1) % capacity;
		}
		keys.set(hash, key);
		values.set(hash, value);
		neverUsed[hash] = false;
		count++;
		return;
	}
	
	public void delete(K key) {
		if (!contains(key)) {
			return;
		}
		int hash = hash(key);
		while (!key.equals(keys.get(hash))) {
			hash = (hash + 1) % capacity;
		}
		keys.set(hash, null);
		values.set(hash, null);
		count--;
		return;
	}
	
}
