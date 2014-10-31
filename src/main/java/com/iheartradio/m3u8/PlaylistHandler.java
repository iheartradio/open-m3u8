package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.PlaylistData;

class PlaylistHandler implements LineHandler {
    @Override
    public void handle(String line, ParseState state) {
        final PlaylistData playlistData;

        if (Constants.URL_PATTERN.matcher(line).matches()) {
            playlistData = PlaylistData.fromUrl(line);
        } else {
            playlistData = PlaylistData.fromPath(line);
        }

        state.getMaster().playlists.add(playlistData);
    }
}
