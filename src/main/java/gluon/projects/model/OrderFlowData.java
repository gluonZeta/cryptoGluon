package gluon.projects.model;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class OrderFlowData {
    private String symbol;

    private boolean isBuyerMarketMaker;

    private float quantity;

    private float price;

    private Date tradingTime;

    public OrderFlowData() {}

    public OrderFlowData(String symbol) {
        this.symbol = symbol;
    }

    public float getTotal() {
        return quantity * price;
    }

    public void fillOrderFlowData(JSONObject orderFlowBinanceData) {
        this.isBuyerMarketMaker = (boolean) orderFlowBinanceData.get("m");
        this.quantity = Float.parseFloat((String) orderFlowBinanceData.get("q"));
        this.price = Float.parseFloat((String) orderFlowBinanceData.get("p"));

        long tradingTimeLong = (long) orderFlowBinanceData.get("E");
        this.tradingTime = new Date(tradingTimeLong);
    }

    public void clearOrderFlowData() {
        this.isBuyerMarketMaker = false;
        this.quantity = 0;
        this.price = 0;
        this.tradingTime = null;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(tradingTime != null) {
            return this.symbol + ";" +
                    this.isBuyerMarketMaker + ";" +
                    this.quantity + ";" +
                    this.price + ";" +
                    this.getTotal() + ";" +
                    sdf.format(this.tradingTime);
        } else {
            return this.symbol + ";" +
                    this.isBuyerMarketMaker + ";" +
                    this.quantity + ";" +
                    this.price + ";" +
                    this.getTotal() + ";";
        }
    }
}
