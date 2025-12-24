package gluon.projects.utilities;

import gluon.projects.exceptions.TechnicalException;

import java.io.InputStream;
import java.util.Properties;

public class FileUtility {

    private FileUtility() {
        throw new IllegalStateException("Utility class");
    }

    public static Properties getPropertiesByFileName(String fileName) {
        Properties properties = new Properties();
        try(InputStream inputStream = FileUtility.class.getClassLoader().getResourceAsStream(fileName)) {
            properties.load(inputStream);
        } catch (Exception e) {
            throw new TechnicalException(e);
        }
        return properties;
    }

}
