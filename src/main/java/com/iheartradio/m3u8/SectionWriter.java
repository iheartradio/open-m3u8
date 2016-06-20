package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.Playlist;

import java.io.IOException;

interface SectionWriter {
    void write(TagWriter tagWriter, Playlist playlist) throws IOException, ParseException;
}
