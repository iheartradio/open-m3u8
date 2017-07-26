package com.iheartradio.m3u8;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.iheartradio.m3u8.data.Playlist;

class ExtendedM3uWriter extends Writer {
    private List<SectionWriter> mExtTagWriter = new ArrayList<SectionWriter>();

    public ExtendedM3uWriter(OutputStream outputStream, Encoding encoding) {
        super(outputStream, encoding);
        // Order influences output in file!
        putWriters(
                ExtTagWriter.EXTM3U_HANDLER,
                ExtTagWriter.EXT_X_VERSION_HANDLER,
                MediaPlaylistTagWriter.EXT_X_PLAYLIST_TYPE,
                MediaPlaylistTagWriter.EXT_X_TARGETDURATION,
                MediaPlaylistTagWriter.EXT_X_START,
                MediaPlaylistTagWriter.EXT_X_MEDIA_SEQUENCE,
                MediaPlaylistTagWriter.EXT_X_I_FRAMES_ONLY,
                MasterPlaylistTagWriter.EXT_X_MEDIA,
                MediaPlaylistTagWriter.EXT_X_ALLOW_CACHE,
                MasterPlaylistTagWriter.EXT_X_STREAM_INF,
                MasterPlaylistTagWriter.EXT_X_I_FRAME_STREAM_INF,
                MediaPlaylistTagWriter.MEDIA_SEGMENTS,
                MediaPlaylistTagWriter.EXT_X_ENDLIST
        );
    }

    private void putWriters(SectionWriter... writers) {
        if (writers != null) {
            Collections.addAll(mExtTagWriter, writers);
        }
    }

    @Override
    void doWrite(Playlist playlist) throws IOException, ParseException, PlaylistException {
        for (SectionWriter singleTagWriter : mExtTagWriter) {
            singleTagWriter.write(tagWriter, playlist);
        }
    }
}
