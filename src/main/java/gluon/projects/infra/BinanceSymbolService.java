package gluon.projects.infra;

import org.json.JSONArray;

public interface BinanceSymbolService {

    public JSONArray getExchangeInfos();

    public JSONArray getSymbolHistoricalData(String symbol, int yearLimit);

}
