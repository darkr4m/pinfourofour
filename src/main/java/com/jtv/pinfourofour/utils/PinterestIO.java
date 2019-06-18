package com.jtv.pinfourofour.utils;

import com.jtv.pinfourofour.Pinterest;
import com.jtv.pinfourofour.exceptions.PinterestException;
import com.jtv.pinfourofour.fields.pin.PinFields;
import com.jtv.pinfourofour.methods.network.ResponseMessageAndStatusCode;
import com.jtv.pinfourofour.models.JPin;
import com.jtv.pinfourofour.responses.pin.Pin;
import com.jtv.pinfourofour.responses.pin.Pins;

import java.io.BufferedReader;
import java.io.FileReader;
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
    private String cursor;
    private int requestsRemaining;


    public PinterestIO(){
        Properties props = new Properties ();
        try {
            props.load (new BufferedReader (new FileReader ("config/pin.config")));
            pinterest = new Pinterest (props.getProperty ("access_token"));
            System.out.println ("New Pinterest IO opened with access token "+props.getProperty ("access_token"));
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public void getPins(){
        Pins pins = pinterest.getMyPins(new PinFields ().withAll ());

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
            pins.getNextPage ().getCursor ();
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
            cursor = pins.getNextPage ().getCursor ();
            System.out.println ("Pins requested: " + pins + "\n cursor:" + cursor);
            pinsPage.add (pins);
            while (pinterest.getNextPageOfPins (pins.getNextPage ()) != null) {
                pins = pinterest.getNextPageOfPins (pins.getNextPage ());
                cursor = pins.getNextPage ().getCursor ();
                System.out.println ("Pins requested: " + "pin cursor " + cursor);
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

    public String updatePin(String pinID, String board, String note, String link){
        ResponseMessageAndStatusCode response = pinterest.patchPin (pinID, board, note, link);
        return response.toString ();
    }


    /**
     *
     * @param pinsPage
     * @return - Map of jPins (Pin ID, JPin)
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



    /**
     * TODO: save cursor and next to file.
     */
    public void savePosition() {

    }


}