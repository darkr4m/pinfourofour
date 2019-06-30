package com.jtv.pinfourofour.utils.services;

import com.jtv.pinfourofour.models.JMap;
import com.jtv.pinfourofour.models.pin.JPin;
import com.jtv.pinfourofour.models.pin.JPinDatabaseDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.jtv.pinfourofour.models.CSVHeaders.*;

public class CSVService {
    private static final CSVFormat format = CSVFormat.DEFAULT
            .withFirstRecordAsHeader()
            .withIgnoreHeaderCase(true)
            .withAllowMissingColumnNames(true)
            .withAutoFlush(true).withTrim(true);


    private static Reader readFile(String fileName) {
        try {
            Reader reader = new FileReader(fileName);
            return reader;
        } catch (FileNotFoundException e) {
            System.out.println("The file " + fileName + " could not found. " + e.getMessage());
            return null;
        }
    }

    /**
     * <b>importFromCSV</b>
     * Imports records from a CSV file.
     *
     * @return List of JPins
     */
    public List<JPinDatabaseDTO> importFromCSV(String fileName) {
        List<JPinDatabaseDTO> JPins = new ArrayList<>();
        try {
            if (readFile(fileName) != null) {
                Iterable<CSVRecord> records = format.parse(readFile(fileName));
                for (CSVRecord record : records) {
                    String pinID = record.get(PIN_ID);
                    String link = record.get(LINK);
                    String board = record.get(BOARD);
                    String note = record.get(NOTE);
                    int linkResponseCode = Integer.parseInt(record.get(LINK_RESPONSE_CODE));
                    String linkRedirectLocation = record.get(LINK_REDIRECT_LOCATION);
                    int redirectLocationResponseCode = Integer.parseInt(record.get(LINK_REDIRECT_RESPONSE_CODE));
                    String action = record.get(ACTION);
                    JPinDatabaseDTO dto = new JPinDatabaseDTO(pinID,link,board,note,linkResponseCode,linkRedirectLocation,redirectLocationResponseCode,action);
                    JPins.add(dto);
                }
            }
            return JPins;
        } catch (IOException e) {
            System.out.println("Could not parse the file " + fileName + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * <b>csvExport</b>
     * Exports the JPins from a query result to a CSV file.
     */
    public boolean csvExport(String fileName, ResultSet results) {
        try (CSVPrinter printer = CSVFormat.DEFAULT.withHeader(results).withAutoFlush(true).print(new FileWriter(fileName))) {
            printer.printRecord(results);
            return true;
        } catch (Exception e) {
            System.out.println("Could not write results to file: " + e.getMessage());
            return false;
        }
    }
}
