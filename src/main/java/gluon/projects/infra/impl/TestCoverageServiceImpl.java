package gluon.projects.infra.impl;

import gluon.projects.infra.TestCoverageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestCoverageServiceImpl implements TestCoverageService {

    private static Logger logger = LoggerFactory.getLogger(TestCoverageServiceImpl.class);

    @Override
    public int addFunction(int a, int b) {
        logger.info("Call add ++ function");
        return a+b;
    }

    @Override
    public int multiplyFunction(int a, int b) {
        logger.info("Call multiply XX function");
        return a*b;
    }
}
