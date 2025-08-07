package com.demo;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;

public class InfoCheck {

    public Optional<Integer> minLengthCheck(List<String> lines) {
        return lines
                .stream()
                .map(String::length)
                .min(Integer::compareTo);
    }

    public Optional<Integer> maxLengthCheck(List<String> lines) {
        return lines
                .stream()
                .map(String::length)
                .max(Integer::compareTo);
    }

    public Optional<Double> maxCheck(List<String> lines) {
        return lines
                .stream()
                .map(Double::parseDouble)
                .max(Double::compareTo);
    }


    public Optional<Double> minCheck(List<String> lines) {
        return lines
                .stream()
                .map(Double::parseDouble)
                .min(Double::compareTo);

    }

    public Double sumCheck(List<String> lines) {
        return change(lines).sum();
    }

    public OptionalDouble averageCheck(List<String> lines) {
        return change(lines).average();
    }

    private DoubleStream change(List<String> lines) {
        return lines
                .stream()
                .map(Double::parseDouble)
                .mapToDouble(Double::doubleValue);
    }
}