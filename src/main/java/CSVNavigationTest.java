/**
 * CONCEPT TEST: CSV File I/O Integration
 * * This script serves as a standalone proof-of-concept for data extraction.
 * It demonstrates how to utilize Java's Scanner class to read an external CSV file,
 * parse the rows, and isolate specific data points (Brand and Model) using string
 * manipulation (RegEx split) to prepare them for injection into a WebDriver instance.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CSVNavigationTest {
    public static void main(String[] args) throws FileNotFoundException {
        File csvFile = new File("track_list.csv");
        Scanner csvscanner = new Scanner(csvFile);
        while(csvscanner.hasNextLine())
        {
            String currentRow = csvscanner.nextLine();
            String dataParts[] = currentRow.split(",");
            String targetBrand = dataParts[0];
            String targetModel = dataParts[1];
            System.out.println("The Bot is preparing to search for: " + targetBrand + " " + targetModel);
        }
        csvscanner.close();
    }
}
