package gluon.projects.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderBookData {

    private List<OrderBookOrderInformation> bids;

    private List<OrderBookOrderInformation> asks;

    @Override
    public String toString() {
        StringBuilder bidsAsks = new StringBuilder();
        bidsAsks.append("\n");
        for(OrderBookOrderInformation bid: bids) {
            bidsAsks.append("[").append(bid.getPrice()).append(",").append(bid.getQuantity()).append("]").append(";");
        }
        bidsAsks.deleteCharAt(bidsAsks.length() - 1);
        bidsAsks.append("\n");

        for(OrderBookOrderInformation ask: asks) {
            bidsAsks.append("[").append(ask.getPrice()).append(",").append(ask.getQuantity()).append("]").append(";");
        }
        bidsAsks.deleteCharAt(bidsAsks.length() - 1);
        return bidsAsks.toString();
    }

    public String toStringBids() {
        StringBuilder bids = new StringBuilder();
        bids.append("\n");
        for(OrderBookOrderInformation bid: this.bids) {
            bids.append("[").append(bid.getPrice()).append(",").append(bid.getQuantity()).append("]").append(";");
        }
        bids.deleteCharAt(bids.length() - 1);
        bids.append("\n");
        return bids.toString();
    }

}
