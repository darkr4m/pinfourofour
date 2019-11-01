package com.jtv.pinfourofour.utils;

import com.jtv.pinfourofour.Pinterest;
import com.jtv.pinfourofour.exceptions.PinterestException;
import com.jtv.pinfourofour.fields.pin.PinFields;
import com.jtv.pinfourofour.methods.network.ResponseMessageAndStatusCode;
import com.jtv.pinfourofour.methods.pin.PinEndPointURIBuilder;
import com.jtv.pinfourofour.models.Datasource;
import com.jtv.pinfourofour.models.JMap;
import com.jtv.pinfourofour.models.pin.JPin;
import com.jtv.pinfourofour.models.pin.JPinDatabaseDTO;
import com.jtv.pinfourofour.responses.pin.Pin;
import com.jtv.pinfourofour.responses.pin.Pins;
import com.jtv.pinfourofour.utils.builders.pin.JPinDatabaseDTOCreator;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * Pinterestio
 * Applies Pinterest specific configuration options
 *
 */
public class PinterestIO {
    //==================================================================
    // CONSTANTS
    //==================================================================
    private static Datasource data = Datasource.getInstance();
    private static Configuration configuration = Configuration.getInstance();
    private static Pinterest pinterest = new Pinterest(configuration.getAccessToken());
    private Properties save = new Properties ();
    private String next;

    private final String DIRECTORY = "save";
    private final String FILENAME = "continue.properties";
    private final String PATH = DIRECTORY+File.separator+FILENAME;

    //==================================================================
    // CONSTRUCTOR
    //==================================================================

    public PinterestIO(){
        data.open();
    }

    //==================================================================
    // GET PINS
    //==================================================================

    public ArrayList<Pins> getMyPinsCont(){
        ArrayList<Pins> pinsPage = new ArrayList<> ();
        try {
            Pins pins = pinterest.getMyPins(new PinFields().withAll());
            pinsPage.add (pins);
            while (pinterest.getNextPageOfPins (pins.getNextPage ()) != null) {
                pins = pinterest.getNextPageOfPins (pins.getNextPage ());
                pinsPage.add (pins);
                String cursor  = pins.getNextPage().getCursor();
                savePosition("myPins",cursor);
            }
        } catch (PinterestException e) {
            System.err.println("Currently rate limited by Pinterest: "+e.getMessage());
            return pinsPage;
        }
        return pinsPage;
    }


    public JMap getPins(Boolean cont){
        ArrayList<Pins> pinsPage = new ArrayList<> ();
        final String fileName = "save.properties";
        try {
            if (cont) {
                if(!getContinueCursor ("myPins").isEmpty ()) PinEndPointURIBuilder.setCursor (getContinueCursor ("myPins"));
            }

            Pins pins = pinterest.getMyPins (new PinFields ().withAll ());
            pinsPage.add (pins);
            if(next != null) {
                savePosition ("myPins", next);
            } else {
                savePosition ("myPins", "");
                System.out.println ("Cursor reset. Operation finished");
            }

            while (pinterest.getNextPageOfPins (pins.getNextPage ()) != null) {
                pins = pinterest.getNextPageOfPins (pins.getNextPage ());
                next = pins.getNextPage ().getNext ();
                if (next != null) savePosition ("myPins", next);
                System.out.println ("Pins requested: " + pinsPage + "\n"+ next);
                pinsPage.add (pins);
            }
            System.out.println (pinsPage);
        } catch (PinterestException e) {
//            e.printStackTrace ();
            System.err.println (e.getMessage());
        }

        return saveToJMap(pinsPage);
    }

