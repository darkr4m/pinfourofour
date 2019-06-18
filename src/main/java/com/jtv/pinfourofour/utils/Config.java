package com.jtv.pinfourofour.utils;

import java.io.*;
import java.util.Properties;
import java.util.Set;

/**<b>Config</b>
 *
 */
public class Config {
    private Properties configFileProps = new Properties();
    private File configFile = new File ("config","pin.config");
    private boolean configured = configFile.exists();

    //Default properties

    public Config(){
        if(configured) {
            load();
        } else {
            setup();
        }
    }

    public String getProperty(String key){
        String value = this.configFileProps.getProperty (key);
        return value;
    }

    public void setProperty(String key, String value){
        try {
            configFileProps.setProperty (key, value);
            configFileProps.store (new FileOutputStream(configFile.getAbsolutePath ()), null);
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public Set<Object> getAllPropertyKeys(){
        Set<Object> keys = configFileProps.keySet ();
        return keys;
    }

    private void setup(){
        if(!configured){
            System.out.println("Configuration file not found... running setup.");
            try {
                File configDir = new File("config");
                configDir.mkdir();
                configFile.createNewFile();
                //TODO: Create default values object.
                proxySetup ();
                configFileProps.store(new FileOutputStream(configFile.getAbsolutePath()), null);
                configured = true;
                System.out.println("New configuration file created.");
                load();
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }

    public void load(){
        try  {
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            configFileProps.load(br);
            System.out.println("Configuration loaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO: add varargs
    private void proxySetup(){
        configFileProps.setProperty("proxy", "");
        configFileProps.setProperty("http_proxy_addr", "");
        configFileProps.setProperty("port", "");
    }
}