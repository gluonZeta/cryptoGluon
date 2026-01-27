package gluon.projects.utilities;

import gluon.projects.exceptions.TechnicalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class RestApiUtilityTest {

    Properties properties;

    @BeforeEach
    void setUp() {
        this.properties = FileUtility.getPropertiesByFileName("application.properties");
    }

    @Test
    void sendRestApiRequest() {
        String binanceApiResponse = RestApiUtility.sendRestApiRequest(this.properties.getProperty("apibinanceurl") + "/ping");
        assertEquals("{}", binanceApiResponse);


        assertThrows(TechnicalException.class, () -> {
            //RestApiUtility.sendRestApiRequest("https://api.binance.com/api/v38");
            RestApiUtility.sendRestApiRequest("https://xxx");
        });
    }
}