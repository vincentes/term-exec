package com.termexec.app.domain;

import java.util.List;

public class NavigableRepository {

    private static Folder root = new Folder();
    private static Folder currentFolder = new Folder();

    public static String pwd() {
        Folder folder = currentFolder;
        String route = "/";
        while (folder.getParent() != null)
        {
            route = folder.getParent() + "/" + route;
            folder = folder.getParent();
        }
        return route;
    }

    public static Folder mkdir(String name) {
        Folder folder = new Folder();
        folder.setName(name);
        currentFolder.addChild(folder);
        return folder;
    }

    public static List<Navigable> ls() {
        return currentFolder.getChildren();
    }

    public static void touch(String fileName) {
        File file = new File();
        file.setName(fileName);
        currentFolder.addChild(file);
    }

    public static Folder getRoot() {
        return root;
    }

    public static void setRoot(Folder root) {
        NavigableRepository.root = root;
    }

    public static Folder getCurrentFolder() {
        return currentFolder;
    }

    public static void setCurrentFolder(Folder currentFolder) {
        NavigableRepository.currentFolder = currentFolder;
    }

}
