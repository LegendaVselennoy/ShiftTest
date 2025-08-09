package com.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class InfoCheck {

    private final int scale = 2;

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

    public Optional<BigDecimal> maxCheck(List<String> lines) {
        return lines
                .stream()
                .map(BigDecimal::new)
                .max(Comparator.naturalOrder());
    }

    public Optional<Double> minCheck(List<String> lines) {
        return lines
                .stream()
                .map(Double::parseDouble)
                .min(Double::compareTo);

    }

    public BigDecimal sumCheck(List<String> lines) {
        return lines
                .stream()
                .map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal averageCheck(List<String> lines) {
        if (!lines.isEmpty()) {
            return sumCheck(lines)
                    .divide(BigDecimal.valueOf(lines.size()), scale, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}