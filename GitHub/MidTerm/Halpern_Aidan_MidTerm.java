/*
 * Author: Aidan Halpern
 * Date: 2026-3-12
 * Course: CIST004B1 - Java Data Structures
 * Homework: MidTerm
 * Description: Ore Sample Manager
 * https://gemini.google.com/share/b8464356eb73
 */

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;

public class Halpern_Aidan_MidTerm {
    static ArrayList<OreSample> oreSamples = new ArrayList<>();
    static Queue<Request> requests = new Queue<Request>();
    static HashTable<OreSample> nickNames = new HashTable<OreSample>();

    public static void main(String[] args) throws Exception {
        // load in ore CSV
        loadOreCSV();
        // load in requests CSV
        loadRequestCSV();

        Scanner input = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Please pick an option:");
                System.out.println("1: Process request");
                System.out.println("2: Look up specific ID");
                System.out.println("3: Delete an ore sample by id");
                System.out.println("4: Add an ore sample");
                System.out.println("5: Search by nick name");
                System.out.println("6: Display top five ore depsoits by profit");
                System.out.println("7: Display the average grams of metals per ton of ore");
                System.out.println("8: Display average statics for profitable ore samples");
                System.out.println("9: Display raw material prices");
                System.out.println("0: Exit program");
                System.out.print("Option: ");
                int key = input.nextInt();
                input.nextLine();
                switch (key) {
                    case 1:// process request
                        if (requests.isEmpty()) {
                            System.out.println("No new requests to process");
                            break;
                        }

                        Request currRequest = requests.deQueue();
                        ArrayList<OreSample> result = new ArrayList<>(oreSamples);
                        filterByDepositType(result, currRequest);
                        filterByLocation(result, currRequest);
                        filterByMinGold(result, currRequest);
                        filterByMinSilver(result, currRequest);
                        filterByMinCopper(result, currRequest);
                        filterByMinProfit(result, currRequest);
                        filterByMinRevenue(result, currRequest);
                        System.out.println();
                        System.out.println(currRequest);
                        System.out.println();
                        if (result.isEmpty()) {
                            System.out.println("No result found\n");
                        } else {
                            System.out.println("Here are your result(s): ");
                            for (int i = 0; i < result.size(); i++) {
                                System.out.println(result.get(i).toString());
                                System.out.println();
                            }
                        }

                        break;

                    case 2:// look up based on id
                        if (oreSamples.isEmpty()) {
                            System.out.println("\nNo data to search\n");
                            break;
                        }

                        System.out.print("\nPlease enter an ID: ");
                        while (!input.hasNextInt()) {
                            System.out.print("Please enter an id as an int: ");
                            input.nextLine();
                        }
                        int iD = input.nextInt();
                        System.out.println();
                        for (int i = 0; i < oreSamples.size(); i++) {
                            if (oreSamples.get(i).getID() == iD) {
                                System.out.println(oreSamples.get(i).toString());
                                System.out.println();
                                break;
                            }
                            if (i == oreSamples.size() - 1) {
                                System.out.println("Id not found in the data base\n");
                            }
                        }

                        break;

                    case 3:// Delete an ore sample by id
                        if (oreSamples.isEmpty()) {
                            System.out.println("\nNo data to search\n");
                            break;
                        }

                        System.out.print("\nPlease enter an id to be deleted: ");
                        while (!input.hasNextInt()) {
                            System.out.print("Please enter an id to be deleted as an int: ");
                            input.nextLine();
                        }
                        int delKey = input.nextInt();

                        for (int i = 0; i < oreSamples.size(); i++) {
                            // if exists removes from arrayList and hashtable
                            if (oreSamples.get(i).getID() == delKey) {
                                System.out.println("Removed ore sample with id " + delKey + "\n");
                                if (oreSamples.get(i).getNickName() != null) {
                                    nickNames.remove(oreSamples.get(i).getNickName());
                                }
                                oreSamples.remove(i);
                                updateOreCSV();
                                break;
                            }
                            // No id found
                            if (i + 1 == oreSamples.size()) {
                                System.out.println("No ore sample with id " + delKey + " was found\n");
                            }
                        }
                        break;

                    case 4:// add an ore sample

                        // Takes in an id and checks if its valid
                        int newID;
                        while (true) {
                            System.out.print("Please enter an id: ");
                            while (!input.hasNextInt()) {
                                System.out.print("Please enter an id as an int: ");
                                input.nextLine();
                            }
                            newID = input.nextInt();
                            for (OreSample ore : oreSamples) {
                                if (ore.getID() == newID) {
                                    System.out.println("Id already exists please try again");
                                    newID = -1;
                                    break;
                                }
                            }
                            if (newID == -1)
                                continue;
                            else {
                                input.nextLine();
                                break;
                            }
                        }

                        // Takes in nick name
                        System.out.print("Please enter a nick name or \"null\": ");
                        String newNickName = input.nextLine();
                        newNickName = newNickName.toLowerCase();

                        // deposit type
                        System.out
                                .print("Please enter the type of deposit (Default, Epithermal, Placer, or Sediment): ");
                        String newTypeOfDeposit = input.nextLine();
                        while (!(newTypeOfDeposit.equals("Default") || newTypeOfDeposit.equals("Epithermal")
                                || newTypeOfDeposit.equals("Placer") || newTypeOfDeposit.equals("Sediment"))) {
                            System.out
                                    .print("Deposit must be in the form (Default, Epithermal, Placer, or Sediment): ");
                            newTypeOfDeposit = input.nextLine();
                        }

                        // location
                        System.out.print("Please enter in a location: ");
                        String newLocation = input.nextLine();

                        // Gold grams per ton
                        System.out.print("Please enter grams of gold per ton: ");
                        while (!input.hasNextDouble()) {
                            System.out.print("Please enter grams of gold per ton as a double: ");
                            input.nextLine();
                        }
                        double newGoldGramsPerTon = input.nextDouble();
                        input.nextLine();

                        // Silver grams per ton
                        System.out.print("Please enter grams of Silver per ton: ");
                        while (!input.hasNextDouble()) {
                            System.out.print("Please enter grams of Silver per ton as a double: ");
                            input.nextLine();
                        }
                        double newSilverGramsPerTon = input.nextDouble();
                        input.nextLine();

                        // Copper grams per ton
                        System.out.print("Please enter grams of Copper per ton: ");
                        while (!input.hasNextDouble()) {
                            System.out.print("Please enter grams of Copper per ton as a double: ");
                            input.nextLine();
                        }
                        double newCopperGramsPerTon = input.nextDouble();
                        input.nextLine();

                        System.out.println();

                        // Switch creates the correct deposit type ands adds it to the storage arraylist
                        // then adds to hash table if it has a nickname
                        switch (newTypeOfDeposit) {
                            case "Placer":
                                oreSamples
                                        .add(new PlacerDeposit(newID, newGoldGramsPerTon, newSilverGramsPerTon,
                                                newCopperGramsPerTon, newLocation,
                                                newNickName));
                                if (!newNickName.equals("null"))
                                    nickNames.insert(newNickName,
                                            new PlacerDeposit(newID, newGoldGramsPerTon, newSilverGramsPerTon,
                                                    newCopperGramsPerTon, newLocation,
                                                    newNickName));
                                break;

                            case "Epithermal":
                                oreSamples
                                        .add(new EpithermalDeposit(newID, newGoldGramsPerTon, newSilverGramsPerTon,
                                                newCopperGramsPerTon, newLocation,
                                                newNickName));
                                if (!newNickName.equals("null"))
                                    nickNames.insert(newNickName,
                                            new EpithermalDeposit(newID, newGoldGramsPerTon, newSilverGramsPerTon,
                                                    newCopperGramsPerTon, newLocation,
                                                    newNickName));
                                break;

                            case "Sediment":
                                oreSamples
                                        .add(new SedimentDeposit(newID, newGoldGramsPerTon, newSilverGramsPerTon,
                                                newCopperGramsPerTon, newLocation,
                                                newNickName));
                                if (!newNickName.equals("null"))
                                    nickNames.insert(newNickName,
                                            new SedimentDeposit(newID, newGoldGramsPerTon, newSilverGramsPerTon,
                                                    newCopperGramsPerTon, newLocation,
                                                    newNickName));
                                break;

                            case "Default":
                                oreSamples
                                        .add(new OreSample(newID, newGoldGramsPerTon, newSilverGramsPerTon,
                                                newCopperGramsPerTon, newLocation,
                                                newNickName));
                                if (!newNickName.equals("null"))
                                    nickNames.insert(newNickName,
                                            new OreSample(newID, newGoldGramsPerTon, newSilverGramsPerTon,
                                                    newCopperGramsPerTon, newLocation,
                                                    newNickName));
                                break;
                        }

                        // Update CSV with new ore deposit
                        updateOreCSV();
                        break;

                    case 5:// look up nick name
                        if (nickNames.isEmpty()) {
                            System.out.println("\nNo ore samples with nick names\n");
                            break;
                        }
                        System.out.print("\nPlease enter a nick name: ");
                        String nickName = input.nextLine();
                        if (nickNames.get(nickName) == null) {
                            System.out.println("No ore sample with this nick name was found\n");
                            break;
                        }
                        System.out.println("Object with the nick name " + nickName + " was found: \n");
                        System.out.println(nickNames.get(nickName).toString());
                        System.out.println();
                        break;

                    case 6:// top 5 profit
                        if (oreSamples.isEmpty()) {
                            System.out.println("No data to search\n");
                            break;
                        }
                        // Transfer from arrayList to an array
                        OreSample[] arrayListToArray = new OreSample[oreSamples.size()];
                        for (int i = 0; i < oreSamples.size(); i++) {
                            arrayListToArray[i] = oreSamples.get(i);
                        }
                        // Merge sort
                        OreSample[] sortedByProfit = MergeSort.mergeRecurion(arrayListToArray);
                        System.out.println();

                        // Print top 5
                        int topDepsoitCounter = 0;
                        for (int i = sortedByProfit.length - 1; i > 0; i--) {
                            System.out.println("Top " + (topDepsoitCounter + 1));
                            System.out.println(sortedByProfit[i]);
                            System.out.println();
                            topDepsoitCounter++;
                            if (topDepsoitCounter == 5)
                                break;
                        }
                        break;

                    case 7:// Averge grams of metal per ton
                        if (oreSamples.isEmpty()) {
                            System.out.println("No data to search\n");
                            break;
                        }

                        double totalGoldGrams = 0;
                        double totalSilverGrams = 0;
                        double totalCopperGrams = 0;
                        for (int i = 0; i < oreSamples.size(); i++) {
                            totalGoldGrams += oreSamples.get(i).getGoldGramsPerTon();
                            totalSilverGrams += oreSamples.get(i).getSilverGramsPerTon();
                            totalCopperGrams += oreSamples.get(i).getCopperGramsPerTon();
                        }
                        System.out.println();
                        System.out.printf("Average grams of gold per ton of ore: %.2f\n",
                                (totalGoldGrams / oreSamples.size()));
                        System.out.printf("Average grams of silver per ton of ore: %.2f\n",
                                (totalSilverGrams / oreSamples.size()));
                        System.out.printf("Average grams of copper per ton of ore: %.2f\n",
                                (totalCopperGrams / oreSamples.size()));
                        System.out.println();
                        break;

                    case 8:// Stats on profitable ore samplmes
                        ArrayList<OreSample> profitabOreSamples = new ArrayList<>();
                        if (oreSamples.isEmpty()) {
                            System.out.println("No data to search\n");
                            break;
                        }
                        // Transfer from arrayList to an array
                        OreSample[] arrayListToArrayP = new OreSample[oreSamples.size()];
                        for (int i = 0; i < oreSamples.size(); i++) {
                            arrayListToArrayP[i] = oreSamples.get(i);
                        }
                        // Merge sort
                        OreSample[] sortedByProfitP = MergeSort.mergeRecurion(arrayListToArrayP);
                        System.out.println();

                        // Find profitable ore samples and add them to an arrayList
                        for (int i = sortedByProfitP.length - 1; i > 0; i--) {
                            if (sortedByProfitP[i].getProfitPerTon() < 0)
                                break;
                            else
                                profitabOreSamples.add(sortedByProfitP[i]);
                        }
                        double totalRevenue = 0;
                        double totalProfit = 0;
                        totalGoldGrams = 0;
                        totalSilverGrams = 0;
                        totalCopperGrams = 0;

                        // Sum profitable stats
                        for (OreSample temp : profitabOreSamples) {
                            totalRevenue += temp.getRevenuePerTon();
                            totalProfit += temp.getProfitPerTon();
                            totalGoldGrams += temp.getGoldGramsPerTon();
                            totalSilverGrams += temp.getSilverGramsPerTon();
                            totalCopperGrams += temp.getCopperGramsPerTon();
                        }
                        System.out.println("Average statics for profitable ore samples:");
                        System.out.printf("Average revenue: $%.2f\n", (totalRevenue / profitabOreSamples.size()));
                        System.out.printf("Average profit: $%.2f\n", (totalProfit / profitabOreSamples.size()));
                        System.out.printf("Average grams of gold per ton of ore: %.2f\n",
                                (totalGoldGrams / profitabOreSamples.size()));
                        System.out.printf("Average grams of silver per ton of ore: %.2f\n",
                                (totalSilverGrams / profitabOreSamples.size()));
                        System.out.printf("Average grams of copper per ton of ore: %.2f\n",
                                (totalCopperGrams / profitabOreSamples.size()));
                        System.out.println();
                        break;

                    case 9:// Live raw material prices
                        System.out.println("\nLive raw material prices:");
                        System.out.printf("Gold price $%.2f per gram\n", CurrOrePrice.getCurrGoldPrice());
                        System.out.printf("Silver price $%.2f per gram\n", CurrOrePrice.getCurrSilverPrice());
                        System.out.printf("Copper price $%.2f per gram\n", CurrOrePrice.getCurrCopperPrice());
                        System.out.printf("Crude oil price $%.2f per barrel\n\n", CurrOrePrice.getCurrOilPrice());
                        break;

                    case 0:// Exit program
                        System.out.print("Exiting");
                        input.close();
                        System.exit(0);
                        break;
                }
            } catch (InputMismatchException e) {
                input.nextLine();
                System.out.println("Invalid input please try again\n");
            }
        }
        // TODO:
    }

    public static void filterByDepositType(ArrayList<OreSample> result, Request currRequest) {
        if (currRequest.getTypeOfDeposit() == null)
            return;

        if (currRequest.getTypeOfDeposit().equals("Placer")) {
            for (int i = result.size() - 1; i >= 0; i--) {
                if (result.get(i) instanceof SedimentDeposit)
                    result.remove(i);

                else if (result.get(i) instanceof EpithermalDeposit)
                    result.remove(i);
            }
        }

        if (currRequest.getTypeOfDeposit().equals("Epithermal")) {
            for (int i = result.size() - 1; i >= 0; i--) {
                if (result.get(i) instanceof SedimentDeposit)
                    result.remove(i);

                else if (result.get(i) instanceof PlacerDeposit)
                    result.remove(i);
            }
        }

        if (currRequest.getTypeOfDeposit().equals("Sediment")) {
            for (int i = result.size() - 1; i >= 0; i--) {
                if (result.get(i) instanceof PlacerDeposit)
                    result.remove(i);

                else if (result.get(i) instanceof EpithermalDeposit)
                    result.remove(i);
            }
        }

    }

    public static void filterByLocation(ArrayList<OreSample> result, Request currRequest) {
        if (currRequest.getLocation() == null)
            return;

        for (int i = result.size() - 1; i >= 0; i--) {
            if (!result.get(i).getLocation().equals(currRequest.getLocation())) {
                result.remove(i);
            }
        }
    }

    public static void filterByMinGold(ArrayList<OreSample> result, Request currRequest) {
        for (int i = result.size() - 1; i >= 0; i--) {
            if (result.get(i).getGoldGramsPerTon() < currRequest.getMinGoldPerTon()) {
                result.remove(i);
            }
        }
    }

    public static void filterByMinSilver(ArrayList<OreSample> result, Request currRequest) {
        for (int i = result.size() - 1; i >= 0; i--) {
            if (result.get(i).getSilverGramsPerTon() < currRequest.getMinSilverPerTon()) {
                result.remove(i);
            }
        }
    }

    public static void filterByMinCopper(ArrayList<OreSample> result, Request currRequest) {
        for (int i = result.size() - 1; i >= 0; i--) {
            if (result.get(i).getCopperGramsPerTon() < currRequest.getMinCopperPerTon()) {
                result.remove(i);
            }
        }
    }

    public static void filterByMinProfit(ArrayList<OreSample> result, Request currRequest) {
        for (int i = result.size() - 1; i >= 0; i--) {
            if (result.get(i).getProfitPerTon() < currRequest.getMinProfitPerTon()) {
                result.remove(i);
            }
        }
    }

    public static void filterByMinRevenue(ArrayList<OreSample> result, Request currRequest) {
        for (int i = result.size() - 1; i >= 0; i--) {
            if (result.get(i).getRevenuePerTon() < currRequest.getMinRevenuePerTon()) {
                result.remove(i);
            }
        }
    }

    // loads in ore CSV
    public static void loadOreCSV() throws Exception {
        oreSamples = new ArrayList<>();
        boolean firstLoad = true;
        if (firstLoad) {
            System.out.println("Loading Ore CSV");
        }

        Path path = Path.of("OreCSV.csv");
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            /* This eats the header */
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] col = line.split(",");
                int iD = Integer.parseInt(col[0]);
                String nickName = col[1];
                if (nickName.equals("null"))
                    nickName = null;
                String typeOfDeposit = col[2];
                String location = col[3];
                double goldPerTon = Double.parseDouble(col[4]);
                double silverPerTon = Double.parseDouble(col[5]);
                double copperPerTon = Double.parseDouble(col[6]);

                // Switch creates correct deposit type and adds to hash table if it has a nick
                // name
                switch (typeOfDeposit) {
                    case "Placer":
                        oreSamples
                                .add(new PlacerDeposit(iD, goldPerTon, silverPerTon, copperPerTon, location, nickName));
                        if (nickName != null)
                            nickNames.insert(nickName,
                                    new PlacerDeposit(iD, goldPerTon, silverPerTon, copperPerTon, location, nickName));
                        break;

                    case "Epithermal":
                        oreSamples
                                .add(new EpithermalDeposit(iD, goldPerTon, silverPerTon, copperPerTon, location,
                                        nickName));
                        if (nickName != null)
                            nickNames.insert(nickName,
                                    new EpithermalDeposit(iD, goldPerTon, silverPerTon, copperPerTon, location,
                                            nickName));
                        break;

                    case "Sediment":
                        oreSamples
                                .add(new SedimentDeposit(iD, goldPerTon, silverPerTon, copperPerTon, location,
                                        nickName));
                        if (nickName != null)
                            nickNames.insert(nickName,
                                    new SedimentDeposit(iD, goldPerTon, silverPerTon, copperPerTon, location,
                                            nickName));
                        break;

                    case "Default":
                        oreSamples
                                .add(new OreSample(iD, goldPerTon, silverPerTon, copperPerTon, location, nickName));
                        if (nickName != null)
                            nickNames.insert(nickName,
                                    new OreSample(iD, goldPerTon, silverPerTon, copperPerTon, location, nickName));
                        break;
                }
            }
        }
    }

    // loads in request CSV
    public static void loadRequestCSV() throws Exception {
        requests = new Queue<Request>();
        boolean firstLoad = true;
        if (firstLoad)
            System.out.println("Loading Request CSV");

        Path pathReq = Path.of("RequestCSV.csv");
        try (BufferedReader reader = Files.newBufferedReader(pathReq)) {
            /* This eats the header */
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] col = line.split(",");
                String typeOfDeposit;
                if (col[0].equals("null"))
                    typeOfDeposit = null;
                else
                    typeOfDeposit = col[0];

                String location;
                if (col[1].equals("null"))
                    location = null;
                else
                    location = col[1];

                double minGoldPerTon = Double.parseDouble(col[2]);
                double minSilverPerTon = Double.parseDouble(col[3]);
                double minCopperPerTon = Double.parseDouble(col[4]);
                double minProfitPerTon = Double.parseDouble(col[5]);
                double minRevenuePerTon = Double.parseDouble(col[6]);

                requests.enQueue(new Request(typeOfDeposit, location, minGoldPerTon, minSilverPerTon, minCopperPerTon,
                        minProfitPerTon, minRevenuePerTon));
            }
            if (firstLoad) {
                System.out.println("Success! Loaded Request CSV");
                firstLoad = false;
            }
        }

    }

    // Writes current ore data to ore CSV
    public static void updateOreCSV() throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("OreCSV.csv"))) {
            writer.write(
                    "id,nick_name,Type_of_deposit,location,gold_Grams_per_ton,silver_Grams_per_ton,copper_Grams_per_ton");
            writer.newLine();

            for (int i = 0; i < oreSamples.size(); i++) {
                writer.write(oreSamples.get(i).getCSVString());
                writer.newLine();
            }
        }
    }
}

