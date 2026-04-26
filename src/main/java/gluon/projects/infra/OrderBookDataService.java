package gluon.projects.infra;

import gluon.projects.model.OrderBookData;
import org.json.JSONArray;

public interface OrderBookDataService {

    OrderBookData createAndFillOrderDataBookBidsAndAsks(JSONArray bids, JSONArray asks);

}
