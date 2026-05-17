/*
 * Author: Aidan Halpern
 * Date: 2026-4-28
 * Course: CIST004B1 - Java Data Structures
 * Homework: #Final Project
 * Description: Fishing Hot Spot Manager
 * https://gemini.google.com/share/f339903ec4c2
 */

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Collections;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Halpern_Aidan_Final {
    static DoubleLinkedList<HotSpot> allHotSpots = new DoubleLinkedList<>();
    static HashTable<HotSpot> iDToObjRef = new HashTable<>();
    static HashSet outOfSeasonSpring = new HashSet();
    static HashSet outOfSeasonSummer = new HashSet();
    static HashSet outOfSeasonFall = new HashSet();
    static HashSet outOfSeasonWinter = new HashSet();
    static GraphAdjList<HotSpot> graph = new GraphAdjList();

    public static void main(String[] args) throws Exception {
        importHotSpotData();
        Scanner input = new Scanner(System.in);
        String season = "";
        while (true) {
            System.out.print("Please enter the current season: ");
            season = input.nextLine().toLowerCase();
            if (season.equals("spring") || season.equals("summer") || season.equals("fall")
                    || season.equals("winter")) {
                break;
            } else {
                System.out.println("Incorrect season please try again.");
            }
        }
        importEdges(season);
        while (true) {
            System.out.println("Please pick an option");
            System.out.println("1: Plan a fishing trip targeting a specific species");
            System.out.println("2: Travel between two fishing hot spots using their IDs");
            System.out.println("3: Visit the most amount of hot spots with a limited time");
            System.out.println("4: Check if a specie is in season");
            System.out.println("5: Display all out of season species");
            System.out.println("6: Search for a hotspot by ID");
            System.out.println("7: Search for a hotspot by species");
            System.out.println("8: Search for a hotspot by location");
            System.out.println("9: Display all species with restrictions");
            System.out.println("10: Display current weather in Santa Cruz");
            System.out.println("0: Exit program");
            System.out.print("Option: ");
            if (!input.hasNextInt()) {
                System.out.println("Please enter a valid integer\n");
                input.nextLine();
                continue;
            }
            int key = input.nextInt();
            input.nextLine();

            switch (key) {
                case 1: // Optimal path to a species
                    String target = "";
                    while (true) {
                        System.out.print("Please enter a specie's name: ");
                        target = input.nextLine().toLowerCase().trim();
                        if (target.equals("")) {
                            System.out.println("Invalid specie's name please try again");
                            continue;
                        }
                        break;
                    }
                    graph.dijkstraSpecie("B0", target, iDToObjRef);
                    break;

                case 2:// Travel between two fishing hotspots via IDS
                    String sID = null;
                    String dID = null;
                    while (true) {
                        System.out.print("Please enter starting ID: ");
                        sID = input.nextLine();
                        if (iDToObjRef.get(sID) == null) {
                            System.out.println("Invalid ID please try again");
                            continue;
                        }
                        String sSpcies = iDToObjRef.get(sID).specie;
                        if (sSpcies != null) {
                            // Check if in season so it has edges
                            if (findCorrectHashSet(season).search(sSpcies.toLowerCase())) {
                                System.out.println("Specie is out of season please try again");
                                continue;
                            }
                        }
                        break;
                    }

                    while (true) {
                        System.out.print("Please enter destination ID: ");
                        dID = input.nextLine();
                        if (iDToObjRef.get(dID) == null) {
                            System.out.println("Invalid ID please try again");
                            continue;
                        }
                        String dSpcies = iDToObjRef.get(dID).specie;
                        if (dSpcies != null) {
                            // Check if in season so it has edges
                            if (findCorrectHashSet(season).search(dSpcies.toLowerCase())) {
                                System.out.println("Specie is out of season please try again");
                                continue;
                            }
                        }
                        break;
                    }
                    graph.dijkstra(sID, dID, iDToObjRef);
                    break;

                case 3: // Visit the most hot spots with a limited time
                    String sIDThree = null;
                    while (true) {
                        System.out.print("Please enter starting ID: ");
                        sIDThree = input.nextLine();
                        if (iDToObjRef.get(sIDThree) == null) {
                            System.out.println("Invalid ID please try again");
                            continue;
                        }
                        String sSpcies = iDToObjRef.get(sIDThree).specie;
                        if (sSpcies != null) {
                            // Check if in season so it has edges
                            if (findCorrectHashSet(season).search(sSpcies.toLowerCase())) {
                                System.out.println("Specie is out of season please try again");
                                continue;
                            }
                        }
                        break;
                    }

                    int limitedHours = -1;
                    while (true) {
                        System.out.print("Please enter hours available: ");
                        if (!input.hasNextInt()) {
                            System.out.println("Please enter an integer");
                            input.nextLine();
                            continue;
                        }
                        limitedHours = input.nextInt();
                        input.nextLine();
                        break;
                    }

                    graph.greedy(sIDThree, limitedHours, iDToObjRef);

                    break;

                case 4: // Check if specie is in season using hashSet
                    String searchName = "";
                    while (true) {
                        System.out.print("Please enter a specie's name: ");
                        searchName = input.nextLine().trim();
                        if (searchName.equals("")) {
                            System.out.println("Invalid specie's name please try again");
                            continue;
                        }
                        break;
                    }

                    if (findCorrectHashSet(season).search(searchName.toLowerCase()))
                        System.out.println(searchName + " is out of season.\n");

                    else
                        System.out.println(searchName + " is in season.\n");

                    break;

                case 5: // Display all out of season species
                    ArrayList<String> arrListFIve = findCorrectHashSet(season).dumpContents();
                    if (arrListFIve == null) {
                        System.out.println("No species are out of season");
                        break;
                    }
                    System.out.println("Out of season specie(s): ");
                    for (String strFive : arrListFIve) {
                        if (strFive != null) {
                            System.out.println(firstCapRestLower(strFive));
                        }
                    }
                    System.out.println();
                    break;

                case 6: // Search for a hotspot by ID
                    System.out.print("Please enter an ID: ");
                    String iDSix = input.nextLine().toUpperCase();

                    HotSpot resultA = iDToObjRef.get(iDSix);
                    // result exists
                    if (resultA != null) {
                        System.out.println(resultA);
                        System.out.println();
                    } else {
                        System.out.println("No hot spot found\n");
                    }
                    break;

                case 7: // Search for a hotspot by species
                    String specieSeven = "";
                    while (true) {
                        System.out.print("Please enter a specie: ");
                        specieSeven = input.nextLine().trim().toLowerCase();
                        if (specieSeven.equals("")) {
                            System.out.println("Invalid specie please try again");
                            continue;
                        }
                        break;
                    }

                    boolean resultFoundSeven = false;
                    // Find all hot spots with specie
                    for (int i = 0; i < allHotSpots.size(); i++) {
                        String compairSpecies = allHotSpots.get(i).specie;
                        if (compairSpecies == null)
                            continue;
                        if (compairSpecies.equals(specieSeven)) {
                            System.out.println(allHotSpots.get(i));
                            System.out.println();
                            resultFoundSeven = true;
                        }
                    }
                    if (!resultFoundSeven)
                        System.out.println("No hotspot with that specie was found\n");
                    break;

                case 8: // Search for a hotspot by location
                    String locationEight = "";
                    while (true) {
                        System.out.print("Please enter a location: ");
                        locationEight = input.nextLine().trim().toLowerCase();
                        if (locationEight.equals("")) {
                            System.out.println("Invalid location please try again");
                            continue;
                        }
                        break;
                    }

                    boolean resultFoundEight = false;
                    // Find all hot spots with location
                    for (int i = 0; i < allHotSpots.size(); i++) {
                        String compairLocation = allHotSpots.get(i).location;
                        if (compairLocation.equals(locationEight)) {
                            System.out.println(allHotSpots.get(i));
                            System.out.println();
                            resultFoundEight = true;
                        }
                    }
                    if (!resultFoundEight)
                        System.out.println("No hotspot with that location was found\n");
                    break;

                case 9: // Display all species with restrictions
                    Boolean resultFoundNine = false;
                    for (int i = 0; i < allHotSpots.size(); i++) {
                        if (allHotSpots.get(i).bagLimit != null || allHotSpots.get(i).minSize != null) {
                            System.out.println(allHotSpots.get(i));
                            System.out.println();
                            resultFoundNine = true;
                        }
                    }
                    if (!resultFoundNine)
                        System.out.println("No hotspot with restrictions was found\n");
                    break;

                case 10:// Display current weather in Santa Cruz
                    System.out.println("Santa Cruz Weather Report");
                    System.out.println("Weather: " + SantaCruzWeather.getWeather() + "\nTemperature: "
                            + SantaCruzWeather.getTemperature() + "F" + "\nSwell: "
                            + SantaCruzWeather.getSwell() + " ft" + "\nWind Speed: " + SantaCruzWeather.getWind()
                            + "kts\n");
                    break;

                case 0:// Exit program
                    System.out.print("Exiting");
                    input.close();
                    System.exit(0);
                    break;

            }
        }

        // TODO:
    }

    public static HashSet findCorrectHashSet(String season) {
        if (season.equals("spring"))
            return outOfSeasonSpring;
        else if (season.equals("summer"))
            return outOfSeasonSummer;
        else if (season.equals("fall"))
            return outOfSeasonFall;
        else if (season.equals("winter"))
            return outOfSeasonWinter;

        return null;
    }

    public static void importHotSpotData() throws Exception {
        Path pathReq = Path.of("HotSpotCSV.csv");
        try (BufferedReader reader = Files.newBufferedReader(pathReq)) {
            /* This eats the header */
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] col = line.split(",");
                String iD = col[0].trim();
                String specie = col[1].toLowerCase().trim().equals("null") ? null : col[1].trim().toLowerCase();
                String location = col[2].trim().toLowerCase();
                String outOfSeason = col[3].toLowerCase().trim().equals("null") ? null : col[3].trim().toLowerCase();
                Double bagLimit = col[4].toLowerCase().trim().equals("null") ? null : Double.parseDouble(col[4].trim());
                Double minSize = col[5].toLowerCase().trim().equals("null") ? null : Double.parseDouble(col[5].trim());

                if (outOfSeason != null && specie != null)
                    findCorrectHashSet(outOfSeason).put(specie);

                HotSpot temp = new HotSpot(iD, specie, location, outOfSeason, bagLimit, minSize);
                allHotSpots.addLast(temp);
                iDToObjRef.insert(iD, temp);
                graph.addVertex(iD);
            }
        }
        System.out.println("Success! Loaded Marine Resource CSV");
    }

    public static void importEdges(String currSeason) throws Exception {
        Path pathReq = Path.of("EdgesCSV.csv");
        try (BufferedReader reader = Files.newBufferedReader(pathReq)) {
            /* This eats the header */
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] col = line.split(",");
                String sourceID = col[0].trim();
                String destID = col[1].trim();
                int weight = Integer.parseInt(col[2].trim());

                HotSpot source = iDToObjRef.get(sourceID);
                HotSpot dest = iDToObjRef.get(destID);

                // Only build a link if both source and dest's species are in season
                if ((source.outOfSeason == null || !findCorrectHashSet(currSeason).search(source.specie))
                        && (dest.outOfSeason == null || !findCorrectHashSet(currSeason).search(dest.specie))) {
                    // Bad weather, increase cost of further out to sea species
                    if (SantaCruzWeather.getWind() >= 10 || SantaCruzWeather.getSwell() >= 8) {
                        if (source.specie != null) {
                            if (source.specie.equals("salmon") || source.specie.equals("pacific halibut")
                                    || source.specie.equals("california halibut")
                                    || source.specie.equals("yellow fin tuna") || source.specie.equals("blue fin tuna"))
                                weight *= 2;
                        }
                    }
                    graph.addEdge(source.iD, dest.iD, weight);
                }
            }
            System.out.println("Success! Loaded Links CSV");
        }
    }

    public static String firstCapRestLower(String base) {
        // dumb input check
        if (base == null)
            return null;
        base = base.toLowerCase().trim();
        // empty string anti crash check
        if (base.equals(""))
            return null;
        // Capitalize first letter for each word
        String[] col = base.split("\\s+");
        for (int i = 0; i < col.length; i++) {
            col[i] = col[i].substring(0, 1).toUpperCase()
                    + col[i].substring(1).toLowerCase();
        }
        // Reassemble target string
        String formatedBase = "";
        for (int i = 0; i < col.length; i++) {
            if (i > 0)
                formatedBase = formatedBase + " ";

            formatedBase = formatedBase + col[i];
        }
        return formatedBase;
    }
}

