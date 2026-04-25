package gluon.projects.infra;

import gluon.projects.model.OrderBookData;

public interface IOBService {

    void processIndicators(OrderBookData orderBookData);

}
