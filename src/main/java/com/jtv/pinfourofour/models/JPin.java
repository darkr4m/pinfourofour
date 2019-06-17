package com.jtv.pinfourofour.models;

public class JPin {
    private String pinID; // id
    private String link; // original_link
    private String creator; // creator
    private String board; // username/boardname
    private String note; // note
    private int status; // for use when JPin link is being checked with HttpUrlConnection
    private String redir; // location header if link status results in a redirect

    /** <b>JPin Constructor</b>
     * Values from Pinterest that are relevant to the functions of this application.
     *
     * @param pinID - String, ID of the pin
     * @param link - String, the original outbound link of the pin
     * @param creator - String, the creator/owner of the pin
     * @param board - String, what board the pin belongs to on pinfourofour.
     * @param note - String, the description or note of the pin
     *
     */
    public JPin(String pinID, String link, String creator, String board, String note) {
        this(pinID,link,creator,board,note,0,"");
    }

    /**<b>JPin Constructor</b>
     * Values from Pinterest that are relevant to the functions of this application.
     * To be used for generation of link report.
     *
     * @param pinID - String, ID of the pin
     * @param link - String, the original outbound link of the pin
     * @param creator - String, the creator/owner of the pin
     * @param board - String, what board the pin belongs to
     * @param note - String, the description or note of the pin
     * @param status - Integer, the status code HttpUrlConnection returns from the original link
     * @param redir - String, location header if link status results in a redirect
     */
    public JPin(String pinID, String link, String creator, String board, String note, int status, String redir) {
        this.pinID = pinID;
        this.link = link;
        this.creator = creator;
        this.board = board;
        this.note = note;
        this.status = status;
        this.redir = redir;
    }

    //Getters and setters

    /**<b>getPinID</b>
     * This value cannot change, and thus does not have a partnered setter method.
     * Pinterest does not allow users to change this value.
     *
     * @return String, PinID - the ID of the pin from Pinterest
     */
    public String getPinID() { return pinID; }

    /**<b>getLink</b>
     * Has partner setter method.
     *
     * @return String, link - The original outbound link of the pin
     */
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getCreator() { return creator; }

    public String getBoard() { return board; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getRedir() { return redir; }
    public void setRedir(String redir) { this.redir = redir; }


}
