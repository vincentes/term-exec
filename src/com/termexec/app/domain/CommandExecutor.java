package com.termexec.app.domain;

import java.util.Scanner;

public class CommandExecutor {
    private static final Scanner scanner = new Scanner(System.in);

    public static CommandResult exec(String line) {
        String[] tokens = line.split(" ");
        if(tokens.length < 1) {
            return CommandResult.NO_COMMAND_FOUND;
        }

        Command command = getCommand(tokens[0]);
        if(command == null) {
            return CommandResult.INEXISTENT_COMMAND;
        }

        if(tokens.length < command.getNumberOfArgs() + 1) {
            return CommandResult.NOT_ENOUGH_ARGS;
        }

        switch(command) {
            case USERADD:
                User user = UserRepository.add(tokens[1]);
                System.out.println("Usuario " + user.getUsername() + " creado con éxito.");
                break;
            case PASSWD:
                User find = UserRepository.find(tokens[1]);
                if(find == null) {
                    System.out.println("El usuario " + tokens[1] + " no existe. Ingrese un nombre de usuario válido.");
                    break;
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
                break;
            case SU:
                find = UserRepository.find(tokens[1]);
                if(find == null) {
                    System.out.println("El usuario no existe.");
                    break;
                }

                if(find.equals(UserRepository.getCurrentUser())) {
                    System.out.println("El usuario ya se encuentra logueado.");
                    break;
                }

                System.out.println("Ingrese la contraseña del usuario.");
                String password = scanner.nextLine();
                if(find.getPassword().equals(password)) {
                    UserRepository.su(tokens[1]);
                    System.out.println("Se ha logueado al usuario " + tokens[1] + ".");
                } else {
                    System.out.println("Contraseña inválida.");
                }
                break;
        }

        return CommandResult.OK;
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
