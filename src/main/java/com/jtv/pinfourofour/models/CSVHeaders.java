package com.jtv.pinfourofour.models;

public enum CSVHeaders {
    PIN_ID("id"),
    BOARD("board"),
    LINK("link"),
    CREATOR("creator"),
    NOTE("note"),
    STATUS("link_response_code"),
    REDIR_LINK("redirect_location"),
    REDIR_STATUS("redirect_response_code"),
    ACTION("action");

    public String name;

    CSVHeaders(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
