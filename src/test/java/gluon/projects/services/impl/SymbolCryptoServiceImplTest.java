package gluon.projects.services.impl;

import gluon.projects.services.SymbolCryptoService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SymbolCryptoServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(SymbolCryptoServiceImplTest.class);

    @Test
    void getFreshListSymbol() {
        SymbolCryptoService symbolCryptoService = new SymbolCryptoServiceImpl();
        List<String> symbols = symbolCryptoService.getFreshListSymbol();
        logger.info("Size of symbols tab: %s", symbols.size());
        logger.info("Symbol example: %d", symbols.get(130));
        assertTrue(symbols.size() > 10);
    }

    @Test
    void getOldListSymbol() {
    }
}