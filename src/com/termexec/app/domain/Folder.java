package com.termexec.app.domain;

import java.util.ArrayList;
import java.util.List;

public class Folder extends Navigable {
    private Navigable parent;
    private List<Navigable> children = new ArrayList<>();

    public Navigable getParent() {
        return parent;
    }

    public void setParent(Navigable parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Navigable> getChildren() {
        return children;
    }

    public void setChildren(List<Navigable> children) {
        this.children = children;
    }

    public boolean addChild(Navigable navigable) {
        return children.add(navigable);
    }
}
