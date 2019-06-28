package com.jtv.pinfourofour.models.pin;

public interface JPinDTO {
    String getPinId();

    String getBoard();

    String getLink();

    String getNote();

    int getLinkResponseCode();

    String getLinkRedirectLocation();

    int getLinkRedirectionResponseCode();

    String getAction();
}
