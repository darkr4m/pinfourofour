package com.jtv.pinfourofour.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(
        commandNames = "status",
        commandDescription = "Check status reports of all links.",
        separators = "="
)
public class StatusCommand {
    @Parameter(
            names = {"-e", "--external"},
            description = "Filter external links."
    )
    public boolean filterExternal = false;

}
