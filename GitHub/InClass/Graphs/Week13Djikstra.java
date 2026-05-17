import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

public class Week13Djikstra {
    public static void main(String[] args) {

    }

    public void dijkstra(T startValue) {
        HashSet<T> visited = new HashSet();
        PriorityQueue weight = new PriorityQueue<>();
        Queue<T> next = new Queue<>();
        next.add(startValue);
        while (!next.isEmpty()) {
            T curr = next.pop();
            for (T neighbor : adjlist.get(curr)) {
                if (!visited.contains(neighbor)) {
                    visited.add(curr);
                    weight.add(neighbor, (neighbor.weight + curr.weight));
                }
            }

        }
    }

}
