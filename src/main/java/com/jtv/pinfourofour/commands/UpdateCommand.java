package com.jtv.pinfourofour.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(
        commandNames = "update",
        commandDescription = "Updates pins on Pinterest.",
        separators = "="
)
public class UpdateCommand {
    @Parameter(
            names = {"-f", "--filename"},
            description = "File that contains the pin data to remove.",
            required = true
    )
    public String fileName;
}