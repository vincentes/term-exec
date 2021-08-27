package com.termexec.app.domain;

import java.text.SimpleDateFormat;
import java.util.Scanner;

public class CommandExecutor {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("d");

    public static Execution exec(String line) {
        String[] tokens = line.split(" ");
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
                path = path.split(".txt")[0];
                echo(content, path);
                break;
            case CAT:
                cat(tokens[1]);
                break;
            case CD:
                cd(tokens[1]);
                break;
        }

        return new Execution(CommandResult.OK, command);
    }

    private static void pwd() {
        System.out.println(NavigableRepository.pwd());
    }

    private static void mkdir(String folderName) {
        NavigableRepository.mkdir(folderName);
    }

    private static void touch(String fileName) {
        NavigableRepository.touch(fileName);
    }

    private static void echo(String content, String path) { NavigableRepository.echo(content,path); }

    private static void cat(String fileName) { NavigableRepository.cat(fileName); }

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