// Class to grab real life prices
class CurrOrePrice {
    protected static boolean webScrapperOverride = true; // true = using hardcoded values | false = using web scrapper
                                                         // values
    protected static double currGoldPrice = -1;
    protected static double currSilverPrice = -1;
    protected static double currCopperPrice = -1;
    protected static double currOilPrice = -1;

    private static void updatePrice() {
        if (currGoldPrice < 0 && currSilverPrice < 0 && currCopperPrice < 0 && currOilPrice < 0) {
            // System.out.println("\nWeb Scrapper called: ");
            if (webScrapperOverride) {// true = using hardcoded values | false = using web scrapper values
                currGoldPrice = 144.76;
                currSilverPrice = 2.40;
                currCopperPrice = 0.0121;
                currOilPrice = 100.758;
            } else
                scrapeMetalPrices();
        }
    }

    private static void scrapeMetalPrices() {
        String url = "https://tradingeconomics.com/commodities";

        try {
            Document doc = Jsoup.connect("https://tradingeconomics.com/commodities")
                    .userAgent("Mozilla/5.0")
                    .get();

            String goldStr = doc.select("tr[data-symbol='XAUUSD:CUR'] td#p").text();
            String silverStr = doc.select("tr[data-symbol='XAGUSD:CUR'] td#p").text();
            String oilStr = doc.select("tr[data-symbol='CO1:COM'] td#p").text();

            Element copperRow = doc.select("tr:has(a[href*='copper'])").first();
            String copperStr = (copperRow != null) ? copperRow.select("td#p").text() : "0";

            double goldPrice = Double.parseDouble(goldStr.replaceAll("[^\\d.]", ""));
            double silverPrice = Double.parseDouble(silverStr.replaceAll("[^\\d.]", ""));
            double copperPrice = Double.parseDouble(copperStr.replaceAll("[^\\d.]", ""));
            double oilPrice = Double.parseDouble(oilStr.replaceAll("[^\\d.]", ""));

            currGoldPrice = goldPrice / 31.1035;
            currSilverPrice = silverPrice / 31.1035;
            currCopperPrice = copperPrice / 453.592;
            currOilPrice = oilPrice;

        } catch (IOException e) {
            System.out.println("Error connecting to the market site: " + e.getMessage());
            webScrapperOverride = true;
            currGoldPrice = 144.76;
            currSilverPrice = 2.40;
            currCopperPrice = 0.0121;
            currOilPrice = 100.758;
        } catch (Exception e) {
            System.out.println("Error parsing data. The website structure might have changed.");
            webScrapperOverride = true;
            currGoldPrice = 144.76;
            currSilverPrice = 2.40;
            currCopperPrice = 0.0121;
            currOilPrice = 100.758;
        }
    }

