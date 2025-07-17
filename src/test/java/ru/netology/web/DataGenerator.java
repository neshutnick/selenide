package ru.netology.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataGenerator {
    private DataGenerator() {}

    public static String generateDate(int daysToAdd) {
        return LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
} 