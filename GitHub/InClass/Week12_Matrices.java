import java.util.HashMap;
import java.util.HashSet;

public class Week12_Matrices {
    public static void main(String[] args) {

    }

}

class Graph {
    HashMap<Integer, HashSet<Integer>> storage;
    int vertexCount = 25;

    Graph() {
        storage = new HashMap<>();
        for (int i = 0; i < vertexCount; i++) {
            storage.put(i, new HashSet<>());
        }
    }

    public void addEdge(int from, int to) {
        storage.get(from).add(to);
    }
}