    public static double getCurrGoldPrice() {
        updatePrice();
        return currGoldPrice;
    }

    public static double getCurrSilverPrice() {
        updatePrice();
        return currSilverPrice;
    }

    public static double getCurrCopperPrice() {
        updatePrice();
        return currCopperPrice;
    }

    public static double getCurrOilPrice() {
        updatePrice();
        return currOilPrice;
    }

}

// Main data class
class OreSample {
    protected int iD;
    protected String nickName;
    protected double goldGramsPerTon;
    protected double silverGramsPerTon;
    protected double copperGramsPerTon;
    protected String location;

    OreSample(int iD, double goldGramsPerTon, double silverGramsPerTon, double copperGramsPerTon, String location,
            String nickName) {
        this.iD = iD;
        this.goldGramsPerTon = goldGramsPerTon;
        this.silverGramsPerTon = silverGramsPerTon;
        this.copperGramsPerTon = copperGramsPerTon;
        this.location = location;
        this.nickName = nickName;
    }

    public int getID() {
        return iD;
    }

    public String getNickName() {
        return nickName;
    }

    public double getGoldGramsPerTon() {
        return goldGramsPerTon;
    }

    public double getSilverGramsPerTon() {
        return silverGramsPerTon;
    }

    public double getCopperGramsPerTon() {
        return copperGramsPerTon;
    }