    /**
     * getMyPins
     *
     * @return ArrayList pinsPage of Pins to be parsed into JPins
     * @see - toJPin
     */
    public ArrayList<Pins> getMyPins() {
        ArrayList<Pins> pinsPage = new ArrayList<> ();
        try {
            Pins pins = pinterest.getMyPins (new PinFields ().withAll ());
            pinsPage.add (pins);

            while (pinterest.getNextPageOfPins (pins.getNextPage ()) != null) {
                pins = pinterest.getNextPageOfPins (pins.getNextPage ());
                pinsPage.add (pins);
            }
        } catch (PinterestException e) {
            System.err.println ("You are rate limited by Pinterest. Please try again later.");
            return pinsPage;
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return pinsPage;
    }

    public ArrayList<Pins> getPinsFromBoard(String board) {
        ArrayList<Pins> pinsPage = new ArrayList<> ();
        try {
            Pins pins = pinterest.getPinsFromBoard (board, new PinFields ().withAll ());
            next = pins.getNextPage ().getCursor ();
            System.out.println ("Pins requested: " + pins + "\n next:" + next);
            pinsPage.add (pins);
            while (pinterest.getNextPageOfPins (pins.getNextPage ()) != null) {
                pins = pinterest.getNextPageOfPins (pins.getNextPage ());
                next = pins.getNextPage ().getCursor ();
                System.out.println ("Pins requested: " + "pin next " + next);
                pinsPage.add (pins);
            }
        } catch (PinterestException e) {
            System.err.println ("You are rate limited by Pinterest. Please try again later.");
            System.out.println ("All the pins you requested: " + pinsPage);
            return pinsPage;
        } catch (Exception e) {
            e.printStackTrace ();
        }

        return pinsPage;
    }

    //==================================================================
    // UPDATE PIN
    //==================================================================
    public boolean updatePin(JPinDatabaseDTO jPin){
        if(pinterest != null) {
            try {
                ResponseMessageAndStatusCode response = pinterest.patchPin (jPin.getPinId(),jPin.getBoard(),jPin.getNote(), jPin.getLink());
                if (response.getStatusCode () == 200) {
                    System.out.println ("Pin " + jPin.getPinId() + " updated successfully. "+response.getMessage());
                    return true;
                } else {
                    System.out.println ("Pin " + jPin.getPinId() + " not updated.");
                    System.out.println (response);
                    return false;
                }
            } catch (PinterestException e) {
                System.out.println ("Error: Unable to update pin. Rate limited by Pinterest. "+e.getMessage ());
                return false;
            }
        }
        return false;
    }

    public boolean updatePin(String pinID, String link){
        if(pinterest != null) {
            try {
                ResponseMessageAndStatusCode response = pinterest.patchPin (pinID,null,null, link);
                if (response.getStatusCode () == 200) {
                    System.out.println ("Pin " + pinID + " updated successfully. "+response.getMessage());
                    return true;
                } else {
                    System.out.println ("Pin " + pinID + " not updated.");
                    System.out.println (response);
                    return false;
                }
            } catch (PinterestException e) {
                System.out.println ("Error: Unable to update pin. Rate limited by Pinterest. "+e.getMessage ());
                return false;
            }
        }
        return false;
    }

    public boolean updatePin(String pinID, String board, String note, String link){
        if(pinterest != null) {
            try {
                ResponseMessageAndStatusCode response = pinterest.patchPin (pinID, board, note, link);
                if (response.getStatusCode () == 200) {
                    System.out.println ("Pin " + pinID + " updated successfully. "+response.getMessage());
                    return true;
                } else {
                    System.out.println ("Pin " + pinID + " not updated.");
                    System.out.println (response);
                    return false;
                }
            } catch (PinterestException e) {
                System.out.println ("Error: Unable to update pin. Rate limited by Pinterest. "+e.getMessage ());
                return false;
            }
        }
        return false;
    }
    //==================================================================
    // DELETE PIN
    //==================================================================
    public boolean deletePin(String pinID){
        if (pinterest !=null) {
            try {
                boolean deleted = pinterest.deletePin (pinID);
                if (deleted) {
                    System.out.println ("Pin " + pinID + " deleted sucessfully.");
                    return true;
                } else {
                    System.out.println ("Pin " + pinID + " not deleted.");
                    return false;
                }
            } catch (PinterestException e) {
                System.out.println ("Unable to delete pin. Rate limited by Pinterest.");
                e.getMessage ();
                return false;
            }
        }
        return false;
    }
    //==================================================================
    // SAVE FUNCTIONS
    //==================================================================

    public void saveToDB(ArrayList<Pins> pinList){
        JPinDatabaseDTOCreator creator = new JPinDatabaseDTOCreator();
        int pins = 0;
        for (Pins pinItems: pinList) {
            for (Pin pin : pinItems) {
                if (data.queryByPinID(pin.getId()) == null) {
                    JPin jPin = creator.createJPin(pin.getId(), pin.getBoard().getName(), pin.getOriginal_link(), pin.getNote());
                    data.insertPinBasic(jPin);
                    pins++;
                    if(data.queryByPinID (pin.getId ())==null) data.updatePinAction ("build",pin.getId ());
                }
            }
        }
        System.out.println (pins+" new pins added to the database.");
    }

    /**<b>toJMap</b>
     *
     * @param pinsPage
     * @return - JMap of jPins (Pin ID, JPin)
     */
    public JMap saveToJMap(ArrayList<Pins> pinsPage) {
        JPinDatabaseDTOCreator creator = new JPinDatabaseDTOCreator();
        JMap jMap = new JMap ();
        for (Pins pinItems: pinsPage) {
            for (Pin pin: pinItems) {
                JPin jPin = creator.createJPin(pin.getId(), pin.getBoard().getName(),pin.getNote(),pin.getOriginal_link());
                jMap.add (jPin);
            }
        }
        return jMap;
    }

    public void savePosition(String method, String next) {
        try{
            File dir = new File (DIRECTORY);
            if(!dir.exists ()) dir.mkdir();
            File saveFile = new File(PATH);
            if(next != null) save.setProperty (method, next);
            else save.setProperty(method, "");
            save.store (new FileOutputStream (saveFile.getAbsolutePath()), null);
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public String getContinueCursor(String method){
        String cursor="";
        File saveFile = new File("saves", "save.properties");
        if(saveFile.exists ()){
            try {
                BufferedReader br = new BufferedReader (new FileReader (saveFile));
                save.load (br);
                //Get next url from save.properties
                String n = save.getProperty (method);
//                System.out.println (n);
                //decode url
                String d = URLDecoder.decode (n,"UTF-8");
//                System.out.println (d);
                //get cursor
                //TODO: replace with regex
                String c = d.substring (d.indexOf ("cursor=")+"cursor=".length ());
//                System.out.println (c);
                cursor = c;
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
        return cursor;
    }

    public String getCursor(String method){
        String cursor;
        File saveFile = new File(PATH);
        if(saveFile.exists()) {
            try(FileReader reader = new  FileReader(saveFile.getAbsolutePath())){
                save.load(reader);
                return cursor = save.getProperty(method);
            } catch (FileNotFoundException e){
                System.err.println("Error: The save file could not be found. "+e.getMessage());
                return null;
            } catch (IOException e){
                System.err.println("Error: Could not create save file: "+e.getMessage());
                return null;
            }
        }
        return null;
    }

}