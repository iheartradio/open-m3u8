package com.iheartradio.m3u8.data;

public class PlaylistData extends LocationData {
    private final PlaylistInfo mPlaylistInfo;

    public static PlaylistData fromPath(String path) {
        return fromPath(path, null);
    }

    public static PlaylistData fromPath(String path, PlaylistInfo playlistInfo) {
        return new PlaylistData(LocationType.PATH, path, playlistInfo);
    }

    public static PlaylistData fromUrl(String url) {
        return fromUrl(url, null);
    }

    public static PlaylistData fromUrl(String url, PlaylistInfo playlistInfo) {
        return new PlaylistData(LocationType.URL, url, playlistInfo);
    }

    PlaylistData(LocationType locationType, String location, PlaylistInfo playlistInfo) {
        super(locationType, location);
        mPlaylistInfo = playlistInfo;
    }

    public boolean hasPlaylistInfo() {
        return mPlaylistInfo != null;
    }

    public PlaylistInfo getPlaylistInfo() {
        return mPlaylistInfo;
    }
}
