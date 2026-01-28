package gluon.projects.infra.impl;

import gluon.projects.infra.BinanceSymbolService;
import gluon.projects.utilities.FileUtility;
import gluon.projects.utilities.RestApiUtility;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class BinanceSymbolServiceImpl implements BinanceSymbolService {

    private final String mainUrlApiBinance;

    public BinanceSymbolServiceImpl() {
        Properties properties = FileUtility.getPropertiesByFileName("application.properties");
        this.mainUrlApiBinance = properties.getProperty("apibinanceurl");
    }

    @Override
    public JSONArray getExchangeInfos() {
        String urlExchangeInfo = String.format("%s%s", this.mainUrlApiBinance,"/exchangeInfo");

        String exchangeInformationResponse = RestApiUtility.sendRestApiRequest(urlExchangeInfo);

        JSONObject jsonObject = new JSONObject(exchangeInformationResponse);
        return (JSONArray) jsonObject.get("symbols");
    }

    @Override
    public JSONArray getSymbolHistoricalData(String symbol, int yearLimit) {
        String urlHistoricalData = this.mainUrlApiBinance +
                this.buildUrlForHistoryLimit(symbol, yearLimit);

        String symbolHistoricalData = RestApiUtility.sendRestApiRequest(urlHistoricalData);
        return(new JSONArray(symbolHistoricalData));
    }

    private String buildUrlForHistoryLimit(String symbol,int yearLimit) {
        String interval = "1M";
        long endTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(endTime));
        calendar.add(Calendar.YEAR, -yearLimit);
        long startTime = calendar.getTimeInMillis();

        return String.format("/klines?symbol=%s&interval=%s&startTime=%d&endTime=%d",
                symbol, interval, startTime, endTime);
    }



}
