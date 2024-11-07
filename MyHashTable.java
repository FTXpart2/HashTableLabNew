@SuppressWarnings("rawtypes")
public class MyHashTable<K, V> {

    private Node<Entry<K, V>>[] table;  // Array to hold the nodes of the table
    private static final int DEFAULT_CAPACITY = 10;

    // Constructor
    @SuppressWarnings("unchecked")
    public MyHashTable() {
        table = (Node<Entry<K, V>>[]) new Node[DEFAULT_CAPACITY];
    }

    // Inner Entry class to store key-value pairs
    private static class Entry<K, V> {
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Put method to add a key-value pair
    public void put(K key, V value) {
        int index = key.hashCode() % table.length;
        Node<Entry<K, V>> newNode = new Node<>(new Entry<>(key, value));
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<Entry<K, V>> current = table[index];
            while (current.next() != null) {
                current = current.next();
            }
            current.setNext(newNode); // Add to the end of the chain
        }
    }

    // Get method to retrieve a value by key
    public V get(K key) {
        int index = key.hashCode() % table.length;
        Node<Entry<K, V>> current = table[index];
        while (current != null) {
            if (current.get().key.equals(key)) {
                return current.get().value; // Return the value if key is found
            }
            current = current.next(); // Move to the next node in the chain
        }
        return null; // Return null if key is not found
    }

    // Remove method to delete a key-value pair
    public void remove(K key) {
        int index = key.hashCode() % table.length;
        Node<Entry<K, V>> current = table[index];
        Node<Entry<K, V>> previous = null;
        
        while (current != null) {
            if (current.get().key.equals(key)) {
                if (previous == null) {
                    table[index] = current.next(); // Remove the node at the head of the chain
                } else {
                    previous.setNext(current.next()); // Remove the node from the middle/end of the chain
                }
                return;
            }
            previous = current;
            current = current.next();
        }
    }

    // Method to check if the table contains a specific key
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    // Method to get all the keys in the hash table
    public MyHashSet<K> keySet() {
        MyHashSet<K> keys = new MyHashSet<>();
        for (int i = 0; i < table.length; i++) {
            Node<Entry<K, V>> current = table[i];
            while (current != null) {
                keys.add(current.get().key); // Add the key to the custom set
                current = current.next(); // Move to the next node
            }
        }
        return keys;
    }

    // Method to get all values in the hash table
    public MyHashSet<V> values() {
        MyHashSet<V> values = new MyHashSet<>();
        for (int i = 0; i < table.length; i++) {
            Node<Entry<K, V>> current = table[i];
            while (current != null) {
                values.add(current.get().value); // Add the value to the custom set
                current = current.next(); // Move to the next node
            }
        }
        return values;
    }
}