public class Week5_StackFromLinkedList {
    public static void main(String[] args) {
        Stack test = new Stack("Please Work");
        System.out.println(test.pop());
    }

}

class LinkedList {
    private LinkedList next;
    private String a;
    private static int counter;

    public LinkedList(String a) {
        this.a = a;
        this.next = null;
        counter++;
    }

    public int getCounter() {
        return counter;
    }

    public LinkedList removeHead() {
        LinkedList temp = next;
        next = null;
        counter--;
        return temp;
    }

    public LinkedList getNext() {
        return next;
    }

    public void add(String a) {
        while (next != null) {
            next = getNext();
        }
        next = LinkedList(a);
        counter++;
    }

    public String getA() {
        return a;
    }

    public String popTail() {
        if (counter == 1) {
            return a;
        } else {
            while (next.getNext() != null) {
                next = next.getNext();
            }
            String temp = a;
            return temp;
        }
    }

    public String peakTail() {
        while (next != null) {
            next = getNext();
        }
        return next.getA();
    }
}

class Stack {
    LinkedList list;

    public Stack(String a) {
        list = new LinkedList(a);
    }

    public push(String a){
        list.add(a);
    }

    public String pop() {
        return list.popTail();
    }

}
