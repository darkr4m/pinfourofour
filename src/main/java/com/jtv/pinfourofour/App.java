package com.jtv.pinfourofour;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.jtv.pinfourofour.models.JMap;

import com.jtv.pinfourofour.utils.Config;
import com.jtv.pinfourofour.utils.Maintenance;
import com.jtv.pinfourofour.utils.PinterestIO;

/**
 * Hello world!
 *
 */
public class App {
    @Parameter(names={"--length", "-l"})
    int length;
    @Parameter(names={"--pattern", "-p"})
    int pattern;

    public static void main( String ... argv )
    {
//        App app = new App();
//        JCommander.newBuilder()
//                .addObject(app)
//                .build()
//                .parse(argv);
//        app.run();
        Maintenance maint = new Maintenance ("reports");
        maint.setDir ("testMaint");
        maint.mergeCSV ("pins","end");
        JMap jMap = new JMap ();
        jMap.csvImport ("testpins.csv");
        jMap.checkLinks ();
        jMap.csvExport ("reports", "linkcheck");
    }

    public void run() {
        System.out.printf("%d %d \n", length, pattern);
    }

    private static void init(){
        Config cfg = new Config();
        JMap jMap = new JMap ();
        PinterestIO pio = new PinterestIO ();
//        pio.getPins (false);
    }



}
