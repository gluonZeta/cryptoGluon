package gluon.projects.model;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class OneCandleDataModel {

    public OneCandleDataModel() {}

    public OneCandleDataModel(JSONArray oneCandleValue) {
        this.openTime = oneCandleValue.getLong(0);
        this.openPrice = oneCandleValue.getString(1);
        this.highPrice = oneCandleValue.getString(2);
        this.lowPrice = oneCandleValue.getString(3);
        this.closePrice = oneCandleValue.getString(4);
        this.volume = oneCandleValue.getString(5);
        this.closeTime = oneCandleValue.getLong(6);
        this.quoteAssetVolume = oneCandleValue.getString(7);
        this.numberOfTrades = String.valueOf(oneCandleValue.getInt(8));
        this.takerBuyBaseAssetVolume = oneCandleValue.getString(9);
        this.takerBuyQuoteAssetVolume = oneCandleValue.getString(10);
    }

    private long openTime;

    private String openPrice;

    private String highPrice;

    private String lowPrice;

    private String closePrice;

    private String volume;

    private long closeTime;

    private String quoteAssetVolume;

    private String numberOfTrades;

    private String takerBuyBaseAssetVolume;

    private String takerBuyQuoteAssetVolume;

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "OneCandleDataModel{" +
                "openTime = " + sdf.format(new Date(openTime)) +
                "\nopenPrice = '" + openPrice + '\'' +
                "\nhighPrice = '" + highPrice + '\'' +
                "\nlowPrice = '" + lowPrice + '\'' +
                "\nclosePrice = '" + closePrice + '\'' +
                "\nvolume = '" + volume + '\'' +
                "\ncloseTime = " + sdf.format(new Date(closeTime)) +
                "\nquoteAssetVolume = '" + quoteAssetVolume + '\'' +
                "\nnumberOfTrades = '" + numberOfTrades + '\'' +
                "\ntakerBuyBaseAssetVolume = '" + takerBuyBaseAssetVolume + '\'' +
                "\ntakerBuyQuoteAssetVolume = '" + takerBuyQuoteAssetVolume + '\'' +
                '}';
    }
}
