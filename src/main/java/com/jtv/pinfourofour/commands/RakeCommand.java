package com.jtv.pinfourofour.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(
        commandNames = "rake",
        commandDescription = "Gather pins from pinterest.",
        separators = "="
)
public class RakeCommand {
    @Parameter(
            names = {"-c", "--continue"},
            description = "Continue from save point."
    )
    public Boolean cont = false;

}
