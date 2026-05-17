import java.util.ArrayList;
import java.util.Collections;

public class Recurion {
    public static void main(String[] args) {
        ArrayList<Integer> test = new ArrayList<>();
        int num = -1234;
        vertNum(num, test, true);
        Collections.reverse(test);

        for (int i = 0; i < test.size(); i++) {
            System.out.println(test.get(i));
        }
    }

    public static void vertNum(int num, ArrayList<Integer> test, Boolean firstRun) {
        if(num == 0 && firstRun == false)
            return;

        if (num >= 0 && num <= 9) {
            System.out.println(num);
            return;
        }

        if (num >= 0 && num <= -9) {
            System.out.println(num);
            return;
        }

        if (num != 0) {
            test.add(num % 10);
        }
        vertNum(num / 10, test, false);
    }
}
