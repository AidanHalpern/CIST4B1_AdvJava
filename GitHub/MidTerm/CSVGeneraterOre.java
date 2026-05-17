import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

public class CSVGeneraterOre {
    public static void main(String[] args) throws Exception {
        int n = 100;
        Random rand = new Random();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("OreCSV.csv"))) {
            writer.write(
                    "id,nick_name,Type_of_deposit,location,gold_Grams_per_ton,silver_Grams_per_ton,copper_Grams_per_ton");
            writer.newLine();
            for (int i = 0; i < n; i++) {
                int id = i + 1;
                String nickName = null;
                String location = "";
                String typeOfDeposit = "";
                double goldPerTon = 0;
                double silverPerTon = 0;
                double copperPerTon = 0;
                int tODRandom = rand.nextInt(3) + 1; // Range 1-3)
                switch (tODRandom) {
                    case 1:
                        typeOfDeposit = "Placer";
                        goldPerTon = rand.nextDouble(.5); // Range 0 - .5);
                        silverPerTon = rand.nextDouble(.1); // Range 0 - .1)
                        copperPerTon = 0;
                        int key = rand.nextInt(3) + 1; // Range 1 - 3)
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
                        goldPerTon = rand.nextDouble(10); // Range 0 - 10)
                        silverPerTon = rand.nextDouble(100); // Range 0 - 100)
                        copperPerTon = 0;
                        key = rand.nextInt(3) + 1; // Rang 1 - 3
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
                        goldPerTon = rand.nextDouble(5); // Range 0 - 5)
                        silverPerTon = rand.nextDouble(52); // Range 0 - 52)
                        copperPerTon = rand.nextDouble(25000); // Range 0 25,000)
                        key = rand.nextInt(3) + 1; // Rang 1 - 3;
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

                writer.write(id + "," + nickName + "," + typeOfDeposit + "," + location + "," + goldPerTon + ","
                        + silverPerTon + "," + copperPerTon);
                writer.newLine();
            }
        }
    }

}
