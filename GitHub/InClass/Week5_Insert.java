public class Week5_Insert {
    public static void main(String[] args) {

    }

    public static void insert(int pos, LinkedList data, int counter){
        if(pos >= counter){
            System.out.println("Can not insert");
            return;
        }
        int i = 0;
        While(true){
            data.next();
            i++;
            if(i + 1 == pos){ // I do not know the syntax for this
                temp = data.next();
                data.next() = insert.next();
                data.next().next() = temp;
                break;
            }
        }
    }

}
