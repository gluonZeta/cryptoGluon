package gluon.projects.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndicatorsOrderBook {

    private double bidsPression = 0;

    private double asksPression = 0;

    public String toStringPression() {
        StringBuilder pression = new StringBuilder();
        pression.append("[BID:").append(bidsPression).append("] xxx ");
        pression.append("[ASK:").append(asksPression).append("]");
        return pression.toString();
    }
}
