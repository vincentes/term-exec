package com.termexec.app.domain;

public class File extends Navigable {
    private String content;

    public File(User user) {
        super(user);
        content = "";
    }

    public void write(String txt) {
        content += txt;
    }

    public void writeLine(String txt) {
        content += txt + "\n";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getName() {
        if(super.getName().endsWith(".txt")) {
            return super.getName();
        } else {
            return super.getName() + ".txt";
        }
    }

}
