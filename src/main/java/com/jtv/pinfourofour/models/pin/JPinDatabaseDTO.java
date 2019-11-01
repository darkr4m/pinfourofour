package com.jtv.pinfourofour.models.pin;

public class JPinDatabaseDTO implements JPinDTO {
    private String pinID;
    private String board;
    private String link;
    private String note;
    private int linkResponseCode;
    private String linkRedirectLocation;
    private int linkRedirectionResponseCode;
    private String action;

    public JPinDatabaseDTO(String pinID){
        this.pinID = pinID;
    }

    public JPinDatabaseDTO(String pinID, String board, String link, String note, int linkResponseCode, String linkRedirectLocation, int linkRedirectionResponseCode, String action) {
        this.pinID = pinID;
        this.board = board;
        this.link = link;
        this.note = note;
        this.linkResponseCode = linkResponseCode;
        this.linkRedirectLocation = linkRedirectLocation;
        this.linkRedirectionResponseCode = linkRedirectionResponseCode;
        this.action = action;
    }

    public String getPinId() {
        return pinID;
    }

    public String getBoard() {
        return board;
    }

    public String getLink() {
        return link;
    }

    public String getNote() {
        return note;
    }

    public int getLinkResponseCode() {
        return linkResponseCode;
    }

    public String getLinkRedirectLocation() {
        return linkRedirectLocation;
    }

    public int getLinkRedirectionResponseCode() {
        return linkRedirectionResponseCode;
    }

    public String getAction() {
        return action;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setLinkResponseCode(int linkResponseCode) {
        this.linkResponseCode = linkResponseCode;
    }

    public void setLinkRedirectLocation(String linkRedirectLocation) {
        this.linkRedirectLocation = linkRedirectLocation;
    }

    public void setLinkRedirectionResponseCode(int linkRedirectionResponseCode) {
        this.linkRedirectionResponseCode = linkRedirectionResponseCode;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString(){
        return "Pin ID: "+pinID+
                "\nBoard: "+board+
                "\nLink: "+link+
                "\nNote: "+note+
                "\nResponse Code: "+linkResponseCode+
                "\nRedirect: "+linkRedirectLocation+
                "\nRedirect Response Code: "+linkRedirectionResponseCode;
    }
}
