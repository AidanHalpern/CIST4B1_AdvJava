import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/* //Weighted graph arrayList<[T, Integer]>
public class GraphAdjList<T> {
    // data structures to represent the adjacency list
    int size;
    HashMap<T, ArrayList<T>> adjlist;

    // constructor with whatever you need
    public GraphAdjList() {
        size = 0;
        adjlist = new HashMap<>();
    }

    // add edge(T source, T destination)
    public void addEdge(T source, T dest, Integer weight) {
        if (adjlist.get(source) == null) {
            ArrayList<T> tempList = new ArrayList<>();
            tempList.add(new edgeInfo(weight, dest));
            adjlist.put(source, tempList);
        } else {
            adjlist.get(source).add(new edgeInfo(weight, dest));
        }

        // Undirected mode
        //
        //  if (adjlist.get(dest) == null) {
        //  ArrayList<T> temptList2 = new ArrayList<>();
        // temptList2.add(source);
        //  adjlist.put(dest, temptList2);
        //  } else {
         // adjlist.get(dest).add(source);
          //}
         

        //MUST KEEP THIS SIZE HERE
        size++;
    }

    class edgeInfo {
        Integer weight;
        T dest;

        edgeInfo(Integer weight, T dest) {
            this.weight = weight;
            this.dest = dest;
        }
    }*/

//Weighted graph arrayList<[T, Integer]>
public class GraphAdjList<T> {
    private class Edge{
        T dest;
        int weight;
        public Edge(T dest, int weight){
            this.dest = dest;
            this.weight = weight;
        }
    }
    // data structures to represent the adjacency list
    int size;
    HashMap<T, ArrayList<Edge>> adjlist;

    // constructor with whatever you need
    public GraphAdjList() {

        size = 0;

        adjlist = new HashMap<>();

    }

    // add edge(T source, T destination)
    public void addEdge(T source, T dest, int weight) {

        if (adjlist.get(source) == null) {

            ArrayList<T> tempList = new ArrayList<>();

            tempList.add(new Edge(dest, weight));

            adjlist.put(source, tempList);

        } else {

            adjlist.get(source).add(new Edge(dest, weight));

        }

        // Undirected mode
        /*
         * 
         * if (adjlist.get(dest) == null) {
         * 
         * ArrayList<T> temptList2 = new ArrayList<>();
         * 
         * temptList2.add(new Edge(source, weight));
         * 
         * adjlist.put(dest, temptList2);
         * 
         * } else {
         * 
         * adjlist.get(dest).add(new Edge(source, weight));
         * 
         * }
         * 
         */

        /* MUST KEEP THIS SIZE HERE */
        size++;

    }

    public void bfs(T startValue) {
        HashSet<T> visited = new HashSet<>();
        ArrayList<T> queue = new ArrayList<>();

        visited.add(startValue);
        queue.add(startValue);

        while (!queue.isEmpty()) {
            T curr = queue.remove(0);
            System.out.println(curr + " ");

            for (T neighbor : adjlist.get(curr)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
    }

    public void dfs(T startValue) {
        HashSet<T> visited = new HashSet();
        Stack<T> stack = new Stack<>();

        stack.push(startValue);

        while (!stack.isEmpty()) {
            T curr = stack.pop();
            if (!visited.contains(curr)) {
                visited.add(curr);
                System.out.println(curr + " ");

                for (T neighbor : adjlist.get(curr)) {
                    if (!visited.contains(neighbor)) {
                        stack.push(neighbor);
                    }
                }
            }
        }
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
