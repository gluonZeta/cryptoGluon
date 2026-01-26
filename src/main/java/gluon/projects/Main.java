package gluon.projects;

import gluon.projects.utilities.FileUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main( String[] args ) {
        Properties properties = FileUtility.getPropertiesByFileName("application.properties");
        String filPath = properties.getProperty("filePath");

        logger.info( "Test 0007" );


        try (BufferedReader reader = Files.newBufferedReader(Path.of(filPath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if(line.contains("dog")) {
                    logger.info(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
