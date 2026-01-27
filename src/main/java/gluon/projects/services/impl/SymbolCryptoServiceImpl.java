package gluon.projects.services.impl;

import gluon.projects.services.SymbolCryptoService;
import gluon.projects.services.SymbolWriter;
import gluon.projects.utilities.FileUtility;
import gluon.projects.utilities.RestApiUtility;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SymbolCryptoServiceImpl implements SymbolCryptoService {

    private String mainUrlApiBinance;

    private String listSymbolFile;

    SymbolWriter symbolWriter;

    public SymbolCryptoServiceImpl(SymbolWriter symbolWriter) {
        Properties properties = FileUtility.getPropertiesByFileName("application.properties");
        this.mainUrlApiBinance = properties.getProperty("apibinanceurl");
        this.listSymbolFile = properties.getProperty("listsymbolfile");
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
                    && filterStringSymbol(symbol)) {
                symbolList.add(symbol);
                this.writeSymbolInFile(symbol);
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

    private void writeSymbolInFile(String symbol) {
        symbolWriter.write(symbol);
    }

}
