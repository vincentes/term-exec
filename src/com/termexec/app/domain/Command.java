package com.termexec.app.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Command {

    USERADD(1),
    PASSWD(1),
    SU(1),
    WHOAMI(0),
    PWD(0),
    MKDIR(1, Permission.FOR_DIRECTORY, PermissionConfig.WRITE_OCTALS),
    TOUCH(1, Permission.FOR_DIRECTORY, PermissionConfig.WRITE_OCTALS),
    ECHO(1, Permission.FOR_FILE, PermissionConfig.WRITE_OCTALS),
    LS (1),
    CAT(1, Permission.FOR_FILE, PermissionConfig.READ_OCTALS),
    CD(1),
    RM(1, Permission.FOR_DIRECTORY, Stream.concat(PermissionConfig.EXECUTION_OCTALS.stream(), PermissionConfig.WRITE_OCTALS.stream()).collect(Collectors.toSet())),
    HISTORY(0),
    GREP(1),
    MV(2, Permission.FOR_DIRECTORY, PermissionConfig.WRITE_OCTALS),
    CP(2, Permission.FOR_DIRECTORY, PermissionConfig.WRITE_OCTALS),
    CHMOD(2),
    CHOWN(2);

    private final int numberOfArgs;
    private final Permission permission;
    private final Set<Integer> requirements;

    Command(int numberOfArgs) {
        this.permission = null;
        this.requirements = new HashSet<>();
        this.numberOfArgs = numberOfArgs;
    }

    Command(int numberOfArgs, Permission permission, Set<Integer> requirements) {
        this.permission = permission;
        this.requirements = requirements;
        this.numberOfArgs = numberOfArgs;
    }

    public Set<Integer> getRequirements() {
        return requirements;
    }

    public int getNumberOfArgs() {
        return numberOfArgs;
    }

    public Permission getPermission() {
        return permission;
    }
}
