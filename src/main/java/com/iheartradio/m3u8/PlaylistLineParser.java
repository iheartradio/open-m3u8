package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.PlaylistData;

class PlaylistLineParser implements LineParser {
    @Override
    public void parse(String line, ParseState state) {
        final PlaylistData.Builder builder = new PlaylistData.Builder();
        final MasterParseState masterState = state.getMaster();

        masterState.playlists.add(builder
                .withUri(line)
                .withStreamInfo(masterState.streamInfo)
                .build());

        masterState.streamInfo = null;
    }
}
