package com.termexec.app.domain;

import java.util.Stack;

public class History {
    private static Stack<String> commands = new Stack<>();
    public static String lastCommandText;
    public static boolean silent = false;

    public static String push(String item) {
        return commands.push(item);
    }

    public static void register(String item) {
        lastCommandText = item;
    }

    public static String pop() {
        return commands.pop();
    }

    public static String peek() {
        return commands.peek();
    }

    public static boolean empty() {
        return commands.empty();
    }

    public static Stack<String> getCommands() {
        return commands;
    }

    public void setCommands(Stack<String> commands) {
        this.commands = commands;
    }
}
