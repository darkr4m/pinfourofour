package com.jtv.pinfourofour.models;

public enum CSVHeaders {
    ACTION("action"),
    PIN_ID("id"),
    BOARD("board"),
    LINK("link"),
    CREATOR("creator"),
    NOTE("note"),
    STATUS("status"),
    REDIR_LINK("redir"),
    REDIR_STATUS("redir_status");

    public String name;

    CSVHeaders(String name){
        this.name = name;
    }

}
