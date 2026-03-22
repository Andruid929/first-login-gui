package com.andrewjones.logingui.utils;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class UI {

    public static void addKeybind(@NotNull JFrame frame, @NotNull String key, KeyStroke keyStroke, Action action) {
        ActionMap actionMap = frame.getRootPane().getActionMap();

        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        inputMap.put(keyStroke, key);

        actionMap.put(key, action);
    }

}
