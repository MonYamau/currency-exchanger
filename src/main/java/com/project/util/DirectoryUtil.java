package com.project.util;

import com.project.exception.ConfigException;

import java.io.File;

public final class DirectoryUtil {
    private DirectoryUtil () {
    }

    public static String getHomeDirectory() {
        String homeUserDir = System.getProperty("user.home");
        if (homeUserDir == null || homeUserDir.isBlank()) {
            throw new ConfigException("Failed to get the user's home directory path");
        }
        return homeUserDir;
    }

    public static void checkDirectory(String path) {
        File pathFile = new File(path);
        File dir = new File(pathFile.getParent());
        if (!dir.exists()) {
            createDirectory(dir);
        }
    }

    private static void createDirectory(File dir) {
        if (!dir.mkdir()) {
            throw new ConfigException("Couldn't create a directory");
        }
    }
}
