package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.Playlist;

import java.io.IOException;

interface IPlaylistParser {
    Playlist parse() throws IOException, ParseException, PlaylistException;
    boolean isAvailable();
}
