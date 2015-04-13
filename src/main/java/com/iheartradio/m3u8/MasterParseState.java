package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.MasterPlaylist;
import com.iheartradio.m3u8.data.MediaData;
import com.iheartradio.m3u8.data.PlaylistData;
import com.iheartradio.m3u8.data.StreamInfo;

import java.util.ArrayList;
import java.util.List;

class MasterParseState implements IParseState<MasterPlaylist> {
    public final List<PlaylistData> playlists = new ArrayList<PlaylistData>();
    public final List<MediaData> mediaData = new ArrayList<MediaData>();
    public final List<String> unknownTags = new ArrayList<String>();
    
    public StreamInfo streamInfo;

    @Override
    public MasterPlaylist buildPlaylist() throws ParseException {
        return new MasterPlaylist.Builder()
                .withPlaylists(playlists)
                .withMediaData(mediaData)
                .withUnknownTags(unknownTags)
                .build();
    }
}
