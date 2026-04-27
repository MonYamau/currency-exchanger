package com.project.util;

import com.project.exception.ConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ResourcesUtil {

    private ResourcesUtil() {
    }

    public static Properties getProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream = ResourcesUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new ConfigException("File couldn't be found: " + fileName);
            }
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new ConfigException("Couldn't load the file: " + fileName);
        }
    }

    public static String readFile(String fileName) {
        try (InputStream inputStream = ResourcesUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new ConfigException("File couldn't be found: " + fileName);
            }
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new ConfigException("Couldn't load the file: " + fileName);
        }
    }
}
