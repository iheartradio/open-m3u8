package com.iheartradio.m3u8;

import java.io.IOException;
import java.io.OutputStream;

import com.iheartradio.m3u8.data.Playlist;

class M3uWriter extends Writer{

    M3uWriter(OutputStream outputStream, Encoding encoding) {
        super(outputStream, encoding);
    }

    @Override
    void doWrite(Playlist playlist) throws IOException {
        throw new UnsupportedOperationException();
    }
}
