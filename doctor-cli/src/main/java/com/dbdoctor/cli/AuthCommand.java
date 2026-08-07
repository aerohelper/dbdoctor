package com.dbdoctor.cli;

import picocli.CommandLine.Command;

/** Parent command for authentication-related subcommands. */
@Command(
        name = "auth",
        description = "Authentication utilities.",
        subcommands = {AuthTestCommand.class}
)
public class AuthCommand {
}
