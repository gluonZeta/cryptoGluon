package gluon.projects.infra.impl;

import gluon.projects.infra.OrderBookService;
import gluon.projects.infra.OrderFlowService;
import lombok.Getter;
import lombok.Setter;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

@Setter
public class BinanceWebSocketExchange extends WebSocketClient {

    private static final Logger logger = LoggerFactory.getLogger(BinanceWebSocketExchange.class);

    private OrderBookService orderBookService;

    private OrderFlowService orderFlowService;

    public BinanceWebSocketExchange(URI serverUri, OrderBookService orderBookService) {
        super(serverUri);
        this.orderBookService = orderBookService;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.info("------------------ CONNECTION OPEN ------------------");
    }

    @Override
    public void onMessage(String messageResponse) {
        JSONObject exchangeResponseData = new JSONObject(messageResponse);
        String streamDataResponseValue = (String) exchangeResponseData.get("stream");
        if(streamDataResponseValue.contains("trade")) {
            logger.info("##### ORDER FLOW");
            orderFlowService.processOrderFlow((JSONObject) exchangeResponseData.get("data"));
        } else if (streamDataResponseValue.contains("depth")) {
            //logger.info("##### ORDER BOOOOOK");
            orderBookService.processOrderBook(messageResponse);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        logger.info("------------------ CONNECTION CLOSE ------------------");
    }

    @Override
    public void onError(Exception e) {
        logger.info("------------------ CONNECTION ERROR ------------------");
    }
}
