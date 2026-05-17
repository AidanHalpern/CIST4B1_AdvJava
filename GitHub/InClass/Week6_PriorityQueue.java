import java.util.Collections;
import java.util.PriorityQueue;

public class Week6_PriorityQueue {
    public static void main(String[] args) {
        //Collections.reverseOrder() inverts the data (makes a max heap) remove it to get min heap
        PriorityQueue<data> test = new PriorityQueue<>(Collections.reverseOrder());
        test.add(new data(1, "james"));
        test.add(new data(3, "blake"));
        test.add(new data(2, "kate"));
        
        
        
        while(!test.isEmpty()){
            System.out.println(test.poll());
        }
    }

}

class data implements Comparable<data> {
    private int dmg;
    private String name;

    data(int dmg, String name) {
        this.dmg = dmg;
        this.name = name;
    }

    @Override
    public int compareTo(data input) {
        if (this.dmg > input.dmg)
            return 1;
        else if (this.dmg < input.dmg)
            return -1;
        return 0;
    }
    @Override
    public String toString(){
        return name +" " +dmg;
    }
}