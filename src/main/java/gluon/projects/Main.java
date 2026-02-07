package gluon.projects;

import gluon.projects.exceptions.TechnicalException;
import gluon.projects.utilities.FileUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Random;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main( String[] args ) throws IOException {
        Properties properties = FileUtility.getPropertiesByFileName("application.properties");
        String filPath = properties.getProperty("filePath");



        long nbLignes = Files.lines(Path.of(filPath)).count();
        logger.info( "Test 0007: {}", nbLignes );
        int randomIndex = new Random().nextInt((int) nbLignes);


        try (BufferedReader reader = Files.newBufferedReader(Path.of(filPath))) {
            String line;
            int currentIndex = 0;

            while ((line = reader.readLine()) != null) {
                if(currentIndex == randomIndex) {
                    logger.info("Symbol: {}", line);
                    logger.info("Position: {}", currentIndex);
                    break;
                }
                currentIndex++;
            }
        } catch (IOException e) {
            throw new TechnicalException(e);
        }
    }
}
