package com.jtv.pinfourofour.utils.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(
        commandNames = "status",
        commandDescription = "Check status reports of all links."
)
public class StatusReportCommand {
    @Parameter(
            names = {"-f", "--filter"},
            description = "Filter external links."
    )
    public boolean filterExternal = false;
}