// Main data class Id, specie or null, location, outOfSeason or null, bag limit
// or null, minSize or null
class HotSpot {
    String iD;
    String specie;
    String location;
    String outOfSeason;
    Double bagLimit;
    Double minSize;

    HotSpot(String iD, String specie, String location, String outOfSeason, Double bagLimit, Double minSize) {
        this.iD = iD;
        this.specie = specie;
        this.location = location;
        this.outOfSeason = outOfSeason;
        this.bagLimit = bagLimit;
        this.minSize = minSize;
    }

    public String toString() {
        String displaySpecie = Halpern_Aidan_Final.firstCapRestLower(specie);
        String displayLocation = Halpern_Aidan_Final.firstCapRestLower(location);

        return "ID: " + iD + "\nSpecie: " + displaySpecie + "\nLocation: " + displayLocation + "\nOut of season: "
                + outOfSeason
                + "\nBag limit: " + bagLimit + "\nMinimum Size in inches: " + minSize;
    }

}

class SantaCruzWeather {
    protected static String weather = null;
    protected static double swell = -1;
    protected static double wind = -1;
    protected static double temperature = -1;
    protected static boolean webScrapperOverride = false; // true = using hardcoded values | false = using web scrapper
                                                         // values

    private static void updateWeather() {
        if (swell < 0 && wind < 0 && temperature < 0 && weather == null) {
            if (webScrapperOverride) {
                swell = 8;
                wind = 10;
                temperature = 61.2;
                weather = "Sunny";
            } else {
                landData("https://forecast.weather.gov/MapClick.php?lat=36.9741&lon=-122.0308");
                swellData("https://www.ndbc.noaa.gov/station_page.php?station=46269");
                windData("https://www.ndbc.noaa.gov/station_page.php?station=46042");

                if (swell == 8) {
                    System.err.println("Calling fall back buoy");
                    swellData("https://www.ndbc.noaa.gov/station_page.php?station=46236");
                }
            }
        }
    }

