package com.andrewjones.logingui.utils;

import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;

import javax.swing.*;

public final class UI {

    public static void addKeybind(@NotNull JFrame frame, @NotNull String key, Action action) {
        ActionMap actionMap = frame.getRootPane().getActionMap();

        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), key);

        actionMap.put(key, action);
    }

}
