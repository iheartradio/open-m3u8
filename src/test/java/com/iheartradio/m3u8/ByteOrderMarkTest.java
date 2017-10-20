package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.Playlist;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.iheartradio.m3u8.TestUtil.inputStreamFromResource;
import static com.iheartradio.m3u8.Constants.UTF_8_BOM_BYTES;
import static org.junit.Assert.*;

public class ByteOrderMarkTest {
    @Test
    public void testParsingByteOrderMark() throws Exception {
        try (final InputStream inputStream = wrapWithByteOrderMark(inputStreamFromResource("simpleMediaPlaylist.m3u8"))) {
            final PlaylistParser playlistParser = new PlaylistParser(inputStream, Format.EXT_M3U, Encoding.UTF_8);
            final Playlist playlist = playlistParser.parse();
            assertEquals(10, playlist.getMediaPlaylist().getTargetDuration());
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testWritingByteOrderMark() throws Exception {
        final Playlist playlist1;
        final Playlist playlist2;
        final String written;

        try (final InputStream inputStream = inputStreamFromResource("simpleMediaPlaylist.m3u8")) {
            playlist1 = new PlaylistParser(inputStream, Format.EXT_M3U, Encoding.UTF_8).parse();
        }

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            final PlaylistWriter writer = new PlaylistWriter.Builder()
                    .withOutputStream(os)
                    .withFormat(Format.EXT_M3U)
                    .withEncoding(Encoding.UTF_8)
                    .useByteOrderMark()
                    .build();

            writer.write(playlist1);
            written = os.toString(Encoding.UTF_8.value);
        }

        assertEquals(Constants.UNICODE_BOM, written.charAt(0));

        try (final InputStream inputStream = new ByteArrayInputStream(written.getBytes(Encoding.UTF_8.value))) {
            playlist2 = new PlaylistParser(inputStream, Format.EXT_M3U, Encoding.UTF_8).parse();
        }

        assertEquals(playlist1, playlist2);
    }

    private static InputStream wrapWithByteOrderMark(final InputStream inputStream) {
        return new InputStream() {
            public int mNumRead;

            @Override
            public int read() throws IOException {
                if (UTF_8_BOM_BYTES.length > mNumRead) {
                    return UTF_8_BOM_BYTES[mNumRead++];
                } else {
                    return inputStream.read();
                }
            }

            @Override
            public void close() throws IOException {
                inputStream.close();
            }
        };
    }
}