    private static void landData(String url) {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)").get();

            // Extract general weather
            Element conditionEl = doc.selectFirst(".myforecast-current");
            if (conditionEl != null && !conditionEl.text().equals("NA"))
                weather = conditionEl.text();

            else
                weather = "Sunny";

            // Extract air temperature
            Element tempEl = doc.selectFirst(".myforecast-current-lrg");
            if (tempEl != null) {
                String temp = tempEl.text().replaceAll("[^\\d.]", "");
                temperature = temp.isEmpty() ? 61.2 : Double.parseDouble(temp);
            } else
                temperature = 61.2;

        } catch (Exception e) {
            System.err.println("Land data fall back");
            weather = "Sunny";
            temperature = 61.2;
        }
    }

    private static void swellData(String url) {
        try {
            // Warning time out is 10 seconds
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();
            String swellStr = getTableDataSwell(doc, "Significant Wave Height");

            if (swellStr != null && !swellStr.equals("MM")) {
                String clean = swellStr.replaceAll("[^\\d.]", "");
                if (!clean.isEmpty()) {
                    swell = Double.parseDouble(clean);
                    return;
                }
            }
            // fall back
            System.err.println("Swell data fall back");
            swell = 8;
        } catch (Exception e) {
            System.err.println("Swell data fall back");
            swell = 8;
        }
    }

    private static void windData(String url) {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
            String windStr = getTableDataWind(doc, "Wind Speed");

            if (windStr != null) {
                windStr = windStr.replaceAll("[^\\d.]", "");
                wind = windStr.isEmpty() ? 10 : Double.parseDouble(windStr);
            } else {
                wind = 10;
            }
        } catch (Exception e) {
            System.err.println("Wind data fall back");
            wind = 10;
        }
    }

    // Helper method for the Wind tables
    private static String getTableDataWind(Document doc, String label) {
        Element labelCell = doc.getElementsContainingOwnText(label).first();
        if (labelCell != null) {
            Element dataCell = labelCell.nextElementSibling();
            if (dataCell != null) {
                return dataCell.text().trim();
            }
        }
        return null;
    }

    // Help merthod for the Swell Tables
    private static String getTableDataSwell(Document doc, String label) {
        // 1. Find the table row (tr) that contains your label (e.g., "WVHT" or "Wave
        // Height")
        // We use lowerCase to make the search case-insensitive
        for (Element row : doc.select("tr")) {
            if (row.text().toLowerCase().contains(label.toLowerCase())) {
                // 2. Once we find the right row, look at all cells (td) in that row
                Elements cells = row.select("td");
                for (Element cell : cells) {
                    String cellText = cell.text().trim();
                    // 3. Find the cell that actually contains a number
                    if (cellText.matches(".*\\d.*") && !cellText.contains(label)) {
                        return cellText;
                    }
                }
            }
        }
        return null;
    }

    public static double getSwell() {
        updateWeather();
        return swell;
    }

    public static double getWind() {
        updateWeather();
        return wind;
    }

    public static String getWeather() {
        updateWeather();
        return weather;
    }

    public static double getTemperature() {
        updateWeather();
        return temperature;
    }

}

