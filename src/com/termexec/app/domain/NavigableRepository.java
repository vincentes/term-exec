package com.termexec.app.domain;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class NavigableRepository {

    private static Folder root;
    private static Folder currentFolder;

    public static String pwd() {
        Folder folder = currentFolder;
        String route = "";
        Stack<String> directories = new Stack<>();
        directories.push(folder.getName());
        while (folder.getParent() != null)
        {
            folder = folder.getParent();
            directories.push(folder.getName());
        }

        route += directories.pop() + "/";
        while(!directories.isEmpty()) {
            route += directories.pop() + "/";
        }

        return route;

    }

    public static void rm(String fileName) {
        File file = getFileByName(fileName);
        currentFolder.getChildren().remove(file);
    }

    public static Folder mkdir(String name) {
        Folder folder = new Folder(UserRepository.getCurrentUser());
        folder.setName(name);
        folder.setParent(currentFolder);
        currentFolder.addChild(folder);
        return folder;
    }

    public static List<Navigable> ls() {
        return currentFolder.getChildren();
    }

    public static Folder getFolder(String path) {
        if(!path.startsWith("/")) {
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }

            String[] tokens = path.split("/");
            String[] tokensDirectory = Arrays.copyOfRange(tokens, 0, tokens.length);
            return getFolder(tokensDirectory);
        }
        return null;
    }

    public static Folder getFolder(String[] tokens) {

        Folder current = currentFolder;
        for(String token : tokens) {
            boolean foundNext = false;
            for(Navigable navigable : currentFolder.getChildren()) {
                if (navigable instanceof Folder && navigable.getName().equals(token)) {
                    foundNext = true;
                    current = (Folder) navigable;
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
            String[] tokensDirectory = Arrays.copyOfRange(tokens, 0, tokens.length - 1);
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

    public static File getFileByName (String fileName) {
        if(!fileName.endsWith(".txt")) {
            return null;
        }

        for(Navigable navigable : currentFolder.getChildren()) {
            if(navigable.getName().equals(fileName)) {
                return (File) navigable;
            }
        }
        return null;
    }

    public static void echo(String content, String fileName) throws FileNotFoundException {
        File file = getFileByName(fileName);
        if(file == null) {
            throw new FileNotFoundException("The file was not found.");
        }

        file.writeLine(content);
    }

    public static String cat(String fileName) throws FileNotFoundException {
        File file = getFileByName(fileName);
        if(file == null) {
            throw new FileNotFoundException(fileName + " was not found.");
        }
        return file.getContent();
    }

    public static void cd(String line){
        if (line.equals("..") && !currentFolder.equals(root)){
            setCurrentFolder(currentFolder.getParent());
        }else {
            String[] path = line.split("/");
            setCurrentFolder(getFolder(path));
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

    public static String getFileNameFromPath(String path) {
        String[] tokens = path.split("/");
        return tokens[tokens.length - 1];
    }

    public static Folder getFolderExcludeFileName(String origin) {
        origin = origin.substring(0, origin.indexOf(getFileNameFromPath(origin)));
        return getFolder(origin);
    }
}
