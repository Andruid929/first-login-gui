package com.andrewjones.logingui.migrate;

import com.andrewjones.logingui.db.DB;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.*;

public final class MigrateActions {

    @Contract(value = " -> new", pure = true)
    public static @NotNull AbstractAction exporterAction() {
        return new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Runnable exporterRunnable = () -> {

                    try {
                        MigrateData.exportUserData();

                    } catch (SQLException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                };

                Thread.ofVirtual()
                        .name("Exporter-Thread")
                        .start(exporterRunnable);
            }
        };
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull AbstractAction importerAction() {
        return new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Runnable importerRunnable = () -> {
                    try (Connection connection = DB.getDbConnection()) {

                        MigrateData.uploadData(connection);

                    } catch (IOException | SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                };

                Thread.ofVirtual()
                        .name("Importer-Thread")
                        .start(importerRunnable);
            }
        };
    }

}
