import java.util.LinkedList;
import java.util.Hashtable;
import java.util.ArrayList;

public class Week12_Gragh {
    public static void main(String[] args) {

    }

}

class GraphAdjList{
HashTable<Integer, ArrayList<Integer>> storage;
    GraphAdjList(){
        storage = new HashTable();
    }

    public void add(int source, int destination){
        if(storage.contains(source)){
        storage.get(source).add(destination);
        }
    }
}
class NodeG {
LinkedList test;
int value;
    NodeG(){
        test = new LinkedList<>();
    }
}
