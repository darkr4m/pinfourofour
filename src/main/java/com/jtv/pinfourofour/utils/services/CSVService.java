package com.jtv.pinfourofour.utils.services;

import com.jtv.pinfourofour.models.CSVHeaders;
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
    private static final CSVFormat format = CSVFormat.RFC4180
            .withFirstRecordAsHeader()
            .withIgnoreHeaderCase(true)
            .withAllowMissingColumnNames(true)
            .withAutoFlush(true);


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
                    pinID = pinID.substring(0,17); //removes the char at the end.
                    String board = record.get(BOARD);
                    String link = record.get(LINK);
                    String note = record.get(NOTE);
                    int linkResponseCode = Integer.parseInt(record.get(LINK_RESPONSE_CODE));
                    String linkRedirectLocation = record.get(LINK_REDIRECT_LOCATION);
                    int redirectLocationResponseCode = Integer.parseInt(record.get(LINK_REDIRECT_RESPONSE_CODE));
                    String action = record.get(ACTION);
                    JPinDatabaseDTO dto = new JPinDatabaseDTO(pinID,board,link,note,linkResponseCode,linkRedirectLocation,redirectLocationResponseCode,action);
                    JPins.add(dto);
                }
            }
            return JPins;
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not parse the file " + fileName + ": " + e.getMessage());
            return JPins;
        }
    }

    /**
     * <b>csvExport</b>
     * Exports the JPins from a list obtained from a query result to a CSV file.
     */
    public boolean csvExport(String fileName, List<JPinDatabaseDTO> dtoList) {
        try (CSVPrinter printer = CSVFormat.RFC4180.withHeader(CSVHeaders.class)
                .withIgnoreHeaderCase(true)
                .withAllowMissingColumnNames(true)
                .withAutoFlush(true)
                .print(new FileWriter(fileName))) {

            for(JPinDatabaseDTO dto : dtoList){
                printer.printRecord(dto.getPinId()+"p",
                        dto.getBoard(), dto.getLink(), dto.getNote(), dto.getLinkResponseCode(),
                        dto.getLinkRedirectLocation(), dto.getLinkRedirectionResponseCode(), dto.getAction());
                printer.flush();
            }
            return true;
        } catch (Exception e) {
            System.out.println("Could not write results to file: " + e.getMessage());
            return false;
        }
    }
}
