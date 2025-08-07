package com.demo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidator {

    public String containsIntCheck(String lineExample) {
        if (lineExample.matches("[0-9]+")) {
            return lineExample;
        }
        return "";
    }

    public String containsDoubleCheck(String lineExample) {
        if (lineExample.matches("^[+-]?\\d*\\.\\d+([eE][+-]?\\d+)?$")) {
            return lineExample;
        }
        return "";
    }

    public String containsStringCheck(String lineExample) {
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
}