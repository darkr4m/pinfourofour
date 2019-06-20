package com.jtv.pinfourofour.utils;

import com.jtv.pinfourofour.Pinterest;
import com.jtv.pinfourofour.exceptions.PinterestException;
import com.jtv.pinfourofour.fields.pin.PinFields;
import com.jtv.pinfourofour.methods.network.ResponseMessageAndStatusCode;
import com.jtv.pinfourofour.methods.pin.PinEndPointURIBuilder;
import com.jtv.pinfourofour.models.JPin;
import com.jtv.pinfourofour.responses.pin.Pin;
import com.jtv.pinfourofour.responses.pin.Pins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;


/**
 * Pinterestio
 * Applies Pinterest specific configuration options
 *
 */
public class PinterestIO {
    private static Pinterest pinterest;
    private Properties save = new Properties ();
    private String next;

    public PinterestIO(){
        Properties props = new Properties ();
        try {
            props.load (new BufferedReader (new FileReader ("config/pin.config")));
            if(!props.getProperty ("access_token").isEmpty ()) {
                pinterest = new Pinterest (props.getProperty ("access_token"));
                System.out.println ("New Pinterest IO opened with access token "+props.getProperty ("access_token"));
            } else {
                System.out.println ("Access token not found.");
            }
        } catch (Exception e) {
            e.printStackTrace ();
            System.out.println ("Access token not found.");
        }
    }

    public void getPins(Boolean cont){
        ArrayList<Pins> pinsPage = new ArrayList<> ();
        final String fileName = "save.properties";
        try {
            if (cont) {
                PinEndPointURIBuilder.setCursor (getContinueCursor ("myPins"));
            }

            Pins pins = pinterest.getMyPins (new PinFields ().withAll ());
            pinsPage.add (pins);
            if(next != null) savePosition ("myPins", next, fileName);

            while (pinterest.getNextPageOfPins (pins.getNextPage ()) != null) {
                pins = pinterest.getNextPageOfPins (pins.getNextPage ());
                next = pins.getNextPage ().getNext ();
                if (next != null) savePosition ("myPins", next, fileName);
                System.out.println ("Pins requested: " + pinsPage + "\n"+ next);
                pinsPage.add (pins);
            }
            System.out.println (pinsPage);
        } catch (PinterestException e) {
            e.printStackTrace ();
            System.out.println ("Message");
        }
        System.out.println ("outer message");
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

    public void updatePin(String pinID, String board, String note, String link){
        if(pinterest != null) {
            try {
                ResponseMessageAndStatusCode response = pinterest.patchPin (pinID, board, note, link);
                if (response.getStatusCode () == 200) {
                    System.out.println ("Pin " + pinID + " updated successfully.");

                } else {
                    System.out.println ("Pin " + pinID + " not updated.");
                }
            } catch (PinterestException e) {
                System.out.println ("Unable to update pin. Rate limited by Pinterest.");
                e.getMessage ();
            }
        }
    }

    public void deletePin(String pinID){
        if (pinterest !=null) {
            try {
                boolean deleted = pinterest.deletePin (pinID);
                if (deleted) {
                    System.out.println ("Pin " + pinID + " deleted sucessfully.");
                } else {
                    System.out.println ("Pin " + pinID + " not deleted.");
                }
            } catch (PinterestException e) {
                System.out.println ("Unable to delete pin. Rate limited by Pinterest.");
                e.getMessage ();
            }
        }
    }


    /**
     *
     * @param pinsPage
     * @return - JMap of jPins (Pin ID, JPin)
     */

    public HashMap<String, JPin> toJPin(ArrayList<Pins> pinsPage) {
        HashMap<String, JPin> jPins = new HashMap<> ();
        for (Pins pinItems: pinsPage) {
            for (Pin pin: pinItems) {
                JPin jPin = new JPin (pin.getId (), pin.getOriginal_link (), pin.getCreator ().getFirstName (), pin.getBoard ().getName (), pin.getNote ());
                jPins.put(jPin.getPinID (), jPin);
            }
        }
        return jPins;
    }

    public void savePosition(String method, String next, String fileName) {
        try{
            File saveDir = new File ("saves");
            saveDir.mkdir ();
            File saveFile = new File(saveDir,fileName);
            if(!saveFile.exists ()) saveFile.createNewFile (); //Creates save file if it does not exist.
            save.setProperty (method, next);
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
                String c = d.substring (d.indexOf ("cursor=")+"cursor=".length ());
//                System.out.println (c);
                cursor = c;
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
        return cursor;
    }



}