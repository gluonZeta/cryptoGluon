package gluon.projects.infra.impl;

import gluon.projects.infra.IOFService;
import gluon.projects.model.IndicatorsOrderFlow;
import gluon.projects.model.OrderFlowData;

import java.text.SimpleDateFormat;

public class IOFServiceImpl implements IOFService {

    @Override
    public void fillOrderFlowPression(IndicatorsOrderFlow indicatorsOrderFlow, OrderFlowData orderFlowData) {
        if(orderFlowData.isBuyerMarketMaker()) {
            indicatorsOrderFlow.addSellerVolume(orderFlowData.getTotal());
        } else {
            indicatorsOrderFlow.addBuyerVolume(orderFlowData.getTotal());
        }
        indicatorsOrderFlow.setTradingTime(orderFlowData.getTradingTime());
    }

    @Override
    public String getCsvLine(String symbol,IndicatorsOrderFlow indicatorsOrderFlow) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("%s;%s;%.2f;%.2f",symbol, sdf.format(indicatorsOrderFlow.getTradingTime())
                ,indicatorsOrderFlow.getBidsCumulPression()
                ,indicatorsOrderFlow.getAsksCumulPression());
    }

    @Override
    public void clean(IndicatorsOrderFlow indicatorsOrderFlow) {
        indicatorsOrderFlow.setBidsCumulPression(0);
        indicatorsOrderFlow.setAsksCumulPression(0);
        indicatorsOrderFlow.setTradingTime(null);
    }
}
