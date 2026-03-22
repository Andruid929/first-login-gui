package com.andrewjones.logingui.normalise;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Normalise {

    @Contract(pure = true)
    public static @NotNull String normaliseName(@NotNull String name) {
        char firstLetter = Character.toUpperCase(name.charAt(0));

        String restOfName = name.substring(1).toLowerCase();

        return String.valueOf(firstLetter).concat(restOfName);
    }

}
