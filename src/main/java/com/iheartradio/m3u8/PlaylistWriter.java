package com.iheartradio.m3u8;

import java.io.IOException;
import java.io.OutputStream;

import com.iheartradio.m3u8.data.Playlist;

/** 
 * Writing out a {@link Playlist} again works the same way as parsing via {@link PlaylistParser}:
<pre>OutputStream outputStream = ...
PlaylistWriter writer = new PlaylistWriter();
writer.write(outputStream, updatedPlaylist, Format.EXT_M3U, Encoding.UTF_8);</pre>
 * <p>causing this playlist to be written:</p>
<pre>#EXTM3U
#EXT-X-VERSION:5
#EXT-X-TARGETDURATION:3
#EXT-X-MEDIA-SEQUENCE:1
#EXTINF:3.0,Example Song
example.mp3
#EXTINF:3.0,Additional Song
additional.mp3
#EXT-X-ENDLIST</pre>
 */
public class PlaylistWriter {
    
    public void write(OutputStream outputStream, Playlist playlist, Format format, Encoding encoding) throws IOException, ParseException {
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream is null");
        }
        if (format == null) {
            throw new IllegalArgumentException("format is null");
        }
        if (encoding == null) {
            throw new IllegalArgumentException("encoding is null");
        }

        switch (format) {
            case M3U:
                new M3uWriter(outputStream, encoding).write(playlist);
                break;
            case EXT_M3U:
                new ExtendedM3uWriter(outputStream, encoding).write(playlist);
                break;
            default:
                throw new RuntimeException("unsupported format detected, this should be impossible: " + format);
        }

    }
    
}
