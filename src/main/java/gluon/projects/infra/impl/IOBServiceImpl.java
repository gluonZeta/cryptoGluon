package gluon.projects.infra.impl;

import gluon.projects.infra.IOBService;
import gluon.projects.model.IndicatorsOrderBook;
import gluon.projects.model.OrderBookData;
import gluon.projects.model.OrderBookOrderInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOBServiceImpl implements IOBService {

    private static final Logger logger = LoggerFactory.getLogger(IOBServiceImpl.class);

    private IndicatorsOrderBook indicatorsOrderBook;

    private OrderBookData orderBookDataTMoins1;

    private String symbol;

    public IOBServiceImpl(IndicatorsOrderBook indicatorsOrderBook, String symbol) {
        this.indicatorsOrderBook = indicatorsOrderBook;
        this.symbol = symbol;
    }

    @Override
    public void processIndicators(OrderBookData orderBookData) {
        this.computePression(orderBookData);
        /*
        logger.info("--------------------------");
        logger.info("bestBidTMoins1:" + String.valueOf(this.orderBookDataTMoins1.getBids().get(0).total()));
        logger.info("bestAskTMoins1:" + String.valueOf(this.orderBookDataTMoins1.getAsks().get(0).total()));
        logger.info(this.symbol + this.indicatorsOrderBook.toStringPression());
        logger.info("--------------------------");

         */
    }

    private void computePression(OrderBookData orderBookData) {
        if(orderBookDataTMoins1 != null) {
            this.indicatorsOrderBook.setBidsPression(this.computeBidsPression(orderBookData));
            this.indicatorsOrderBook.setAsksPression(this.computeAsksPression(orderBookData));
        }
        orderBookDataTMoins1 = orderBookData;
    }

    /**
     * S'inspire du projet scalpingproject
     * @param orderBookDataT
     * @return
     */
    private double computeBidsPression(OrderBookData orderBookDataT) {
        double bidsPression = 0;
        double bestBidTMoins1 = this.orderBookDataTMoins1.getBids().get(0).getPrice();
        double bestBidT = orderBookDataT.getBids().get(0).getPrice();
        if(bestBidTMoins1 < bestBidT) {
            for(OrderBookOrderInformation orderBookOrderInformation: orderBookDataT.getBids()) {
                if(orderBookOrderInformation.getPrice() > bestBidTMoins1) {
                    bidsPression += orderBookOrderInformation.total();
                }
            }
        }
        return bidsPression;
    }

    private double computeAsksPression(OrderBookData orderBookDataT) {
        double asksPression = 0;
        double bestAskTMoins1 = this.orderBookDataTMoins1.getAsks().get(0).getPrice();
        double bestAskT = orderBookDataT.getAsks().get(0).getPrice();

        if(bestAskTMoins1 > bestAskT) {
            for(OrderBookOrderInformation orderBookOrderInformation: orderBookDataT.getAsks()) {
                if(orderBookOrderInformation.getPrice() < bestAskTMoins1) {
                    asksPression += orderBookOrderInformation.total();
                }
            }
        }
        return asksPression;
    }

}
