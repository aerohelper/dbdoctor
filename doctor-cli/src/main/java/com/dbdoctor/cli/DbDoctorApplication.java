package com.dbdoctor.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

/** Root command / CLI entrypoint for dbdoctor. */
@Command(
        name = "dbdoctor",
        mixinStandardHelpOptions = true,
        version = "dbdoctor 0.1.0",
        description = "Open-source Databricks workspace health analyzer and diagnostics CLI.",
        subcommands = {ScanCommand.class}
)
public class DbDoctorApplication implements Runnable {

    @Override
    public void run() {
        // No subcommand given — print help.
        new CommandLine(this).usage(System.out);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new DbDoctorApplication()).execute(args);
        System.exit(exitCode);
    }
}
