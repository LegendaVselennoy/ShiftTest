package com.demo;

import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        new CommandLine(new FileHandler()).execute(args);
    }
}