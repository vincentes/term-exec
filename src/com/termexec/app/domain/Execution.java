package com.termexec.app.domain;

public class Execution {
    private CommandResult result;
    private Command command;

    public Execution(CommandResult result) {
        this.result = result;
    }

    public Execution(CommandResult result, Command command) {
        this.result = result;
        this.command = command;
    }

    public CommandResult getResult() {
        return result;
    }

    public void setResult(CommandResult result) {
        this.result = result;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }
}