class GraphAdjList<T> {
    private class Edge implements Comparable<Edge> {
        String dest;
        int weight;

        public Edge(String dest, int weight) {
            this.dest = dest;
            this.weight = weight;
        }

        public int compareTo(Edge b) {
            return this.weight - b.weight;
        }
    }

    // data structures to represent the adjacency list
    int sizeEdge;
    int sizeVertex;
    HashTable<ArrayList<Edge>> adjlist;

    // constructor with whatever you need
    public GraphAdjList() {
        adjlist = new HashTable<>();

    }

    public void addVertex(String vertex) {
        if (adjlist.get(vertex) == null) {
            ArrayList<Edge> temp = new ArrayList<>();
            adjlist.insert(vertex, temp);
            sizeVertex++;
        }
    }

    public void addEdge(String source, String dest, int weight) {
        // Ensures that the soruce vertext exists
        addVertex(source);
        // Ensures that the dest vertext exists
        addVertex(dest);
        // Adds Edge
        adjlist.get(source).add(new Edge(dest, weight));
        // Undirected Graph
        adjlist.get(dest).add(new Edge(source, weight));
        sizeEdge++;

    }

    public void dijkstra(String startId, String targetId, HashTable<HotSpot> iDToObjRef) {
        // Lowest weight from id to start
        HashTable<Integer> distanceFromStart = new HashTable();
        // Stores Id and current cumulative weight
        PriorityQueue<Edge> next = new PriorityQueue<>();
        // tracks child ID -> parent ID
        HashTable<String> traceID = new HashTable();
        // tracks ID -> weight
        HashTable<String> traceWeight = new HashTable();
        // Flag if path is found or not
        Boolean foundPath = false;

        // Initalize
        distanceFromStart.insert(startId, 0);
        next.add(new Edge(startId, 0));

        while (!next.isEmpty()) {
            Edge curr = next.poll();
            String currID = curr.dest;

            // Found target
            if (currID.equals(targetId)) {
                printPath(traceID, traceWeight, currID, iDToObjRef);
                foundPath = true;
                break;
            }

            // optimization - if a better path has already been found skip
            if (curr.weight > distanceFromStart.get(currID))
                continue;

            ArrayList<Edge> neighbors = adjlist.get(currID);
            for (Edge neighboreEdge : neighbors) {
                String neighborID = neighboreEdge.dest;
                int neighborWeight = neighboreEdge.weight;

                int neighborNewDistance = neighborWeight + distanceFromStart.get(currID);

                // If a new shorter path is found update
                Integer neighborOldBest = distanceFromStart.get(neighborID);
                if (neighborOldBest == null || neighborNewDistance < neighborOldBest) {
                    distanceFromStart.insert(neighborID, neighborNewDistance);
                    // New edge object with new weight to get PQ to work correctly
                    next.add(new Edge(neighborID, neighborNewDistance));
                    traceID.insert(neighborID, currID);
                    traceWeight.insert(neighborID, neighborWeight + "");
                }
            }

        }
        if (!foundPath) {
            System.out.println("No path found\n");
        }
    }

