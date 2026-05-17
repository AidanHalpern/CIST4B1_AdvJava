
public class Week11_RightRotate {
    public static void main(String[] args) {

    }

    private int height(Node current) {
        if (current == null) {
            return -1;
        } else if (current.left == null && current.right == null) {
            return 0;
        }
        return Math.max(height(current.left), height(current.right) + 1);
    }

    private Node rightRotate(Node oldRoot) {
        Node newRoot = oldRoot.left;
        oldRoot.left = newRoot.right;
        newRoot.right = oldRoot;
        
        oldRoot.height = height(oldRoot);
        newRoot.height = height(newRoot);
        return newRoot;
    }

}
