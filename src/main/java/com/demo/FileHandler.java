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
    int count = 0;
    double sum = 0.0;
    private OptionalDouble average;
    private Optional<Double> min;
    private Optional<Double> max;
    private Optional<Integer> minLength;
    private Optional<Integer> maxLength;
    private final List<String> linesList = new ArrayList<>();
    private final List<String> linesListString = new ArrayList<>();
    private final InfoCheck infoCheck;
    private final RegexValidator regexValidator;

    public FileHandler() {
        this.infoCheck = new InfoCheck();
        this.regexValidator = new RegexValidator();
    }

    private void viewStatistics(boolean completeStat, boolean briefStat) {
        if (!completeStat) {
            System.out.println("Brief statistics contain only the number of elements written into outgoing files = " + count);
        }
        if (!briefStat) {
            System.out.println("Sum value: " + sum);
            System.out.println("Average value: " + average.orElse(0.0));
            System.out.println("Min value: " + min.orElse(0.0));
            System.out.println("Max value: " + max.orElse(0.0));
            System.out.println("Min length string: " + minLength.orElse(0));
            System.out.println("Max length string: " + maxLength.orElse(0));
        }
    }

    @Override
    public void run() {
        String integersFile;
        String floatsFile;
        String stringsFile;

        if (nameBypassFiles != null) {
            integersFile = new File(nameBypassFiles + "integers.txt").toString();
            floatsFile = new File(nameBypassFiles + "floats.txt").toString();
            stringsFile = new File(nameBypassFiles + "strings.txt").toString();
        } else {
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

                            checkLine = regexValidator.containsIntCheck(line);
                            if (!checkLine.isEmpty()) {
                                linesList.add(line);
                                bwIntegers.write((line));
                                bwIntegers.newLine();
                                if (briefStatistics) {
                                    count++;
                                }
                            }

                            checkLine = regexValidator.containsDoubleCheck(line);
                            if (!checkLine.isEmpty()) {
                                linesList.add(line);

                                sum = infoCheck.sumCheck(linesList);
                                average = infoCheck.averageCheck(linesList);
                                min = infoCheck.minCheck(linesList);
                                max = infoCheck.maxCheck(linesList);

                                bwFloats.write((line));
                                bwFloats.newLine();
                                if (briefStatistics) {
                                    count++;
                                }
                            }

                            checkLine = regexValidator.containsStringCheck(line);
                            if (!checkLine.isEmpty()) {
                                linesListString.add(line);

                                minLength = infoCheck.minLengthCheck(linesListString);
                                maxLength = infoCheck.maxLengthCheck(linesListString);

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
            if (!theRegimen) {
                viewStatistics(completeStatistics, briefStatistics);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}