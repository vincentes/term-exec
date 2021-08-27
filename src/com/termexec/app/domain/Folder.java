package com.termexec.app.domain;

import java.util.ArrayList;
import java.util.List;

public class Folder extends Navigable {
    private List<Navigable> children = new ArrayList<>();

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
