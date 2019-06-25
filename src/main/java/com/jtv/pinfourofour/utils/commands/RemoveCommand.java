package com.jtv.pinfourofour.utils.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(
        commandNames = "remove",
        commandDescription = "Removes pins from Pinterest.",
        separators = "="
)
public class RemoveCommand {
    @Parameter(
            names = {"-f", "--filename"},
            description = "File that contains the pin data to remove.",
            required = true
    )
    public String fileName;
}
