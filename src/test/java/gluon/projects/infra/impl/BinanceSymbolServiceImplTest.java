package gluon.projects.infra.impl;

import gluon.projects.infra.BinanceSymbolService;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BinanceSymbolServiceImplTest {

    BinanceSymbolService binanceSymbolService;

    @BeforeEach
    void setUp() {
        binanceSymbolService = new BinanceSymbolServiceImpl();
    }

    @Test
    void getExchangeInfos() {
        JSONArray symbolsDetail = binanceSymbolService.getExchangeInfos();
        assertFalse(symbolsDetail.isEmpty());
    }

    @Test
    void getSymbolHistoricalData() {
        JSONArray symbolHistoricalData = binanceSymbolService.getSymbolHistoricalData("BTCUSDT",2);
        assertFalse(symbolHistoricalData.isEmpty());
    }
}