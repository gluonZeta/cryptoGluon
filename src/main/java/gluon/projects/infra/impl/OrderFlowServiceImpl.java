package gluon.projects.infra.impl;

import gluon.projects.infra.IOFService;
import gluon.projects.infra.OrderFlowService;
import gluon.projects.model.IndicatorsOrderFlow;
import gluon.projects.model.OrderFlowData;
import lombok.Setter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Setter
public class OrderFlowServiceImpl implements OrderFlowService {

    private static final Logger logger = LoggerFactory.getLogger(OrderFlowServiceImpl.class);

    private IndicatorsOrderFlow indicatorsOrderFlow;

    private IOFService iofService;

    private OrderFlowData orderFlowData;

    private String symbol;

    public OrderFlowServiceImpl(IndicatorsOrderFlow indicatorsOrderFlow, IOFService iofService, String symbol) {
        this.indicatorsOrderFlow = indicatorsOrderFlow;
        this.iofService = iofService;
        this.symbol = symbol;
    }

    @Override
    public void processOrderFlow(JSONObject orderFlowBinanceData) {
        fillOrderFlowData(orderFlowBinanceData);
        this.iofService.fillOrderFlowPression(this.indicatorsOrderFlow,this.orderFlowData);
        orderFlowData.clearOrderFlowData();
    }

    public void fillOrderFlowData(JSONObject orderFlowBinanceData) {
        orderFlowData = new OrderFlowData(symbol);
        orderFlowData.fillOrderFlowData(orderFlowBinanceData);
    }

}
