package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    private Properties properties = new Properties();
    private static final Logger logger = LogManager.getLogger();

    public PropertiesLoader(String propFileName) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propFileName)) {
            if (input == null) {
                logger.error("Unable to find {}", propFileName);
                return;
            }

            properties.load(input);
        } catch (IOException ex) {
            logger.error("An error occurred while performing the task", ex);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
