import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class Week14kruskal {
    public static void main(String[] args) {

    }

}

class GraphAdjList<T> {
    class Edge implements Comparable<Edge> {
        T source;
        T destination;
        int weight;

        public Edge(T source, T destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        public int compareTo(Edge other) {
            return this.weight - other.weight;
        }
    }

    HashMap<T, ArrayList<Edge>> adjacencyList;
    ArrayList<Edge> edges;

    public GraphAdjList(int vertices) {
        adjacencyList = new HashMap<>();
        edges = new ArrayList<>();

    }

    public void addVertex(T vertex) {
        adjacencyList.put(vertex, new ArrayList<>());
    }

    public void addEdge(T from, T to, int weight) {
        addVertex(from);
        addVertex(to);

        Edge edge = new Edge(from, to, weight);

        adjacencyList.get(from).add(edge);
        adjacencyList.get(to).add(new Edge(to, from, weight));

        edges.add(edge);
    }

    public ArrayList<Edge> kruskal() {
        // sort the edges list
        Collections.sort(edges);

        // create our set of vertexes
        HashMap<T, T> roots = new HashMap<>();
        // have every vertex in its own set at first
        for (T vertex : adjacencyList.keySet()) {
            roots.put(vertex, vertex);
        }

        ArrayList<Edge> mst = new ArrayList<>();
        int totalWeight = 0;

        // iterate through the sorted edges
        // ---see if the edge would cause a cycle (if verte)
        // ---if not, union the two sets
        for (Edge e : edges) {
            T sourceRoot = find(roots, e.source);
            T destRoot = find(roots, e.destination);

            if (!sourceRoot.equals(destRoot)) {
                mst.add(e);
                totalWeight += e.weight;
                roots.put(destRoot, sourceRoot);
            }
        }
        return mst;
    }

    private T find(HashMap<T, T> parent, T vertex) {
        // can our sets be joined together
        if (!parent.get(vertex).equals(vertex)) {
            parent.put(vertex, find(parent, parent.get(vertex)));
        }
        return parent.get(vertex);
    }
}
