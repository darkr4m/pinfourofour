package com.jtv.pinfourofour.models;

public enum CSVHeaders {
    PIN_ID("id"),
    LINK("link"),
    CREATOR("creator"),
    BOARD("board"),
    NOTE("note"),
    STATUS("status"),
    REDIR_LINK("redir"),
    REDIR_STATUS("redir_status");

    private String name;

    CSVHeaders(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
