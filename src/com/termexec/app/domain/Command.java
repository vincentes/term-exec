package com.termexec.app.domain;

public enum Command {
    USERADD(1),
    PASSWD(1),
    SU(1),
    WHOAMI(0);

    private final int numberOfArgs;

    Command(int numberOfArgs) {
        this.numberOfArgs = numberOfArgs;
    }

    public int getNumberOfArgs() {
        return numberOfArgs;
    }
}
