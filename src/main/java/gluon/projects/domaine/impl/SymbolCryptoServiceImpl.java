package gluon.projects.domaine.impl;

import gluon.projects.domaine.SymbolCryptoService;
import gluon.projects.infra.BinanceSymbolService;
import gluon.projects.infra.FileStorageService;
import gluon.projects.utilities.RestApiUtility;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SymbolCryptoServiceImpl implements SymbolCryptoService {

    private static final Logger logger = LoggerFactory.getLogger(SymbolCryptoServiceImpl.class);

    private final FileStorageService fileStorageService;

    private final BinanceSymbolService binanceSymbolService;

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

        String cleanSymbol;
        List<String> cryptoCapFilter = this.marketCapFilter();

        for(int i = 0; i < symbols.length(); i++) {
            symbolInfo = new JSONObject(symbols.get(i).toString());
            symbol = (String) symbolInfo.get("symbol");
            cleanSymbol = symbol.substring(0, symbol.length() - 4);
            isMarginTradingAllowed = (boolean) symbolInfo.get("isMarginTradingAllowed");
            if(!this.excludedSymbol().contains(symbol)
                    && filterStringSymbol(symbol)
                    && isMarginTradingAllowed
                    && dataHistoryLengthFilter(symbol)
                    && cryptoCapFilter.contains(cleanSymbol) ) {
                symbolList.add(symbol);
                this.fileStorageService.write(symbol);
            }
        }
        return symbolList;
    }

    @Override
    public List<String> getOldListSymbol() {
        return this.fileStorageService.readSymbolExistingFile();
    }

    private List<String> excludedSymbol() {
        List<String> symbolExclus = new ArrayList<>();
        symbolExclus.add("TUSDUSDC");
        symbolExclus.add("FDUSDUSDC");
        symbolExclus.add("BROCCOLI714USDC");
        return symbolExclus;
    }

    private boolean filterStringSymbol(String symbol) {
        boolean allow = false;
        if(symbol.endsWith("USDC")
                && !symbol.startsWith("USDC")
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
        float priceThreshold = 0.005f;
        float closePrice;
        JSONArray historicalDataElement;

        JSONArray symbolHistoricalDataArray = this.binanceSymbolService.getSymbolHistoricalData(symbol,yearLimit);
        if(symbolHistoricalDataArray.length() >= (numberOfMonth-1)) {
            historicalDataElement = (JSONArray) symbolHistoricalDataArray.get(symbolHistoricalDataArray.length()-1);
            closePrice = Float.parseFloat((String) historicalDataElement.get(4));

            if((closePrice > priceThreshold) || allowedSymbolException(symbol)) {
                logger.info("{} ------- {}", symbol, closePrice);
                result = true;
            }
        }
        return result;
    }

    private boolean allowedSymbolException(String symbol) {
        List<String> listAllowedSymbol = new ArrayList<>();
        listAllowedSymbol.add("SHIBUSDC");
        boolean result = false;
        if(listAllowedSymbol.contains(symbol)) result = true;
        return result;
    }

    private List<String> marketCapFilter() {
        List<String> cryptoValide = new ArrayList<>();
        String coinGekoRequest;
        String exchangeInformationResponse;
        JSONArray coinGekoResultArray;
        JSONObject coinGekoSymbolInfo;
        long capThreshold = 100000000L;
        Long cryptoCapValue;

        for(int page = 1; page <= 2; page++) {
            coinGekoRequest = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=250&page=" + page;
            exchangeInformationResponse = RestApiUtility.sendRestApiRequest(coinGekoRequest);
            coinGekoResultArray = new JSONArray(exchangeInformationResponse);
            for(int i = 0; i < coinGekoResultArray.length(); i++) {
                coinGekoSymbolInfo = (JSONObject) coinGekoResultArray.get(i);
                cryptoCapValue = Long.valueOf(String.valueOf(coinGekoSymbolInfo.get("market_cap")));
                if(cryptoCapValue > capThreshold) {
                    cryptoValide.add(((String) coinGekoSymbolInfo.get("symbol")).toUpperCase());
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        logger.info("LA TAILLE est: {}", cryptoValide.size());

        return cryptoValide;
    }
}
