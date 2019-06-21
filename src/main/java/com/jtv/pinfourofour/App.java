package com.jtv.pinfourofour;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import com.jtv.pinfourofour.utils.Config;

/**
 * Hello world!
 *
 */
public class App {
    @Parameter(names={"name", "-n"})
    public String name;

    public static void main( String[] args ) {
        String[] argv = {"-n" ,"max"};
        App app = new App();

        Config config = new Config();
        JCommander jc = JCommander.newBuilder()
                .addObject(app)
                .addCommand("config", config)
                .build();
        jc.parse(argv);
        boolean command = !(jc.getParsedCommand() == null);
        if(command) {
            switch (jc.getParsedCommand()) {
                case "config":
                    if (!config.access_token.isEmpty()) config.setProperty("access_token", config.access_token);
                    if (!config.username.isEmpty()) config.setProperty("username", config.username);
                    break;

                default:
                    System.out.println("nope");
            }
        }
    }

    public void run() {

    }
}
