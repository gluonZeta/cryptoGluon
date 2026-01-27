package gluon.projects.services.impl;

import gluon.projects.services.SymbolCryptoService;
import gluon.projects.services.SymbolWriter;
import gluon.projects.utilities.FileUtility;
import gluon.projects.utilities.RestApiUtility;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class SymbolCryptoServiceImpl implements SymbolCryptoService {

    private String mainUrlApiBinance;

    SymbolWriter symbolWriter;

    public SymbolCryptoServiceImpl(SymbolWriter symbolWriter) {
        Properties properties = FileUtility.getPropertiesByFileName("application.properties");
        this.mainUrlApiBinance = properties.getProperty("apibinanceurl");

        this.symbolWriter = symbolWriter;
    }

    @Override
    public List<String> getFreshListSymbol() {

        List<String> symbolList = new ArrayList<>();
        JSONObject symbolInfo;
        String symbol;
        boolean isMarginTradingAllowed = false;
        String urlExchangeInfo = String.format("%s%s", this.mainUrlApiBinance,"/exchangeInfo");

        String exchangeInformationResponse = RestApiUtility.sendRestApiRequest(urlExchangeInfo);
        
        JSONObject jsonObject = new JSONObject(exchangeInformationResponse);
        JSONArray symbols = (JSONArray) jsonObject.get("symbols");

        for(int i = 0; i < symbols.length(); i++) {
            symbolInfo = new JSONObject(symbols.get(i).toString());
            symbol = (String) symbolInfo.get("symbol");
            isMarginTradingAllowed = (boolean) symbolInfo.get("isMarginTradingAllowed");
            if(!this.excludedSymbol().contains(symbol)
                    && isMarginTradingAllowed
                    && filterStringSymbol(symbol)
                    && dataHistoryLengthFilter(symbol)) {
                symbolList.add(symbol);
                this.symbolWriter.write(symbol);
            }
        }

        return symbolList;
    }

    @Override
    public List<String> getOldListSymbol() {
        return List.of();
    }

    private List<String> excludedSymbol() {
        List<String> symbolExclus = new ArrayList<>();
        symbolExclus.add("TUSDUSDT");
        symbolExclus.add("FDUSDUSDT");
        return symbolExclus;
    }

    private boolean filterStringSymbol(String symbol) {
        boolean allow = false;
        if(symbol.endsWith("USDT")
                && !symbol.startsWith("USDT")
                && !symbol.contains("DOWN")
                && !symbol.contains("BULL")
                && !symbol.contains("BEAR")
                && symbol.length() >= 6
        ) {
            allow = true;
        }
        return allow;
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

    private boolean dataHistoryLengthFilter(String symbol) {
        boolean result = false;
        int yearLimit = 2;
        int numberOfMonth = yearLimit * 12;
        JSONArray symbolHistoricalDataArray;

        String urlHistoricalData = this.mainUrlApiBinance +
                this.buildUrlForHistoryLimit(symbol, yearLimit);

        String symbolHistoricalData = RestApiUtility.sendRestApiRequest(urlHistoricalData);
        symbolHistoricalDataArray = new JSONArray(symbolHistoricalData);

        if(symbolHistoricalDataArray.length() >= (numberOfMonth-1)) {
            result = true;
        }
        return result;
    }

}
