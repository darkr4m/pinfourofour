package com.jtv.pinfourofour;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import com.beust.jcommander.ParameterException;
import com.jtv.pinfourofour.models.JMap;
import com.jtv.pinfourofour.models.JPin;
import com.jtv.pinfourofour.responses.pin.Pin;
import com.jtv.pinfourofour.utils.Config;
import com.jtv.pinfourofour.utils.Maintenance;
import com.jtv.pinfourofour.utils.PinterestIO;
import com.jtv.pinfourofour.utils.commands.*;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class App {

    public static void main( String[] args ) {
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
                    statusReport (status.filterExternal);
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
    }

    private static void rake(Boolean cont) {
        PinterestIO pio = new PinterestIO ();
        JMap jMap = pio.getPins (cont);
        jMap.csvExport ("pins"+File.separator+"rake", "rake");
        System.out.println (jMap.count ()+" pin(s) raked. Output file found in pins/rake.");
    }

    private static void statusReport(boolean filterExternal){
        JMap jMap = new JMap ();
        jMap.csvImport ("pins.csv");
        jMap.count ();
        if(filterExternal) jMap.filterExternal ();
        jMap.checkLinks ();
        jMap.csvExport ("pins"+File.separator+"internal","internal");
    }

//    private static void clean(){
//        Maintenance maint = new Maintenance ("testMaint");
//        maint.mergeCSV ("testMaint", "master");
//    }

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

    private static void configure(String access_token, String username){
        Config cfg = new Config();
        if(access_token != null) cfg.setProperty("access_token", access_token);
        if(username != null) cfg.setProperty("username", username);
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
