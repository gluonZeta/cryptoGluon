package gluon.projects.services.impl;

import gluon.projects.services.TestCoverageService;

public class TestCoverageServiceImpl implements TestCoverageService {
    @Override
    public int addFunction(int a, int b) {
        return a+b;
    }

    @Override
    public int multiplyFunction(int a, int b) {
        return a*b;
    }
}
