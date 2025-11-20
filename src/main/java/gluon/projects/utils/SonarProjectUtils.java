package gluon.projects.utils;

import gluon.projects.exceptions.TechnicalProjectException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SonarProjectUtils {

    public static Properties getPropertiesByFileName(String fileName) {
        Properties properties = new Properties();
        try(InputStream inputStream = SonarProjectUtils.class.getClassLoader().getResourceAsStream(fileName)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new TechnicalProjectException(e);
        }
        return properties;
    }

}