    public String getLocation() {
        return location;
    }

    public double getRevenuePerTon() {
        double revenue = 0;
        revenue += (goldGramsPerTon * CurrOrePrice.getCurrGoldPrice());
        revenue += (silverGramsPerTon * CurrOrePrice.getCurrSilverPrice());
        revenue += (copperGramsPerTon * CurrOrePrice.getCurrCopperPrice());
        return revenue;
    }

    public double getProfitPerTon() {
        return getRevenuePerTon();
    }

    public String getCSVString() {
        return ("" + iD + "," + nickName + "," + "Default," + location + "," + goldGramsPerTon + "," + silverGramsPerTon
                + "," + copperGramsPerTon);
    }

    @Override
    public String toString() {
        return ("Default ore Sample\n" + "Id: " + iD + "\nLocation: " + location + "\nNick Name: " + nickName
                + "\nRevenue per ton: $" + String.format("%.2f", getRevenuePerTon()) + "\nProfit per ton: $"
                + String.format("%.2f", getProfitPerTon())
                + "\nGold grams per ton: " + String.format("%.2f", goldGramsPerTon) + "\nSilver grams per ton: "
                + String.format("%.2f", silverGramsPerTon)
                + "\nCopper grams per ton: " + String.format("%.2f", copperGramsPerTon));
    }
}

