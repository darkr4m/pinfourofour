package com.jtv.pinfourofour.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(
        commandNames = "update",
        commandDescription = "Updates pins on Pinterest and in the database. Adds the pin to the database if it does not exist.",
        separators = "="
)
public class UpdateCommand {
    @Parameter(
            names = {"-f", "--filename"},
            description = "CSV File that contains the pin data to update.",
            required = true
    )
    public String fileName;
}