package gluon.projects.services.impl;

import gluon.projects.services.SymbolCryptoService;
import gluon.projects.services.SymbolWriter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SymbolCryptoServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(SymbolCryptoServiceImplTest.class);

    @Test
    void getFreshListSymbol() {
        SymbolWriter symbolWriter = mock(FileSymbolWriterImpl.class);
        SymbolCryptoService symbolCryptoService = new SymbolCryptoServiceImpl(symbolWriter);
        List<String> symbols = symbolCryptoService.getFreshListSymbol();
        logger.info("Size of symbols tab: {}", symbols.size());
        logger.info("Symbol example: {}", symbols.get(30));
        assertTrue(symbols.size() > 10);
    }

    @Test
    void getOldListSymbol() {
    }
}