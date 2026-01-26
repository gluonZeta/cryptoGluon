package gluon.projects.services.impl;

import gluon.projects.services.SymbolCryptoService;
import gluon.projects.utilities.FileUtility;

import java.util.List;
import java.util.Properties;

public class SymbolCryptoServiceImpl implements SymbolCryptoService {

    private String urlApiBinance;

    public SymbolCryptoServiceImpl() {
        Properties properties = FileUtility.getPropertiesByFileName("application.properties");
        this.urlApiBinance = properties.getProperty("apibinanceurl");
    }

    @Override
    public List<String> getFreshListSymbol() {
        return List.of();
    }

    @Override
    public List<String> getOldListSymbol() {
        return List.of();
    }

}
