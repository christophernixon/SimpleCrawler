package org.example;

import org.testng.annotations.*;

import static org.testng.Assert.fail;

import org.testng.Assert;

public class AppTest {

    @Test
    public void testAppStarts() {
        try {
            new App();
        } catch (Exception e) {
            Assert.fail("Unexpected exception:", e);
        }
    }
}
