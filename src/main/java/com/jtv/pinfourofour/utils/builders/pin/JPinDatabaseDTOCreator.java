package com.jtv.pinfourofour.utils.builders.pin;

import com.jtv.pinfourofour.models.pin.JPin;
import com.jtv.pinfourofour.models.pin.JPinDTO;
import com.jtv.pinfourofour.utils.services.NetworkService;

public final class JPinDatabaseDTOCreator {

    public static JPin createJPin(String pinID, String creator, String board, String note, String link){
        JPin jPin = new JPin (pinID, creator, board, note, link);
        return jPin;
    }

    public static JPinDTO buildDTODirector(JPinDTOBuilder builder, JPin jPin){
        NetworkService nws = new NetworkService (jPin.getLink ());
        nws.execute ();
        return builder.withPinID (jPin.getPinID ()).withCreator (jPin.getCreator ()).withBoard (jPin.getBoard ())
                .withLink (jPin.getLink ())
                .withLinkResponseCode (nws.getLinkResponseCode ())
                .withLinkRedirectLocation (nws.getRedirectLocation ())
                .withLinkRedirectLocationResponseCode (nws.getRedirectLocationResponseCode ())
                .build ();
    }
}
