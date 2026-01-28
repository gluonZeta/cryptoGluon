package gluon.projects.domaine.impl;

import gluon.projects.domaine.SymbolCryptoService;
import gluon.projects.infra.BinanceSymbolService;
import gluon.projects.infra.FileStorageService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SymbolCryptoServiceImpl implements SymbolCryptoService {

    private static final Logger logger = LoggerFactory.getLogger(SymbolCryptoServiceImpl.class);

    private FileStorageService fileStorageService;

    private BinanceSymbolService binanceSymbolService;

    public SymbolCryptoServiceImpl(
            FileStorageService fileStorageService,
            BinanceSymbolService binanceSymbolService) {
        this.fileStorageService = fileStorageService;
        this.binanceSymbolService = binanceSymbolService;
    }

    @Override
    public List<String> getFreshListSymbol() {
        fileStorageService.cleanFolder();

        JSONObject symbolInfo;
        String symbol;
        boolean isMarginTradingAllowed = false;
        List<String> symbolList = new ArrayList<>();
        JSONArray symbols = this.binanceSymbolService.getExchangeInfos();

        for(int i = 0; i < symbols.length(); i++) {
            symbolInfo = new JSONObject(symbols.get(i).toString());
            symbol = (String) symbolInfo.get("symbol");
            isMarginTradingAllowed = (boolean) symbolInfo.get("isMarginTradingAllowed");
            if(!this.excludedSymbol().contains(symbol)
                    && isMarginTradingAllowed
                    && filterStringSymbol(symbol)
                    && dataHistoryLengthFilter(symbol)) {
                symbolList.add(symbol.substring(0, symbol.length() - 1) + "C");
                this.fileStorageService.write(symbol.substring(0, symbol.length() - 1) + "C");
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

    private boolean dataHistoryLengthFilter(String symbol) {
        boolean result = false;
        int yearLimit = 2;
        int numberOfMonth = yearLimit * 12;
        float priceThreshold = 0.05f;
        float closePrice;
        JSONArray historicalDataElement;

        JSONArray symbolHistoricalDataArray = this.binanceSymbolService.getSymbolHistoricalData(symbol,yearLimit);
        if(symbolHistoricalDataArray.length() >= (numberOfMonth-1)) {
            historicalDataElement = (JSONArray) symbolHistoricalDataArray.get(symbolHistoricalDataArray.length()-1);
            closePrice = Float.parseFloat((String) historicalDataElement.get(4));

            if(closePrice > priceThreshold) {
                logger.info("{} ------- {}", symbol, closePrice);
                logger.info(symbolHistoricalDataArray.toString());
                result = true;
            }
        }
        return result;
    }
}
