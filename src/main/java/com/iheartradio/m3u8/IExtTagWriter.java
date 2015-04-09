package com.iheartradio.m3u8;

import java.io.IOException;

import com.iheartradio.m3u8.data.Playlist;

interface IExtTagWriter {
    String getTag();
    
    void write(TagWriter tagWriter, Playlist playlist) throws IOException, ParseException;
}
