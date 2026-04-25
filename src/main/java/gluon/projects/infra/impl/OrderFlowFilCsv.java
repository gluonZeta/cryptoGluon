package gluon.projects.infra.impl;

import gluon.projects.infra.FileStorageService;
import gluon.projects.infra.IOFService;
import gluon.projects.model.IndicatorsOrderFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class OrderFlowFilCsv implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(OrderFlowFilCsv.class);

    private final String symbol;

    private IndicatorsOrderFlow indicatorsOrderFlow;

    private IOFService iofService;

    private FileStorageService fileStorageSymbolService;

    public OrderFlowFilCsv(String symbol, IndicatorsOrderFlow indicatorsOrderFlow, IOFService iofService, FileStorageService fileStorageSymbolService) {
        this.symbol = symbol;
        this.indicatorsOrderFlow = indicatorsOrderFlow;
        this.iofService = iofService;
        this.fileStorageSymbolService = fileStorageSymbolService;
    }

    @Override
    public void run() {
        logger.info(String.format("XXXXXX %s", this.symbol));
        while(true) {
            try {
                TimeUnit.MINUTES.sleep(5);
                if(indicatorsOrderFlow.getAsksCumulPression() != 0 && indicatorsOrderFlow.getBidsCumulPression() != 0) {
                    String csvLine = this.iofService.getCsvLine(symbol,indicatorsOrderFlow);
                    fileStorageSymbolService.write(csvLine);
                }
                this.iofService.clean(indicatorsOrderFlow);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