    public void dijkstraSpecie(String startId, String targetSpecie, HashTable<HotSpot> iDToObjRef) {
        // Lowest weight from id to start
        HashTable<Integer> distanceFromStart = new HashTable();
        // Stores Id and current cumulative weight
        PriorityQueue<Edge> next = new PriorityQueue<>();
        // tracks child -> parent
        HashTable<String> traceID = new HashTable();
        // tracks ID -> weight
        HashTable<String> traceWeight = new HashTable();
        // Flag if path is found or not
        Boolean foundPath = false;

        // Initalize
        distanceFromStart.insert(startId, 0);
        next.add(new Edge(startId, 0));

        while (!next.isEmpty()) {
            Edge curr = next.poll();
            String currID = curr.dest;

            // Found target
            String currSpecie = iDToObjRef.get(currID).specie;
            // Ignore beach entry point with no specie
            if (currSpecie != null) {
                if (currSpecie.equals(targetSpecie)) {
                    printPath(traceID, traceWeight, currID, iDToObjRef);
                    foundPath = true;
                }
            }

            // optimization - if a better path has already been found skip
            if (curr.weight > distanceFromStart.get(currID))
                continue;

            ArrayList<Edge> neighbors = adjlist.get(currID);
            for (Edge neighboreEdge : neighbors) {
                String neighborID = neighboreEdge.dest;
                int neighborWeight = neighboreEdge.weight;

                int neighborNewDistance = neighborWeight + distanceFromStart.get(currID);

                // If a new shorter path is found update
                Integer neighborOldBest = distanceFromStart.get(neighborID);
                if (neighborOldBest == null || neighborNewDistance < neighborOldBest) {
                    distanceFromStart.insert(neighborID, neighborNewDistance);
                    // New edge object with new weight to get PQ to work correctly
                    next.add(new Edge(neighborID, neighborNewDistance));
                    traceID.insert(neighborID, currID);
                    traceWeight.insert(neighborID, neighborWeight + "");
                }
            }

        }
        if (!foundPath) {
            System.out.println("No path found\n");
        }
    }

