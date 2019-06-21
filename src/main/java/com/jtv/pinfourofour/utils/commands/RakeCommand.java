package com.jtv.pinfourofour.utils.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(
        commandNames = "rake",
        commandDescription = "Gather pins from pinterest."
)
public class RakeCommand {
    @Parameter(
            names = {"-c", "-continue"},
            description = "Continue from save point."
    )
    public Boolean cont = false;

}
