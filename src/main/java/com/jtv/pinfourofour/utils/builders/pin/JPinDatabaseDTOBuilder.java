package com.jtv.pinfourofour.utils.builders.pin;

import com.jtv.pinfourofour.models.pin.JPinDTO;
import com.jtv.pinfourofour.models.pin.JPinDatabaseDTO;
import com.jtv.pinfourofour.utils.Configuration;

public class JPinDatabaseDTOBuilder implements JPinDTOBuilder {
    private Configuration configuration = Configuration.getInstance ();

    private String pinID;
    private String board;
    private String link;
    private String note;
    private int linkResponseCode;
    private String linkRedirectLocation;
    private int linkRedirectLocationResponseCode;
    private String action;
    private JPinDatabaseDTO dto;

    @Override
    public JPinDTOBuilder withPinID(String pinID) {
        this.pinID = pinID;
        return this;
    }


    @Override
    public JPinDTOBuilder withBoard(String board) {
        this.board = configuration.getUsername ()+"\\"+board.replaceAll ("[^A-Za-z0-9\\s]","").replaceAll (" ","-").toLowerCase ();
        return this;
    }

    @Override
    public JPinDTOBuilder withLink(String link) {
        this.link = link;
        return this;
    }

    @Override
    public JPinDTOBuilder withNote(String note) {
        this.note = note;
        return this;
    }

    @Override
    public JPinDTOBuilder withLinkResponseCode(int linkResponseCode) {
        this.linkResponseCode = linkResponseCode;
        return this;
    }

    @Override
    public JPinDTOBuilder withLinkRedirectLocation(String linkRedirectLocation) {
        this.linkRedirectLocation = linkRedirectLocation;
        return this;
    }

    @Override
    public JPinDTOBuilder withLinkRedirectLocationResponseCode(int linkRedirectLocationResponseCode) {
        this.linkRedirectLocationResponseCode = linkRedirectLocationResponseCode;
        return this;
    }

    @Override
    public JPinDTOBuilder withAction(String action) {
        this.action = action;
        return this;
    }

    @Override
    public JPinDTO build() {
        dto = new JPinDatabaseDTO(pinID, board, link, note, linkResponseCode, linkRedirectLocation, linkRedirectLocationResponseCode, action);
        return dto;
    }

    @Override
    public JPinDTO getJPinDTO() {
        return dto;
    }
}
