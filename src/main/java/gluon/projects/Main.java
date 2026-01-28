package gluon.projects;

import gluon.projects.domaine.SymbolCryptoService;
import gluon.projects.infra.BinanceSymbolService;
import gluon.projects.infra.FileStorageService;
import gluon.projects.infra.impl.BinanceSymbolServiceImpl;
import gluon.projects.infra.impl.FileStorageServiceImpl;
import gluon.projects.domaine.impl.SymbolCryptoServiceImpl;
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

        FileStorageService fileStorageService = new FileStorageServiceImpl(Paths.get(listSymbolFile));
        BinanceSymbolService binanceSymbolService = new BinanceSymbolServiceImpl();

        SymbolCryptoService symbolCryptoService = new SymbolCryptoServiceImpl(fileStorageService, binanceSymbolService);
        List<String> symbols = symbolCryptoService.getFreshListSymbol();
        logger.info( "Programme END #################" );
    }
}
