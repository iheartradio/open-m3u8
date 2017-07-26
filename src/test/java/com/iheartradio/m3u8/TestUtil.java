package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.Playlist;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    public static Playlist parsePlaylistFromResource(final String fileName) throws IOException, ParseException, PlaylistException {
        assertNotNull(fileName);

        try (InputStream is = new FileInputStream("src/test/resources/" + fileName)) {
            return new PlaylistParser(is, Format.EXT_M3U, Encoding.UTF_8).parse();
        }
    }
}