// Child of main data class
class PlacerDeposit extends OreSample {

    PlacerDeposit(int iD, double goldGramsPerTon, double silverGramsPerTon, double copperGramsPerTon,
            String location, String nickName) {
        super(iD, goldGramsPerTon, silverGramsPerTon, copperGramsPerTon, location, nickName);
    }

    @Override
    public double getProfitPerTon() {
        double OilMultiplier = .5;

        return (super.getRevenuePerTon() - (OilMultiplier * CurrOrePrice.getCurrOilPrice()));
    }

    @Override
    public String getCSVString() {
        return ("" + iD + "," + nickName + "," + "Placer," + location + "," + goldGramsPerTon + "," + silverGramsPerTon
                + "," + copperGramsPerTon);
    }

    @Override
    public String toString() {
        return ("Placer Deposit\n" + "Id: " + iD + "\nLocation: " + location + "\nNick Name: " + nickName
                + "\nRevenue per ton: $" + String.format("%.2f", getRevenuePerTon()) + "\nProfit per ton: $"
                + String.format("%.2f", getProfitPerTon())
                + "\nGold grams per ton: " + String.format("%.2f", goldGramsPerTon) + "\nSilver grams per ton: "
                + String.format("%.2f", silverGramsPerTon)
                + "\nCopper grams per ton: " + String.format("%.2f", copperGramsPerTon));
    }
}

