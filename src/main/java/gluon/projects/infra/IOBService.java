package gluon.projects.infra;

import gluon.projects.model.IndicatorsOrderBook;
import gluon.projects.model.OrderBookData;

public interface IOBService {

    void processIndicators(OrderBookData orderBookData);

    public void cleanIndicatorsOrderBook();

    String getCsvLine(String symbol, IndicatorsOrderBook indicatorsOrderBook);
}
