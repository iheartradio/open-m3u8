package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.TrackData;

class TrackHandler implements LineHandler {
    @Override
    public void handle(String line, ParseState state) {
        final TrackData trackData;

        if (Constants.URL_PATTERN.matcher(line).matches()) {
            trackData = TrackData.fromUrl(line);
        } else {
            trackData = TrackData.fromPath(line);
        }

        state.getMedia().tracks.add(trackData);
    }
}
