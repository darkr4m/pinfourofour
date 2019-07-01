package com.jtv.pinfourofour.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(
        commandNames = "report",
        commandDescription = "Generates a report in CSV format from the database.",
        separators = "="
)
public class ReportCommand {
    @Parameter(
            names = {"-f", "--filename"},
            description = "File that contains the pin data to remove.",
            required = true
    )
    public String fileName;
}
