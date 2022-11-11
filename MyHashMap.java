/*
 * name: Daniel Gil
 * class: csc210
 * purpose: HashMap implementation backed up with 
 * an arraylist that holds a linkedlist which holds
 * HashNodes.
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class MyHashMap<K, V>{
	
	private MyHashTable<K,V> hashMap;
	private java.util.Set<K> keys;
	private int size;
	
	public MyHashMap() {
		hashMap = new MyHashTable<>();
		keys = new HashSet<>();
		size = 0;
	}
	
	/*
	 * Associates the specified value with the specified key in this map.
	 * 
	 * key - key with which the specified value is to be associated
	 * val - value to be associated with the specified key
	 * 
	 * the previous value associated with key, or 
	 * null if there was no mapping for key. 
	 * (A null return can also indicate that the 
	 * map previously associated null with key.)
	 */
	public V put(K key, V value) {
		HashNode<K, V> oldNode = hashMap.put(key, value);
		if (oldNode == null) {
			size++;
			keys.add(key);
			return null;
		}
		return oldNode.getValue();
	}
	
	/*
	 * Returns the value to which the specified key is mapped, 
	 * or null if this map contains no mapping for the key.
	 * 
	 * key - the key whose associated value is to be returned
	 * 
	 * the value to which the specified key is mapped, 
	 * or null if this map contains no mapping for the key
	 */
	public V get(K key) {
		return hashMap.get(key);
	}

	/*
	 * Removes the mapping for the specified key from this map if present.
	 * 
	 * key - whose mapping is to be removed from the map
	 * 
	 * the previous value associated with key, 
	 * or null if there was no mapping for key. 
	 * (A null return can also indicate that the 
	 * map previously associated null with key.)
	 */
	public V remove(K key) {
		HashNode<K, V> oldNode = hashMap.remove(key);
		if (oldNode != null) {
			size--;
			keys.remove(key);
			return oldNode.getValue();
		}
		return null;
	}
	
	/*
	 * Returns true if this map contains a mapping for the specified key.
	 * 
	 * key - The key whose presence in this map is to be tested
	 * 
	 * true if this map contains a mapping for the specified key.
	 */
	public boolean containsKey(K key) {
		return hashMap.containsKey(key);
	}
	
	/*
	 * Returns true if this map maps one or more keys to the specified value.
	 * 
	 * val - value whose presence in this map is to be tested
	 * 
	 * true if this map maps one or more keys to the specified value
	 */
	public boolean containsValue(V value) {
		return hashMap.containsValue(value);
	}
	
	/*
	 * Returns true if this map contains no key-value mappings.
	 * 
	 * true if this map contains no key-value mappings
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/*
	 * Returns the number of key-value mappings in this map.
	 */
	public int size() {
		return size;
	}
	
	/*
	 * Returns a Set view of the keys contained in this map.
	 */
	public java.util.Set<K> keySet() {
		return keys;
	}
	
	/*
	 * Removes all of the mappings from this map.
	 */
	public void clear() {
		size = 0;
		keys.clear();
		hashMap.clear();
	}
	
	/*
	 * Outputs how many conflicts occur at each bucket 
	 * and list the keys in that bucket.
	 */
	public void printTable() {
		System.out.println(hashMap.toString());
	}
	
	@Override
	public String toString() {
		return hashMap.toString();
	}
	
	/*
	 * MyHashTable class holds the 8 buckets that each point
	 * to a LinkedList.
	 */
	private class MyHashTable<K, V>{
		
		private final static int numBuckets = 8;
		private ArrayList<LinkedList<HashNode<K,V>>> hashTable = 
				new ArrayList<>(numBuckets);
		
		public MyHashTable()
		{
			for (int i = 0; i < numBuckets; i++)
				hashTable.add(new LinkedList<HashNode<K, V>>());
		}
		
		/*
		 * Associates the specified value with the specified key in this map.
		 * 
		 * key - key with which the specified value is to be associated
		 * val - value to be associated with the specified key
		 * 
		 * returns a HashNode for the actual put function
		 * to check whether it is returning null or an object
		 */
		public HashNode<K, V> put(K key, V value) {
			int index = hash(key); 
			HashNode<K, V> node = new HashNode<>(key, value);
			LinkedList<HashNode<K,V>> curBucket = hashTable.get(index);
			
			//if bucket is empty
			if (curBucket.size() == 0) {
				curBucket.addFirst(node);
				return null;
			}
			//find if key already exists
			HashNode<K,V> currentNode = curBucket.get(0);
			while (currentNode != null) {
				if(currentNode.getKey().equals(node.getKey())) {
					HashNode<K, V> oldNode = 
							new HashNode<>(currentNode.getKey(), currentNode.getValue());
					currentNode.setValue(node.getValue());
					return oldNode;
				}
				currentNode = currentNode.getNext();
			}
			//add to the start of bucket if it doesn't exist
			HashNode<K,V> prev = curBucket.set(0, node);
			node.setNext(prev);
			return null;
		}
		
		/*
		 * Returns the value to which the specified key is mapped, 
		 * or null if this map contains no mapping for the key.
		 * 
		 * key - the key whose associated value is to be returned
		 * 
		 * the value to which the specified key is mapped, 
		 * or null if this map contains no mapping for the key
		 */
		public V get(K key) {
			int index = hash(key);
			LinkedList<HashNode<K,V>> curBucket = hashTable.get(index);
			
			//find if key already exists
			if (curBucket.size() != 0) {
				HashNode<K,V> currentNode = curBucket.get(0);
				while (currentNode != null) {
					if(currentNode.getKey().equals(key)) {
						return currentNode.getValue();
					}
					currentNode = currentNode.getNext();
				}
			}
			return null;
		}
		
		/*
		 * Removes the mapping for the specified key from this map if present.
		 * 
		 * key - whose mapping is to be removed from the map
		 * 
		 * returns a HashNode for the actual remove function
		 * to check whether it is returning null or an object
		 */
		public HashNode<K,V> remove(K key) {
			int index = hash(key);
			LinkedList<HashNode<K,V>> curBucket = hashTable.get(index);
			
			//find if key already exists
			if (curBucket.size() != 0) {
				HashNode<K,V> currentNode = curBucket.get(0);
				if (currentNode.getKey().equals(key)) {
					return curBucket.set(0, currentNode.getNext());
				}
				while (currentNode.getNext() != null) {
					if(currentNode.getNext().getKey().equals(key)) {
						HashNode<K, V> node = currentNode.getNext();
						currentNode.setNext(currentNode.getNext().getNext());
						return node;
					}
					currentNode = currentNode.getNext();
				}
			}
			return null;
		}
		
		/*
		 * Returns true if this map contains a mapping for the specified key.
		 * 
		 * key - The key whose presence in this map is to be tested
		 * 
		 * true if this map contains a mapping for the specified key.
		 */
		public boolean containsKey(K key) {
			int index = hash(key);
			LinkedList<HashNode<K,V>> curBucket = hashTable.get(index);
			
			//iterate through bucket if not empty
			if (curBucket.size() != 0) {
				HashNode<K,V> currentNode = curBucket.get(0);
				while (currentNode != null) {
					if(currentNode.getKey().equals(key)) {
						return true;
					}
					currentNode = currentNode.getNext();
				}
			}
			return false;
		}
		
		/*
		 * Returns true if this map maps one or more keys to the specified value.
		 * 
		 * val - value whose presence in this map is to be tested
		 * 
		 * true if this map maps one or more keys to the specified value
		 */
		public boolean containsValue(V value) {
			//look through all buckets
			for (int i = 0; i < numBuckets; i++) 
			{
				LinkedList<HashNode<K,V>> list = hashTable.get(i);
				
				//iterate through bucket if not empty
				if (list.size() != 0) {
					HashNode<K,V> currentNode = list.get(0);
					while (currentNode != null) {
						if(currentNode.getValue().equals(value))
							return true;
						currentNode = currentNode.getNext();
					}
				}
			}
			return false;
		}
		
		private int hash(K key) {
			int hashCode = key.hashCode();
			int index = hashCode % numBuckets;
			return Math.abs(index);
		}
		
		/*
		 * Removes all of the mappings from this map.
		 */
		public void clear() {
			for (int i = 0; i < numBuckets; i++) 
			{
				LinkedList<HashNode<K,V>> list = hashTable.get(i);
				
				if (list.size() != 0)
					list.clear();
			}
		}
		
		@Override
		public String toString()
		{
			int totalProblems = 0;
			String str = "";
			for (int i = 0; i < numBuckets; i++) 
			{
				str += "Index " + i + ": (";
				LinkedList<HashNode<K,V>> list = hashTable.get(i);
				
				int problems = 0;
				if (list.size() >= 1) {
					HashNode<K,V> cur = list.get(0);
					while (cur.getNext() != null) {
						problems++;
						cur = cur.getNext();
					}
				}
				
				totalProblems += problems;
				str += problems + " conflicts), [";
				
				if (list.size() != 0) {
					HashNode<K,V> currentNode = list.get(0);
					while (currentNode != null) {
						str += currentNode.getKey() + ", ";
						currentNode = currentNode.getNext();
					}
				}
				
				str += "]\n";
			}
			str += "Total # of conflicts: " + totalProblems;
			return str;		
		}
	}
}
