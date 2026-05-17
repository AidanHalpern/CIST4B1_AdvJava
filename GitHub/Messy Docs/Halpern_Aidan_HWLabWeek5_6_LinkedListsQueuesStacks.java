/*
 * Author: Aidan Halpern
 * Date: 2026-3-8
 * Course: CIST004B1 - Java Data Structures
 * Homework: #Lab 5&6
 * Description: Restaurant operations simulation
 * Ai use: https://gemini.google.com/share/244d03a6f446
 */

import java.util.ArrayList;

public class Halpern_Aidan_HWLabWeek5_6_LinkedListsQueuesStacks {
    protected static Stack<String> logOfEachHour = new Stack<>();
    protected static Queue lineAndOrders = new Queue();
    protected static DoubleLinkedList<ArrayList<Order>> activeKitchen = new DoubleLinkedList<>();
    protected static Stack<String> prepStationOne = new Stack<>();
    protected static Stack<String> prepStationTwo = new Stack<>();
    protected static Stack<String> prepStationThree = new Stack<>();
    protected static ArrayList<Order> serveNextHour = new ArrayList<>();
    protected static ArrayList<Order> completedOrderStroage = new ArrayList<>();
    protected static double totalRenue = 0;

    public static void main(String[] args) {
        ArrayList<Menu> menu = new ArrayList<>();
        menu.add(new Menu(1, "Cheese Pizza", 10.0));
        menu.add(new Menu(2, "Pepperoni Pizza", 5.0));
        menu.add(new Menu(3, "Mushroom Pizza", 15.01));

        Menu.displayMenuTitles();
        for (int i = 0; i < menu.size(); i++) {
            menu.get(i).displayMenuItem();
        }
        System.err.println("");

        Order testName = new DineInOrder("Ted", 1, 10.0);
        lineAndOrders.enQueue(testName);
        lineAndOrders.enQueue(new DineInOrder("Ned", 2, 5.0));
        lineAndOrders.enQueue(new TakeoutOrder("Ed", 3, 15.01));
        lineAndOrders.enQueue(new TakeoutOrder("James", 3, 15.01));

        int hours = 5;
        for (int currHour = 0; currHour < hours; currHour++) {
            serveOrderComplete();
            prepOrderToServe();
            kitchenToPrepStation();
            lineToKitchen();

            logOfEachHour.push("Log of hour " + (currHour + 1));
            while (!logOfEachHour.isEmpty()) {
                System.out.println(logOfEachHour.pop());
            }
        }

        // Print Summary
        System.out.println("End of shift summary");
        System.out.println(
                "Total customers processed: " + (DineInOrder.getdOrderCount() + TakeoutOrder.getTOrderCount()));
        System.out.println("Total completed orders: " + completedOrderStroage.size());
        System.out.println("Dine in order count: " + DineInOrder.getdOrderCount());
        System.out.println("Takeout order count: " + TakeoutOrder.getTOrderCount());
        System.out.printf("%s $%.2f", "Total revenue:", totalRenue);
        // TODO:
    }

    public static void prepSteps(Stack prepStation, Order order) {
        prepStation.push("Preping " + order.getName() + "'s order at prep station");
        prepStation.push("Toss dough");
        prepStation.push("Add sauce");

        int currFoodID = order.getID();
        switch (currFoodID) {
            // Chese pizza
            case 1:
                prepStation.push("Add cheese");
                break;
            // Pepperoni piiza
            case 2:
                prepStation.push("Add pepperoni");
                break;
            // Mushroom pizza
            case 3:
                prepStation.push("Add mushrooms");
                break;

        }

        prepStation.push("Bake pizza");
        prepStation.push(order.servingStyle());
        prepStation.push("");

    }

    // move waiting line to active kitchen
    public static void lineToKitchen() {
        activeKitchen.addFirst(new ArrayList());
        logOfEachHour.push("");
        if (lineAndOrders.isEmpty()) {
            logOfEachHour.push("Waiting line is empty!");
            return;
        }
        int numOfOrders;
        for (numOfOrders = 0; numOfOrders < 3; numOfOrders++) {
            if (lineAndOrders.isEmpty()) {
                break;
            }
            if (lineAndOrders.peek() instanceof DineInOrder) {
                DineInOrder.addOneDOrderCount();
            } else if (lineAndOrders.peek() instanceof TakeoutOrder) {
                TakeoutOrder.addOneTOrderCount();
            }
            activeKitchen.getFirst().add(lineAndOrders.deQueue());

        }
        logOfEachHour.push("Emptying the line sent " + numOfOrders + " order(s) to active kitchen");
    }

    // takes the arrayList of orders and add thier prep steps to the prepStation
    // stacks
    public static void kitchenToPrepStation() {
        logOfEachHour.push("");
        if (activeKitchen.isEmpty()) {
            logOfEachHour.push("No orders to send to prep for this hour");
        } else if (activeKitchen.getLast().isEmpty()) {
            logOfEachHour.push("No orders to send to prep for this hour");
        } else {
            for (int sendToPrepNum = activeKitchen.getLast().size(); sendToPrepNum > 0; sendToPrepNum--) {
                logOfEachHour.push("Sent " + activeKitchen.getLast().get(sendToPrepNum - 1).getName()
                        + "'s order to prep station ");
                Order currFoodOrder = activeKitchen.getLast().get(sendToPrepNum - 1);

                switch (sendToPrepNum) {
                    case 1:
                        prepSteps(prepStationOne, currFoodOrder);
                        break;

                    case 2:
                        prepSteps(prepStationTwo, currFoodOrder);
                        break;

                    case 3:
                        prepSteps(prepStationThree, currFoodOrder);
                        break;
                }
            }
        }
    }

