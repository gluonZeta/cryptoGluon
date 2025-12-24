package gluon.projects.utilities;

import gluon.projects.exceptions.TechnicalException;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilityTest {

    @Test
    void getPropertiesByFileName() {
        Properties properties = FileUtility.getPropertiesByFileName("application.properties");
        assertNotNull(properties);

        assertThrows(TechnicalException.class, () -> {
            FileUtility.getPropertiesByFileName("applications.properties");
        });
    }
}