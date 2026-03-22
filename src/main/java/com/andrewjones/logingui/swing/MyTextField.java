package com.andrewjones.logingui.swing;

import javax.swing.*;

public final class MyTextField extends JTextField {

    private final String placeholder;

    public MyTextField(String placeholder) {
        super(placeholder);
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }
}
