
/*
 * Author: Aidan Halpern
 * Date: 2026-2-17
 * Course: CIST004B1 - Java Data Structures
 * Homework: #Lab Week 3
 * Description: Loads a csv file and lets the user manipulate the data
 */
import java.util.Scanner;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.util.ArrayList;

public class Halpern_Aidan_HWLabWeek3 {
    public static void main(String[] args) throws Exception {
        ArrayList<data> arrList = new ArrayList<data>();
        while (true) {
            int num = -1;
            Scanner input = new Scanner(System.in);
            System.out.println("1. Load in sales data");
            System.out.println("2. Retrieve the latest sale");
            System.out.println("3. Compute the total revenue");
            System.out.println("4. Check for duplicate sale IDs ");
            System.out.println("5. Search for a sale by its ID");
            System.out.print("Please choise an option: ");
            num = input.nextInt();

            /* Load in sales data */
            if (num == 1) {
                long start = System.nanoTime();
                System.out.println("Loading csv");
                Path path = Path.of("data100000.csv");
                try (BufferedReader reader = Files.newBufferedReader(path)) {
                    /* This eats the header */
                    reader.readLine();

                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] col = line.split(",");
                        String[] colDate = col[1].split("-");
                        arrList.add(new data(Integer.parseInt(col[0]), Integer.parseInt(colDate[0]),
                                Integer.parseInt(colDate[1]), Integer.parseInt(colDate[2]), Double.parseDouble(col[2]),
                                col[3]));
                    }
                    System.out.println("Success!");
                }
                long end = System.nanoTime();
                double seconds = (end - start) / 1000000000.0;
                System.out.println("Time in seconds: " + seconds + "\n");
            }
            /* Retrieve the latest sale */
            if (num == 2) {
                long start = System.nanoTime();
                int indexMostRecent = 0;
                for (int i = 1; i <= arrList.size() - 1; i++) {
                    if (arrList.get(indexMostRecent).getYear() < arrList.get(i).getYear()) {
                        indexMostRecent = i;
                        continue;
                    }

                    if (arrList.get(indexMostRecent).getYear() > arrList.get(i).getYear())
                        continue;

                    if (arrList.get(indexMostRecent).getMonth() < arrList.get(i).getMonth()) {
                        indexMostRecent = i;
                        continue;
                    }

                    if (arrList.get(indexMostRecent).getMonth() > arrList.get(i).getMonth())
                        continue;

                    if (arrList.get(indexMostRecent).getDay() < arrList.get(i).getDay()) {
                        indexMostRecent = i;
                        continue;
                    }

                    if (arrList.get(indexMostRecent).getDay() > arrList.get(i).getDay())
                        continue;

                }
                System.out.println(arrList.get(indexMostRecent).toString());
                long end = System.nanoTime();
                double seconds = (end - start) / 1000000000.0;
                System.out.println("Time in seconds: " + seconds + "\n");
            }

            /* Compute the total revenue */
            double sum = 0;
            if (num == 3) {
                long start = System.nanoTime();
                for (int i = 0; i <= arrList.size() - 1; i++) {
                    sum += arrList.get(i).getAmount();
                }
                String formatedSum = String.format("%.2f", sum);
                System.out.println("Total Revenue is: $" + formatedSum);

                long end = System.nanoTime();
                double seconds = (end - start) / 1000000000.0;
                System.out.println("Time in seconds: " + seconds + "\n");
            }

            /* Check for duplicate sale IDs */
            if (num == 4) {
                boolean flagFoundDupe = false;
                long start = System.nanoTime();
                ArrayList<Integer> dupeIds = new ArrayList<Integer>();
                for (int i = 0; i < arrList.size() - 1; i++) {
                    for (int k = i + 1; k <= arrList.size() - 1; k++) {
                        if (arrList.get(i).getSale_Id() == arrList.get(k).getSale_Id()) {
                            if (!dupeIds.contains(arrList.get(i).getSale_Id())) {
                                dupeIds.add(arrList.get(i).getSale_Id());
                                flagFoundDupe = true;
                            }
                        }
                    }
                }
                if (flagFoundDupe) {
                    System.out.print("Found the fallowing duplicate sale Ids: ");
                    for (int i = 0; i < dupeIds.size(); i++)
                        System.out.print(dupeIds.get(i) + " ");
                } else
                    System.out.println("No duplicate sale Ids found");

                long end = System.nanoTime();
                double seconds = (end - start) / 1000000000.0;
                System.out.println("Time in seconds: " + seconds + "\n");
            }

            /* Search for a sale by its ID */
            if (num == 5) {
                long start = System.nanoTime();
                System.out.print("Please enter an Id: ");
                int key = input.nextInt();
                boolean flagIdFound = false;

                for (int i = 0; i < arrList.size(); i++) {
                    if (key == arrList.get(i).getSale_Id()) {
                        flagIdFound = true;
                        System.out.println(arrList.get(i).toString());
                    }
                }

                if (!flagIdFound)
                    System.out.println("No Sale Id found");

                long end = System.nanoTime();
                double seconds = (end - start) / 1000000000.0;
                System.out.println("Time in seconds: " + seconds + "\n");
            }

        }

        // TODO:
    }
}

class data {
    private int sale_Id;
    private int year;
    private int month;
    private int day;
    private double amount;
    private String product;

    data(int sale_Id, int year, int month, int day, double amount, String product) {
        this.sale_Id = sale_Id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.amount = amount;
        this.product = product;
    }

    public int getSale_Id() {
        return sale_Id;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public double getAmount() {
        return amount;
    }

    public String getProduct() {
        return product;
    }

    public String toString() {
        return ("Sale's Id: " + sale_Id + " Year: " + year + " Month: " + month + " Day: " + day + " Amount: " + amount
                + " product " + product);
    }
}

/*
 * Performance Trends:
 * How does each operation’s execution time change as the dataset grows?
 * Generally, it trends upwards, but at a much slower rate than I expected.
 * Do the results align with the theoretical Big O expectations?
 * No, according to expectations, the time to completion should be increasing at
 * a much faster rate.
 * Real-World Implications:
 * Which steps might become bottlenecks in a production system processing
 * millions of records?
 * I suspect creating an object for each record will eventually cause a memory
 * bottleneck. There is over a 100-fold time increase when checking for
 * duplicate IDs between 10,000 records and 100,000 records. My hypothesis is
 * that this is caused by running out of heap space, forcing the garbage truck
 * to run around freeing up space. This unfortunately means my solution to
 * finding duplicate IDs is impractical at large scales.
 * How would you optimize or replace the inefficient (quadratic) approach?
 * As I used presorted IDs, I would replace my nested for loop duplicate ID
 * finding method O(n^2) with binary O(log n).
 * Practical Adjustments:
 * How might you put together a testing plan for this project?
 * I would stress test the program above its theoretical worst-case load. From
 * there, I would fix bottlenecks and the various things that will break, and
 * keep repeating this process until the program is stable and runs at a
 * reasonable speed.
 * What additional error handling or data validation would be necessary?
 * Invalid data inputs, a way to deal with missing csv files, a way to prevent
 * heap memory overflows from users inputting infinite data, and a way to
 * prevent users from accessing other options when no data is loaded.
 * 
 */