package gluon.projects;

import gluon.projects.domaine.SymbolCryptoService;
import gluon.projects.infra.*;
import gluon.projects.infra.impl.*;
import gluon.projects.domaine.impl.SymbolCryptoServiceImpl;
import gluon.projects.model.IndicatorsOrderBook;
import gluon.projects.model.IndicatorsOrderFlow;
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
        logger.info( "Programme BEGIN ###############" );
        Properties properties = FileUtility.getPropertiesByFileName("application.properties");
        String listSymbolFile = properties.getProperty("listsymbolfile");
        String orderFlowData = properties.getProperty("orderFlowData");
        String orderBookData = properties.getProperty("orderBookData");

        FileStorageService fileStorageService = new FileStorageServiceImpl(Paths.get(listSymbolFile));
        BinanceSymbolService binanceSymbolService = new BinanceSymbolServiceImpl();


        SymbolCryptoService symbolCryptoService = new SymbolCryptoServiceImpl(fileStorageService, binanceSymbolService);
        List<String> symbols = symbolCryptoService.getOldListSymbol();
        logger.info("Size equal: {}", symbols.size());


        /*
        BinanceWebsocketService binanceWebsocketService;
        for(String symbolLoop: symbols) {
            binanceWebsocketService = new BinanceWebsocketServiceImpl(symbolLoop);
            binanceWebsocketService.launchExchange();
        }

         */

        for(String symbolToProcess: symbols) {
            IndicatorsOrderFlow indicatorsOrderFlow = new IndicatorsOrderFlow();
            IOFService iofService = new IOFServiceImpl();

            IndicatorsOrderBook indicatorsOrderBook = new IndicatorsOrderBook();
            IOBService iobService = new IOBServiceImpl(indicatorsOrderBook, symbolToProcess);
            /*
            int aleatoire = ThreadLocalRandom.current().nextInt(0, symbols.size());

            String symbolToProcess = symbols.get(aleatoire);
             */
            logger.info("exampl crypto: {}", symbolToProcess);
            BinanceWebsocketService binanceWebsocketService = new BinanceWebsocketServiceImpl(symbolToProcess
                    ,indicatorsOrderFlow, iofService, indicatorsOrderBook, iobService);
            binanceWebsocketService.launchExchange();

            String fileName = orderFlowData + symbolToProcess + ".csv";
            FileStorageService fileStorageSymbolService = new FileStorageServiceImpl(Paths.get(fileName));
            OrderFlowFilCsv orderFlowFilCsv = new OrderFlowFilCsv(symbolToProcess,indicatorsOrderFlow, iofService, fileStorageSymbolService);
            Thread thread = new Thread(orderFlowFilCsv);
            thread.start();


            String orderBookFileName = orderBookData + symbolToProcess + ".csv";
            FileStorageService orderBookFileStorageSymbolService = new FileStorageServiceImpl(Paths.get(orderBookFileName));
            OrderBookFilCsv orderBookFilCsv = new OrderBookFilCsv(symbolToProcess,indicatorsOrderBook,iobService,orderBookFileStorageSymbolService);
            Thread threadOb = new Thread(orderBookFilCsv);
            threadOb.start();

        }








        logger.info( "Programme END #################" );
    }
}
