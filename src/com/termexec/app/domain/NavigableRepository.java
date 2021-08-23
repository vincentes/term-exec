package com.termexec.app.domain;

public class NavigableRepository {

    private static Folder root = new Folder();
    private static Folder currentFolder = new Folder();

    public static String pwd() {
        return currentFolder.getName();
    }

    public static Folder mkdir(String name) {
        Folder folder = new Folder();
        folder.setName(name);
        currentFolder.addChild(folder);
        currentFolder = folder;
        return folder;
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
