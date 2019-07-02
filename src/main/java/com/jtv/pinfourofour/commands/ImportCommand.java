package com.jtv.pinfourofour.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(
        commandNames = "import",
        commandDescription = "Imports data from a CSV file to the database. Ensure your headers are correct!",
        separators = "="
)
public class ImportCommand {
    @Parameter(
            names = {"-f", "--filename"},
            description = "CSV File that contains the pin data to import.",
            required = true
    )
    public String fileName;
}
