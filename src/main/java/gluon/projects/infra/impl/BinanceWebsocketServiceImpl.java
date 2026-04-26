package gluon.projects.infra.impl;

import gluon.projects.infra.*;
import gluon.projects.model.IndicatorsOrderBook;
import gluon.projects.model.IndicatorsOrderFlow;
import gluon.projects.model.OrderFlowData;
import gluon.projects.utilities.FileUtility;
import lombok.Setter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

@Setter
public class BinanceWebsocketServiceImpl implements BinanceWebsocketService {

    private String websocketUrl;

    private final String symbol;

    private IndicatorsOrderFlow indicatorsOrderFlow;

    private IOFService iofService;

    private IndicatorsOrderBook indicatorsOrderBook;

    private IOBService iobService;

    public BinanceWebsocketServiceImpl(String symbol, IndicatorsOrderFlow indicatorsOrderFlow, IOFService iofService
            ,IndicatorsOrderBook indicatorsOrderBook, IOBService iobService) {
        Properties properties = FileUtility.getPropertiesByFileName("application.properties");
        this.websocketUrl = properties.getProperty("streambinancesocket");
        this.symbol = symbol;
        this.indicatorsOrderFlow = indicatorsOrderFlow;
        this.iofService = iofService;
        this.indicatorsOrderBook = indicatorsOrderBook;
        this.iobService = iobService;
    }

    @Override
    public void launchExchange() {
        String urlWebsocketExchange = this.createUrlWebsocketExchange();
        try {
            /**
             * Partie Order book
             */
            OrderBookService orderBookService = new OrderBookServiceImpl(this.iobService);



            /**
             * Partie Order flow
             */
            OrderFlowService orderFlowService = new OrderFlowServiceImpl(indicatorsOrderFlow,iofService,symbol);


            /**
             * Websocket launch
             */
            BinanceWebSocketExchange binanceWebSocketExchange = new BinanceWebSocketExchange(new URI(urlWebsocketExchange), orderBookService);
            binanceWebSocketExchange.setOrderFlowService(orderFlowService);
            binanceWebSocketExchange.connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String createUrlWebsocketExchange() {
        return this.websocketUrl
                + "/stream?streams="
                + this.symbol.toLowerCase()
                + "@depth20/"
                + this.symbol.toLowerCase()
                + "@trade";
    }
}
