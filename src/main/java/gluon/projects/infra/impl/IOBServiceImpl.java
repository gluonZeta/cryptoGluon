package gluon.projects.infra.impl;

import gluon.projects.infra.IOBService;
import gluon.projects.model.IndicatorsOrderBook;
import gluon.projects.model.OrderBookData;
import gluon.projects.model.OrderBookOrderInformation;
import gluon.projects.model.VariationDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IOBServiceImpl implements IOBService {

    private static final Logger logger = LoggerFactory.getLogger(IOBServiceImpl.class);

    private IndicatorsOrderBook indicatorsOrderBook;

    private OrderBookData orderBookDataTMoins1;

    private OrderBookData orderBookInitial;

    private String symbol;

    private double orderBookImbalanceThreshold = 0.005f;

    public IOBServiceImpl(IndicatorsOrderBook indicatorsOrderBook, String symbol) {
        this.indicatorsOrderBook = indicatorsOrderBook;
        this.symbol = symbol;
    }

    @Override
    public void processIndicators(OrderBookData orderBookData) {
        if(orderBookDataTMoins1 != null) {
            this.computeVolumeMvt(orderBookData);
            this.computeVariation(orderBookData);
            this.computeSpread(orderBookData);
            this.computeOrderLimitVolume(orderBookData);
        }
        orderBookDataTMoins1 = orderBookData;
    }

    public void computeOrderLimitVolume(OrderBookData orderBookDataT) {
        double bestBidT = orderBookDataT.getBids().get(0).getPrice();
        double bestAskT = orderBookDataT.getAsks().get(0).getPrice();

        double bidsCumulValue = 0;
        double asksCumulValue = 0;

        // Bids
        double limitBidPrice = bestBidT * (1-orderBookImbalanceThreshold);
        for(OrderBookOrderInformation orderBookOrderInformation: orderBookDataT.getBids()) {
            if(orderBookOrderInformation.getPrice() > limitBidPrice) {
                bidsCumulValue += orderBookOrderInformation.total();
            }
        }
        this.indicatorsOrderBook.setBidsOrderLimitVolume(bidsCumulValue);

        // Asks
        double limitAskPrice = bestAskT * (1+orderBookImbalanceThreshold);
        for(OrderBookOrderInformation askInformation: orderBookDataT.getAsks()) {
            if(askInformation.getPrice() < limitAskPrice) {
                asksCumulValue += askInformation.total();
            }
        }
        this.indicatorsOrderBook.setAsksOrderLimitVolume(asksCumulValue);
    }


    private void computeSpread(OrderBookData orderBookDataT) {
        double bestBidT = orderBookDataT.getBids().get(0).getPrice();
        double bestAskT = orderBookDataT.getAsks().get(0).getPrice();
        indicatorsOrderBook.setSpreadValue(bestAskT-bestBidT);
    }

    private void computeVariation(OrderBookData orderBookDataT) {
        double bestBidTMoins1 = this.orderBookDataTMoins1.getBids().get(0).getPrice();
        double bestBidT = orderBookDataT.getBids().get(0).getPrice();

        double bestAskTMoins1 = this.orderBookDataTMoins1.getAsks().get(0).getPrice();
        double bestAskT = orderBookDataT.getAsks().get(0).getPrice();

        indicatorsOrderBook.setVariationDirection(VariationDirection.RAS);

        if(bestBidTMoins1 > bestBidT) {
            indicatorsOrderBook.addDecreaseVariation((bestBidTMoins1-bestBidT)*100/bestBidTMoins1);
        }
        if(bestAskTMoins1 < bestAskT) {
            indicatorsOrderBook.addIncreaseVariation((bestAskT-bestAskTMoins1)*100/bestAskTMoins1);
        }

        if(this.orderBookInitial == null) {
            this.orderBookInitial = orderBookDataT;
        } else {
            if(this.orderBookInitial.getBids().get(0).getPrice() > bestBidT) {
                indicatorsOrderBook.setVariationDirection(VariationDirection.DOWN);
            }
            if(this.orderBookInitial.getAsks().get(0).getPrice() < bestAskT) {
                indicatorsOrderBook.setVariationDirection(VariationDirection.UP);
            }
        }

    }


    private void computeOrderBookThresholdImbalance(OrderBookData orderBookData) {
        // Bids
        double limitBidPrice = orderBookData.getBids().get(0).getPrice() * (1-orderBookImbalanceThreshold);
        for(OrderBookOrderInformation orderBookOrderInformation: orderBookData.getBids()) {
            if(orderBookOrderInformation.getPrice() > limitBidPrice) {
                this.indicatorsOrderBook.addBidsOrderBookImbalanceQtt(orderBookOrderInformation.getQuantity());
            }
        }

        // Asks
        double limitAskPrice = orderBookData.getAsks().get(0).getPrice() * (1+orderBookImbalanceThreshold);
        for(OrderBookOrderInformation askInformation: orderBookData.getAsks()) {
            if(askInformation.getPrice() < limitAskPrice) {
                this.indicatorsOrderBook.addAsksOrderBookImbalanceQtt(askInformation.getQuantity());
            }
        }
    }

    private void computeVolumeMvt(OrderBookData orderBookData) {
        this.indicatorsOrderBook.addByerPression(this.computeAsksVolumeMvt(orderBookData));
        this.indicatorsOrderBook.addSellerPression(this.computeBidsVolumeMvt(orderBookData));
    }

    /**
     * Seller Volume eat <=> pression de vente
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


    /**
     * Pression d'achat
     * @param orderBookDataT
     * @return
     */
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
        this.indicatorsOrderBook.setDecreaseVariationValue(0);
        this.indicatorsOrderBook.setIncreaseVariationValue(0);
        this.indicatorsOrderBook.setVariationDirection(VariationDirection.RAS);
        this.indicatorsOrderBook.setSpreadValue(0);
        this.indicatorsOrderBook.setBidsOrderLimitVolume(0);
        this.indicatorsOrderBook.setAsksOrderLimitVolume(0);
        this.orderBookInitial = null;
    }

    @Override
    public String getCsvLine(String symbol, IndicatorsOrderBook indicatorsOrderBook) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("%s;%s;%.5f;%.5f;%.5f;%.5f;%s;%.5f;%.5f;%.5f"
                ,symbol
                ,sdf.format(new Date())
                ,indicatorsOrderBook.getBuyerPression() // Pression d'achat, acheteur va attaqué les ligne du ASKS
                ,indicatorsOrderBook.getSellerPression() // Pression de vente, vendeur volume attack coté acheteur
                ,indicatorsOrderBook.getDecreaseVariationValue() // variation descente
                ,indicatorsOrderBook.getIncreaseVariationValue() // augmentation
                ,indicatorsOrderBook.getVariationDirection().getDirection() // direction de la variation
                ,indicatorsOrderBook.getSpreadValue() // spread
                ,indicatorsOrderBook.getBidsOrderLimitVolume() // volume des order en attente côté bids
                ,indicatorsOrderBook.getAsksOrderLimitVolume() // volume des ordre en attente côté Asks
        );
    }

}
