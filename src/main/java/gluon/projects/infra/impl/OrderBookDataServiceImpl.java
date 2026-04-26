package gluon.projects.infra.impl;

import gluon.projects.infra.OrderBookDataService;
import gluon.projects.model.OrderBookData;
import gluon.projects.model.OrderBookOrderInformation;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class OrderBookDataServiceImpl implements OrderBookDataService {
    @Override
    public OrderBookData createAndFillOrderDataBookBidsAndAsks(JSONArray bids, JSONArray asks) {
        OrderBookData orderBookData = new OrderBookData();
        List<OrderBookOrderInformation> bidsElements = new ArrayList<>();
        List<OrderBookOrderInformation> asksElements = new ArrayList<>();
        JSONArray arrayElement;
        float price;
        float quantity;

        for(int i = 0; i < bids.length(); i++) {
            arrayElement = (JSONArray) bids.get(i);
            price = Float.parseFloat((String) arrayElement.get(0));
            quantity = Float.parseFloat((String) arrayElement.get(1));
            bidsElements.add(new OrderBookOrderInformation(price, quantity));
        }
        orderBookData.setBids(bidsElements);

        for(int i = 0; i < asks.length(); i++) {
            arrayElement = (JSONArray) asks.get(i);
            price = Float.parseFloat((String) arrayElement.get(0));
            quantity = Float.parseFloat((String) arrayElement.get(1));
            asksElements.add(new OrderBookOrderInformation(price, quantity));
        }
        orderBookData.setAsks(asksElements);
        return orderBookData;
    }
}
