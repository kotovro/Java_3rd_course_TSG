package services;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigRepository {

    private Properties properties;
    private static ConfigRepository instance;

    public static ConfigRepository getInstance(String configFilePath) {
        if (instance == null) {
            instance = new ConfigRepository(configFilePath);
        }
        return instance;
    }
    // Constructor to load the properties file
    private ConfigRepository(String filePath)  {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        }
        catch (IOException e) {

        }
    }

    // Method to get a property by key
    public String getPropertyValue(String key) {
        return properties.getProperty(key);
    }

    // Method to get a property with a default value if the key is not found
    public String getPropertyValue(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}