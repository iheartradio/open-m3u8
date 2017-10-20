package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.Playlist;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.iheartradio.m3u8.TestUtil.inputStreamFromResource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlaylistValidationTest {
    @Test
    public void testAllowNegativeNumbersValidation() throws Exception {
        Playlist playlist;
        boolean found = false;

        try (final InputStream inputStream = inputStreamFromResource("negativeDurationMediaPlaylist.m3u8")) {
            new PlaylistParser(inputStream, Format.EXT_M3U, Encoding.UTF_8).parse();
        } catch (final PlaylistException exception) {
            found = exception.getErrors().contains(PlaylistError.TRACK_INFO_WITH_NEGATIVE_DURATION);
        }

        assertTrue(found);

        try (final InputStream inputStream = inputStreamFromResource("negativeDurationMediaPlaylist.m3u8")) {
            playlist = new PlaylistParser(inputStream, Format.EXT_M3U, Encoding.UTF_8, ParsingMode.LENIENT).parse();
        }

        assertEquals(-1f, playlist.getMediaPlaylist().getTracks().get(0).getTrackInfo().duration, 0f);
    }

    @Test
    public void testInvalidBytRange() throws Exception {
        List<PlaylistError> errors = new ArrayList<>();
        try {
            TestUtil.parsePlaylistFromResource("mediaPlaylistWithInvalidByteRanges.m3u8");
        } catch (PlaylistException e) {
            errors.addAll(e.getErrors());
        }
        assertEquals(Collections.singletonList(PlaylistError.BYTERANGE_WITH_UNDEFINED_OFFSET), errors);
    }
}