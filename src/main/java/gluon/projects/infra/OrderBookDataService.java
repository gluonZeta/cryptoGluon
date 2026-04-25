package gluon.projects.infra;

import gluon.projects.model.OrderBookData;
import org.json.JSONArray;

public interface OrderBookDataService {

    OrderBookData fillOrderDataBookBidsAndAsks(JSONArray bids, JSONArray asks);

}
