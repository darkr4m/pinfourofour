package com.jtv.pinfourofour.utils.builders.pin;

import com.jtv.pinfourofour.models.pin.JPinDTO;

public interface JPinDTOBuilder {

    JPinDTOBuilder withPinID(String pinID);

    JPinDTOBuilder withBoard(String board);

    JPinDTOBuilder withLink(String link);

    JPinDTOBuilder withNote(String note);

    JPinDTOBuilder withLinkResponseCode(int link);

    JPinDTOBuilder withLinkRedirectLocation(String link);

    JPinDTOBuilder withLinkRedirectLocationResponseCode(int link);

    JPinDTOBuilder withAction(String action);

    JPinDTO build();

    JPinDTO getJPinDTO();
}
