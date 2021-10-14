package com.termexec.app.domain;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PermissionConfig {

    public static Set<Integer> READ_OCTALS = new HashSet<Integer>(Arrays.asList(4,5,6,7));
    public static Set<Integer> WRITE_OCTALS = new HashSet<Integer>(Arrays.asList(2,3,6,7));
    public static Set<Integer> EXECUTION_OCTALS = new HashSet<Integer>(Arrays.asList(1,3,5,7));

    private int fileOwner;
    private int groupMembers;
    private int others;

    public PermissionConfig(int fileOwner, int groupMembers, int others) {
        this.fileOwner = fileOwner;
        this.groupMembers = groupMembers;
        this.others = others;
        validateOctalValues();
    }

    public PermissionConfig() {

    }

    public int getFileOwner() {
        return fileOwner;
    }

    public void setFileOwner(int fileOwner) {
        this.fileOwner = fileOwner;
    }

    public int getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(int groupMembers) {
        this.groupMembers = groupMembers;
    }

    public boolean canOthersRead() {
        switch(this.others) {
            case 4:
            case 5:
            case 6:
            case 7:
                return true;
            default:
                return false;
        }
    }

    public boolean canOwnerRead() {
        switch (this.fileOwner) {
            case 4:
            case 5:
            case 6:
            case 7:
                return true;
            default:
                return false;
        }
    }

    public boolean canOwnerWrite() {
        switch(this.fileOwner) {
            case 2:
            case 3:
            case 6:
            case 7:
                return true;
            default:
                return false;
        }
    }

    public boolean canOwnerExecute() {
        switch(this.fileOwner) {
            case 1:
            case 3:
            case 5:
            case 7:
                return true;
            default:
                return false;
        }
    }


    public boolean canOthersWrite() {
        switch(this.others) {
            case 2:
            case 3:
            case 6:
            case 7:
                return true;
            default:
                return false;
        }
    }

    public boolean canOthersExecute() {
        switch(this.others) {
            case 1:
            case 3:
            case 5:
            case 7:
                return true;
            default:
                return false;
        }
    }



    public int getOthers() {
        return others;
    }

    public void setOthers(int others) {
        this.others = others;
    }

    private void validateOctalValues() {
        assert(isOctalValue(this.fileOwner) && isOctalValue(this.groupMembers) && isOctalValue(this.others));
    }

    public static boolean isOctalValue(int octalValue) {
        return octalValue >= 0 && octalValue <= 7;
    }

    public static boolean validateOctalValues(int file, int group, int others) {
        return isOctalValue(file) && isOctalValue(group) && isOctalValue(others);
    }

    private String octalToString(int octal) {
        switch(octal) {
            case 0:
                return "---";
            case 1:
                return "--x";
            case 2:
                return "-w-";
            case 3:
                return "-wx";
            case 4:
                return "r--";
            case 5:
                return "r-x";
            case 6:
                return "rw-";
            case 7:
                return "rwx";
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return octalToString(this.fileOwner) + octalToString(this.groupMembers) + octalToString(this.others);
    }
}