    // Prep stacks are poped and order is moved to be served next hour
    public static void prepOrderToServe() {
        if (prepStationOne.isEmpty() && prepStationTwo.isEmpty() && prepStationThree.isEmpty()) {
            logOfEachHour.push("Nothing to prep from last hour\n");
        } else {
            serveNextHour = activeKitchen.getLast();
            activeKitchen.removeLast();
            while (!prepStationOne.isEmpty()) {
                logOfEachHour.push(prepStationOne.pop());
            }

            while (!prepStationTwo.isEmpty()) {
                logOfEachHour.push(prepStationTwo.pop());
            }

            while (!prepStationThree.isEmpty()) {
                logOfEachHour.push(prepStationThree.pop());
            }

        }
    }

    // Serve people, collect payment and stats
    public static void serveOrderComplete() {
        logOfEachHour.push("");
        if (serveNextHour.size() == 0) {
            logOfEachHour.push("No one waiting to be served");
        } else {
            for (int currOrderServing = 0; currOrderServing < serveNextHour.size(); currOrderServing++) {
                logOfEachHour.push(serveNextHour.get(currOrderServing).toString());
                completedOrderStroage.add(serveNextHour.get(currOrderServing));
                totalRenue += serveNextHour.get(currOrderServing).getPrice();
            }
            serveNextHour = new ArrayList();
        }
    }
}

class Order {
    protected String name;
    protected int iD;
    protected double price;

    Order(String name, int foodID, double price) {
        this.name = name;
        this.iD = foodID;
        this.price = price;
    }

    public int getID() {
        return iD;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String servingStyle() {
        return ("Just the pizza");
    }

    @Override
    public String toString() {
        return "Name: " + name + " Order iD: " + iD + " Price: $";
    }

}

class DineInOrder extends Order {
    private static int dOrderCount;

    DineInOrder(String name, int foodID, double price) {
        super(name, foodID, price);
    }

    @Override
    public String servingStyle() {
        return ("Plating pizza for " + name);
    }

    public static void addOneDOrderCount() {
        dOrderCount++;
    }

    public static int getdOrderCount() {
        return dOrderCount;
    }

    @Override
    public String toString() {
        return "Served dine in order for " + name;
    }
}

class TakeoutOrder extends Order {
    private static int tOrderCount;

    TakeoutOrder(String name, int foodID, double price) {
        super(name, foodID, price);
    }

    @Override
    public String servingStyle() {
        return ("Boxing pizza for " + name);
    }

    public static void addOneTOrderCount() {
        tOrderCount++;
    }

    public static int getTOrderCount() {
        return tOrderCount;
    }

    @Override
    public String toString() {
        return "Served to go order for " + name;
    }
}

class Menu {
    int foodID;
    String foodName;
    double price;

    Menu(int foodID, String foodName, double price) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.price = price;
    }

    public static void displayMenuTitles() {
        System.out.printf("%s\n%-14s %-17s %s\n", "Welcome to a pizza restaurant", "Order Number", "Food Name",
                "Price");
    }

    public void displayMenuItem() {
        System.out.printf("%-14d %-17s %.2f\n", foodID, foodName, price);
    }
}

// Stack
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

// Queue
/*
 * head points to the next open index
 * tail is the last filled index
 * count is total indexs filled
 */
class Queue {
    int head;
    int tail;
    int count;
    Order[] arr;

    Queue() {
        arr = new Order[2];
    }

    public void enQueue(Order order) {
        /*
         * Check to see if arr has any room left
         * if it does'nt it unziped old arr into new arr
         */
        if (count == arr.length) {
            Order[] temp = new Order[arr.length * 2];

            // need modulo here because the old array might be offset / not start at index 0
            for (int i = 0; i < count; i++) {
                temp[i] = arr[(tail + i) % arr.length];
            }

            tail = 0;
            head = count;
            arr = temp;
        }
        // better wrapper around logic using modulo & always adds data to the array
        arr[head] = order;
        head = (head + 1) % arr.length;
        count++;

    }

    public Order deQueue() {
        if (isEmpty()) {
            // System.err.println("Queue is empty");
            return null;
        }
        int temp = tail;
        // better wrapper around logic using modulo
        tail = (tail + 1) % arr.length;
        count--;

        return arr[temp];

    }

    public Order peek() {
        return isEmpty() ? null : arr[tail];
    }

    public boolean isEmpty() {
        return (count == 0);
    }
}

// Double Linked List
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

        // curr is one before the added index
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

    public int size() {
        return length;
    }

    public boolean isEmpty() {
        return (length == 0);
    }
}

// Node
class Node<T> {
    // data
    public T data;

    // links
    public Node<T> next;
    public Node<T> prev;

    Node(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}
