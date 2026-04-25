package gluon.projects.infra.impl;

import gluon.projects.infra.IOFService;
import gluon.projects.model.IndicatorsOrderFlow;
import gluon.projects.model.OrderFlowData;

public class IOFServiceImpl implements IOFService {

    @Override
    public void fillOrderFlowPression(IndicatorsOrderFlow indicatorsOrderFlow, OrderFlowData orderFlowData) {
        if(orderFlowData.isBuyerMarketMaker()) {
            indicatorsOrderFlow.addSellerVolume(orderFlowData.getTotal());
        } else {
            indicatorsOrderFlow.addBuyerVolume(orderFlowData.getTotal());
        }
    }
}
