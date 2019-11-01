package com.jtv.pinfourofour.utils;

import java.io.*;
import java.util.Properties;

public class Configuration {
    //==================================================================
    // CONSTANTS
    //==================================================================
    private static Properties configProps = new Properties();

    private final String DIRECTORY = "user";
    private final String FILENAME = "pin.config";
    private final String PATH = DIRECTORY+File.separator+FILENAME;

    private static final String PROP_DATABASE = "database";
    private static final String PROP_USERNAME = "username";
    private static final String PROP_ACCESS_TOKEN = "access_token";

    //==================================================================
    // LOAD CONFIGURATION
    //==================================================================
    private boolean loadConfiguration(){
        File configFile = new File(PATH);
        if(!configFile.exists()) createConfiguration();
        try(FileReader reader = new  FileReader(configFile.getAbsolutePath())){
            configProps.load(reader);
            return true;
        } catch (FileNotFoundException e){
            System.err.println("Error: The file could not be found. "+e.getMessage());
            return false;
        } catch (IOException e){
            System.err.println("Error: Could not create configuration file: "+e.getMessage());
            return false;
        }
    }

    //==================================================================
    // DEFAULT SETUP
    //==================================================================
    /**<b>createConfig</b>
     * Create a brand new config file with some default values.
     *
     * @return - Boolean - True if saving is success.
     */
    private boolean createConfiguration() {
        File dir = new File(DIRECTORY);
        if(!dir.exists()) dir.mkdir();
        File configFile = new File(PATH);
        try{
            configProps.setProperty("username", "");
            configProps.setProperty("access_token", "");
            configProps.setProperty("database", "");
            configProps.store(new FileOutputStream(configFile.getAbsolutePath()),null);
            return true;
        } catch (IOException e){
            System.err.println("Error: Could not create configuration file: "+e.getMessage());
            return false;
        }
    }
    //==================================================================
    // SAVE CONFIGURATION
    //==================================================================
    /**<b>saveConfig</b>
     * Save the current Configuration values out to a file to be retrieved in the future.
     *
     * @return - Boolean - True if saving is success.
     */
    public boolean saveConfig() {
        File configFile = new File(PATH);
        if(configFile.exists() && configFile.canWrite()){
            try(FileOutputStream out = new FileOutputStream(configFile.getAbsolutePath())){
                configProps.store(out,null);
                System.out.println("Configuration saved successfully.");
                return true;
            } catch (FileNotFoundException e) {
                System.err.println("Error: The file could not be found. "+e.getMessage());
                return false;
            } catch (UnsupportedEncodingException e){
                System.err.println("Error: This encoding is not supported. "+e.getMessage());
                return false;
            } catch (IOException e){
                System.err.println("Error: The file could not be written. "+e.getMessage());
                return false;
            }
        }
        return false;
    }
    //==================================================================
    // GET AND SET
    //==================================================================
    public String getUsername(){
        return this.configProps.getProperty(PROP_USERNAME);
    }

    public void setUsername(String prop){
        configProps.setProperty(PROP_USERNAME,prop);
        saveConfig();
    }

    public String getDatabase(){
        return this.configProps.getProperty(PROP_DATABASE);
    }

    public void setDatabase(String prop){
        configProps.setProperty(PROP_DATABASE,prop);
        saveConfig();
    }

    public String getAccessToken(){
        return this.configProps.getProperty(PROP_ACCESS_TOKEN);
    }

    public void setAccessToken(String prop){
        configProps.setProperty(PROP_ACCESS_TOKEN,prop);
        saveConfig();
    }

    //==================================================================
    // SINGLETON MAGIC
    //==================================================================
    private Configuration() {
        boolean config = loadConfiguration();
        if(!config){
            createConfiguration();
            if(!this.createConfiguration()){
                System.exit(0);
            }
        } else {
            System.out.println("Configuration loaded.");
        }
    }
    private static class ConfigurationHolder {
        private static Configuration INSTANCE = new Configuration();
    }
    public static Configuration getInstance() {
        return ConfigurationHolder.INSTANCE;
    }

}
