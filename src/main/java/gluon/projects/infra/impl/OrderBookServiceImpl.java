package gluon.projects.infra.impl;

import gluon.projects.infra.IOBService;
import gluon.projects.infra.OrderBookDataService;
import gluon.projects.infra.OrderBookService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderBookServiceImpl implements OrderBookService {

    private static final Logger logger = LoggerFactory.getLogger(OrderBookServiceImpl.class);

    private OrderBookDataService orderBookDataService;

    private IOBService IOBService;

    public OrderBookServiceImpl(IOBService IOBService) {
        this.orderBookDataService = new OrderBookDataServiceImpl();
        this.IOBService = IOBService;
    }

    @Override
    public void processOrderBook(String messageResponse) {
        JSONObject exchangeResponseData = new JSONObject(messageResponse);
        JSONObject orderBookResponseData = exchangeResponseData.getJSONObject("data");
        JSONArray bids = orderBookResponseData.getJSONArray("bids");
        JSONArray asks = orderBookResponseData.getJSONArray("asks");
        this.IOBService.processIndicators(this.orderBookDataService.fillOrderDataBookBidsAndAsks(bids,asks));
    }
}
