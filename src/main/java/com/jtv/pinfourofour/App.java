package com.jtv.pinfourofour;

import com.jtv.pinfourofour.models.JMap;
import com.jtv.pinfourofour.utils.Config;
import com.jtv.pinfourofour.utils.PinterestIO;


/**
 * Hello world!
 *
 */
public class App {

    public static void main( String[] args )
    {
        init();
    }

    private static void init(){
        Config cfg = new Config();
        JMap jMap = new JMap ();
        jMap.csvImport ();
        jMap.checkLinks ();
        jMap.getJPins ();
        jMap.csvExport ();
    }



}
