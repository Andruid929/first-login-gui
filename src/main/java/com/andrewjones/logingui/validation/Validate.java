package com.andrewjones.logingui.validation;

import com.andrewjones.logingui.form.Gender;
import com.andrewjones.logingui.form.MaritalStatus;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Validate {

    public static final String EMAIL_VALIDATION_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static final String PASSWORD_VALIDATION_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*.]).*$";

    @Contract(pure = true)
    public static boolean invalidEmail(@NotNull String email) {
        return !email.matches(EMAIL_VALIDATION_REGEX);
    }

    public static boolean weakPassword(char[] password) {
        String passwordAsString = Stream.of(password)
                .map(String::valueOf)
                .collect(Collectors.joining());

        return !passwordAsString.matches(PASSWORD_VALIDATION_REGEX);
    }

    @Contract(pure = true)
    public static boolean invalidPasswordLength(char @NotNull [] password) {
        return password.length < 8 || password.length > 32;
    }

    @Contract(pure = true)
    public static boolean isTooLong(@NotNull String input) {
        return input.length() > 30;
    }

    public static @NotNull Gender validGender(@NotNull String gender) {
        for (Gender g : Gender.values()) {

            if (gender.equalsIgnoreCase(g.getValue()) || gender.equalsIgnoreCase(g.getAlternativeValue())) {
                return g;
            }
        }

        throw new IllegalArgumentException("Unknown gender");
    }

    public static @NotNull MaritalStatus validMaritalStatus(@NotNull String maritalStatus) {
        for (MaritalStatus m : MaritalStatus.values()) {

            if (maritalStatus.equalsIgnoreCase(m.getStatus()) || maritalStatus.equalsIgnoreCase(m.getStatusShort())) {
                return m;
            }
        }

        throw new IllegalArgumentException("Unknown marital status");
    }

    @Contract(pure = true)
    public static void validateDateOfBirth(@NotNull String dob) throws IllegalArgumentException {
        String[] dateMonthYear = dob.split("-");

        if (dateMonthYear.length != 3) {
            throw new IllegalArgumentException("Check your date of birth input");
        }

        int date;
        int month;
        int year;

        try {
            year = Integer.parseInt(dateMonthYear[0]);
            month = Integer.parseInt(dateMonthYear[1]);
            date = Integer.parseInt(dateMonthYear[2]);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Birthday info is numbers only, check your input");
        }

        int currentYear = LocalDate.now().getYear();

        if (year >= (currentYear - 18)) {
            throw new IllegalArgumentException("You are not old enough for this");

        } else if (year < 1900) {
            throw new IllegalArgumentException("You are too old for this lol");
        }

        if (!isValidDate(date, month, year)) {
            throw new IllegalArgumentException("Birthday info is invalid, check input");
        }
    }

    private static boolean isValidDate(int date, int month, int year) {
        try {

            YearMonth yearMonth = YearMonth.of(year, month);

            int maxDayOfMonth = yearMonth.lengthOfMonth();

            return date >= 1 && date <= maxDayOfMonth;

        } catch (DateTimeException ignored) {
            return false;
        }
    }
}
