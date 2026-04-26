package gluon.projects.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndicatorsOrderBook {

    private double buyerPression = 0;

    private double sellerPression = 0;

    private double bidsOrderBookImbalanceQtt = 0;

    private double asksOrderBookImbalanceQtt = 0;

    public void addBidsOrderBookImbalanceQtt(double qtt) {
        this.bidsOrderBookImbalanceQtt += qtt;
    }

    public void addAsksOrderBookImbalanceQtt(double qtt) {
        this.asksOrderBookImbalanceQtt += qtt;
    }

    public void addByerPression(double addValue) {
        this.buyerPression += addValue;
    }

    public void addSellerPression(double addValue) {
        this.sellerPression += addValue;
    }

    public String toStringPression() {
        StringBuilder pression = new StringBuilder();
        pression.append("[BYER Pression:").append(buyerPression).append("] xxx ");
        pression.append("[SELLER Pression:").append(sellerPression).append("]");
        return pression.toString();
    }
}
