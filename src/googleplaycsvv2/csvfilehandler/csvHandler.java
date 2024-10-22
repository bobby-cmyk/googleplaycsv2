package googleplaycsvv2.csvfilehandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.FileNotFoundException;

public class CsvHandler {

    //arraylist of apps
    private ArrayList<App> apps;
    
    public CsvHandler() {
        this.apps = new ArrayList<>();
    }

    public void read(String csvPath) throws FileNotFoundException, IOException{

        FileReader fr = new FileReader(csvPath);
        BufferedReader br = new BufferedReader(fr);

        // Skip header line
        br.readLine();
        // Read line by line of the csv file
        while (true) {
            
            String line = br.readLine();

            // At the end of the file, close the buffered reader and break the loop
            if (line == null) {
                br.close();
                break;
            }

            // Split by this regex, split by comma (Note: to solve tricky situations where "Hi", "Hi, there", "How are you?", "Doing fine, how about you?")
            String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

            String name = parts[0];

            String category = parts[1];

            String ratingString = parts[2];

            double rating = 0;

            // Apps without any rating will be assigned "-1" 
            if (ratingString.equals("NaN")) {
                rating = -1; 
            }
            else {
                rating = Double.parseDouble(ratingString);
            }

            apps.add(new App(name, category, rating));
        }
    }

    private Set<String> getCategoriesSet() {
        
        return apps.stream()
                    .map(a -> a.getCategory())
                    .map(c -> c.toLowerCase())
                    .collect(Collectors.toSet());
    }

    public String getCategories() {

        return this.getCategoriesSet().stream()
                                    .sorted((a1, a2) -> a1.compareTo(a2))
                                    .map(c -> "* " + c)
                                    .collect(Collectors.joining("\n"));
    }

    public boolean isValidCategory(String category) {
        return this.getCategoriesSet().contains(category);
    }

    public String getMax(String category) {

        App maxApp = apps.stream()
                        .filter(a -> a.getCategory().toLowerCase().equals(category))
                        .filter(y -> y.getRating() >= 0)
                        .max(Comparator.comparing(r -> r.getRating()))
                        .orElse(null);
        
        if (maxApp == null) {
            return "No apps found in this category";
        }

        return "Name: %s, Rating: %.2f".formatted(maxApp.getName(), maxApp.getRating());
    }
    
    public String getMin(String category) {
        App minApp = apps.stream()
                        .filter(x -> x.getCategory().toLowerCase().equals(category))
                        .filter(y -> y.getRating() >= 0)
                        .min(Comparator.comparing(r -> r.getRating()))
                        .orElse(null);

        return "Name: %s, Rating: %.2f".formatted(minApp.getName(), minApp.getRating());
    }

    public String getAvg(String category) {
        double average = apps.stream()
                            .filter(x -> x.getCategory().toLowerCase().equals(category))
                            .filter(y -> y.getRating() >= 0)
                            .mapToDouble(a -> a.getRating())
                            .average()
                            .orElse(0);

            return "Average Rating of %s: %.2f".formatted(category, average);
        }

} 