// Child of main data class
class EpithermalDeposit extends OreSample {

    EpithermalDeposit(int iD, double goldGramsPerTon, double silverGramsPerTon, double copperGramsPerTon,
            String location, String nickName) {
        super(iD, goldGramsPerTon, silverGramsPerTon, copperGramsPerTon, location, nickName);
    }

    @Override
    public double getProfitPerTon() {
        double OilMultiplier = 1.5;

        return (super.getRevenuePerTon() - (OilMultiplier * CurrOrePrice.getCurrOilPrice()));
    }

    @Override
    public String getCSVString() {
        return ("" + iD + "," + nickName + "," + "Epithermal," + location + "," + goldGramsPerTon + ","
                + silverGramsPerTon + "," + copperGramsPerTon);
    }

    @Override
    public String toString() {
        return ("Epithermal Deposit\n" + "Id: " + iD + "\nLocation: " + location + "\nNick Name: " + nickName
                + "\nRevenue per ton: $" + String.format("%.2f", getRevenuePerTon()) + "\nProfit per ton: $"
                + String.format("%.2f", getProfitPerTon())
                + "\nGold grams per ton: " + String.format("%.2f", goldGramsPerTon) + "\nSilver grams per ton: "
                + String.format("%.2f", silverGramsPerTon)
                + "\nCopper grams per ton: " + String.format("%.2f", copperGramsPerTon));
    }
}

// Child of main data class
class SedimentDeposit extends OreSample {

    SedimentDeposit(int iD, double goldGramsPerTon, double silverGramsPerTon, double copperGramsPerTon,
            String location, String nickName) {
        super(iD, goldGramsPerTon, silverGramsPerTon, copperGramsPerTon, location, nickName);
    }

    @Override
    public double getProfitPerTon() {
        double OilMultiplier = 1.8;

        return (super.getRevenuePerTon() - (OilMultiplier * CurrOrePrice.getCurrOilPrice()));
    }

    @Override
    public String getCSVString() {
        return ("" + iD + "," + nickName + "," + "Sediment," + location + "," + goldGramsPerTon + ","
                + silverGramsPerTon + "," + copperGramsPerTon);
    }

    @Override
    public String toString() {
        return ("Sediment Deposit\n" + "Id: " + iD + "\nLocation: " + location + "\nNick Name: " + nickName
                + "\nRevenue per ton: $" + String.format("%.2f", getRevenuePerTon()) + "\nProfit per ton: $"
                + String.format("%.2f", getProfitPerTon())
                + "\nGold grams per ton: " + String.format("%.2f", goldGramsPerTon) + "\nSilver grams per ton: "
                + String.format("%.2f", silverGramsPerTon)
                + "\nCopper grams per ton: " + String.format("%.2f", copperGramsPerTon));
    }
}

