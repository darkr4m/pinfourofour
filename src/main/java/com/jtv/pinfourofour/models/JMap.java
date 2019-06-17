package com.jtv.pinfourofour.models;

import org.jetbrains.annotations.NotNull;
import java.util.LinkedHashMap;

/** <b>JMap</b>
 * This class is designed to be a holder for <b>JPins</b>.
 * @see JPin
 *
 * TODO: Provide functionality for importing and exporting to CSV files.
 */
public class JMap {
    /**
     * LinkedHashMap <b>JPins</b> -
     * Key - String - The pinID of the JPin
     * Value - JPin - The actual JPin object
     */
    private LinkedHashMap<String, JPin> JPins = new LinkedHashMap<>();

    /** <b>JMap Constructor</b>
     *  Just prints an instantiation confirmation to the console.
     */
    public JMap() {
        System.out.println("New JMap created.");
    }

    /**<b>getJPins</b>
     * Invoking this method prints out a nice list of pins in the console and returns a LinkedHashMap of JPins.
     *
     * @return - JPins - A LinkedHashMap of JPins
     */
    public LinkedHashMap<String, JPin> getJPins() {
        JPins.forEach((k,v) ->  System.out.println(k+" "+v.getLink()));
        return JPins;
    }

    /**<b>add</b>
     * Adds a JPin to the JMap.
     * Checks if the JPin already exists in the JMap.
     * Prints confirmation to the console.
     *
     * @param jPin Add a JPin Object containing information about a pin from Pinterest.
     */
    public void add(@NotNull JPin jPin) {
        String pinID = jPin.getPinID();
        if(!JPins.keySet().contains(pinID)){
            JPins.put(jPin.getPinID(), jPin);
            System.out.println("JPin added.");
        } else {
            System.out.println("A JPin with the id "+pinID+" already exists.");
        }
    }

    /**<b>remove</b> - by pinID only
     * Checks if the JPin exists in the JMap.
     * Removes a JPin from the JMap.
     * Prints confirmation to the console.
     *
     * @param pinID - The pinID as a string.
     */
    public void remove(String pinID){
        if(JPins.keySet().contains(pinID)) {
            JPins.remove(pinID);
            System.out.println("Pin " + pinID + " removed.");
        } else {
            System.out.println("Pin not found.");
        }
    }

    /**<b>remove</b> - JPin
     * Checks if the JPin exists in the JMap.
     * Removes a JPin from the JMap.
     * Prints confirmation to the console.
     *
     * @param jPin - The JPin object to be removed.
     */
    public void remove(@NotNull JPin jPin) {
        String pinID = jPin.getPinID();
        if(JPins.keySet().contains(pinID)) {
            JPins.remove(pinID);
            System.out.println("Pin "+pinID+" removed.");
        } else {
            System.out.println("Pin"+pinID+" not found.");
        }
    }

    /**<b>count</b>
     * Returns a count of records - JPins - in the JMap.
     * Prints the count to the console
     *
     * @return size - Return an integer number of JPins in the JMap
     */
    public int count(){
        int size = JPins.keySet().size();
        System.out.println(size);
        return size;
    }

    /**
     *
     */
    public void csvImport(){

    }

    public void csvExport(){

    }

}
