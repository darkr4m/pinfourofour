package com.jtv.pinfourofour.utils;

import com.jtv.pinfourofour.models.JMap;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Maintenance {
    private String dirName;

    public Maintenance(String dirName){
        this.dirName = dirName;
    }
    //General maintenance options

    /**<b>setDir</b>
     * Allows you to change the maintenance target, the directory.
     *
     * @param dirName - String - The name of the directory to perform maintenance tasks on.
     */
    public void setDir(String dirName) {
        this.dirName = dirName;
    }

    public void mergeCSV(String dirName, String master){
        //This is the target directory that the file merge is looking for files in.
        File dir = new File(this.dirName);
        dir.mkdir (); //Creates directory if it doesn't exist.
        JMap jMap = new JMap ();
        //For each file - imports to a JMap and exported as a new CSV file. The old file is deleted.
            for (File file : dir.listFiles ()){
                if(file.isFile ()){
                    String fileName = file.getName ();
                    jMap.csvImport (dir+File.separator+fileName);
                    if(!file.getName ().equals (master+".csv"));
                    file.delete ();
                }
                else {
                    System.out.println (file.getName ()+" is not a file.");
                }
            }
            jMap.count ();
            jMap.csvExport (dirName, master);

    }
}