    public void greedy(String startId, int limitedHours, HashTable<HotSpot> iDToObjRef) {
        HashSet visited = new HashSet();
        HashTable<String> traceID = new HashTable();
        HashTable<String> traceWeight = new HashTable();

        visited.put(startId);
        traceID.insert(startId, null);
        String currID = startId;

        while (true) {
            // Implmented best pracitce to not alter orignal data strucure
            ArrayList<Edge> nullPointerCrashCheck = adjlist.get(currID);
            // Will still print a partial path
            if (nullPointerCrashCheck == null) {
                System.err.println("Edge arrList for " + currID + " does not exist - graph is broken");
                break;
            }
            // copy arrList
            ArrayList<Edge> neighbors = new ArrayList<>(nullPointerCrashCheck);
            // Dead end check
            if (neighbors.isEmpty())
                break;
            // sort for greed
            Collections.sort(neighbors);

            // flag if done
            Boolean finished = false;

            for (Edge edge : neighbors)
                if (!visited.search(edge.dest) && limitedHours - edge.weight >= 0) {
                    visited.put(edge.dest);
                    traceID.insert(edge.dest, currID);
                    traceWeight.insert(edge.dest, edge.weight + "");

                    finished = true;
                    limitedHours -= edge.weight;
                    currID = edge.dest;
                    break;
                }
            // if false hit dead end and its time to print path
            if (!finished)
                break;
        }
        printPath(traceID, traceWeight, currID, iDToObjRef);
    }

