package com.jtv.pinfourofour;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import com.jtv.pinfourofour.models.JMap;
import com.jtv.pinfourofour.models.JPin;
import com.jtv.pinfourofour.responses.pin.Pin;
import com.jtv.pinfourofour.utils.Config;
import com.jtv.pinfourofour.utils.Maintenance;
import com.jtv.pinfourofour.utils.PinterestIO;
import com.jtv.pinfourofour.utils.commands.RakeCommand;
import com.jtv.pinfourofour.utils.commands.StatusReportCommand;

import java.io.File;
import java.util.LinkedHashMap;

/**
 * Hello world!
 *
 */
public class App {
    @Parameter(names={"name", "-n"})
    public String name;

    public static void main( String[] args ) {
        String[] argv = {"-n" ,"max"};
        App app = new App();
        Config config = new Config();
        RakeCommand rake = new RakeCommand ();
        StatusReportCommand status = new StatusReportCommand ();
        JCommander jc = JCommander.newBuilder()
                .addObject(app)
                .addCommand("config", config)
                .addCommand ("rake", rake)
                .addCommand ("status",status)
                .build();
        jc.parse(argv);
        boolean command = !(jc.getParsedCommand() == null);
        if(command) {
            switch (jc.getParsedCommand()) {
                case "config":
                    System.out.println ("test");
                    if (!config.access_token.isEmpty()) config.setProperty("access_token", config.access_token);
                    if (!config.username.isEmpty()) config.setProperty("username", config.username);
                    break;
                case "rake":
                    rake(rake.cont);
                    break;
                case "status":
                    statusReport (status.filterExternal);
                    break;
//                case "clean":
//                    clean();
//                    break;
                default:
                    System.out.println("nope");
            }
        }
        removePins ("pins.csv");
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
        JMap jMap = new JMap ();
        PinterestIO pio = new PinterestIO ();
        jMap.csvImport (fileName);
        LinkedHashMap<String, JPin> JPins = jMap.getJPins ();
        JPins.forEach((k,v) ->
                //filler code -- pio delete pin goes here
                System.out.println(k+" "+v.getLink())
        );
    }
}
