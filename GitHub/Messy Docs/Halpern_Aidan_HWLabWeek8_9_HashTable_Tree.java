
/*
 * Author: Aidan Halpern
 * Date: 2026-3-16
 * Course: CIST004B1 - Java Data Structures
 * Homework: #Lab7&8
 * Description: management system for a zoo
 * https://gemini.google.com/share/76a39189a8b8
 */
import java.util.ArrayList;
import java.util.Random;

public class Halpern_Aidan_HWLabWeek8_9_HashTable_Tree {
    public static void main(String[] args) {
        ArrayList<Animal> input = new ArrayList<>();
        input.add(new Animal("Bob", "Bear", 5));
        input.add(new Animal("Ed", "Turkey", 10));
        input.add(new Animal("Ted", "Bee", 1));
        input.add(new Animal("Sam", "Fox", 10));
        input.add(new Animal("Jill", "Owl", 2));
        input.add(new Animal("Jake", "Cow", 3));
        input.add(new Animal("Ned", "Seal", 10));
        input.add(new Animal("Blake", "Hawk", 10));
        input.add(new Animal("Phill", "Ant", 9));
        input.add(new Animal("James", "Gold fish", 7));

        HashTable<Animal> allLivingAnimals = new HashTable();
        BinaryTree<String> allWoundedAnimals = new BinaryTree();
        // put animals into hash table / binary tree
        for (Animal animal : input) {
            allLivingAnimals.put(animal.getName(), animal);
            if (animal.getCareLevel() > 0) {
                allWoundedAnimals.insert(animal.getCareLevel(), animal.getName());
            }
        }

        int day = 1;
        while (!allWoundedAnimals.isEmpty()) {
            System.out.println("Day " + day++);
            // heal animals
            ArrayList<String> animalUpdates = allWoundedAnimals.heal();

            // update healed animals status in the hash tree
            for (String healedAnimal : animalUpdates) {
                if (healedAnimal == null)
                    continue;

                allLivingAnimals.get(healedAnimal).setCareLevel(0);
                System.out.println((allLivingAnimals.get(healedAnimal)).healMsg());
            }

            // Simulates animals getting worse / dying
            animalUpdates = allWoundedAnimals.lackOfCare();
            for (String nameOfDmgAnimal : animalUpdates) {
                // Animal died remove from hash tree
                if (allLivingAnimals.get(nameOfDmgAnimal).getCareLevel() == 10) {
                    System.out.println(allLivingAnimals.get(nameOfDmgAnimal).deathMsg());
                    allLivingAnimals.remove(nameOfDmgAnimal);
                    continue;
                }
                // Animal got worse update status in hash tree
                int updatedDMg = (allLivingAnimals.get(nameOfDmgAnimal)).getCareLevel();
                allLivingAnimals.get(nameOfDmgAnimal).setCareLevel(updatedDMg);
                System.out.println((allLivingAnimals.get(nameOfDmgAnimal)).careInc());
            }
            System.out.println();
        }
        // TODO:
    }
}

class Animal {
    private String name;
    private String species;
    private int careLevel;