    public void printPath(HashTable<String> traceID, HashTable<String> traceWeight, String currID,
            HashTable<HotSpot> iDToObjRef) {
        ArrayList<String> iD = new ArrayList<>();
        // Tranfer path from hashTable to arrayList
        while (currID != null) {
            iD.add(currID);
            currID = traceID.get(currID);
        }
        // Reverse so path prints correctly
        Collections.reverse(iD);
        // Print path
        for (int i = 0; i < iD.size(); i++) {
            String printID = iD.get(i);
            String currSpecie = null;
            if (iDToObjRef.get(printID) != null)
                currSpecie = iDToObjRef.get(printID).specie;
            if (currSpecie != null)
                System.out.print(currSpecie + " ID: " + printID);
            else if (printID.equals("B0"))
                System.out.print("Beach ID: " + printID);
            else
                System.out.print("Unknown specie ID: " + printID);
            if (i + 1 < iD.size()) {
                System.out.print(" " + traceWeight.get(iD.get(i + 1)) + " hour(s) --> ");
            }
            if (i != 0 && i % 4 == 0) {
                System.out.println();
            }
        }
        System.out.println("\n");
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

class HashSet {
    private int size;
    private HTEntry[] table;
    private double loadFactor;

    HashSet() {
        table = new HTEntry[16];
        size = 0;
    }

    HashSet(int capacity) {
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
        HTEntry[] oldTable = table;
        table = new HTEntry[oldTable.length * 2];
        loadFactor = 0;
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null && !oldTable[i].deleted) {
                put(oldTable[i].key);
            }
        }
    }

    // add new hash table entry
    public void put(String key) {
        // transfers to bigger hash table
        if (loadFactor > 0.7) {
            rehash();
        }

        int index1 = hash(key);
        int index2 = hash2(key);
        int startIndex = index1;
        int i = 1;

        while (table[index1] != null) {
            // Reacctvate old nodes
            if (table[index1].key.equals(key)) {
                if (table[index1].deleted) {
                    table[index1].deleted = false;
                    size++;
                    return;
                } else
                    return;
            }

            // Double hashing
            index1 = (startIndex + i * index2) % table.length;
            i++;

            // all spots checked no space left must increase table size
            if (index1 == startIndex) {
                rehash();
                put(key);
                return;
            }
        }
        // insert into table
        table[index1] = new HTEntry(key);
        size++;
        loadFactor = ((double) size / table.length);
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

    public boolean search(String key) {
        int index1 = hash(key);
        int index2 = hash2(key);
        int startIndex = index1;
        int i = 1;

        while (table[index1] != null) {
            // key is found and data is set to deleated
            if (!table[index1].deleted && table[index1].key.equals(key)) {
                return true;
            }

            // Double hashing
            index1 = (startIndex + i * index2) % table.length;
            i++;

            // all spots checked key not found
            if (index1 == startIndex) {
                break;
            }
        }
        return false;
    }

    public void printAll() {
        for (HTEntry temp : table) {
            if (temp != null && !temp.deleted)
                System.out.println(temp.key);
        }
    }

    public ArrayList<String> dumpContents() {
        ArrayList<String> arrList = new ArrayList<>();
        for (HTEntry temp : table) {
            if (temp != null && !temp.deleted)
                arrList.add(temp.key);
        }
        if (arrList.isEmpty())
            return null;

        // Normal return
        return arrList;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    class HTEntry {
        String key;
        Boolean deleted;

        public HTEntry(String key) {
            this.key = key;
            this.deleted = false;
        }
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

class Stack<T> {
    DoubleLinkedList<T> list;

    Stack() {
        list = new DoubleLinkedList<>();
    }

    public void push(T data) {
        list.addLast(data);
    }

    public T peek() {
        return list.getLast();
    }

    public T pop() {
        T temp = list.getLast();
        list.removeLast();
        return temp;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}