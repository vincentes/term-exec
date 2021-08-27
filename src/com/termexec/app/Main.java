package com.termexec.app;

import com.termexec.app.domain.*;

import java.awt.*;
import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws IOException {
		// Args[0]
		Scanner scanner = new Scanner(System.in);
		boolean executing = true;
		User user = UserRepository.add("root");
		UserRepository.su(user.getUsername());
		NavigableRepository.mkdir("");
		while(executing) {
			System.out.print(UserRepository.getCurrentUser().getUsername() + "@$_ ");
			Execution result = CommandExecutor.exec(scanner.nextLine());
			switch (result.getResult()) {
				case NOT_ENOUGH_ARGS:
					System.out.println("El comando requiere de dos parametros.");
					break;
				case INEXISTENT_COMMAND:
					System.out.println("No se reconoce el comando introducido.");
					break;
			}
		}
    }
}
