package gluon.projects.services.impl;

import gluon.projects.utils.SonarProjectUtils;
import gluon.projects.services.ListCryptoService;

import java.util.Properties;

public class ListCryptoServiceImpl implements ListCryptoService {

    public ListCryptoServiceImpl() {
        Properties properties = SonarProjectUtils.getPropertiesByFileName("application.properties");
    }

}
