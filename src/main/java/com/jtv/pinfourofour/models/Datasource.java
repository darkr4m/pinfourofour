package com.jtv.pinfourofour.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Datasource {
    public static final String DB_NAME = "matsupins.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:"+DB_NAME;

    public static final String TABLE_PINS = "pins";
    public static final String COLUMN_PIN_ID = "pin_id";
    public static final String COLUMN_BOARD= "board";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_REDIR_LOCATION = "redir_location";
    public static final String COLUMN_REDIR_LOCATION_STATUS = "redir_location_status";
    public static final String COLUMN_ACTION = "action";


    private Connection conn;

    public boolean open(){
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
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
            }
        } catch (SQLException e) {
            System.err.println("Error - Could not close the connection to the database: "+e.getMessage());
        }
    }

}
