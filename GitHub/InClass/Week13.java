import java.util.HashSet;
import java.util.Queue;
import java.util.Stack;

public class Week13 {
    public static void main(String[] args) {
    }

    public void bFS(Node startNode) {
        Queue<Node> nextBFSNode = new Queue<>();
        HashSet<Node> searched = new HashSet<>();
        nextBFSNode.add(startNode);
        searched.add(startNode);
        while (!nextBFSNode.isEmpty()) {
            Node[] links = nextBFSNode.peek().links;
            if (links != null) {
                for (Node temp : links) {
                    if (temp != null) {
                        if (!searched.contains(temp)) {
                            nextBFSNode.add(temp);
                            searched.add(temp);
                        }
                    }
                }
            }
            System.out.println(nextBFSNode.poll());
        }
    }

    public void dFS(Node startNode) {
        Stack<Node> stack = new Stack<>();
        HashSet searched = new HashSet<>();

        stack.push(startNode);

        while (!stack.isEmpty()) {
            Node curr = stack.pop();
            if (!searched.contains(curr)) {
                searched.add(curr);
                System.out.println(curr);
            }

            for (Node neighbor : adjlist.get(curr)) {
                if (!searched.contains(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }
    }

}
