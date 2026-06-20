package gluon.projects;

import gluon.projects.domaine.SymbolCryptoService;
import gluon.projects.infra.*;
import gluon.projects.infra.impl.*;
import gluon.projects.domaine.impl.SymbolCryptoServiceImpl;
import gluon.projects.utilities.FileUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main( String[] args ) {
        beginProgramme();
        analysOneSymbol();
        endProgramme();
    }

    private static String getSymbolToProcess() {
        Properties properties = FileUtility.getPropertiesByFileName("application.properties");
        String listSymbolFile = properties.getProperty("listsymbolfile");

        FileStorageService fileStorageService = new FileStorageServiceImpl(Paths.get(listSymbolFile));
        BinanceSymbolService binanceSymbolService = new BinanceSymbolServiceImpl();


        SymbolCryptoService symbolCryptoService = new SymbolCryptoServiceImpl(fileStorageService, binanceSymbolService);
        List<String> symbols = symbolCryptoService.getOldListSymbol();
        logger.info("Size equal: {}", symbols.size());
        int aleatoire = ThreadLocalRandom.current().nextInt(0, symbols.size());
        return symbols.get(aleatoire);
    }

    private static void analysOneSymbol() {
        T4JAnalysisServiceImpl t4JAnalysisService = new T4JAnalysisServiceImpl();
        List<String> filteredSymbol = t4JAnalysisService.getFilteredSymbol();
    }

    private static void processAllSymbol() {
        Properties properties = FileUtility.getPropertiesByFileName("application.properties");
        String listSymbolFile = properties.getProperty("listsymbolfile");

        FileStorageService fileStorageService = new FileStorageServiceImpl(Paths.get(listSymbolFile));
        BinanceSymbolService binanceSymbolService = new BinanceSymbolServiceImpl();


        SymbolCryptoService symbolCryptoService = new SymbolCryptoServiceImpl(fileStorageService, binanceSymbolService);
        List<String> symbols = symbolCryptoService.getOldListSymbol();

        T4JAnalysisServiceImpl t4JAnalysisService = new T4JAnalysisServiceImpl();
        for (String symbolToProcess: symbols) {
            t4JAnalysisService.getNewHistoricalData(symbolToProcess);
        }
    }

    private static void beginProgramme() {
        logger.info( "###############################################" );
        logger.info( "############### BEGIN ###############" );
        logger.info( "###############################################" );
        logger.info("\n\n");
    }

    private static void endProgramme() {
        logger.info("\n\n");
        logger.info( "###############################################" );
        logger.info( "############### END Bye Bye #################" );
        logger.info( "###############################################" );
    }
}
