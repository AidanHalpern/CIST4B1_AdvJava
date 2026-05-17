import java.util.ArrayList;
import java.util.HashMap;

public class GraphAdjMatrix<T> {
    private ArrayList<ArrayList<Integer>> matrix;
    private HashMap<T, Integer> verticies;

    public GraphAdjMatrix() {
        verticies = new HashMap<>();
        matrix = new ArrayList<>();
    }

    public void addVertex(T vertex){
        verticies.put(vertex, verticies.size());

        //Update old vertex's possble connections
        for(ArrayList<Integer> row : matrix){
            row.add(0);
        }
        //Add new vertex and its possble connections
        ArrayList<Integer> newRow =  new ArrayList<>();
        for(int i = 0; i < verticies.size(); i++){
            newRow.add(0);
        }
        matrix.add(newRow);
    }

    public void addEdge(T source, T dest, int weight){
        int indSrc = verticies.get(source);
        int indDest = verticies.get(dest);

        if(indSrc == -1 || indDest == -1){
            System.err.println("Error, source or destination does not exist!");
            return;
        }

        matrix.get(indSrc).set(indDest, weight);
        matrix.get(indDest).set(indSrc, weight);
    }

}
