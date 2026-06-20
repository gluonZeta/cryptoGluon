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


/**
 * [
 *     [
 *         1499040000000,         // Kline open time
 *         "0.01634790",          // Open price
 *         "0.80000000",          // High price
 *         "0.01575800",          // Low price
 *         "0.01577100",          // Close price
 *         "148976.11427815",     // Volume
 *         1499644799999,         // Kline Close time
 *         "2434.19055334",       // Quote asset volume
 *         308,                   // Number of trades
 *         "1756.87402397",       // Taker buy base asset volume
 *         "28.46694368",         // Taker buy quote asset volume
 *         "0"                    // Unused field, ignore.
 *     ]
 * ]
 */
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
        return(new JSONArray(this.dataInStringFormat(urlHistoricalData)));
    }

    @Override
    public JSONArray getSymbolHistoricalDateBySymbolAndInterval(String symbol, String interval) {
        String urlHistoricalData = this.mainUrlApiBinance + this.buildUrlBySymbolAndInterval(symbol,interval);
        return new JSONArray(this.dataInStringFormat(urlHistoricalData));
    }

    private String dataInStringFormat(String urlHistoricalData) {
        logger.info("URL: {}", urlHistoricalData);
        return RestApiUtility.sendRestApiRequest(urlHistoricalData);
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

    private String buildUrlBySymbolAndInterval(String symbol, String interval) {
        return String.format("/klines?symbol=%s&interval=%s&limit=1000", symbol,interval);
    }

}
