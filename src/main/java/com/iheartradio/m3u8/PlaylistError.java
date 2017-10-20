package com.iheartradio.m3u8;

public enum PlaylistError {
    NO_PLAYLIST,

    /**
     * Compatibility version cannot be less than Playlist.MIN_COMPATIBILITY_VERSION.
     */
    COMPATIBILITY_TOO_LOW,

    /**
     * A master or media playlist is required.
     */
    NO_MASTER_OR_MEDIA,

    /**
     * Having both a master and a media playlist is not allowed.
     */
    BOTH_MASTER_AND_MEDIA,

    /**
     * A master playlist must use the extend M3U tags.
     */
    MASTER_NOT_EXTENDED,

    /**
     * StartData requires a time offset.
     */
    START_DATA_WITHOUT_TIME_OFFSET,

    /**
     * EncryptionData requires a method.
     */
    ENCRYPTION_DATA_WITHOUT_METHOD,

    /**
     * MediaData requires a type.
     */
    MEDIA_DATA_WITHOUT_TYPE,

    /**
     * MediaData requires a group ID.
     */
    MEDIA_DATA_WITHOUT_GROUP_ID,

    /**
     * MediaData requires a name.
     */
    MEDIA_DATA_WITHOUT_NAME,

    /**
     * Close captions MediaData cannot have a URI.
     */
    CLOSE_CAPTIONS_WITH_URI,

    /**
     * Close captions MediaData requires in stream ID.
     */
    CLOSE_CAPTIONS_WITHOUT_IN_STREAM_ID,

    /**
     * MediaData can only have in stream ID if it is close captions.
     */
    IN_STREAM_ID_WITHOUT_CLOSE_CAPTIONS,

    /**
     * MediaData must be auto selected if it is default.
     */
    DEFAULT_WITHOUT_AUTO_SELECT,

    /**
     * Only subtitles MediaData can be forced.
     */
    FORCED_WITHOUT_SUBTITLES,

    /**
     * TrackData requires a location.
     */
    TRACK_DATA_WITHOUT_URI,

    /**
     * TrackData requires TrackInfo for playlists that use extended M3U tags.
     */
    EXTENDED_TRACK_DATA_WITHOUT_TRACK_INFO,

    /**
     * TrackInfo duration must be non-nagative.
     * @see com.iheartradio.m3u8.ParsingMode#allowNegativeNumbers
     */
    TRACK_INFO_WITH_NEGATIVE_DURATION,

    /**
     * PlaylistData requires a URI.
     */
    PLAYLIST_DATA_WITHOUT_URI,

    /**
     * StreamInfo requires a bandwidth.
     */
    STREAM_INFO_WITH_NO_BANDWIDTH,

    /**
     * The average bandwidth in StreamInfo must be non-negative or StreamInfo.NO_BANDWIDTH.
     */
    STREAM_INFO_WITH_INVALID_AVERAGE_BANDWIDTH,

    /**
     * IFrameStreamInfo requires a URI.
     */
    I_FRAME_STREAM_WITHOUT_URI,

    /**
     * IFrameStreamInfo requires a bandwidth.
     */
    I_FRAME_STREAM_WITH_NO_BANDWIDTH,

    /**
     * The average bandwidth in IFrameStreamInfo must be non-negative or StreamInfo.NO_BANDWIDTH.
     */
    I_FRAME_STREAM_WITH_INVALID_AVERAGE_BANDWIDTH,

    /**
     * MapInfo requires a URI.
     */
    MAP_INFO_WITHOUT_URI,

    /**
     * If a byte range offset is not present, a previous media segment must appear in the playlist
     * with a sub-range of the same media resource.
     */
    BYTERANGE_WITH_UNDEFINED_OFFSET,
}
