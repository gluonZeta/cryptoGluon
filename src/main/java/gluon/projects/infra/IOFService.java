package gluon.projects.infra;

import gluon.projects.model.IndicatorsOrderFlow;
import gluon.projects.model.OrderFlowData;

public interface IOFService {
    void fillOrderFlowPression(IndicatorsOrderFlow indicatorsOrderFlow, OrderFlowData orderFlowData);

    String getCsvLine(String symbole, IndicatorsOrderFlow indicatorsOrderFlow);

    void clean(IndicatorsOrderFlow indicatorsOrderFlow);
}
