package com.jtv.pinfourofour.models.pin;

public class JPin {
    private String pinID; // id
    private String board; // name of the board
    private String link; // original_link
    private String note; // note

    /**<b>JPin Constructor</b>
     * Values from Pinterest that are relevant to the functions of this application.
     * To be used for generation of link report.
     *
     * @param pinID - String, ID of the pin
     * @param link - String, the original outbound link of the pin
     * @param board - String, what board the pin belongs to
     * @param note - String, the description or note of the pin
     */
    public JPin(String pinID, String board, String link, String note) {
        this.pinID = pinID;
        this.board = board;
        this.link = link;
        this.note = note;
    }

    //Getters and setters

    public String getPinID() { return pinID; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getBoard() { return board; }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

}
