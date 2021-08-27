package com.termexec.app.domain;

import java.util.Arrays;
import java.util.List;

public class NavigableRepository {

    private static Folder root;
    private static Folder currentFolder;

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
        Folder folder = new Folder(UserRepository.getCurrentUser());
        folder.setName(name);
        currentFolder.addChild(folder);
        return folder;
    }

    public static List<Navigable> ls() {
        return currentFolder.getChildren();
    }

    public static Folder getFolder(String[] tokens) {
        Folder current = currentFolder;
        for(String token : tokens) {
            boolean foundNext = false;
            for(Navigable navigable : currentFolder.getChildren()) {
                if (navigable instanceof Folder && navigable.getName().equals(token)) {
                    foundNext = true;
                    break;
                }
            }

            if(!foundNext) {
                return null;
            }
        }
        return current;
    }

    public static File getFile(String path) {
        if(!path.startsWith("/")) {
            if(path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }

            String[] tokens = path.split("/");
            String[] tokensDirectory = Arrays.copyOfRange(tokens, 0, tokens.length);
            Folder folder = getFolder(tokensDirectory);
            assert folder != null;
            for(Navigable navigable : folder.getChildren()) {
                if(navigable instanceof File && navigable.getName().equals(tokens[tokens.length - 1])) {
                    return (File) navigable;
                }
            }
            return null;
        }
        return null;
    }

    public static void echo(String content, String path) {
        File file = getFile(path);
        file.writeLine(content);
    }

    public static String cat(String fileName) {
        File file = getFile(fileName);
        return file.getContent();
    }

    public static void cd(String line){
        if (line.equals("..")){
            setCurrentFolder(currentFolder.getParent());
        }else {
            String[] path = line.split("/");
            setCurrentFolder(getFolder(Arrays.copyOf(path,path.length -1)));
        }
    }

    public static void touch(String fileName) {
        File file = new File(UserRepository.getCurrentUser());
        file.setName(fileName);
        currentFolder.addChild(file);
    }

    public static void init() {
        root = new Folder(UserRepository.getCurrentUser(), "");
        currentFolder = root;
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
