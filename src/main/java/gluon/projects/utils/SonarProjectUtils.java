package gluon.projects.utils;

import gluon.projects.exceptions.ElementProjectException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SonarProjectUtils {

    public static Properties getPropertiesByFileName(String fileName) {
        Properties properties = new Properties();
        try(InputStream inputStream = SonarProjectUtils.class.getClassLoader().getResourceAsStream(fileName)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new ElementProjectException(e);
        }
        return properties;
    }

}
