package gluon.projects.services.impl;

import gluon.projects.services.TestCoverageService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestCoverageServiceImplTest {

    TestCoverageService testCoverageService = new TestCoverageServiceImpl();

    @Test
    void addFunction() {
        assertEquals(7, testCoverageService.addFunction(3,4));
    }

    @Test
    void multiplyFunction() {
        assertEquals(30, testCoverageService.multiplyFunction(5,6));
    }
}