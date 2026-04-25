package gluon.projects.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndicatorsOrderFlow {

    private double bidsCumulPression = 0;

    private double asksCumulPression = 0;

    public void addBuyerVolume(double volume) {
        this.bidsCumulPression += volume;
    }

    public void addSellerVolume(double volume) {
        this.asksCumulPression += volume;
    }

    public String toString() {
        return String.format("BidPression: %.2f; AskPression: %.2f", bidsCumulPression, asksCumulPression);
    }

}
