/*
 * Author: Aidan Halpern
 * Date: 2026-4-14
 * Course: CIST004B1 - Java Data Structures
 * Homework: #Lab Week 11
 * Description: Dictionary using AVL tree and hashset
 * https://gemini.google.com/share/a581ce214292
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Halpern_Aidan_HWLabWeek11 {
    public static void main(String[] args) {
        AVLTree dictionary = new AVLTree();
        HashSet misspelledWords = new HashSet();
        // read in dictionary
        try (BufferedReader br = new BufferedReader(new FileReader("dictionary.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // push line to AVL Tree
                dictionary.insert(line.toLowerCase());
            }
        } catch (IOException e) {
            System.out.println("Dictionary was not read in");
        }

        // read in document
        try (BufferedReader br = new BufferedReader(new FileReader("document.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String col[] = line.split("\\s+");
                for (int i = 0; i < col.length; i++) {
                    col[i] = col[i].replaceAll("[^a-zA-Z]", "");
                    col[i] = col[i].toLowerCase();
                    if (!dictionary.search(col[i])) {
                        misspelledWords.put(col[i]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Document was not read in");
        }
        System.out.println("Dictionary: ");
        dictionary.printAll();

        System.out.println();

        System.out.print("Misspelled words: ");
        misspelledWords.printAll();
        // TODO:
    }
}

class AVLTree {
    Node root;
    int countNode;

    AVLTree() {
        root = null;
        countNode = 0;
    }

    AVLTree(String data) {
        root = new Node(data);
        countNode = 1;
    }

    public Node insert(String data) {
        root = insertRecursive(data, root);
        return root;
    }

    private Node insertRecursive(String data, Node current) {
        if (current == null) {
            countNode++;
            return new Node(data);
        }
        if (data.compareTo(current.data) == 0) {
            return current;
        } else if (data.compareTo(current.data) < 0)
            current.left = insertRecursive(data, current.left);

        else
            current.right = insertRecursive(data, current.right);

        return rebalance(current);
    }

    public boolean search(String key) {
        return searchRecursive(key, root);
    }

    private boolean searchRecursive(String key, Node curr) {
        if (curr == null) {
            return false;
        }

        if (key.compareTo(curr.data) == 0)
            return true;

        // less then
        if (key.compareTo(curr.data) < 0)
            return searchRecursive(key, curr.left);

        // greater then
        if (key.compareTo(curr.data) > 0)
            return searchRecursive(key, curr.right);

        return false;
    }

    public Node remove(String key) {
        root = removeRecursive(key, root);
        return root;
    }

    private Node removeRecursive(String key, Node current) {
        if (current == null) {
            return null;
        }
        if (key.compareTo(current.data) == 0) {
            // value found meaning a node WILL be removed from 0 or 1 child
            // case 1: no children
            if (current.left == null && current.right == null) {
                countNode--;
                return null;
            }
            // case 2: 1 child
            if (current.left == null) {
                countNode--;
                return current.right;
            }
            if (current.right == null) {
                countNode--;
                return current.left;
            }

            // case 3: 2 children - doesnt remove a node just overwrites it
            // 1) find smallest value in right subtree
            Node inOrderSuccessorNode = findMinValue(current.right);
            // 2) replace current with smallest value / data
            current.data = inOrderSuccessorNode.data;
            // 3) recursively delete the old smallest value
            // need to re-add a count becasue if there 2 childern the method is called twice
            // and cound is subtacted twice
            current.right = removeRecursive(inOrderSuccessorNode.data, current.right);
        } else if (key.compareTo(current.data) < 0)
            current.left = removeRecursive(key, current.left);

        else
            current.right = removeRecursive(key, current.right);

        return rebalance(current);
    }

    private Node findMinValue(Node subRoot) {
        while (subRoot.left != null) {
            subRoot = subRoot.left;
        }
        return subRoot;
    }

    private Node rebalance(Node subRoot) {
        subRoot.height = Math.max(height(subRoot.left), height(subRoot.right)) + 1;
        // RH
        if (getBalance(subRoot) > 1) {
            // kink
            if (getBalance(subRoot.right) < 0) {
                return rightLeftRotate(subRoot);
            }
            // straight
            else {
                return leftRotate(subRoot);
            }
        }

        // LH
        if (getBalance(subRoot) < -1) {
            // kink
            if (getBalance(subRoot.left) > 0) {
                return leftRightRotate(subRoot);
            }
            // straight
            else {
                return rightRotate(subRoot);
            }
        }
        return subRoot;
    }

    private int height(Node curr) {
        if (curr == null)
            return 0;
        return curr.height;
    }

    private int getBalance(Node curr) {
        if (curr == null)
            return 0;
        return height(curr.right) - height(curr.left);
    }

    private Node rightRotate(Node oldRoot) {
        Node newRoot = oldRoot.left;
        oldRoot.left = newRoot.right;
        newRoot.right = oldRoot;

        oldRoot.height = Math.max(height(oldRoot.left), height(oldRoot.right)) + 1;
        newRoot.height = Math.max(height(newRoot.left), height(newRoot.right)) + 1;
        return newRoot;
    }

    private Node leftRotate(Node oldRoot) {
        Node newRoot = oldRoot.right;
        oldRoot.right = newRoot.left;
        newRoot.left = oldRoot;

        oldRoot.height = Math.max(height(oldRoot.left), height(oldRoot.right)) + 1;
        newRoot.height = Math.max(height(newRoot.left), height(newRoot.right)) + 1;
        return newRoot;
    }

    private Node leftRightRotate(Node oldRoot) {
        oldRoot.left = leftRotate(oldRoot.left);
        return rightRotate(oldRoot);
    }

    private Node rightLeftRotate(Node oldRoot) {
        oldRoot.right = rightRotate(oldRoot.right);
        return leftRotate(oldRoot);
    }

    public void printAll() {
        inOrderPrint(root);
    }

    private void inOrderPrint(Node curr) {
        if (curr != null) {
            inOrderPrint(curr.left);
            System.out.printf("Word: %-7s  Balance: %d\n", curr.data, getBalance(curr));
            // System.out.println("Word: " + curr.data + " Balance: " + getBalance(curr));
            inOrderPrint(curr.right);
        }
    }

    class Node {
        String data;
        int height;
        // Links
        Node right;
        Node left;

        public Node(String data) {
            this.data = data;
            this.height = 1;
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