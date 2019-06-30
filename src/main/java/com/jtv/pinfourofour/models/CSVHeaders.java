package com.jtv.pinfourofour.models;

public enum CSVHeaders {
    PIN_ID("id"),
    BOARD("board"),
    LINK("link"),
    CREATOR("creator"),
    NOTE("note"),
    LINK_RESPONSE_CODE("link_response_code"),
    LINK_REDIRECT_LOCATION("link_redirect_location"),
    LINK_REDIRECT_RESPONSE_CODE("redirect_response_code"),
    ACTION("action");

    public String name;

    CSVHeaders(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
