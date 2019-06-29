package com.jtv.pinfourofour.models;

import com.jtv.pinfourofour.models.pin.JPin;
import com.jtv.pinfourofour.models.pin.JPinDatabaseDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {
    public static final String DB_NAME = "jpins.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:"+DB_NAME;

    public static final String TABLE_PINS = "JPins";
    public static final String COLUMN_PIN_ID = "id";
    public static final String COLUMN_BOARD= "board";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_LINK_RESPONSE_CODE = "link_response_code";
    public static final String COLUMN_REDIRECT_LOCATION = "redirect_location";
    public static final String COLUMN_REDIRECT_LOCATION_RESPONSE_CODE = "redirect_location_response_code";
    public static final String COLUMN_ACTION = "action";

    //Singleton Logic
    private Datasource(){}
    private static class DatasourceHolder{
        private static Datasource INSTANCE = new Datasource();
    }
    public static Datasource getInstance(){
        return DatasourceHolder.INSTANCE;
    }


    private Connection conn;

    /**<b>open</b>
     * Opens a database connection.
     * @return
     */
    public boolean open(){
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            System.out.println("Successfully established a database connection to "+DB_NAME+".");
            return true;
        } catch (SQLException e){
            System.err.println("Error - Could not establish a connection to the database "+DB_NAME+": "+e.getMessage());
            return false;
        }
    }

    public void close(){
        try{
            if(conn != null){
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error - Could not close the connection to the database: "+e.getMessage());
        }
    }

    public List<JPinDatabaseDTO> queryAllPins(){
        try (Statement statement = conn.createStatement ();
             ResultSet results = statement.executeQuery ("SELECT * FROM "+TABLE_PINS)) {
            List<JPinDatabaseDTO> jPins = new ArrayList<> ();
            while (results.next ()){
                JPinDatabaseDTO dto = new JPinDatabaseDTO (String.valueOf (results.getBigDecimal (COLUMN_PIN_ID)));
                dto.setBoard (results.getString (COLUMN_BOARD));
                dto.setLink (results.getString (COLUMN_LINK));
                dto.setNote (results.getString (COLUMN_NOTE));
                dto.setLinkResponseCode (results.getInt (COLUMN_LINK_RESPONSE_CODE));
                dto.setLinkRedirectLocation (results.getString (COLUMN_REDIRECT_LOCATION));
                dto.setLinkRedirectionResponseCode (results.getInt(COLUMN_REDIRECT_LOCATION_RESPONSE_CODE));
                dto.setAction (results.getString (COLUMN_ACTION));
                jPins.add (dto);
            }
            return jPins;
        } catch (SQLException e) {
            System.err.println ("Query failed:" +e.getMessage ());
            return null;
        }
    }

}
