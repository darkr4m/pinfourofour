package com.jtv.pinfourofour.models;

public class JPin {
    private String pinID; // id
    private String link; // original_link
    private String creator; // creator
    private String board; // username/boardname
    private String note; // note
    private String status; // for use when JPin link is being checked with HttpUrlConnection
    private String redir; // location header if link status results in a redirect
    private String redir_status; //status of the redirected link

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
        this(pinID,link,creator,board,note,"","","");
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
     * @param status - String, the status code HttpUrlConnection returns from the original link
     * @param redir - String, location header if link status results in a redirect
     * @param redir_status - String, status of the redirected link
     */
    public JPin(String pinID, String link, String creator, String board, String note, String status, String redir, String redir_status) {
        this.pinID = pinID;
        this.link = link;
        this.creator = creator;
        this.board = board;
        this.note = note;
        this.status = status;
        this.redir = redir;
        this.redir_status = redir_status;
    }

    //Getters and setters

    public String getPinID() { return pinID; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getCreator() { return creator; }

    public String getBoard() { return board; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRedir() { return redir; }
    public void setRedir(String redir) { this.redir = redir; }

    public String getRedir_status() { return redir_status; }

    public void setRedir_status(String redir_status) { this.redir_status = redir_status; }
}
