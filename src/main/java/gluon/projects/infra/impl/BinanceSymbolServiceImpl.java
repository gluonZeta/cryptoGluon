package gluon.projects.infra.impl;

import gluon.projects.infra.BinanceSymbolService;
import gluon.projects.utilities.FileUtility;
import gluon.projects.utilities.RestApiUtility;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class BinanceSymbolServiceImpl implements BinanceSymbolService {

    private static final Logger logger = LoggerFactory.getLogger(BinanceSymbolServiceImpl.class);

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

        logger.info("URL: {}", urlHistoricalData);
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
