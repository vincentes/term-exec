package com.termexec.app.domain;

import java.io.FileNotFoundException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
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
        if(UserRepository.getCurrentUser().equals(file.getAuthor())) {
            if(!file.getConfig().canOwnerWrite()) {
                System.out.println("rm: " + file.getName() + ": Permission denied");
                return;
            }
        } else {
            if(!file.getConfig().canOthersWrite()) {
                System.out.println("rm: " + file.getName() + ": Permission denied");
                return;
            }
        }

        currentFolder.getChildren().remove(file);
    }

    public static Folder mkdir(String name) {
        Folder folder = new Folder(UserRepository.getCurrentUser());
        folder.setName(name);
        folder.setParent(currentFolder);

        if(UserRepository.getCurrentUser().equals(folder.getAuthor())) {
            if(!folder.getConfig().canOwnerWrite()) {
                System.out.println("mkdir: " + folder.getName() + ": Permission denied");
                return folder;
            }
        } else {
            if(!folder.getConfig().canOthersWrite()) {
                System.out.println("mkdir: " + folder.getName() + ": Permission denied");
                return folder;
            }
        }

        currentFolder.addChild(folder);
        return folder;
    }

    public static List<Navigable> ls() {
        return currentFolder.getChildren();
    }

    public static Folder getFolder(String path) {
        boolean goBack = false;
        if(path.startsWith("..")) {
            goBack = true;
            path = path.substring(2);
        }

        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        String[] tokens = path.split("/");
        return getFolder(tokens, goBack);
    }

    public static Folder getFolder(String[] tokens, boolean goBack) {
        Folder current;
        if(goBack) {
            current = currentFolder.parent;
            if(tokens[0].equals("")) {
                return current;
            }
        } else {
            current = currentFolder;
        }

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
        boolean goBack = false;
        if(path.startsWith("..")) {
            goBack = true;
            path = path.substring(1);
        }


        if(!path.startsWith("/")) {
            if(path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }

            String[] tokens = path.split("/");
            String[] tokensDirectory = Arrays.copyOfRange(tokens, 0, tokens.length - 1);
            Folder folder = getFolder(tokensDirectory, goBack);
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
        if(UserRepository.getCurrentUser().equals(file.getAuthor())) {
            if(!file.getConfig().canOwnerWrite()) {
                System.out.println("echo: " + file.getName() + ": Permission denied");
                return;
            }
        } else {
            if(!file.getConfig().canOthersWrite()) {
                System.out.println("echo: " + file.getName() + ": Permission denied");
                return;
            }
        }

        if(file == null) {
            throw new FileNotFoundException("The file was not found.");
        }

        file.writeLine(content);
    }

    public static String cat(String fileName) throws FileNotFoundException {
        File file = getFileByName(fileName);

        if(UserRepository.getCurrentUser().equals(file.getAuthor())) {
            if(!file.getConfig().canOwnerRead() || !file.getConfig().canOwnerRead()) {
                System.out.println("cat: " + file.getName() + ": Permission denied");
                return "";
            }
        } else {
            if(!file.getConfig().canOthersRead() || !file.getConfig().canOthersExecute()) {
                System.out.println("cat: " + file.getName() + ": Permission denied");
                return "";
            }
        }

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

            Folder folder = getFolder(path, false);
            if(folder == null) {
                System.out.println("Folder not found.");
                return;
            }
            if(UserRepository.getCurrentUser().equals(folder.getAuthor())) {
                if(!folder.getConfig().canOwnerExecute()) {
                    System.out.println("cd: " + folder.getName() + ": Permission denied");
                    return;
                }
            } else {
                if(!folder.getConfig().canOthersExecute()) {
                    System.out.println("cd: " + folder.getName() + ": Permission denied");
                    return;
                }
            }

            setCurrentFolder(folder);
        }
    }

    public static void touch(String fileName) {
        if(UserRepository.getCurrentUser().equals(NavigableRepository.getCurrentFolder().getAuthor())) {
            if(!NavigableRepository.getCurrentFolder().getConfig().canOwnerWrite()) {
                System.out.println("touch: " + NavigableRepository.getCurrentFolder().getName() + ": Permission denied");
                return;
            }
        } else {
            if(!NavigableRepository.getCurrentFolder().getConfig().canOthersWrite()) {
                System.out.println("touch: " + NavigableRepository.getCurrentFolder().getName() + ": Permission denied");
                return;
            }
        }

        File file = new File(UserRepository.getCurrentUser());
        file.setName(fileName);
        file.setParent(currentFolder);
        currentFolder.addChild(file);
    }

    public static void chown (String username, String fileName) throws FileNotFoundException, UserPrincipalNotFoundException {
        File file = getFileByName(fileName);
        if (file == null){
            System.out.println(fileName + " was not found.");
            return;
        }else {
            User user = UserRepository.find(username);
            if(user == null) {
                System.out.println(username + " does not exists.");
                return;
            } else {
                file.setAuthor(user);
            }
        }
    }

    public static void chmod (int file, int group, int others, String fileName) throws FileNotFoundException {
        PermissionConfig config = new PermissionConfig(file, group, others);

        Navigable navigable = getFileByName(fileName);
        if (navigable == null){
            navigable = getFolder(fileName);
            if(navigable == null) {
                System.out.println(fileName + " was not found.");
                return;
            }
            navigable.setConfig(config);
        }else{
            navigable.setConfig(config);
        }
    }

    public static void init() {
        root = new Folder(UserRepository.getCurrentUser(), "");
        root.setConfig(new PermissionConfig(7,7,7));
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
        //origin = origin.substring(0, origin.indexOf(getFileNameFromPath(origin)));
        return getFolder(origin);
    }
}
