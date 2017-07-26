package com.iheartradio.m3u8;

import java.util.ArrayList;
import java.util.List;

import com.iheartradio.m3u8.data.EncryptionData;
import com.iheartradio.m3u8.data.MediaPlaylist;
import com.iheartradio.m3u8.data.PlaylistType;
import com.iheartradio.m3u8.data.StartData;
import com.iheartradio.m3u8.data.TrackData;
import com.iheartradio.m3u8.data.TrackInfo;

class MediaParseState implements PlaylistParseState<MediaPlaylist> {
    private List<String> mUnknownTags;
    private StartData mStartData;

    public final List<TrackData> tracks = new ArrayList<>();

    public Integer targetDuration;
    public Integer mediaSequenceNumber;
    public boolean isIframesOnly;
    public PlaylistType playlistType;
    public TrackInfo trackInfo;
    public EncryptionData encryptionData;
    public String programDateTime;
    public boolean endOfList;
    public boolean hasDiscontinuity;

    @Override
    public PlaylistParseState<MediaPlaylist> setUnknownTags(final List<String> unknownTags) {
        mUnknownTags = unknownTags;
        return this;
    }

    @Override
    public PlaylistParseState<MediaPlaylist> setStartData(final StartData startData) {
        mStartData = startData;
        return this;
    }

    @Override
    public MediaPlaylist buildPlaylist() throws ParseException {
        return new MediaPlaylist.Builder()
                .withTracks(tracks)
                .withUnknownTags(mUnknownTags)
                .withTargetDuration(targetDuration == null ? maximumDuration(tracks, 0) : targetDuration)
                .withIsIframesOnly(isIframesOnly)
                .withStartData(mStartData)
                .withIsOngoing(!endOfList)
                .withMediaSequenceNumber(mediaSequenceNumber == null ? 0 : mediaSequenceNumber)
                .withPlaylistType(playlistType)
                .build();
    }

    private static int maximumDuration(List<TrackData> tracks, float minValue) {
        float max = minValue;

        for (final TrackData trackData : tracks) {
            if (trackData.hasTrackInfo()) {
                max = Math.max(max, trackData.getTrackInfo().duration);
            }
        }

        return 0;
    }
}
