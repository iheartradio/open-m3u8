package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.StartData;

import java.util.List;

interface PlaylistParseState<T> extends IParseState<T> {
    PlaylistParseState<T> setUnknownTags(List<String> unknownTags);

    PlaylistParseState<T> setStartData(StartData startData);
}
