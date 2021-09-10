package com.termexec.app.domain;

import jdk.nashorn.internal.ir.CallNode;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import static com.termexec.app.domain.NavigableRepository.rm;

public class CommandExecutor {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("d");

    public static Execution exec(String line) {
        String[] commands = line.split("\\|");
        if(commands.length > 1) {
            History.silent = true;
        }

        Stack<String> concat = new Stack<>();
        for(String command : commands) {
            Execution e = eval(command, concat);
            History.push(line);
            if(e.getResult() != CommandResult.OK) {
                return e;
            }
        }

        return new Execution(CommandResult.OK);
    }

    public static Execution eval(String line, Stack<String> concat)
    {
        String[] tokens = line.trim().split(" ");

        if(tokens.length < 1) {
            return new Execution(CommandResult.NO_COMMAND_FOUND);
        }

        Command command = getCommand(tokens[0]);
        if(command == null) {
            return new Execution(CommandResult.INEXISTENT_COMMAND);
        }

        if(tokens.length < command.getNumberOfArgs() + 1) {
            return new Execution(CommandResult.NOT_ENOUGH_ARGS, command);
        }

        switch(command) {
            case USERADD:
                useradd(tokens[1]);
                break;
            case PASSWD:
                passwd(tokens[1]);
                break;
            case SU:
                su(tokens[1]);
                break;
            case WHOAMI:
                whoami();
                break;
            case PWD:
                pwd();
                break;
            case MKDIR:
                mkdir(tokens[1]);
                break;
            case LS:
                ls(tokens[1]);
                break;
            case TOUCH:
                touch(tokens[1]);
                break;
            case ECHO:
                String content = line.split("\"")[1];
                String path = line.split(" >> ")[1];
//                path = path.split(".txt")[0];
                echo(content, path);
                break;
            case CAT:
                cat(tokens[1]);
                break;
            case CD:
                cd(tokens[1]);
                break;
            case RM:
                rm(tokens[1]);
                break;
            case HISTORY:
                history();
                break;
            case GREP:
                grep(line);
                break;
            case MV:
                mv(tokens[1], tokens[2]);
                break;
        }
        return new Execution(CommandResult.OK, command);
    }

    private static void mv(String origin, String destination) {
        File originFile = NavigableRepository.getFile(origin);
        if(originFile == null) {
            System.out.println("El archivo origen no existe.");
            return;
        }
        Folder originFolder = NavigableRepository.getFolderExcludeFileName(origin);
        Folder destinationFolder = NavigableRepository.getFolder(origin);
        if(destinationFolder == null) {
            System.out.println("La carpeta destino no existe.");
            return;
        }
        originFile.getParent().remove(originFile);
        originFile.setParent(destinationFolder);
        destinationFolder.addChild(originFile);

    }

    private static void grep(String line) {
        List<String> print = new ArrayList<>();
        String[] lines = History.lastCommandText.split("\n");
        String query = line.trim().split(" ")[1];

        for(String l : lines) {
            if(l.contains(query)) {
                print.add(l);
            }
        }

        for(String p : print) {
            System.out.println(p);
        }
    }

    private static void pwd() {
        System.out.println(NavigableRepository.pwd());
    }

    private static void history() {
        String historyText = "";
        for(String command : History.getCommands()) {
            if(!History.silent) {
                System.out.println(command);
            }
            historyText += command + "\n";
        }

        History.register(historyText);
    }

    private static void mkdir(String folderName) {
        NavigableRepository.mkdir(folderName);
    }

    private static void touch(String fileName) {
        NavigableRepository.touch(fileName);
    }

    private static void echo(String content, String path) {
        try {
            NavigableRepository.echo(content,path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void cat(String fileName) {
        try {
            System.out.println(NavigableRepository.cat(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void ls(String command) {
        if(command.equals("-l")) {
            for (Navigable node : NavigableRepository.ls()) {
                System.out.println(node.getPermissions()
                        + "\t" + node.getAuthor()
                        + "\t" + monthFormat.format(node.getDateTime())
                        + "\t" + dayFormat.format(node.getDateTime())
                        + "\t" + timeFormat.format(node.getDateTime())
                        + "\t" + node.getName());
            }
        }else{
            System.out.println("Ingrese un comando válido");
        }
    }

    private static void cd(String path){ NavigableRepository.cd(path); }

    private static void useradd(String username) {
        User user = UserRepository.add(username);
        System.out.println("Usuario " + user.getUsername() + " creado con éxito.");
    }

    private static void passwd(String username) {
        User find = UserRepository.find(username);
        if(find == null) {
            System.out.println("El usuario " + username + " no existe. Ingrese un nombre de usuario válido.");
            return;
        }

        System.out.println("Introduzca la contraseña.");
        String firstInput = scanner.nextLine();
        System.out.println("Introduzca la confirmación de la contraseña.");
        String secondInput = scanner.nextLine();
        if(firstInput.equals(secondInput)) {
            UserRepository.setPassword(find, firstInput);
            System.out.println("La contraseña fue seteada con éxito");
        } else {
            System.out.println("Las contraseñas no coinciden.");
        }
    }

    private static void su(String username) {
        User find = UserRepository.find(username);
        if(find == null) {
            System.out.println("El usuario no existe.");
            return;
        }

        if(find.equals(UserRepository.getCurrentUser())) {
            System.out.println("El usuario ya se encuentra logueado.");
            return;
        }

        System.out.println("Ingrese la contraseña del usuario.");
        String password = scanner.nextLine();
        if(find.getPassword().equals(password)) {
            UserRepository.su(username);
            System.out.println("Se ha logueado al usuario " + username+ ".");
        } else {
            System.out.println("Contraseña inválida.");
        }
    }

    public static void whoami() {
        System.out.println("El usuario autenticado actual es '" + UserRepository.getCurrentUser() + "'.");
    }

    public static Command getCommand(String command) {
        for(Command c : Command.values()) {
            if(c.name().toLowerCase().equals(command.toLowerCase())) {
                return c;
            }
        }
        return null;
    }
}
