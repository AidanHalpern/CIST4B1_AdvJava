/*
 * Author: Aidan Halpern
 * Date: 2026-5-3
 * Course: CIST004B1 - Java Data Structures
 * Homework: #Lab Week 13
 * Description: Graph with BFS and DFS
 * https://gemini.google.com/share/187574add760
 */

import java.util.ArrayList;
import java.util.Collections;

public class Halpern_Aidan_HWLabWeek13 {
    public static void main(String[] args) {
        Graph<String> city = new Graph();
        genCity(city);

        city.bfs("docks", "bridge");
        city.bfs("docks", "oak tree");
        city.bfs("delta", "downtown");
        System.out.println();
        city.dfs("docks", "bridge");
        city.dfs("docks", "oak tree");
        city.dfs("delta", "downtown");
        // TODO:
    }

    public static void genCity(Graph<String> city) {
        // Land Marks
        city.addVertex("tower");
        city.addVertex("bridge");
        city.addVertex("park");
        city.addVertex("downtown");
        city.addVertex("oak tree");

        city.addVertex("docks");
        city.addVertex("sea");
        city.addVertex("pond");
        city.addVertex("delta");
        city.addVertex("school");

        // Roads
        city.addEdge("docks", "sea", 1);
        city.addEdge("sea", "delta", 1);
        city.addEdge("delta", "park", 1);
        city.addEdge("park", "school", 1);
        city.addEdge("park", "pond", 1);

        city.addEdge("sea", "oak tree", 1);
        city.addEdge("sea", "bridge", 1);
        city.addEdge("docks", "oak tree", 1);
        city.addEdge("docks", "school", 1);
        city.addEdge("pond", "park", 1);

        city.addEdge("school", "oak tree", 1);
        city.addEdge("oak tree", "pond", 1);
        city.addEdge("park", "tower", 1);
        city.addEdge("tower", "pond", 1);
        city.addEdge("tower", "delta", 1);
    }
}

class Graph<T> {
    private class Edge {
        String dest;
        int weight;

        public Edge(String dest, int weight) {
            this.dest = dest;
            this.weight = weight;
        }
    }

    // data structures to represent the adjacency list
    int sizeEdge;
    int sizeVertex;
    HashTable<ArrayList<Edge>> adjlist;

    // constructor with whatever you need
    public Graph() {
        adjlist = new HashTable<>();

    }

    public void addVertex(String vertex) {
        if (adjlist.get(vertex) == null) {
            ArrayList<Edge> temp = new ArrayList<>();
            adjlist.insert(vertex, temp);
            sizeVertex++;
        }
    }

    public void addEdge(String source, String dest, int weight) {
        // Ensures that the soruce vertext exists
        addVertex(source);
        // Ensures that the dest vertext exists
        addVertex(dest);
        // Adds Edge
        adjlist.get(source).add(new Edge(dest, weight));
        sizeEdge++;

    }

    public void bfs(String startValue, String targetValue) {
        HashSet visited = new HashSet();
        Queue<String> queue = new Queue<>();
        HashTable<String> childToParent = new HashTable<>();

        visited.put(startValue);
        childToParent.insert(startValue, null);
        queue.enQueue(startValue);

        while (!queue.isEmpty()) {
            String curr = queue.deQueue();
            // Found target
            if (curr.equals(targetValue)) {
                printPath(childToParent, curr);
                return;
            }
            ArrayList<Edge> neighbors = adjlist.get(curr);
            if (neighbors != null) {
                for (Edge neighbor : neighbors) {
                    if (!visited.search(neighbor.dest)) {
                        visited.put(neighbor.dest);
                        childToParent.insert(neighbor.dest, curr);
                        queue.enQueue(neighbor.dest);
                    }
                }
            }
        }
        System.out.println("No BFS path found");
    }

    public void dfs(String startValue, String targetValue) {
        HashSet visited = new HashSet();
        Stack<String> stack = new Stack<>();
        HashTable<String> childToParent = new HashTable<>();

        stack.push(startValue);
        childToParent.insert(startValue, null);

        while (!stack.isEmpty()) {
            String curr = stack.pop();
            // Found target
            if (curr.equals(targetValue)) {
                printPath(childToParent, curr);
                return;
            }
            if (!visited.search(curr)) {
                visited.put(curr);
                ArrayList<Edge> neighbors = adjlist.get(curr);
                if (neighbors != null) {
                    for (Edge neighbor : neighbors) {
                        if (!visited.search(neighbor.dest)) {
                            childToParent.insert(neighbor.dest, curr);
                            stack.push(neighbor.dest);
                        }
                    }
                }
            }
        }
        System.out.println("No DFS path found");
    }

    public void printPath(HashTable<String> childToParent, String curr) {
        ArrayList<String> temp = new ArrayList<>();
        // Tranfer path from hashTable to arrayList
        while (curr != null) {
            temp.add(curr);
            curr = childToParent.get(curr);
        }
        // Reverse so path prints correctly
        Collections.reverse(temp);
        // Print path
        for (int i = 0; i < temp.size(); i++) {
            System.out.print(temp.get(i));
            if (i + 1 < temp.size()) {
                System.out.print(" --> ");
            }
        }
        System.out.println();
    }
}

