import java.util.ArrayList;
import java.util.HashMap;

public class Week14 {
    public static void main(String[] args) {
        HashMap<Integer> solved = new HashMap<>();
    }

    public static int solveFib(HashMap<Integer> solved, int n) {
        if(n <= 1)
            return n;
        int soltion;
        if (solved.containsKey(n)) {
            return solved.get(n);
        } else {
            soltion = solveFib(solved, n - 1) + solveFib(solved, n - 2);
        }

        return soltion;

    }

}
