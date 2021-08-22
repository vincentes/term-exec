package com.termexec.app;

import com.termexec.app.domain.CommandExecutor;
import com.termexec.app.domain.CommandResult;
import com.termexec.app.domain.User;
import com.termexec.app.domain.UserRepository;

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
		while(executing) {
			System.out.print(UserRepository.getCurrentUser().getUsername() + "@$_ ");
			CommandResult result = CommandExecutor.exec(scanner.nextLine());
		}
    }
}
