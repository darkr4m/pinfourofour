package com.jtv.pinfourofour.utils.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(
        commandNames = "status",
        commandDescription = "Check status reports of all links.",
        separators = "="
)
public class StatusReportCommand {
    @Parameter(
            names = {"-e", "--external"},
            description = "Filter external links."
    )
    public boolean filterExternal = false;

    @Parameter(
            names = {"-f", "--filename"},
            description = "File that contains the pin data to check.",
            required = true
    )
    public String fileName;
}
