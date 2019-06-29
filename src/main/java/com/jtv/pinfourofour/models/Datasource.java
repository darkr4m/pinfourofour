package com.jtv.pinfourofour.models;

import com.jtv.pinfourofour.models.pin.JPinDatabaseDTO;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Datasource {
    private static final String DB_NAME = "jpins.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;
    private static final String INTERNAL = "%www.jtv.com%";

    //==================================================================
    // TABLE AND COLUMN CONSTANTS
    //==================================================================
    private static final String TABLE_PINS = "JPins";
    private static final String COLUMN_PIN_ID = "id";
    private static final String COLUMN_BOARD = "board";
    private static final String COLUMN_LINK = "link";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_LINK_RESPONSE_CODE = "link_response_code";
    private static final String COLUMN_REDIRECT_LOCATION = "redirect_location";
    private static final String COLUMN_REDIRECT_LOCATION_RESPONSE_CODE = "redirect_location_response_code";
    private static final String COLUMN_ACTION = "action";

    //==================================================================
    // PREPARED STATEMENTS
    //==================================================================
    private static final String QUERY_TABLE_BY_PINID_PREP = "SELECT * FROM "+ TABLE_PINS + " WHERE " + COLUMN_PIN_ID + " = ?";
    private static final String QUERY_TABLE_BY_BOARD_PREP = "SELECT * FROM "+ TABLE_PINS + " WHERE " + COLUMN_BOARD + " = ?";
    private static final String QUERY_TABLE_BY_RESPSONSE_PREP = "SELECT * FROM "+ TABLE_PINS + " WHERE " + COLUMN_LINK_RESPONSE_CODE + " LIKE ?";
    private static final String INSERT_PIN_BASIC_PREP = "INSERT INTO "+TABLE_PINS+"("+COLUMN_PIN_ID+", "+COLUMN_BOARD+", "+COLUMN_LINK+", "+COLUMN_NOTE+", "+COLUMN_ACTION+") VALUES(?, ?, ?, ?, ?)";
    private static final String INSERT_PIN_FULL_PREP = "INSERT INTO "+TABLE_PINS+"("+COLUMN_PIN_ID+", "+COLUMN_BOARD+", "+COLUMN_LINK+", "+COLUMN_NOTE+", "+COLUMN_LINK_RESPONSE_CODE+", "
            +COLUMN_REDIRECT_LOCATION+", "+COLUMN_REDIRECT_LOCATION_RESPONSE_CODE+", "+COLUMN_ACTION+") VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

    //==================================================================
    // SINGLETON MAGIC
    //==================================================================
    private Datasource() {
    }
    private static class DatasourceHolder {
        private static Datasource INSTANCE = new Datasource();
    }
    public static Datasource getInstance() {
        return DatasourceHolder.INSTANCE;
    }


    private Connection conn;
    private PreparedStatement queryByPin;
    private PreparedStatement queryByBoard;
    private PreparedStatement queryByResponseCode;
    private PreparedStatement insertPinBasic;
    private PreparedStatement insertPinFull;

    //==================================================================
    // RESOURCE MANAGEMENT
    //==================================================================
    /**
     * <b>open</b>
     * Opens a database connection.
     *
     * @return Boolean
     */
    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            queryByPin = conn.prepareStatement(QUERY_TABLE_BY_PINID_PREP);
            queryByBoard = conn.prepareStatement(QUERY_TABLE_BY_BOARD_PREP);
            queryByResponseCode = conn.prepareStatement(QUERY_TABLE_BY_RESPSONSE_PREP);
            insertPinBasic = conn.prepareStatement(INSERT_PIN_BASIC_PREP);
            insertPinFull = conn.prepareStatement(INSERT_PIN_FULL_PREP);
            System.out.println("Successfully established a database connection to " + DB_NAME + ".");
            return true;
        } catch (SQLException e) {
            System.err.println("Error - Could not establish a connection to the database " + DB_NAME + ": " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if(queryByPin != null) queryByPin.close();
            if(queryByBoard != null) queryByBoard.close();
            if(queryByResponseCode != null) queryByResponseCode.close();
            if(insertPinBasic != null) insertPinBasic.close();
            if(insertPinFull != null) insertPinFull.close();
            if (conn != null) {
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error - Could not close the connection to the database: " + e.getMessage());
        }
    }

    //==================================================================
    // QUERIES
    //==================================================================
    public List<JPinDatabaseDTO> queryAllPins() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_PINS)) {
            List<JPinDatabaseDTO> jPins = new ArrayList<>();
            while (results.next()) {
                jPins.add(dtoAllColumns(results));
            }
            return jPins;
        } catch (SQLException e) {
            System.err.println("Query failed:" + e.getMessage());
            return null;
        }
    }

    public JPinDatabaseDTO queryByPinID(String pinID) {
        try {
            queryByPin.setString(1, pinID);
            ResultSet results = queryByPin.executeQuery();

            List<JPinDatabaseDTO> jpins = new ArrayList<>();

            while (results.next()){
                jpins.add(dtoAllColumns(results));
            }
            if(jpins.isEmpty()) return null;
            return jpins.get(0);
        } catch (SQLException e) {
            System.err.println("Query failed:" + e.getMessage());
            return null;
        }
    }

    public List<JPinDatabaseDTO> queryByBoard(String board) {
        try {
            queryByBoard.setString(1, board);
            ResultSet results = queryByBoard.executeQuery();

            List<JPinDatabaseDTO> jpins = new ArrayList<>();

            while (results.next()){
                jpins.add(dtoAllColumns(results));
            }
            return jpins;
        } catch (SQLException e) {
            System.err.println("Query failed:" + e.getMessage());
            return null;
        }
    }

    public List<JPinDatabaseDTO> queryByResponseCode(int code) {
        try {
            queryByResponseCode.setString(1, "%"+code+"%");
            ResultSet results = queryByResponseCode.executeQuery();

            List<JPinDatabaseDTO> jpins = new ArrayList<>();

            while (results.next()){
                jpins.add(dtoAllColumns(results));
            }
            return jpins;
        } catch (SQLException e) {
            System.err.println("Query failed:" + e.getMessage());
            return null;
        }
    }

    public List<JPinDatabaseDTO> queryByInternal(){
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM JPins WHERE link LIKE \"%www.jtv.com%\"")){
            List<JPinDatabaseDTO> jPins = new ArrayList<>();
            while (results.next()) {
                jPins.add(dtoAllColumns(results));
            }
            return jPins;
        } catch (SQLException e) {
            System.err.println("Query failed:" + e.getMessage());
            return null;
        }
    }

    public List<JPinDatabaseDTO> queryByExternal(){
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM JPins WHERE link NOT LIKE \"%www.jtv.com%\"")){
            List<JPinDatabaseDTO> jPins = new ArrayList<>();
            while (results.next()) {
                jPins.add(dtoAllColumns(results));
            }
            return jPins;
        } catch (SQLException e) {
            System.err.println("Query failed:" + e.getMessage());
            return null;
        }
    }

    public List<JPinDatabaseDTO> queryEmptyLinks(){
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM "+TABLE_PINS+" WHERE "+COLUMN_LINK+" ISNULL ")) {
            List<JPinDatabaseDTO> jPins = new ArrayList<>();
            while (results.next()) {
                jPins.add(dtoAllColumns(results));
            }
            return jPins;
        } catch (SQLException e) {
            System.err.println("Query failed:" + e.getMessage());
            return null;
        }
    }

    //==================================================================
    // INSERT
    //==================================================================
    public boolean insertPinBasic(String pinID, String board, String link, String note, String action){
        try {
            insertPinBasic.setString(1, pinID);
            insertPinBasic.setString(2, board);
            insertPinBasic.setString(3, link);
            insertPinBasic.setString(4, note);
            insertPinBasic.setString(5, action);
            if(queryByPinID(pinID) == null) {
                insertPinBasic.execute();
                return true;
            }
            System.err.println("There is a pin with this ID already in the database: ");
            System.out.println(queryByPinID(pinID));
            return false;
        } catch (SQLException e){
            System.out.println("Inserting pins failed.\n");
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertPinFull(String pinID, String board, String link, String note, int linkResponseCode, String linkRedirectLocation, int linkRedirectionResponseCode, String action ){
        try {
            insertPinFull.setString(1, pinID);
            insertPinFull.setString(2, board);
            insertPinFull.setString(3, link);
            insertPinFull.setString(4, note);
            insertPinFull.setInt(5, linkResponseCode);
            insertPinFull.setString(6, linkRedirectLocation);
            insertPinFull.setInt(7, linkRedirectionResponseCode);
            insertPinFull.setString(8, action);
            if(queryByPinID(pinID) == null) {
                insertPinFull.execute();
                return true;
            }
            System.err.println("There is a pin with this ID already in the database: ");
            System.out.println(queryByPinID(pinID));
            return false;
        } catch (SQLException e){
            System.out.println("Inserting pins failed.\n");
            e.printStackTrace();
            return false;
        }
    }

    //==================================================================
    // UPDATE
    //==================================================================
    


    //==================================================================
    // ENTITIES
    //==================================================================
    private JPinDatabaseDTO dtoAllColumns(ResultSet results) throws SQLException{
        JPinDatabaseDTO dto = new JPinDatabaseDTO(String.valueOf(results.getBigDecimal(COLUMN_PIN_ID)));
        dto.setBoard(results.getString(COLUMN_BOARD));
        dto.setLink(results.getString(COLUMN_LINK));
        dto.setNote(results.getString(COLUMN_NOTE));
        dto.setLinkResponseCode(results.getInt(COLUMN_LINK_RESPONSE_CODE));
        dto.setLinkRedirectLocation(results.getString(COLUMN_REDIRECT_LOCATION));
        dto.setLinkRedirectionResponseCode(results.getInt(COLUMN_REDIRECT_LOCATION_RESPONSE_CODE));
        dto.setAction(results.getString(COLUMN_ACTION));
        return dto;
    }
}
