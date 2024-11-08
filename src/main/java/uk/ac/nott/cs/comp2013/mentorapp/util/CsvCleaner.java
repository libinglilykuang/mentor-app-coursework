package uk.ac.nott.cs.comp2013.mentorapp.util;

import java.io.*;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class CsvCleaner {
    public static void cleanCsv(String inputFilePath, String outputFilePath) {
        try (CSVReader reader = new CSVReader(new FileReader(inputFilePath));
             CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath))) {
            List<String[]> records = reader.readAll();
            writer.writeNext(records.get(0)); // Write header
            records.remove(0);

            for (String[] record : records) {
                if (record[0] == null || record[0].isEmpty()) {
                    continue;
                }
                String password = record[1];
                if (!isValidPassword(password)) {
                    continue;
                }
                writer.writeNext(record); // Write valid record
            }

            System.out.println("CSV cleaning completed. Output saved to: " + outputFilePath);

        } catch (IOException | CsvException e) {
            System.err.println("Error during CSV cleaning: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Validates if the password is valid.
     * A valid password does not contain ",", "\n", "\r\n" or spaces.
     *
     * @param password the password to validate
     * @return true if the password is valid, false otherwise
     */
    private static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return !password.contains(",") && !password.contains("\n") && !password.contains("\r") && !password.contains(" ");
    }

}
