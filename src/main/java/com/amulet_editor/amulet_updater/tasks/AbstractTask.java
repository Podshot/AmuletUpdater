package com.amulet_editor.amulet_updater.tasks;

import com.amulet_editor.amulet_updater.ui.UpdateUI;

import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public abstract class AbstractTask {

    public AbstractTask() { }

    public abstract boolean runTask(String[] args, Map<String, Object> environment);

    public abstract String getTaskID();

    public void reportError(Throwable e, File workingDirectory) {
        File logFile = Paths.get(workingDirectory.getAbsolutePath(), "updater.log").toFile();
        StringWriter sw = new StringWriter();
        String errorLog = null;
        try {
            PrintWriter pw = new PrintWriter(sw);
            pw.println("=== Exception while executing " + this.getTaskID() + " ===");
            pw.println();
            e.printStackTrace(pw);
            pw.flush();
            errorLog = sw.toString();
            pw.close();

            Files.write(logFile.toPath(), errorLog.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (errorLog != null) {
            JOptionPane.showMessageDialog(UpdateUI.getInstanceComponent(), errorLog, "An Error has Occurred", JOptionPane.ERROR_MESSAGE);
        }
    }
}
