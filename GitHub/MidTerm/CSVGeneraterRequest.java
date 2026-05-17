import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

public class CSVGeneraterRequest {
    public static void main(String[] args) throws Exception {
        int n = 10;
        Random rand = new Random();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("RequestCSV.csv"))) {
            writer.write(
                    "Type_of_deposit,location,min_Gold_Grams_per_ton,min_Silver_Grams_per_ton,min_Copper_Grams_per_ton,min_Profit,min_Revenue");
            writer.newLine();
            for (int i = 0; i < n; i++) {
                String location = "";
                String typeOfDeposit = "";
                double minGoldPerTon = 0;
                double minSilverPerTon = 0;
                double minCopperPerTon = 0;
                double minProfit = rand.nextDouble(1000); // Range 0 - 1000);
                double minRevenue = rand.nextDouble(2000); // Range 0 - 2000);

                int tODRandom = rand.nextInt(3) + 1; // Range 1-3
                switch (tODRandom) {
                    case 1:
                        typeOfDeposit = "Placer";
                        minGoldPerTon = rand.nextDouble(.5); // Range 0 - .5);
                        minSilverPerTon = rand.nextDouble(.1); // Range 0 - .1)
                        minCopperPerTon = 0;
                        int key = rand.nextInt(3) + 1; // Range 1 - 3);
                        switch (key) {
                            case 1:
                                location = "Colorado River";
                                break;

                            case 2:
                                location = "Klondike";
                                break;
                            case 3:
                                location = "Streams in the Sierra Nevada Mountains";
                                break;
                        }
                        break;
                    case 2:
                        typeOfDeposit = "Epithermal";
                        minGoldPerTon = rand.nextDouble(10); // Range 0 - 10)
                        minSilverPerTon = rand.nextDouble(100); // Range 0 - 100)
                        minCopperPerTon = 0;
                        key = rand.nextInt(3) + 1; // Rang 1 - 3)
                        switch (key) {
                            case 1:
                                location = "Search Light";
                                break;

                            case 2:
                                location = "Good Springs";
                                break;
                            case 3:
                                location = "Oatmann";
                                break;
                        }
                        break;
                    case 3:
                        typeOfDeposit = "Sediment";
                        minGoldPerTon = rand.nextDouble(5); // Range 0 - 5);
                        minSilverPerTon = rand.nextDouble(52); // Range 0 - 52)
                        minCopperPerTon = rand.nextDouble(25000); // Range 0 25,000)
                        key = rand.nextInt(3) + 1; // Rang 1 - 3);
                        switch (key) {
                            case 1:
                                location = "Pacific Ocean";
                                break;

                            case 2:
                                location = "Arctic Ocean";
                                break;
                            case 3:
                                location = "Indian Ocean";
                                break;
                        }
                        break;
                }
                if (rand.nextInt(2) == 0)
                    location = null;
                if (rand.nextInt(2) == 0)
                    typeOfDeposit = null;
                if (rand.nextInt(2) == 0)
                    minGoldPerTon = 0;
                if (rand.nextInt(2) == 0)
                    minSilverPerTon = 0;
                if (rand.nextInt(2) == 0)
                    minCopperPerTon = 0;
                if (rand.nextInt(2) == 0)
                    minProfit = 0;
                if (rand.nextInt(2) == 0)
                    minRevenue = 0;

                writer.write(typeOfDeposit + "," + location + "," + minGoldPerTon + ","
                        + minSilverPerTon + "," + minCopperPerTon + "," + minProfit + "," + minRevenue);
                writer.newLine();
            }
        }
    }

}
