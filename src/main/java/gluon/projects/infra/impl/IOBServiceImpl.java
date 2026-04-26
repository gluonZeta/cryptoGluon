package gluon.projects.infra.impl;

import gluon.projects.infra.IOBService;
import gluon.projects.model.IndicatorsOrderBook;
import gluon.projects.model.OrderBookData;
import gluon.projects.model.OrderBookOrderInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IOBServiceImpl implements IOBService {

    private static final Logger logger = LoggerFactory.getLogger(IOBServiceImpl.class);

    private IndicatorsOrderBook indicatorsOrderBook;

    private OrderBookData orderBookDataTMoins1;

    private String symbol;

    private float orderBookImbalanceThreshold = 0.5f;

    public IOBServiceImpl(IndicatorsOrderBook indicatorsOrderBook, String symbol) {
        this.indicatorsOrderBook = indicatorsOrderBook;
        this.symbol = symbol;
    }

    @Override
    public void processIndicators(OrderBookData orderBookData) {
        this.computeVolumeMvt(orderBookData);
    }

    private void computeOrderBookThresholdImbalance(OrderBookData orderBookData) {
        // Bids
        float limitBidPrice = orderBookData.getBids().get(0).getPrice() * (1-orderBookImbalanceThreshold);
        for(OrderBookOrderInformation orderBookOrderInformation: orderBookData.getBids()) {
            if(orderBookOrderInformation.getPrice() > limitBidPrice) {
                this.indicatorsOrderBook.addBidsOrderBookImbalanceQtt(orderBookOrderInformation.getQuantity());
            }
        }

        // Asks
        float limitAskPrice = orderBookData.getAsks().get(0).getPrice() * (1+orderBookImbalanceThreshold);
        for(OrderBookOrderInformation askInformation: orderBookData.getAsks()) {
            if(askInformation.getPrice() < limitAskPrice) {
                this.indicatorsOrderBook.addAsksOrderBookImbalanceQtt(askInformation.getQuantity());
            }
        }
    }

    private void computeVolumeMvt(OrderBookData orderBookData) {
        if(orderBookDataTMoins1 != null) {
            this.indicatorsOrderBook.addByerPression(this.computeAsksVolumeMvt(orderBookData));
            this.indicatorsOrderBook.addSellerPression(this.computeBidsVolumeMvt(orderBookData));
        }
        orderBookDataTMoins1 = orderBookData;
    }

    /**
     * S'inspire du projet scalpingproject
     * @param orderBookDataT
     * @return
     */
    private double computeBidsVolumeMvt(OrderBookData orderBookDataT) {
        double sellerVolumeEat = 0;
        double bestBidTMoins1 = this.orderBookDataTMoins1.getBids().get(0).getPrice();
        double bestBidT = orderBookDataT.getBids().get(0).getPrice();
        if(bestBidTMoins1 >= bestBidT) {
            for(OrderBookOrderInformation orderBookOrderInformation: orderBookDataTMoins1.getBids()) {
                if(orderBookOrderInformation.getPrice() > bestBidT) {
                    sellerVolumeEat += orderBookOrderInformation.total();
                }
                if(orderBookOrderInformation.getPrice() == bestBidT) {
                    if(orderBookDataT.getBids().get(0).total() < orderBookOrderInformation.total()) {
                        sellerVolumeEat += orderBookOrderInformation.total()-orderBookDataT.getBids().get(0).total();
                    }
                }
            }
        }
        return sellerVolumeEat;
    }

    private double computeAsksVolumeMvt(OrderBookData orderBookDataT) {
        double byerVolumeEat = 0;
        double bestAskTMoins1 = this.orderBookDataTMoins1.getAsks().get(0).getPrice();
        double bestAskT = orderBookDataT.getAsks().get(0).getPrice();

        if(bestAskTMoins1 <= bestAskT) {
            for(OrderBookOrderInformation orderBookOrderInformation: orderBookDataTMoins1.getAsks()) {
                if(orderBookOrderInformation.getPrice() < bestAskT) {
                    byerVolumeEat += orderBookOrderInformation.total();
                }
                if(orderBookOrderInformation.getPrice() == bestAskT) {
                    if(orderBookDataT.getAsks().get(0).total() < orderBookOrderInformation.total()) {
                        byerVolumeEat += orderBookOrderInformation.total()-orderBookDataT.getAsks().get(0).total();
                    }
                }
                if(orderBookOrderInformation.getPrice() > bestAskT) break;
            }
        }
        return byerVolumeEat;
    }

    public void cleanIndicatorsOrderBook() {
        this.indicatorsOrderBook.setAsksOrderBookImbalanceQtt(0);
        this.indicatorsOrderBook.setBidsOrderBookImbalanceQtt(0);
        this.indicatorsOrderBook.setBuyerPression(0);
        this.indicatorsOrderBook.setSellerPression(0);
    }

    @Override
    public String getCsvLine(String symbol, IndicatorsOrderBook indicatorsOrderBook) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("%s;%s;%.2f;%.2f",symbol, sdf.format(new Date())
                ,indicatorsOrderBook.getBuyerPression()
                ,indicatorsOrderBook.getSellerPression());
    }

}
