package com.jtv.pinfourofour.commands;

import com.jtv.pinfourofour.models.Datasource;
import com.jtv.pinfourofour.models.JMap;
import com.jtv.pinfourofour.models.pin.JPin;
import com.jtv.pinfourofour.models.pin.JPinDatabaseDTO;
import com.jtv.pinfourofour.utils.Configuration;
import com.jtv.pinfourofour.utils.PinterestIO;
import com.jtv.pinfourofour.utils.services.CSVService;
import com.jtv.pinfourofour.utils.services.NetworkService;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

public class CommandMethods {

    private Configuration configuration = Configuration.getInstance ();
    private Datasource data = Datasource.getInstance ();
    //    private NetworkService nws = new NetworkService();
    private PinterestIO pio = new PinterestIO ();
    private CSVService csv = new CSVService ();
    private Scanner sc = new Scanner (System.in);


    /**
     * <b>rake</b>
     * Rake will attempt to connect to Pinterest and retrieve pins from the account associated with the access token provided under “My Pins.”
     *
     * @param cont - Boolean, a flag indicating that the user wishes to continue from a save point.
     */
    public void rake(Boolean cont) {
        PinterestIO pio = new PinterestIO ();
        JMap jMap = pio.getPins (cont);
        jMap.csvExport ("pins" + File.separator + "rake", "rake");
        System.out.println (jMap.count () + " pin(s) raked. Output file found in pins/rake.");
    }

    public void status(boolean filterExternal) {
        List<JPinDatabaseDTO> dtoList;
        if (filterExternal) {
            dtoList = data.queryByInternal ();
        } else {
            dtoList = data.queryAllPins ();
        }
        for (JPinDatabaseDTO dto : dtoList) {
            NetworkService nws = new NetworkService ();
            nws.execute (dto.getLink ());
            data.updatePinResponses (nws.getLinkResponseCode (), nws.getRedirectLocation (), nws.getRedirectLocationResponseCode (), dto.getPinId ());
        }
    }

    public void removePins(String fileName) {
        JMap jMap = new JMap ();
        jMap.csvImport (fileName);
        LinkedHashMap<String, JPin> JPins = jMap.getJPins ();
        JPins.forEach ((k, v) -> {
                    pio.deletePin (k);
                }
        );
    }

    /**
     * <b>updatePins</b>
     * Reads a CSV file to batch update pins from Pinterest from the account associated with the access token provided.
     *
     * @param fileName - String, the exact path of the CSV file containing pin information to update.
     */
    public void updatePins(String fileName) {
        JMap jMap = new JMap ();
        jMap.csvImport (fileName);
        LinkedHashMap<String, JPin> JPins = jMap.getJPins ();
        JPins.forEach ((k, v) -> {
                    pio.updatePin (k, "", v.getNote (), v.getLink ());
                }
        );
    }

    /**
     * <b>configure</b>
     * Set or change application configuration values.
     *
     * @param access_token - String, Pinterest access token.
     * @param username     - String, Pinterest username associated with the access token.
     */
    public void configure(String access_token, String username) {
        if (access_token != null) {
            configuration.setAccessToken (access_token);
            System.out.println ("Access token set successfully.");
        }
        if (username != null) {
            configuration.setUsername (username);
            System.out.println ("Username set to " + username + " successfully.");
        }
    }

    public void report(String fileName) {
        List<JPinDatabaseDTO> dtoList = data.queryAllPins ();
        boolean exported = csv.csvExport (fileName + ".csv", dtoList);
        if (exported) System.out.println ("Report successfully generated: " + fileName + ".csv");
    }

    public void importCSVToDatabase(String fileName) {
        List<JPinDatabaseDTO> dtoList = csv.importFromCSV (fileName);
        int added = 0;
        int updated = 0;
        for (JPinDatabaseDTO dto : dtoList)
            if (data.queryByPinID (dto.getPinId ()) == null) {
                added++;
            } else {
                updated++;
            }
        System.err.println ("Warning: Existing pins will updated. This change is considered destructive and is final.");
        System.out.println("There are "+added+" new pins that can be inserted and "+updated+" pins that will be updated. Continue? [Y/n]: ");
        String answer = sc.nextLine ();
        if(answer.toLowerCase ().equals ("y")){
            for (JPinDatabaseDTO dto : dtoList)
                if (data.queryByPinID (dto.getPinId ()) == null) {
                    added = 0;
                    data.insertPinFull (dto);
                    added++;
                } else { updated = 0;
                data.updateAll (dto.getBoard (),dto.getLink (), dto.getLinkResponseCode (),dto.getLinkRedirectLocation (), dto.getLinkRedirectionResponseCode (), dto.getAction (), dto.getPinId ());
                updated++;
                }
        } else {
            System.out.println ("Aborted.");
        }
        }
    }

