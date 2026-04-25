package gluon.projects.infra;

import org.json.JSONObject;

public interface OrderFlowService {

    public void processOrderFlow(JSONObject orderFlowBinanceData);

}
