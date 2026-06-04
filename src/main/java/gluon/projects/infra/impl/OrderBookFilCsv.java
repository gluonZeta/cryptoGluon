package gluon.projects.infra.impl;

import gluon.projects.infra.FileStorageService;
import gluon.projects.infra.IOBService;
import gluon.projects.model.IndicatorsOrderBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class OrderBookFilCsv implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(OrderBookFilCsv.class);

    private IOBService iobService;

    private final String symbol;

    private IndicatorsOrderBook indicatorsOrderBook;

    FileStorageService orderBookFileStorageSymbolService;

    public OrderBookFilCsv(String symbol, IndicatorsOrderBook indicatorsOrderBook, IOBService iobService
            , FileStorageService orderBookFileStorageSymbolService) {
        this.iobService = iobService;
        this.symbol = symbol;
        this.indicatorsOrderBook = indicatorsOrderBook;
        this.orderBookFileStorageSymbolService = orderBookFileStorageSymbolService;
    }

    @Override
    public void run() {
        logger.info(String.format("OrderBook %s", this.symbol));
        while(true) {
            try {
                TimeUnit.MINUTES.sleep(1);
                String csvLine = this.iobService.getCsvLine(symbol,indicatorsOrderBook);
                this.orderBookFileStorageSymbolService.write(csvLine);
                iobService.cleanIndicatorsOrderBook();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
