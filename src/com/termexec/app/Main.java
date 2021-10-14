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
		User user = new User("root", true);
		user.setPassword("1234");
		UserRepository.add(user);
		UserRepository.su(user.getUsername());
		NavigableRepository.init();
		NavigableRepository.mkdir("a");
		NavigableRepository.mkdir("b");
		NavigableRepository.cd("a");
		NavigableRepository.touch("archivoA.txt");
		NavigableRepository.cd("..");
		while(executing) {
			System.out.print(UserRepository.getCurrentUser().getUsername() + "@$_ ");
			Execution result = CommandExecutor.exec(scanner.nextLine());
			switch (result.getResult()) {
				case NOT_ENOUGH_ARGS:
					System.out.println("El comando requiere más parametros.");
					break;
				case INEXISTENT_COMMAND:
					System.out.println("No se reconoce el comando introducido.");
					break;
			}
		}
    }
}
