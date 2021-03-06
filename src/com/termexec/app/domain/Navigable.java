package com.termexec.app.domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class Navigable {
    protected String name;
    protected Folder parent;
    protected User author;
    protected Date dateTime;
    private PermissionConfig config;

    public Navigable(User user) {
        this.config = new PermissionConfig(7,7,7);
        this.dateTime = new Date();
        this.author = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public PermissionConfig getConfig() {
        return config;
    }

    public void setConfig(PermissionConfig config) {
        this.config = config;
    }

    public String getPermissions() {
        return config.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Navigable navigable = (Navigable) o;
        return name.equals(navigable.name) &&
                Objects.equals(parent, navigable.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parent);
    }
}