    Animal(String name, String species, int careLevel) {
        this.name = name;
        this.species = species;
        this.careLevel = careLevel;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public int getCareLevel() {
        return careLevel;
    }

    public void setCareLevel(int careLevel) {
        this.careLevel = careLevel;
    }

    public String deathMsg() {
        return (name + " a " + species + " has died.");
    }

    public String healMsg() {
        return (name + " a " + species + " was healed.");
    }

    public String careInc() {
        return (name + "'s care level increased due to a lack of care; now at " + (careLevel + 1));
    }

    @Override
    public String toString() {
        return (name + " is a " + species + " at care level " + careLevel + ".");
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
                put(oldTable[i].key, oldTable[i].value);
            }
        }
    }

    // add new hash table entry
    public void put(String key, T value) {
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
                put(key, value);
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

class BinaryTree<T> {
    Node<T> root;
    int countNode;

    BinaryTree() {
        root = null;
        countNode = 0;
    }

    BinaryTree(int key, T data) {
        root = new Node(key, data);
        countNode = 1;
    }

    public void insert(int key, T data) {
        // Check if root exists
        if (this.root == null) {
            this.root = new Node(key, data);
            countNode++;
            return;
        }

        Node<T> curr = root;
        while (true) {
            // Node exists can add data directly to it
            // Can change code so each value has it own Node by removing this if statment
            // and adding <= to one of the other if statments
            // Can over write exisitng Node data if it shares the same value by setting the
            // arrayList to a new arrayList before adding

            if (key == curr.value) {
                curr.data.add(data);
                break;
            }

            // Greater then
            // Checks if right node exists if it does it traverses it if not it makes a new
            // node
            if (key > curr.value) {
                if (curr.right == null) {
                    curr.right = new Node(key, data);
                    countNode++;
                    break;
                }
                curr = curr.right;
            }

            // less then
            // Checks if left node exists if it does it traverses it if not it makes a new
            // node
            if (key < curr.value) {
                if (curr.left == null) {
                    curr.left = new Node(key, data);
                    countNode++;
                    break;
                }
                curr = curr.left;
            }
        }
    }

    public boolean search(int key) {
        return searchRecursive(key, root);
    }

    private boolean searchRecursive(int key, Node<T> curr) {
        if (curr == null)
            return false;

        if (curr.value == key)
            return true;

        if (key > curr.value) {
            return searchRecursive(key, curr.right);
        }
        if (key < curr.value)
            return searchRecursive(key, curr.left);

        return false;
    }

    public Node<T> remove(int key) {
        root = removeRecursive(key, root);
        return root;
    }

    private Node<T> removeRecursive(int key, Node<T> current) {
        if (current == null) {
            return null;
        }
        if (key == current.value) {
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
            Node<T> inOrderSuccessorNode = findMinValue(current.right);
            // 2) replace current with smallest value / data
            current.value = inOrderSuccessorNode.value;
            current.data = inOrderSuccessorNode.data;
            // 3) recursively delete the old smallest value
            // need to re-add a count becasue if there 2 childern the method is called twice
            // and cound is subtacted twice
            current.right = removeRecursive(inOrderSuccessorNode.value, current.right);
        } else if (key < current.value)
            current.left = removeRecursive(key, current.left);

        else
            current.right = removeRecursive(key, current.right);

        return current;
    }

    private Node<T> findMinValue(Node<T> subRoot) {
        while (subRoot.left != null) {
            subRoot = subRoot.left;
        }
        return subRoot;
    }

    public Node<T> findNode(int key) {
        return findNode(key, root);
    }

    private Node<T> findNode(int key, Node<T> curr) {
        if (curr == null)
            return null;

        if (curr.value == key)
            return curr;

        if (key > curr.value) {
            return findNode(key, curr.right);
        }
        if (key < curr.value)
            return findNode(key, curr.left);

        return null;
    }

    // Finds Node with key then returns and deleates the first vaule in the
    // arrayList- aka a QUEUE also removes node if it is emptyed
    public T pollFirst(int key) {
        Node<T> tempNode = findNode(key);
        // Check if node exists
        if (tempNode == null) {
            return null;
        }

        // check if arrayList is empty and remove it if its empty
        if (tempNode.data.isEmpty()) {
            remove(key);
            return null;
        }

        T tempObject = tempNode.data.get(0);
        tempNode.data.remove(0);

        if (tempNode.data.isEmpty())
            remove(key);

        return tempObject;
    }

    // Finds Node with key then returns and deleates the last vaule in the
    // arrayList- aka a STACK also removes node if it is emptyed
    public T pollLast(int key) {
        Node<T> tempNode = findNode(key);
        // Check if node exists
        if (tempNode == null) {
            return null;
        }

        // check if arrayList is empty and remove it if its empty
        if (tempNode.data.isEmpty()) {
            remove(key);
            return null;
        }

        T tempObject = tempNode.data.get(tempNode.data.size() - 1);
        tempNode.data.remove(tempNode.data.size() - 1);

        if (tempNode.data.isEmpty())
            remove(key);

        return tempObject;
    }

    // Clean up method
    public void removeAllEmptyNodes() {
        ArrayList<Integer> keysToRemoveLatter = new ArrayList<>();
        removeAllEmptyNodesRec(root, keysToRemoveLatter);
        for (Integer remove : keysToRemoveLatter)
            remove((int) remove);
    }

    private ArrayList<Integer> removeAllEmptyNodesRec(Node curr, ArrayList<Integer> keysToRemoveLatter) {
        if (curr != null) {
            removeAllEmptyNodesRec(curr.right, keysToRemoveLatter);
            if (curr.data.isEmpty())
                keysToRemoveLatter.add(curr.value);
            removeAllEmptyNodesRec(curr.left, keysToRemoveLatter);
        }
        return keysToRemoveLatter;
    }

    // Past this is custom code for lab 8/9 REMEMBER to take the Node class and
    // isEmpty() if I need to use this on midterm
    public ArrayList<T> heal() {
        ArrayList<T> updatesForHashTable = new ArrayList<>();
        T animalToBeHealed = null;
        // Intensive care
        animalToBeHealed = null;
        animalToBeHealed = pollFirst(10);
        if (animalToBeHealed == null)
            animalToBeHealed = pollFirst(9);
        if (animalToBeHealed == null)
            animalToBeHealed = pollFirst(8);
        // if there's an animal to be healed add to arrayList
        if (animalToBeHealed != null)
            updatesForHashTable.add(animalToBeHealed);

        // Advanced care
        animalToBeHealed = null;
        animalToBeHealed = pollFirst(7);
        if (animalToBeHealed == null)
            animalToBeHealed = pollFirst(6);
        if (animalToBeHealed == null)
            animalToBeHealed = pollFirst(5);
        if (animalToBeHealed == null)
            animalToBeHealed = pollFirst(4);
        // if there's an animal to be healed add to arrayList
        if (animalToBeHealed != null)
            updatesForHashTable.add(animalToBeHealed);

        // Basic care
        animalToBeHealed = null;
        animalToBeHealed = pollFirst(3);
        if (animalToBeHealed == null)
            animalToBeHealed = pollFirst(2);
        if (animalToBeHealed == null)
            animalToBeHealed = pollFirst(1);
        // if there's an animal to be healed add to arrayList
        if (animalToBeHealed != null)
            updatesForHashTable.add(animalToBeHealed);

        return updatesForHashTable;
    }

    /*
     * visit all spots starting from worst aka node.right
     * remove and re-add aninmal if they get worse
     * remove node if its empty
     * 10% chance for an animal to be hurt
     */

    public ArrayList<T> lackOfCare() {
        ArrayList<T> updatesForHashTable = new ArrayList<>();
        lackOfCareRecursive(root, updatesForHashTable);
        removeAllEmptyNodes();
        return updatesForHashTable;
    }

    // visits from largest value to smallest value to prevent double DMG
    private void lackOfCareRecursive(Node<T> curr, ArrayList<T> updatesForHashTable) {
        Random rand = new Random();
        if (curr != null) {
            lackOfCareRecursive(curr.right, updatesForHashTable);
            for (int i = curr.data.size() - 1; i >= 0; i--) {
                if (0 == rand.nextInt(10)) {
                    updatesForHashTable.add(curr.data.get(i));
                    if (curr.value == 10) {
                        curr.data.remove(i);
                    } else {
                        insert(curr.value + 1, curr.data.get(i));
                        curr.data.remove(i);
                    }
                }
            }
            lackOfCareRecursive(curr.left, updatesForHashTable);
        }
    }

    public int size() {
        return countNode;
    }

    public boolean isEmpty() {
        return countNode == 0;
    }

    // Node class for binary tree
    class Node<T> {
        int value;
        ArrayList<T> data;
        // ^^^ this is data storage
        Node<T> right;
        Node<T> left;

        public Node(int value, T data) {
            this.value = value;
            this.data = new ArrayList<>();
            this.data.add(data);
        }
    }
}