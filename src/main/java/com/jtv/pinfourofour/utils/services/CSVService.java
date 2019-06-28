package com.jtv.pinfourofour.utils.services;

import com.jtv.pinfourofour.models.JMap;
import com.jtv.pinfourofour.models.pin.JPin;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.jtv.pinfourofour.models.CSVHeaders.*;

public class CSVService {
    private static final CSVFormat format = CSVFormat.DEFAULT
            .withFirstRecordAsHeader ()
            .withIgnoreHeaderCase (true)
            .withAllowMissingColumnNames (true)
            .withAutoFlush (true).withTrim (true);


    private static Reader readFile(String fileName){
        try{
            Reader reader = new FileReader (fileName);
            return reader;
        } catch (FileNotFoundException e) {
            System.out.println ("The file "+fileName+" could not found. "+e.getMessage ());
            return null;
        }
    }

    /**<b>importFromCSV</b>
     * Imports records from a CSV file.
     *
     * @return List of JPins
     */
    public List<JPin> importFromCSV(String fileName){
        List<JPin> JPins = new ArrayList<> ();
        try{
            if(readFile (fileName) != null) {
                Iterable<CSVRecord> records = format.parse (readFile (fileName));
                for (CSVRecord record : records) {
                    String pinID = record.get (PIN_ID);
                    String link = record.get (LINK);
                    String board = record.get (BOARD);
                    String creator = record.get (CREATOR);
                    String note = record.get (NOTE);
                    JPin jPin = JMap.createJPin (pinID, link, board, creator, note);
                    JPins.add (jPin);
                }
            }
            return JPins;
        } catch(IOException e) {
            System.out.println ("Could not parse the file "+fileName+": "+e.getMessage ());
            return null;
        }
    }

    /**<b>csvExport</b>
     * Exports the JPins from this JMap to a CSV file. Parses JPins into records and and writes to a CSV file in the <i>Reports</i> directory.
     *
     */
    public void csvExport(String fileName, JMap JPins){
        try(CSVPrinter printer = new CSVPrinter(new FileWriter (fileName), format)){

        } catch(Exception e){
            System.err.println("The file "+fileName+" could not be written.");
            System.exit(0);
        }
    }
}
