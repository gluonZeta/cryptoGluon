package gluon.projects.domaine.impl;

import gluon.projects.domaine.SymbolCryptoService;
import gluon.projects.infra.BinanceSymbolService;
import gluon.projects.infra.FileStorageService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class SymbolCryptoServiceImpl implements SymbolCryptoService {

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
                symbolList.add(symbol);
                this.fileStorageService.write(symbol);
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
        JSONArray symbolHistoricalDataArray = this.binanceSymbolService.getSymbolHistoricalData(symbol,yearLimit);
        if(symbolHistoricalDataArray.length() >= (numberOfMonth-1)) {
            result = true;
        }
        return result;
    }
}
