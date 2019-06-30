package com.jtv.pinfourofour.models;

import com.jtv.pinfourofour.models.pin.JPin;
import com.jtv.pinfourofour.models.pin.JPinDTO;
import com.jtv.pinfourofour.utils.builders.pin.JPinDTOBuilder;
import com.jtv.pinfourofour.utils.services.NetworkService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jtv.pinfourofour.models.CSVHeaders.*;

/** <b>JMap</b>
 * This class is designed to be a holder for <b>JPins</b>.
 *
 * @see JPin
 */
public class JMap {
    /**
     * LinkedHashMap <b>JPins</b> -
     * Key - String - The pinID of the JPin
     * Value - JPin - The actual JPin object
     */
    private LinkedHashMap<String, JPin> JPins = new LinkedHashMap<>();

    /** <b>JMap Constructor</b>
     *  Just prints an instantiation confirmation to the console.
     */
//    public JMap() {
//        System.out.println("New JMap created.");
//    }

    /**<b>getJPins</b>
     * Invoking this method prints out a nice list of pins in the console and returns a LinkedHashMap of JPins.
     *
     * @return - JPins - A LinkedHashMap of JPins
     */
    public LinkedHashMap<String, JPin> getJPins() {
        JPins.forEach((k,v) ->  System.out.println(k+" "+v.getLink()));
        return JPins;
    }

    /**<b>createJPin</b>
     * TODO: doc
     *
     * @param pinID
     * @param creator
     * @param board
     * @param note
     * @param link
     * @return
     */
    public static JPin createJPin(String pinID, String creator, String board, String note, String link){
        JPin jPin = new JPin (pinID, creator, board, note, link);
        return jPin;
    }

    /**<b>add</b>
     * Adds a JPin to the JMap.
     * Checks if the JPin already exists in the JMap.
     * Prints confirmation to the console.
     *
     * @param jPin Add a JPin Object containing information about a pin from Pinterest.
     */
    public void add(@NotNull JPin jPin) {
        String pinID = jPin.getPinID();
        if(!JPins.keySet().contains(pinID)){
            JPins.put(jPin.getPinID(), jPin);
            System.out.println("JPin added.");
        } else {
            System.out.println("A JPin with the id "+pinID+" already exists.");
        }
    }

    /**<b>remove</b> - by pinID only
     * Checks if the JPin exists in the JMap.
     * Removes a JPin from the JMap.
     * Prints confirmation to the console.
     *
     * @param pinID - The pinID as a string.
     */
    public void remove(String pinID){
        if(JPins.keySet().contains(pinID)) {
            JPins.remove(pinID);
            System.out.println("Pin " + pinID + " removed.");
        } else {
            System.out.println("Pin not found.");
        }
    }

    /**<b>remove</b> - JPin
     * Checks if the JPin exists in the JMap.
     * Removes a JPin from the JMap.
     * Prints confirmation to the console.
     *
     * @param jPin - The JPin object to be removed.
     */
    public void remove(@NotNull JPin jPin) {
        String pinID = jPin.getPinID();
        if(JPins.keySet().contains(pinID)) {
            JPins.remove(pinID);
            System.out.println("Pin "+pinID+" removed.");
        } else {
            System.out.println("Pin"+pinID+" not found.");
        }
    }

    /**<b>count</b>
     * Returns a count of records - JPins - in the JMap.
     * Prints the count to the console
     *
     * @return size - Return an integer number of JPins in the JMap
     */
    public int count(){
        int size = JPins.keySet().size();
        System.out.println(size);
        return size;
    }


    /**<b>csvImport</b>
     * Imports contents from a CSV file. Parses records into JPins and adds each to JMap.
     *
     */
    public void csvImport(String fileName){
        try (Reader reader = new FileReader (fileName)){
            CSVParser parser = new CSVParser (reader, CSVFormat.DEFAULT.withFirstRecordAsHeader ().withHeader (CSVHeaders.class).withAllowMissingColumnNames(true));
            for (CSVRecord record : parser){
                String pinID = record.get(PIN_ID);
                String board = record.get (BOARD);
                String link = record.get(LINK);
                String creator = record.get (CREATOR);
                String note = record.get(NOTE);
            }
        } catch (Exception e){
            System.err.println(e.getMessage());
        }

    }

    /**<b>csvExport</b>
     * Exports the JPins from this JMap to a CSV file. Parses JPins into records and and writes to a CSV file in the <i>Reports</i> directory.
     *
     */
    public void csvExport(String dirName, String fileName){
        File dir = new File (dirName);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern ("MM_dd_hh-mm");
        String date = formatter.format(LocalDateTime.now());
        String outfile = date+fileName+".csv";
        dir.mkdir ();
        try(CSVPrinter printer = new CSVPrinter(new FileWriter (dir+File.separator+outfile), CSVFormat.DEFAULT.withHeader (CSVHeaders.class))){
            JPins.forEach ((k, v) -> {
                try {
                    printer.printRecord(v.getPinID(), v.getBoard(), v.getLink(), v.getCreator (), v.getNote());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch(Exception e){
            System.err.println("The file "+dir+File.separator+outfile+" could not be written.");
            System.exit(0);
        }
    }
}
