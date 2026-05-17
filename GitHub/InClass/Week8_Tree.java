

public class Week8_Tree {
    public static void main(String[] args) {

    }

    public void insert(int key, String data){
        Node curr = root;
        while(true){
            //greater then or equal
            if(curr >= root){
                if(node.right == null){
                    node.right = data;
                    break;
                }
                curr = node.right;
            }   

            //less then
            if(curr < root){
                if(node.left == null){
                    node.left = data;
                    break;
                }
                curr = node.left;
            }
        }
    }

}