class Stack<T> {
    DoubleLinkedList<T> list;

    Stack() {
        list = new DoubleLinkedList<>();
    }

    public void push(T data) {
        list.addLast(data);
    }

    public T peek() {
        return list.getLast();
    }

    public T pop() {
        T temp = list.getLast();
        list.removeLast();
        return temp;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}

class Queue<T> {
    DoubleLinkedList<T> list;

    Queue() {
        list = new DoubleLinkedList<>();
    }

    public void enQueue(T data) {
        list.addLast(data);
    }

    public T deQueue() {
        T temp = list.getFirst();
        list.removeFirst();
        return temp;
    }

    public T peek() {
        return list.getFirst();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}

class DoubleLinkedList<T> {
    public Node<T> head;
    public Node<T> tail;
    public int length;

    DoubleLinkedList() {
        this.head = null;
        this.tail = null;
        length = 0;
    }

    DoubleLinkedList(T input) {
        this.head = new Node<>(input);
        this.tail = head;
        length = 1;
    }

    public void addFirst(T data) {
        if (isEmpty()) {
            head = new Node<T>(data);
            tail = head;
        } else {
            Node<T> temp = new Node(data);
            temp.next = head;
            head.prev = temp;
            head = temp;
        }
        length++;
    }

    public void addLast(T data) {
        if (isEmpty()) {
            tail = new Node<T>(data);
            head = tail;
        } else {
            Node<T> temp = new Node(data);
            temp.prev = tail;
            tail.next = temp;
            tail = temp;
        }
        length++;
    }

    public T getFirst() {
        return isEmpty() ? null : head.data;
    }

    public T getLast() {
        return isEmpty() ? null : tail.data;
    }

    public void removeFirst() {
        if (isEmpty())
            return;
        else if (length == 1) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        length--;
    }

    public void removeLast() {
        if (isEmpty())
            return;
        else if (length == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        length--;
    }

    public void removeAt(int index) {
        if (index < 0 || index >= length) {
            System.err.println("Index out of bounds");
            return;
        }

        if (index == 0) {
            removeFirst();
            return;
        }

        if (index == length - 1) {
            removeLast();
            return;
        }

        Node<T> curr = head;
        for (int i = 1; i <= index; i++) {
            curr = curr.next;
        }

        curr.next.prev = curr.prev;
        curr.prev.next = curr.next;
        curr = null;
        length--;
    }

    // Inserts a new node after the index inputed
    public void add(int index, T data) {
        if (index < 0 || index > length) {
            System.err.println("Index out of bounds");
            return;
        }

        if (index == 0) {
            addFirst(data);
            return;
        }

        if (index == length) {
            addLast(data);
            return;
        }

        Node<T> curr = head;
        for (int i = 1; i + 1 <= index; i++) {
            curr = curr.next;
        }
        Node<T> temp = new Node(data);
        temp.prev = curr;
        temp.next = curr.next;
        temp.next.prev = temp;
        curr.next = temp;
        length++;

    }

    public void printList() {
        if (isEmpty()) {
            System.err.println("list is empty");
            return;
        }
        Node<T> curr = head;
        while (curr != null) {
            System.out.println(curr.data);
            curr = curr.next;
        }
    }

    public T get(int index) {
        if (index >= length || index < 0)
            return null;

        Node<T> curr = head;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr.data;
    }

    public int size() {
        return length;
    }

    public boolean isEmpty() {
        return (length == 0);
    }

    // Node class for double linked list
    class Node<T> {
        public T data;

        public Node<T> next;
        public Node<T> prev;

        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
}

class HashTable<T> {
    private int size;
    private HTEntry<T>[] table;
    private double loadFactor;// size / table.length (not int division)

    HashTable() {
        table = new HTEntry[16];
        size = 0;
    }

    HashTable(int capacity) {
        table = new HTEntry[capacity];
        size = 0;
    }

    public int hash(String key) {
        int hashValue = 0;
        for (int idx = 0; idx < key.length(); idx++) {
            hashValue += key.charAt(idx);
        }
        return Math.abs(hashValue % table.length);
    }

    public int hash2(String key) {
        if (Math.abs(key.hashCode()) % 2 == 0)
            return Math.abs(key.hashCode()) + 1;
        return Math.abs(key.hashCode());
    }

    // expands hash table
    private void rehash() {
        HTEntry<T>[] oldTable = table;
        table = new HTEntry[oldTable.length * 2];
        loadFactor = 0;
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null && !oldTable[i].deleted) {
                insert(oldTable[i].key, oldTable[i].value);
            }
        }
    }

