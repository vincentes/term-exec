package com.termexec.app.domain;

import java.security.InvalidParameterException;

public class PermissionConfig {
    private int octalValue;

    public PermissionConfig(int octalValue) {
        validateOctalValue();
        this.octalValue = octalValue;
    }

    public PermissionConfig() {
    }

    public int getOctalValue() {
        return octalValue;
    }

    public void setOctalValue(int octalValue) {
        validateOctalValue();
        this.octalValue = octalValue;
    }

    private void validateOctalValue() {
        assert(isOctalValue(this.octalValue));
    }

    public static boolean isOctalValue(int octalValue) {
        return octalValue >= 0 && octalValue <= 7;
    }
    @Override
    public String toString() {
        switch(octalValue) {
            case 0:
                return "- - -";
            case 1:
                return "- - x";
            case 2:
                return "- w -";
            case 3:
                return "- w x";
            case 4:
                return "r - -";
            case 5:
                return "r - x";
            case 6:
                return "r w -";
            case 7:
                return "r w x";
            default:
                return "";
        }
    }
}
