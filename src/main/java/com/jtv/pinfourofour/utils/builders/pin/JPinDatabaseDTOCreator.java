package com.jtv.pinfourofour.utils.builders.pin;

import com.jtv.pinfourofour.models.pin.JPin;
import com.jtv.pinfourofour.models.pin.JPinDTO;
import com.jtv.pinfourofour.utils.Configuration;
import com.jtv.pinfourofour.utils.services.NetworkService;

public final class JPinDatabaseDTOCreator {
    private static Configuration configuration = Configuration.getInstance();
//    private static NetworkService nws = new NetworkService ();


    public static JPin createJPin(String pinID, String link, String board, String note ){
        JPin jPin = new JPin (pinID, link, board, note);
        return jPin;
    }

    public static JPinDTO buildDTODirector(JPinDTOBuilder builder, JPin jPin){
        NetworkService nws = new NetworkService();
        nws.execute (jPin.getLink());
        return builder.withPinID (jPin.getPinID ()).withCreator (configuration.getUsername()).withBoard (jPin.getBoard ())
                .withLink (jPin.getLink ())
                .withLinkResponseCode (nws.getLinkResponseCode ())
                .withLinkRedirectLocation (nws.getRedirectLocation ())
                .withLinkRedirectLocationResponseCode (nws.getRedirectLocationResponseCode ())
                .build ();
    }
}
