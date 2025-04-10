
package de.daslaboratorium.machinelearning.classifier.data;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataHelper {

    public static List<DataModel> loadData(String filePath, String fileType, String delimiter, char separator) {
        if ("csv".equalsIgnoreCase(fileType)) {
            return loadDataFromCSV(filePath, separator);
        } else if ("txt".equalsIgnoreCase(fileType)) {
            return loadDataFromTXT(filePath, delimiter);
        } else {
            System.err.println("Jenis file tidak valid. Gunakan 'csv' atau 'txt'.");
            return new ArrayList<>(); // Mengembalikan daftar kosong jika jenis file tidak valid
        }
    }

    private static List<DataModel> loadDataFromTXT(String filePath, String delimiter) {
        List<DataModel> data = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            // Lewati baris header (jika ada)
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(delimiter);

                if (values.length == 5) {
                    DataModel row = new DataModel(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim());
                    data.add(row);
                } else {
                    System.err.println("Skipping line due to incorrect number of fields: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error loading data from TXT: " + e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

    private static List<DataModel> loadDataFromCSV(String filePath, char separator) {
        List<DataModel> data = new ArrayList<>();
        try (FileReader fileReader = new FileReader(filePath)) {
            CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
            CSVReader csvReader = new CSVReaderBuilder(fileReader)
                    .withCSVParser(parser)
                    .withSkipLines(1) // Skip header row
                    .build();

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                DataModel row = parseCsvLine(line);
                if (row != null) {
                    data.add(row);
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error loading data from CSV: " + e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

    private static DataModel parseCsvLine(String[] line) {
        if (line != null && line.length == 5) {
            return new DataModel(line[0].trim(), line[1].trim(), line[2].trim(), line[3].trim(), line[4].trim());
        } else {
            System.err.println("Skipping line due to incorrect number of fields: " + String.join(",", line));
            return null;
        }
    }
}