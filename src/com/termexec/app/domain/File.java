package com.termexec.app.domain;

public class File extends Navigable {
    private String content;

    public void write(String txt) {
        content += txt;
    }

    public void writeLine(String txt) {
        content += "\n" + txt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
