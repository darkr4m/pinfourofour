package com.jtv.pinfourofour;

import com.beust.jcommander.JCommander;

import com.beust.jcommander.ParameterException;
import com.jtv.pinfourofour.models.Datasource;
import com.jtv.pinfourofour.commands.*;
import com.jtv.pinfourofour.utils.services.NetworkService;

/** Pin404
 *  An internal application for Pinterest Pin management.
 *  An access token is required for use.
 *
 *  Please see the Pinterest Developers API documentation for full instructions on how to obtain a token via Postman.
 *  Generating access tokens is not supported in this application.
 */
public class App {
    public static void main( String[] args ) {
        Datasource data = Datasource.getInstance();
        CommandMethods cmds = new CommandMethods();
        if(!data.open()){
            System.out.println("Could not establish a connection to the database.");
            return;
        }
        String[] argv = {"import", "-f=allPins.csv"};
        App app = new App();
        ConfigCommand config = new ConfigCommand();
        RakeCommand rake = new RakeCommand ();
        StatusCommand status = new StatusCommand();
        UpdateCommand update = new UpdateCommand();
        ImportCommand imprt = new ImportCommand ();
        ReportCommand report = new ReportCommand ();
        RemoveCommand remove = new RemoveCommand();
        JCommander jc = JCommander.newBuilder()
                .addObject(app)
                .addCommand("config", config)
                .addCommand ("rake", rake)
                .addCommand ("status",status)
                .addCommand ("import", imprt)
                .addCommand ("report", report)
                .addCommand("remove", remove)
                .addCommand("update", update)
                .build();
        try {
            jc.parse(argv);
        } catch (ParameterException e){
            System.out.println(e.getMessage());
            e.usage();
        }
        boolean command = (jc.getParsedCommand() != null);
        if(command) {
            switch (jc.getParsedCommand()) {
                case "config":
                    cmds.configure(config.access_token, config.username);
                    break;
                case "rake":
                    cmds.rake(rake.cont);
                    break;
                case "status":
                    cmds.status(status.filterExternal);
                    break;
                case "update":
                    cmds.updatePins(update.fileName);
                    break;
                case "import":
                    cmds.importCSVToDatabase (imprt.fileName);
                    break;
                case "report":
                    cmds.report (report.fileName);
                    break;
                case "remove":
                    cmds.removePins(remove.fileName);
                    break;
                default:
                    System.out.println("Nope. This command is not supported.");
                    jc.usage();
            }
        }

        data.close();
    }
}
