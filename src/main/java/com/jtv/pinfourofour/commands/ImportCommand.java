package com.jtv.pinfourofour.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(
        commandNames = "import",
        commandDescription = "Removes pins from Pinterest.",
        separators = "="
)
public class ImportCommand {
    @Parameter(
            names = {"-f", "--filename"},
            description = "File that contains the pin data to remove.",
            required = true
    )
    public String fileName;
}