// Requests data class
class Request {
    private String typeOfDeposit;
    private String location;
    private double minGoldPerTon;
    private double minSilverPerTon;
    private double minCopperPerTon;
    private double minProfitPerTon;
    private double minRevenuePerTon;

    Request(String typeOfDeposit, String location, double minGoldPerTon, double minSilverPerTon,
            double minCopperPerTon, double minProfitPerTon,
            double minRevenuePerTon) {
        this.typeOfDeposit = typeOfDeposit;
        this.location = location;
        this.minGoldPerTon = minGoldPerTon;
        this.minSilverPerTon = minSilverPerTon;
        this.minCopperPerTon = minCopperPerTon;
        this.minProfitPerTon = minProfitPerTon;
        this.minRevenuePerTon = minRevenuePerTon;
    }

    public String getTypeOfDeposit() {
        return typeOfDeposit;
    }

    public String getLocation() {
        return location;
    }

    public double getMinGoldPerTon() {
        return minGoldPerTon;
    }

    public double getMinSilverPerTon() {
        return minSilverPerTon;
    }

    public double getMinCopperPerTon() {
        return minCopperPerTon;
    }

    public double getMinProfitPerTon() {
        return minProfitPerTon;
    }

    public double getMinRevenuePerTon() {
        return minRevenuePerTon;
    }

    @Override
    public String toString() {
        return ("Request Requirements:\nType of deposit: " + typeOfDeposit + "\nLocation: " + location
                + "\nMinimum revenue per ton: $"
                + String.format("%.2f", minRevenuePerTon) + "\nMinimum profit per ton: $"
                + String.format("%.2f", minProfitPerTon) + "\nMinimum gold grams per ton: "
                + String.format("%.2f", minGoldPerTon) + "\nMinimum silver grams per ton: "
                + String.format("%.2f", minSilverPerTon)
                + "\nMinimum copper grams per ton: " + String.format("%.2f", minCopperPerTon));
    }
}

class Queue<T> {
    DoubleLinkedList<T> list;

    Queue() {
        list = new DoubleLinkedList<>();
    }

    public void enQueue(T data) {
        list.addLast(data);
    }

    public T deQueue() {
        T temp = list.getFirst();
        list.removeFirst();
        return temp;
    }

    public T peek() {
        return list.getFirst();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}

class DoubleLinkedList<T> {
    public Node<T> head;
    public Node<T> tail;
    public int length;

    DoubleLinkedList() {
        this.head = null;
        this.tail = null;
        length = 0;
    }

    DoubleLinkedList(T input) {
        this.head = new Node<>(input);
        this.tail = head;
        length = 1;
    }

    public void addFirst(T data) {
        if (isEmpty()) {
            head = new Node<T>(data);
            tail = head;
        } else {
            Node<T> temp = new Node(data);
            temp.next = head;
            head.prev = temp;
            head = temp;
        }
        length++;
    }

    public void addLast(T data) {
        if (isEmpty()) {
            tail = new Node<T>(data);
            head = tail;
        } else {
            Node<T> temp = new Node(data);
            temp.prev = tail;
            tail.next = temp;
            tail = temp;
        }
        length++;
    }

    public T getFirst() {
        return isEmpty() ? null : head.data;
    }

    public T getLast() {
        return isEmpty() ? null : tail.data;
    }

    public void removeFirst() {
        if (isEmpty())
            return;
        else if (length == 1) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        length--;
    }

