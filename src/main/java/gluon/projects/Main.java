package gluon.projects;

import gluon.projects.services.SymbolCryptoService;
import gluon.projects.services.SymbolWriter;
import gluon.projects.services.impl.FileSymbolWriter;
import gluon.projects.services.impl.SymbolCryptoServiceImpl;
import gluon.projects.utilities.FileUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main( String[] args ) {
        logger.info( "Programme BEGIN ###############" );
        Properties properties = FileUtility.getPropertiesByFileName("application.properties");
        String listSymbolFile = properties.getProperty("listsymbolfile");
        SymbolWriter symbolWriter = new FileSymbolWriter(Paths.get(listSymbolFile));
        SymbolCryptoService symbolCryptoService = new SymbolCryptoServiceImpl(symbolWriter);
        List<String> symbols = symbolCryptoService.getFreshListSymbol();
        logger.info( "Programme END #################" );
    }
}
