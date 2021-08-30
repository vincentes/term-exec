package com.termexec.app.domain;

public enum Command {
    USERADD(1),
    PASSWD(1),
    SU(1),
    WHOAMI(0),
    PWD(0),
    MKDIR(1),
    TOUCH(1),
    ECHO(1),
    LS (1),
    CAT(1),
    CD(1),
    RM(1),
    CHMOD(2),
    CHOWN(2);


    private final int numberOfArgs;

    Command(int numberOfArgs) {
        this.numberOfArgs = numberOfArgs;
    }

    public int getNumberOfArgs() {
        return numberOfArgs;
    }
}