    public void removeLast() {
        if (isEmpty())
            return;
        else if (length == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        length--;
    }

    public void removeAt(int index) {
        if (index < 0 || index >= length) {
            System.err.println("Index out of bounds");
            return;
        }

        if (index == 0) {
            removeFirst();
            return;
        }

        if (index == length - 1) {
            removeLast();
            return;
        }

        Node<T> curr = head;
        for (int i = 1; i <= index; i++) {
            curr = curr.next;
        }

        curr.next.prev = curr.prev;
        curr.prev.next = curr.next;
        curr = null;
        length--;
    }

    // Inserts a new node after the index inputed
    public void add(int index, T data) {
        if (index < 0 || index > length) {
            System.err.println("Index out of bounds");
            return;
        }

        if (index == 0) {
            addFirst(data);
            return;
        }

        if (index == length) {
            addLast(data);
            return;
        }

        Node<T> curr = head;
        for (int i = 1; i + 1 <= index; i++) {
            curr = curr.next;
        }
        Node<T> temp = new Node(data);
        temp.prev = curr;
        temp.next = curr.next;
        temp.next.prev = temp;
        curr.next = temp;
        length++;

    }

    public void printList() {
        if (isEmpty()) {
            System.err.println("list is empty");
            return;
        }
        Node<T> curr = head;
        while (curr != null) {
            System.out.println(curr.data);
            curr = curr.next;
        }
    }

    public T get(int index) {
        if (index >= length || index < 0)
            return null;

        Node<T> curr = head;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr.data;
    }

    public int size() {
        return length;
    }

    public boolean isEmpty() {
        return (length == 0);
    }

    // Node class for double linked list
    class Node<T> {
        public T data;

        public Node<T> next;
        public Node<T> prev;

        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
}

class MergeSort {
    public static OreSample[] mergeRecurion(OreSample[] arr) {
        OreSample[] leftArr;
        OreSample[] rightArr;
        /* Done Spilting logic */
        if (arr.length <= 1) {
            return arr;
        }
        /* Spilting logic */
        else {
            int breakPoint = arr.length / 2;
            leftArr = new OreSample[breakPoint];
            rightArr = new OreSample[arr.length - breakPoint];
            for (int i = 0; i < breakPoint; i++) {
                leftArr[i] = arr[i];
            }
            for (int i = breakPoint; i < arr.length; i++) {
                rightArr[i - breakPoint] = arr[i];
            }
            leftArr = mergeRecurion(leftArr);
            rightArr = mergeRecurion(rightArr);
        }
        /* Merging logic */
        OreSample[] sortedArr = new OreSample[leftArr.length + rightArr.length];

        int rightCounter = 0;
        int leftCounter = 0;

        while (rightCounter < rightArr.length && leftCounter < leftArr.length) {
            if (leftArr[leftCounter].getProfitPerTon() <= rightArr[rightCounter].getProfitPerTon()) {
                sortedArr[rightCounter + leftCounter] = leftArr[leftCounter];
                leftCounter++;
            } else {
                sortedArr[rightCounter + leftCounter] = rightArr[rightCounter];
                rightCounter++;
            }
        }

        while (rightCounter < rightArr.length) {
            sortedArr[rightCounter + leftCounter] = rightArr[rightCounter];
            rightCounter++;
        }

        while (leftCounter < leftArr.length) {
            sortedArr[rightCounter + leftCounter] = leftArr[leftCounter];
            leftCounter++;
        }

        return sortedArr;
    }

}

class HashTable<T> {
    private int size;
    private HTEntry<T>[] table;
    private double loadFactor;// size / table.length (not int division)

    HashTable() {
        table = new HTEntry[16];
        size = 0;
    }

    HashTable(int capacity) {
        table = new HTEntry[capacity];
        size = 0;
    }

    public int hash(String key) {
        int hashValue = 0;
        for (int idx = 0; idx < key.length(); idx++) {
            hashValue += key.charAt(idx);
        }
        return Math.abs(hashValue % table.length);
    }

    public int hash2(String key) {
        if (Math.abs(key.hashCode()) % 2 == 0)
            return Math.abs(key.hashCode()) + 1;
        return Math.abs(key.hashCode());
    }

    // expands hash table
    private void rehash() {
        HTEntry<T>[] oldTable = table;
        table = new HTEntry[oldTable.length * 2];
        loadFactor = 0;
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null && !oldTable[i].deleted) {
                insert(oldTable[i].key, oldTable[i].value);
            }
        }
    }

    // add new hash table entry
    public void insert(String key, T value) {
        // transfers to bigger hash table
        if (loadFactor > 0.7) {
            rehash();
        }

        int index1 = hash(key);
        int index2 = hash2(key);
        int startIndex = index1;
        int i = 1;

        while (table[index1] != null) {
            // If key already exist overridde the current data with new data
            if (table[index1].key.equals(key)) {
                table[index1].value = value;
                // Edge case if you remove a node then try to re-add it
                if (table[index1].deleted) {
                    table[index1].deleted = false;
                    size++;
                }
                return;
            }

            // Double hashing
            index1 = (startIndex + i * index2) % table.length;
            i++;

            // all spots checked no space left must increase table size
            if (index1 == startIndex) {
                rehash();
                insert(key, value);
                return;
            }
        }
        // insert into table
        table[index1] = new HTEntry(key, value);
        size++;
        loadFactor = ((double) size / table.length);
    }

    // loop up method
    public T get(String key) {
        int index1 = hash(key);
        int index2 = hash2(key);
        int startIndex = index1;
        int i = 1;

        while (table[index1] != null) {
            // key is found and object is returned
            if (!table[index1].deleted && table[index1].key.equals(key)) {
                return table[index1].value;
            }

            // Double hashing
            index1 = (startIndex + i * index2) % table.length;
            i++;

            // all spots checked key not found
            if (index1 == startIndex) {
                return null;
            }
        }

        return null;
    }

    // remove hash table entry
    public void remove(String key) {
        int index1 = hash(key);
        int index2 = hash2(key);
        int startIndex = index1;
        int i = 1;

        while (table[index1] != null) {
            // key is found and data is set to deleated
            if (!table[index1].deleted && table[index1].key.equals(key)) {
                table[index1].deleted = true;
                size--;
                loadFactor = ((double) size / table.length);
                return;
            }

            // Double hashing
            index1 = (startIndex + i * index2) % table.length;
            i++;

            // all spots checked key not found
            if (index1 == startIndex) {
                break;
            }
        }
    }

    public boolean isEmpty() {
        if (size == 0)
            return true;

        return false;
    }

    // Data storage for the hash table
    class HTEntry<T> {
        String key;
        T value;
        Boolean deleted;

        public HTEntry(String key, T value) {
            this.key = key;
            this.value = value;
            this.deleted = false;
        }
    }
}