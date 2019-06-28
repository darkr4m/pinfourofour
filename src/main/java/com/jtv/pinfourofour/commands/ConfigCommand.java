package com.jtv.pinfourofour.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(
        commandNames = "config",
        commandDescription = "Configuration options",
        separators = "="
)
public class ConfigCommand {
    //Pinterest command line parameters
    @Parameter(
            names = { "-t","--token" },
            description = "access token"

    )
    public String access_token;

    @Parameter(
            names = { "-u", "--username" },
            description = "username"

    )
    public String username;
}
