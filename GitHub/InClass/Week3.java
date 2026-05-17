public class Week3 {
   public static void main(String[] args) {
        long [] times = new long[5];
        int sum = 0;
        for (int i = 0; i < 5; i++) {
            long start = System.nanoTime(); //start time
            //thing we're testing
            int x = 0;
            for (int j = 0; j < 10000; j++) {
                x = j;
            }
            
            long end = System.nanoTime(); //end time
            System.out.println("10000 iterations took: "+(end-start)+" ns!");
            times[i] = (end-start);
            sum += (end-start);
        }
        System.out.println("Average time to complete was "+(sum/5)+" ns!");
    }

}