    // add new hash table entry
    public void insert(String key, T value) {
        // transfers to bigger hash table
        if (loadFactor > 0.7) {
            rehash();
        }

        int index1 = hash(key);
        int index2 = hash2(key);
        int startIndex = index1;
        int i = 1;

        while (table[index1] != null) {
            // If key already exist overridde the current data with new data
            if (table[index1].key.equals(key)) {
                table[index1].value = value;
                // Edge case if you remove a node then try to re-add it
                if (table[index1].deleted) {
                    table[index1].deleted = false;
                    size++;
                }
                return;
            }

            // Double hashing
            index1 = (startIndex + i * index2) % table.length;
            i++;

            // all spots checked no space left must increase table size
            if (index1 == startIndex) {
                rehash();
                insert(key, value);
                return;
            }
        }
        // insert into table
        table[index1] = new HTEntry(key, value);
        size++;
        loadFactor = ((double) size / table.length);
    }

    // loop up method
    public T get(String key) {
        int index1 = hash(key);
        int index2 = hash2(key);
        int startIndex = index1;
        int i = 1;

        while (table[index1] != null) {
            // key is found and object is returned
            if (!table[index1].deleted && table[index1].key.equals(key)) {
                return table[index1].value;
            }

            // Double hashing
            index1 = (startIndex + i * index2) % table.length;
            i++;

            // all spots checked key not found
            if (index1 == startIndex) {
                return null;
            }
        }

        return null;
    }

    // remove hash table entry
    public void remove(String key) {
        int index1 = hash(key);
        int index2 = hash2(key);
        int startIndex = index1;
        int i = 1;

        while (table[index1] != null) {
            // key is found and data is set to deleated
            if (!table[index1].deleted && table[index1].key.equals(key)) {
                table[index1].deleted = true;
                size--;
                loadFactor = ((double) size / table.length);
                return;
            }

            // Double hashing
            index1 = (startIndex + i * index2) % table.length;
            i++;

            // all spots checked key not found
            if (index1 == startIndex) {
                break;
            }
        }
    }

    public boolean isEmpty() {
        if (size == 0)
            return true;

        return false;
    }

    // Data storage for the hash table
    class HTEntry<T> {
        String key;
        T value;
        Boolean deleted;

        public HTEntry(String key, T value) {
            this.key = key;
            this.value = value;
            this.deleted = false;
        }
    }
}

class HashSet {
    private int size;
    private HTEntry[] table;
    private double loadFactor;

    HashSet() {
        table = new HTEntry[16];
        size = 0;
    }

    HashSet(int capacity) {
        table = new HTEntry[capacity];
        size = 0;
    }

    public int hash(String key) {
        int hashValue = 0;
        for (int idx = 0; idx < key.length(); idx++) {
            hashValue += key.charAt(idx);
        }
        return Math.abs(hashValue % table.length);
    }

    public int hash2(String key) {
        if (Math.abs(key.hashCode()) % 2 == 0)
            return Math.abs(key.hashCode()) + 1;
        return Math.abs(key.hashCode());
    }

    // expands hash table
    private void rehash() {
        HTEntry[] oldTable = table;
        table = new HTEntry[oldTable.length * 2];
        loadFactor = 0;
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null && !oldTable[i].deleted) {
                put(oldTable[i].key);
            }
        }
    }

    // add new hash table entry
    public void put(String key) {
        // transfers to bigger hash table
        if (loadFactor > 0.7) {
            rehash();
        }

        int index1 = hash(key);
        int index2 = hash2(key);
        int startIndex = index1;
        int i = 1;

        while (table[index1] != null) {
            // Reacctvate old nodes
            if (table[index1].key.equals(key)) {
                if (table[index1].deleted) {
                    table[index1].deleted = false;
                    size++;
                    return;
                } else
                    return;
            }

            // Double hashing
            index1 = (startIndex + i * index2) % table.length;
            i++;

            // all spots checked no space left must increase table size
            if (index1 == startIndex) {
                rehash();
                put(key);
                return;
            }
        }
        // insert into table
        table[index1] = new HTEntry(key);
        size++;
        loadFactor = ((double) size / table.length);
    }

    // remove hash table entry
    public void remove(String key) {
        int index1 = hash(key);
        int index2 = hash2(key);
        int startIndex = index1;
        int i = 1;

        while (table[index1] != null) {
            // key is found and data is set to deleated
            if (!table[index1].deleted && table[index1].key.equals(key)) {
                table[index1].deleted = true;
                size--;
                loadFactor = ((double) size / table.length);
                return;
            }

            // Double hashing
            index1 = (startIndex + i * index2) % table.length;
            i++;

            // all spots checked key not found
            if (index1 == startIndex) {
                break;
            }
        }
    }

    public boolean search(String key) {
        int index1 = hash(key);
        int index2 = hash2(key);
        int startIndex = index1;
        int i = 1;

        while (table[index1] != null) {
            // key is found and data is set to deleated
            if (!table[index1].deleted && table[index1].key.equals(key)) {
                return true;
            }

            // Double hashing
            index1 = (startIndex + i * index2) % table.length;
            i++;

            // all spots checked key not found
            if (index1 == startIndex) {
                break;
            }
        }
        return false;
    }

    public void printAll() {
        for (HTEntry temp : table) {
            if (temp != null && !temp.deleted)
                System.out.println(temp.key);
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    class HTEntry {
        String key;
        Boolean deleted;

        public HTEntry(String key) {
            this.key = key;
            this.deleted = false;
        }
    }
}
