package gluon.projects.services.impl;

import gluon.projects.services.SymbolCryptoService;
import gluon.projects.utilities.FileUtility;
import gluon.projects.utilities.RestApiUtility;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SymbolCryptoServiceImpl implements SymbolCryptoService {

    private String mainUrlApiBinance;

    public SymbolCryptoServiceImpl() {
        Properties properties = FileUtility.getPropertiesByFileName("application.properties");
        this.mainUrlApiBinance = properties.getProperty("apibinanceurl");
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
                    && filterStringSymbol(symbol)) {
                symbolList.add(symbol);
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
        symbolExclus.add("TUSDUSDC");
        symbolExclus.add("FDUSDUSDC");
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

}
