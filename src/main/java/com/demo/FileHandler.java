package com.demo;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Command(name = "File Handler", version = "file 1.0",
        description = "Processes the input file and writes the result to the output file")
public class FileHandler implements Runnable {

    @Parameters(index = "0..*")
    private File[] files;
    @Option(names = "-a")
    private boolean theRegimen;
    @Option(names = "-p")
    private String nameBypassFiles;
    @Option(names = "-s")
    private boolean briefStatistics;
    @Option(names = "-f")
    private boolean completeStatistics;

    private String containsIntCheck(String lineExample) {
        if (lineExample.matches("[0-9]+")) {
            return lineExample;
        }
        return "";
    }

    private int sumCheck(List<String> lines) {
        List<Integer> a = lines
                .stream()
                .map(Integer::parseInt)
                .toList();

        return a.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    private OptionalDouble averageCheck(List<String> lines) {
        List<Integer> a = lines
                .stream()
                .map(Integer::parseInt)
                .toList();

        return a.stream()
                .mapToInt(Integer::intValue)
                .average();
    }

    private Optional<Integer> maxCheck(List<String> lines) {
        List<Integer> a = lines
                .stream()
                .map(Integer::parseInt)
                .toList();

        return a.stream()
                .max(Integer::compareTo);
    }

    private Optional<Integer> minCheck(List<String> lines) {
        List<Integer> a = lines
                .stream()
                .map(Integer::parseInt)
                .toList();

        return a.stream()
                .min(Integer::compareTo);
    }

    private String containsDoubleCheck(String lineExample) {
        if (lineExample.matches("^[+-]?\\d*\\.\\d+([eE][+-]?\\d+)?$")) {
            return lineExample;
        }
        return "";
    }

    private String containsStringCheck(String lineExample) {
        Pattern pattern = Pattern.compile(".*[a-zA-Zа-яА-Я].*");
        Matcher matcher = pattern.matcher(lineExample);
        if (matcher.find() && !isScientificNumber(lineExample)) {
            return matcher.group();
        }
        return "";
    }

    private boolean isScientificNumber(String str) {
        String regex = "^[+-]?\\d*\\.?\\d+([eE][+-]?\\d+)?$";
        return str.matches(regex);
    }

    @Override
    public void run() {
        String integersFile = "";
        String floatsFile = "";
        String stringsFile = "";
        List<String> linesList = new ArrayList<>();
        int count = 0;
        int sum = 0;
        OptionalDouble average = OptionalDouble.empty();
        Optional<Integer> min = Optional.empty();
        Optional<Integer> max = Optional.empty();
        if (nameBypassFiles != null) {
            integersFile = new File(nameBypassFiles + "integers.txt").toString();
            floatsFile = new File(nameBypassFiles + "floats.txt").toString();
            stringsFile = new File(nameBypassFiles + "strings.txt").toString();
        }else {
            integersFile = new File("integers.txt").toString();
            floatsFile = new File("floats.txt").toString();
            stringsFile = new File("strings.txt").toString();
        }
            try (BufferedWriter bwIntegers = new BufferedWriter(new FileWriter(integersFile, theRegimen));
                 BufferedWriter bwFloats = new BufferedWriter(new FileWriter(floatsFile, theRegimen));
                 BufferedWriter bwStrings = new BufferedWriter(new FileWriter(stringsFile, theRegimen))) {
                if (files != null) {
                    for (File file : files) {
                        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                String checkLine;

                                checkLine = containsIntCheck(line);
                                if (!checkLine.isEmpty()) {
                                    linesList.add(line);

                                    sum = sumCheck(linesList);
                                    average = averageCheck(linesList);
                                    min = minCheck(linesList);
                                    max = maxCheck(linesList);

                                    bwIntegers.write((line));
                                    bwIntegers.newLine();
                                    if (briefStatistics) {
                                        count++;
                                    }
                                }

                                checkLine = containsDoubleCheck(line);
                                if (!checkLine.isEmpty()) {
                                    bwFloats.write((line));
                                    bwFloats.newLine();
                                    if (briefStatistics) {
                                        count++;
                                    }
                                }

                                checkLine = containsStringCheck(line);
                                if (!checkLine.isEmpty()) {
                                    bwStrings.write((line));
                                    bwStrings.newLine();
                                    if (briefStatistics) {
                                        count++;
                                    }
                                }
                            }
                        }
                    }
                }
                System.out.println("Brief statistics contain only the number of elements written into outgoing files = " +count);
                System.out.println(sum);
                average.ifPresent(System.out::println);
                min.ifPresent(System.out::println);
                max.ifPresent(System.out::println);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }