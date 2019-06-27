package com.jtv.pinfourofour;

import com.beust.jcommander.JCommander;

import com.beust.jcommander.ParameterException;
import com.jtv.pinfourofour.models.Datasource;
import com.jtv.pinfourofour.models.JMap;
import com.jtv.pinfourofour.models.JPin;
import com.jtv.pinfourofour.utils.Config;
import com.jtv.pinfourofour.utils.PinterestIO;
import com.jtv.pinfourofour.utils.commands.*;

import java.io.File;
import java.util.LinkedHashMap;

/** Pin404
 *  An internal application for Pinterest Pin management.
 *  An access token is required for use.
 *
 *  Please see the Pinterest Developers API documentation for full instructions on how to obtain a token via Postman.
 *  Generating access tokens is not supported in this application.
 */
public class App {

    public static void main( String[] args ) {
        Datasource data = new Datasource();
        if(!data.open()){
            System.out.println("Could not establish a connection to the database.");
            return;
        }
        String[] argv = {};
        init();
        App app = new App();
        ConfigCommand config = new ConfigCommand();
        RakeCommand rake = new RakeCommand ();
        StatusReportCommand status = new StatusReportCommand ();
        UpdateCommand update = new UpdateCommand();
        RemoveCommand remove = new RemoveCommand();
        JCommander jc = JCommander.newBuilder()
                .addObject(app)
                .addCommand("config", config)
                .addCommand ("rake", rake)
                .addCommand ("status",status)
                .addCommand("remove", remove)
                .addCommand("update", update)
                .build();
        try {
            jc.parse(argv);
        } catch (ParameterException e){
            e.usage();
        }
        boolean command = !(jc.getParsedCommand() == null);
        if(command) {
            switch (jc.getParsedCommand()) {
                case "config":
                    configure(config.access_token, config.username);
                    break;
                case "rake":
                    rake(rake.cont);
                    break;
                case "status":
                    statusReport (status.filterExternal, status.fileName);
                    break;
                case "update":
                    updatePins(update.fileName);
                    break;
                case "remove":
                    removePins(remove.fileName);
                    break;
//                case "clean":
//                    clean();
//                    break;
                default:
                    System.out.println("Nope. This command is not supported.");
                    jc.usage();
            }
        }
        data.close();
    }

    /**<b>rake</b>
     * Rake will attempt to connect to Pinterest and retrieve pins from the account associated with the access token provided under “My Pins.”
     *
     * @param cont - Boolean, a flag indicating that the user wishes to continue from a save point.
     */
    private static void rake(Boolean cont) {
        PinterestIO pio = new PinterestIO ();
        JMap jMap = pio.getPins (cont);
        jMap.csvExport ("pins"+File.separator+"rake", "rake");
        System.out.println (jMap.count ()+" pin(s) raked. Output file found in pins/rake.");
    }

    /**<b>statusReport</b>
     * Checks for the response code for each link. If the link ends up being redirected, the redirect location is also checked for a response code.
     *
     * @param filterExternal - Boolean, a flag indicating the user wishes to filter any link that does not go to their website.
     * @param fileName - String, the exact path of the CSV file containing pin information to check.
     */
    private static void statusReport(boolean filterExternal, String fileName){
        JMap jMap = new JMap ();
        jMap.csvImport (fileName);
        jMap.count ();
        if(filterExternal) jMap.filterExternal ();
        jMap.checkLinks ();
        jMap.csvExport ("pins"+File.separator+"internal","internal");
    }

//    private static void clean(){
//        Maintenance maint = new Maintenance ("testMaint");
//        maint.mergeCSV ("testMaint", "master");
//    }

    /**<b>removePins</b>
     * Reads a CSV file to batch delete pins from Pinterest from the account associated with the access token provided.
     *
     * @param fileName - String, the exact path of the CSV file containing pin information to delete.
     */
    private static void removePins(String fileName){
        PinterestIO pio = new PinterestIO ();
        JMap jMap = new JMap ();
        jMap.csvImport (fileName);
        LinkedHashMap<String, JPin> JPins = jMap.getJPins ();
        JPins.forEach((k,v) -> {
                    pio.deletePin(k);
                }
        );
    }

    /** <b>updatePins</b>
     * Reads a CSV file to batch update pins from Pinterest from the account associated with the access token provided.
     *
     * @param fileName - String, the exact path of the CSV file containing pin information to update.
     */
    private static void updatePins(String fileName){
        JMap jMap = new JMap ();
        PinterestIO pio = new PinterestIO ();
        jMap.csvImport (fileName);
        LinkedHashMap<String, JPin> JPins = jMap.getJPins ();
        JPins.forEach((k,v) -> {
                    pio.updatePin(k, "", v.getNote(), v.getLink());
                }
        );
    }

    /** <b>configure</b>
     * Set or change application configuration values.
     *
     * @param access_token - String, Pinterest access token.
     * @param username - String, Pinterest username associated with the access token.
     */
    private static void configure(String access_token, String username){
        Config cfg = new Config();
        if(access_token != null) {
            cfg.setProperty("access_token", access_token);
            System.out.println("Access token set successfully.");
        }
        if(username != null) {
            cfg.setProperty("username", username);
            System.out.println("Username set to "+username+" successfully.");
        }
    }


    private static void init(){
        Config config = new Config();
        if(config.isConfigured()) {
            config.load();
            System.out.println("Configuration loaded successfully.");
        } else {
            config.default_setup();
        }
    }
}
