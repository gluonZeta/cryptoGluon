package gluon.projects.infra.impl;

import gluon.projects.infra.TestCoverageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestCoverageServiceImplTest {

    private TestCoverageService testCoverageService;

    @BeforeEach
    void setUp() {
        testCoverageService = new TestCoverageServiceImpl();
    }

    @Test
    void addFunction() {
        assertEquals(13, testCoverageService.addFunction(5,8));
    }

    @Test
    void multiplyFunction() {
        assertEquals(56, testCoverageService.multiplyFunction(7,8));
    }
}