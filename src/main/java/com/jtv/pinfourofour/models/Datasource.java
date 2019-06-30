package com.jtv.pinfourofour.models;

import com.jtv.pinfourofour.models.pin.JPin;
import com.jtv.pinfourofour.models.pin.JPinDatabaseDTO;

import java.sql.*;
import java.util.ArrayList;
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
    private static final String UPDATE_PIN_LINK_PREP = "UPDATE "+TABLE_PINS+" SET "+COLUMN_LINK+"= ?"+" WHERE "+COLUMN_PIN_ID+"= ?";
    private static final String UPDATE_PIN_ACTION_PREP = "UPDATE "+TABLE_PINS+" SET "+COLUMN_ACTION+"= ?"+" WHERE "+COLUMN_PIN_ID+"= ?";
    private static final String UPDATE_PIN_BOARD_PREP = "UPDATE "+TABLE_PINS+" SET "+COLUMN_BOARD+"= ?"+" WHERE "+COLUMN_PIN_ID+"= ?";
    private static final String UPDATE_PIN_RESPONSES_PREP = "UPDATE "+TABLE_PINS+" SET "+COLUMN_LINK_RESPONSE_CODE+"= ?, "+COLUMN_REDIRECT_LOCATION+"= ?, "
            +COLUMN_REDIRECT_LOCATION_RESPONSE_CODE+"= ?" +" WHERE "+COLUMN_PIN_ID+"= ?";
    private static final String DELETE_PIN_PREP = "DELETE FROM "+TABLE_PINS+" WHERE "+COLUMN_PIN_ID+"= ?";

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
    private PreparedStatement updatePinLink;
    private PreparedStatement updatePinAction;
    private PreparedStatement updatePinBoard;
    private PreparedStatement updatePinResponses;
    private PreparedStatement deletePin;

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
            updatePinLink = conn.prepareStatement(UPDATE_PIN_LINK_PREP);
            updatePinAction = conn.prepareStatement(UPDATE_PIN_ACTION_PREP);
            updatePinBoard = conn.prepareStatement(UPDATE_PIN_BOARD_PREP);
            updatePinResponses = conn.prepareStatement(UPDATE_PIN_RESPONSES_PREP);
            deletePin = conn.prepareStatement(DELETE_PIN_PREP);

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
            if(updatePinLink != null) updatePinLink.close();
            if(updatePinAction != null) updatePinAction.close();
            if(updatePinBoard != null) updatePinBoard.close();
            if(updatePinResponses != null) updatePinResponses.close();
            if(deletePin != null) deletePin.close();
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

    public boolean insertPinBasic(JPin jPin){
        try {
            insertPinBasic.setString(1, jPin.getPinID());
            insertPinBasic.setString(2, jPin.getBoard());
            insertPinBasic.setString(3, jPin.getLink());
            insertPinBasic.setString(4, jPin.getNote());
            insertPinBasic.setString(5, "build");
            if(queryByPinID(jPin.getPinID()) == null) {
                insertPinBasic.execute();
                return true;
            }
            System.err.println("There is a pin with this ID already in the database: ");
            System.out.println(queryByPinID(jPin.getPinID()));
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
    public boolean updatePinLink(String link, String pinID){
        try{
            updatePinLink.setString(1, link);
            updatePinLink.setString(2, pinID);
            if(queryByPinID(pinID) != null) {
                updatePinLink.execute();
                System.out.println("Sucessfully changed link on pin "+pinID+" to "+link+".");
                return true;
            }
            System.err.println("The pin with the ID "+pinID+" does not exist in the database.");
            return false;
        } catch (SQLException e){
            System.out.println("Could not update pin: "+e.getMessage());
        }
        return false;
    }

    public boolean updatePinAction(String action, String pinID){
        try{
            updatePinAction.setString(1, action);
            updatePinAction.setString(2, pinID);
            if(queryByPinID(pinID) != null) {
                updatePinAction.execute();
                System.out.println("Sucessfully changed action on pin "+pinID+" to "+action+".");
                return true;
            }
            System.err.println("The pin with the ID "+pinID+" does not exist in the database.");
            return false;
        } catch (SQLException e){
            System.out.println("Could not update pin: "+e.getMessage());
        }
        return false;
    }

    public boolean updatePinBoard(String board, String pinID){
        try{
            updatePinBoard.setString(1, board);
            updatePinBoard.setString(2, pinID);
            if(queryByPinID(pinID) != null) {
                updatePinBoard.execute();
                System.out.println("Sucessfully changed board on pin "+pinID+" to "+board+".");
                return true;
            }
            System.err.println("The pin with the ID "+pinID+" does not exist in the database.");
            return false;
        } catch (SQLException e){
            System.out.println("Could not update pin: "+e.getMessage());
        }
        return false;
    }

    public boolean updatePinResponses(int linkResponseCode, String linkRedirectLocation, int linkRedirectionResponseCode, String pinID){
        try{
            updatePinResponses.setInt(1, linkResponseCode);
            updatePinResponses.setString(2, linkRedirectLocation);
            updatePinResponses.setInt(3, linkRedirectionResponseCode);
            updatePinResponses.setString(4, pinID);
            if(queryByPinID(pinID) != null) {
                updatePinResponses.execute();
                System.out.println("Sucessfully changed responses on pin "+pinID+".");
                return true;
            }
            System.err.println("The pin with the ID "+pinID+" does not exist in the database.");
            return false;
        } catch (SQLException e){
            System.out.println("Could not update pin: "+e.getMessage());
        }
        return false;
    }

    //==================================================================
    // DELETE
    //==================================================================
    public boolean deletePin(String pinID) {
        try {
            deletePin.setString(1, pinID);
            if (queryByPinID(pinID) != null) {
                deletePin.execute();
                System.out.println("Sucessfully deleted " + pinID + ".");
                return true;
            }
            System.err.println("The pin with the ID " + pinID + " does not exist in the database.");
            return false;
        } catch (SQLException e) {
            System.out.println("Could not update pin: " + e.getMessage());
        }
        return false;
    }

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
