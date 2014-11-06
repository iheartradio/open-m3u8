package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.EncryptionData;
import com.iheartradio.m3u8.data.MediaPlaylist;
import com.iheartradio.m3u8.data.TrackData;
import com.iheartradio.m3u8.data.TrackInfo;

import java.util.ArrayList;
import java.util.List;

class MediaParseState implements IParseState<MediaPlaylist> {
    public final List<TrackData> tracks = new ArrayList<TrackData>();

    public Integer targetDuration;
    public Integer mediaSequenceNumber;
    public TrackInfo trackInfo;
    public EncryptionData encryptionData;

    @Override
    public MediaPlaylist buildPlaylist() throws ParseException {
        return new MediaPlaylist.Builder()
                .withTracks(tracks)
                .withTargetDuration(targetDuration)
                .withMediaSequenceNumber(mediaSequenceNumber == null ? 0 : mediaSequenceNumber)
                .build();
    }
}
