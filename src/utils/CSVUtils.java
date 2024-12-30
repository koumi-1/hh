package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
    public static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<String[]>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) data.add(line.split(","));
        } catch (IOException e) {
            System.out.printf("Error reading file: %s.\n", filePath);
            e.printStackTrace();
        }
        return data;
    }

    public static void writeCSV(String filePath, List<String[]> data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.printf("Error writing to file: %s.\n", filePath);
            e.printStackTrace();
        }
    }

}
