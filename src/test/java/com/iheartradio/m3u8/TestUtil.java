package com.iheartradio.m3u8;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;

public class TestUtil {
    public static InputStream inputStreamFromResource(final String fileName) {
        assertNotNull(fileName);

        try {
            return new FileInputStream("src/test/resources/" + fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("failed to open playlist file: " + fileName);
        }
    }
}
