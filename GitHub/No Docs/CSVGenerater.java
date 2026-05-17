import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

public class CSVGenerater {
    public static void main(String[] args) throws Exception {
        int n = 100000;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data" + n + ".csv"))) {
            writer.write("sale_id,sale_date,amount,product");
            writer.newLine();
            for (int i = 0; i < n; i++) {
                Random rand = new Random();
                int year = rand.nextInt(2501); // Range 0-2500
                int month = rand.nextInt(12) + 1; // Range 1-12
                int day = rand.nextInt(31) + 1; // Range 1-31
                double price = rand.nextDouble(101) + .01; // Range .01 - 100
                String formatedPrice = String.format("%.2f", price);
                String date = ("" + year + "-" + month + "-" + day);
                writer.write(i + "," + date + "," + formatedPrice + "," + "goodName" + i);
                writer.newLine();
            }
        }
    }

}
