package com.jtv.pinfourofour.utils;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.io.*;
import java.util.Properties;
import java.util.Set;

/**<b>Config</b>
 * Load configuration from config/pin.config file
 *
 */
public class Config {
    private Properties configFileProps = new Properties();
    private File configFile = new File ("config","pin.config");
    private boolean configured = configFile.exists();

    public Config(){
        if(configured){
            load();
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
        Set<Object> keys = this.configFileProps.keySet ();
        return keys;
    }

    public void default_setup(){
        if(!configured){
            System.out.println("Configuration file not found... running default_setup.");
            try {
                File configDir = new File("config");
                configDir.mkdir();
                configFile.createNewFile();
//                proxySetup ();
                pinterestDefaultSetup();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void pinterestDefaultSetup(){
        configFileProps.setProperty ("access_token", "");
        configFileProps.setProperty("username", "");
        System.out.println("An access token from Pinterest is required.");
    }

    public boolean isConfigured() {
        return configured;
    }

    //TODO: proxy default_setup
//    private void proxySetup(){
//        configFileProps.setProperty("proxy", "");
//        configFileProps.setProperty("http_proxy_addr", "");
//        configFileProps.setProperty("port", "");
//    }
}