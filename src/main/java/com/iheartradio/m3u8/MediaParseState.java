package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.MediaPlaylist;
import com.iheartradio.m3u8.data.TrackData;

import java.util.ArrayList;
import java.util.List;

class MediaParseState implements IParseState<MediaPlaylist> {
    public final List<TrackData> tracks = new ArrayList<TrackData>();

    public Integer targetDuration;

    @Override
    public MediaPlaylist buildPlaylist() throws ParseException {
        return new MediaPlaylist.Builder()
                .withTracks(tracks)
                .withTargetDuration(targetDuration)
                .build();
    }
}
