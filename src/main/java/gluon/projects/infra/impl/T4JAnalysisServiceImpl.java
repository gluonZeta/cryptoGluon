package gluon.projects.infra.impl;

import gluon.projects.infra.BinanceSymbolService;
import gluon.projects.model.OneCandleDataModel;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class T4JAnalysisServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(T4JAnalysisServiceImpl.class);

    private final BinanceSymbolService binanceSymbolService;

    private final String INTERVAL = "15m";

    String filPath = "C:\\Users\\Andraina\\Documents\\projects\\java" +
            "\\cryptoWindowsFiles\\historicalData\\";

    String SYMBOL = "DYDXUSDT";
    String cryptoChoice = SYMBOL + "HistoricalData.json";

    public T4JAnalysisServiceImpl () {
        this.binanceSymbolService = new BinanceSymbolServiceImpl();
    }

    public List<String> getFilteredSymbol() {
        BarSeries series = this.getBarSeriesFromJsonArray(this.getOldHistoricalData());
        logger.info(series.getName());
        logger.info(String.valueOf(series.getBarCount()));

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        Bar lastBar = series.getBar(series.getEndIndex());

        System.out.println("Open  = " + lastBar.getOpenPrice());
        System.out.println("High  = " + lastBar.getHighPrice());
        System.out.println("Low   = " + lastBar.getLowPrice());
        System.out.println("Close = " + lastBar.getClosePrice());
        System.out.println("Volume= " + lastBar.getVolume());
        System.out.println("Time  = " + lastBar.getEndTime());
        return List.of();
    }

    private BarSeries getBarSeriesFromJsonArray(JSONArray historicalData) {
        BarSeries series = new BaseBarSeriesBuilder()
                .withName(SYMBOL)
                .build();
        OneCandleDataModel oneCandleDataModel;
        for(int i = 0; i < historicalData.length(); i++) {
            oneCandleDataModel = new OneCandleDataModel(historicalData.getJSONArray(i));
            ZonedDateTime endTime = Instant.ofEpochMilli(oneCandleDataModel.getCloseTime())
                    .atZone(ZoneId.of("Europe/Paris"));

            Bar bar = new BaseBar(
                    Duration.ofMinutes(15),
                    endTime,
                    oneCandleDataModel.getOpenPrice(),
                    oneCandleDataModel.getHighPrice(),
                    oneCandleDataModel.getLowPrice(),
                    oneCandleDataModel.getClosePrice(),
                    oneCandleDataModel.getVolume()
            );

            series.addBar(bar);
        }
        return series;
    }

    /**
     * Sens index 12 avant index 989
     * le dernier element de la list est le plus récent
     */
    public JSONArray getOldHistoricalData() {
        JSONArray historicalData = null;
        try {
            String content = new String(
                    Files.readAllBytes(new File(filPath + cryptoChoice).toPath())
            );
            historicalData = new JSONArray(content);
            historicalData.remove(historicalData.length() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return historicalData;
    }

    public void getNewHistoricalData(String symbolToProcess) {
        JSONArray historicalData = this.binanceSymbolService.getSymbolHistoricalDateBySymbolAndInterval(symbolToProcess,INTERVAL);
        try (FileWriter file = new FileWriter( filPath + symbolToProcess + "HistoricalData.json")) {

            file.write(historicalData.toString(2)); // indentation = 2 espaces
            file.flush();

            System.out.println("JSON écrit avec succès !");

        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info(String.valueOf(historicalData.length()));
    }

}
