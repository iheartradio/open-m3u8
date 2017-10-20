package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlaylistValidation {
    private final Set<PlaylistError> mErrors;

    private PlaylistValidation(Set<PlaylistError> errors) {
        mErrors = Collections.unmodifiableSet(errors);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("(PlaylistValidation")
                .append(" valid=").append(isValid())
                .append(" errors=").append(mErrors)
                .append(")")
                .toString();
    }

    public boolean isValid() {
        return mErrors.isEmpty();
    }

    public Set<PlaylistError> getErrors() {
        return mErrors;
    }

    /**
     * Equivalent to: PlaylistValidation.from(playlist, ParsingMode.STRICT)
     */
    public static PlaylistValidation from(Playlist playlist) {
        return PlaylistValidation.from(playlist, ParsingMode.STRICT);
    }

    public static PlaylistValidation from(Playlist playlist, ParsingMode parsingMode) {
        Set<PlaylistError> errors = new HashSet<>();

        if (playlist == null) {
            errors.add(PlaylistError.NO_PLAYLIST);
            return new PlaylistValidation(errors);
        }

        if (playlist.getCompatibilityVersion() < Playlist.MIN_COMPATIBILITY_VERSION) {
            errors.add(PlaylistError.COMPATIBILITY_TOO_LOW);
        }

        if (hasNoPlaylistTypes(playlist)) {
            errors.add(PlaylistError.NO_MASTER_OR_MEDIA);
        } else if (hasBothPlaylistTypes(playlist)) {
            errors.add(PlaylistError.BOTH_MASTER_AND_MEDIA);
        }

        if (playlist.hasMasterPlaylist()) {
            if (!playlist.isExtended()) {
                errors.add(PlaylistError.MASTER_NOT_EXTENDED);
            }

            addMasterPlaylistErrors(playlist.getMasterPlaylist(), errors);
        }

        if (playlist.hasMediaPlaylist()) {
            addMediaPlaylistErrors(playlist.getMediaPlaylist(), errors, playlist.isExtended(), parsingMode);
        }

        return new PlaylistValidation(errors);
    }

    private static boolean hasNoPlaylistTypes(Playlist playlist) {
        return !(playlist.hasMasterPlaylist() || playlist.hasMediaPlaylist());
    }

    private static boolean hasBothPlaylistTypes(Playlist playlist) {
        return playlist.hasMasterPlaylist() && playlist.hasMediaPlaylist();
    }

    private static void addMasterPlaylistErrors(MasterPlaylist playlist, Set<PlaylistError> errors) {
        for (PlaylistData playlistData : playlist.getPlaylists()) {
            addPlaylistDataErrors(playlistData, errors);
        }

        for (IFrameStreamInfo iFrameStreamInfo : playlist.getIFramePlaylists()) {
            addIFrameStreamInfoErrors(iFrameStreamInfo, errors);
        }

        for (MediaData mediaData : playlist.getMediaData()) {
            addMediaDataErrors(mediaData, errors);
        }
    }

    private static void addMediaPlaylistErrors(MediaPlaylist playlist, Set<PlaylistError> errors, boolean isExtended, ParsingMode parsingMode) {
        if (isExtended && playlist.hasStartData()) {
            addStartErrors(playlist.getStartData(), errors);
        }

        addByteRangeErrors(playlist.getTracks(), errors, parsingMode);

        for (TrackData trackData : playlist.getTracks()) {
            addTrackDataErrors(trackData, errors, isExtended, parsingMode);
        }
    }

    private static void addByteRangeErrors(List<TrackData> tracks, Set<PlaylistError> errors, ParsingMode parsingMode) {
        Set<String> knownOffsets = new HashSet<>();
        for (TrackData track : tracks) {
            if (!track.hasByteRange()) {
                continue;
            }

            if (track.getByteRange().hasOffset()) {
                knownOffsets.add(track.getUri());
            } else if (!knownOffsets.contains(track.getUri())) {
                errors.add(PlaylistError.BYTERANGE_WITH_UNDEFINED_OFFSET);
            }
        }
    }

    private static void addStartErrors(StartData startData, Set<PlaylistError> errors) {
        if (Float.isNaN(startData.getTimeOffset())) {
            errors.add(PlaylistError.START_DATA_WITHOUT_TIME_OFFSET);
        }
    }

    private static void addPlaylistDataErrors(PlaylistData playlistData, Set<PlaylistError> errors) {
        if (playlistData.getUri() == null || playlistData.getUri().isEmpty()) {
            errors.add(PlaylistError.PLAYLIST_DATA_WITHOUT_URI);
        }


        if (playlistData.hasStreamInfo()) {
            if (playlistData.getStreamInfo().getBandwidth() == StreamInfo.NO_BANDWIDTH) {
                errors.add(PlaylistError.STREAM_INFO_WITH_NO_BANDWIDTH);
            }

            if (playlistData.getStreamInfo().getAverageBandwidth() < StreamInfo.NO_BANDWIDTH) {
                errors.add(PlaylistError.STREAM_INFO_WITH_INVALID_AVERAGE_BANDWIDTH);
            }
        }
    }

    private static void addIFrameStreamInfoErrors(IFrameStreamInfo streamInfo, Set<PlaylistError> errors) {
        if (streamInfo.getUri() == null || streamInfo.getUri().isEmpty()) {
            errors.add(PlaylistError.I_FRAME_STREAM_WITHOUT_URI);
        }

        if (streamInfo.getBandwidth() == StreamInfo.NO_BANDWIDTH) {
            errors.add(PlaylistError.I_FRAME_STREAM_WITH_NO_BANDWIDTH);
        }

        if (streamInfo.getAverageBandwidth() < StreamInfo.NO_BANDWIDTH) {
            errors.add(PlaylistError.I_FRAME_STREAM_WITH_INVALID_AVERAGE_BANDWIDTH);
        }
    }

    private static void addMediaDataErrors(MediaData mediaData, Set<PlaylistError> errors) {
        if (mediaData.getType() == null) {
            errors.add(PlaylistError.MEDIA_DATA_WITHOUT_TYPE);
        }

        if (mediaData.getGroupId() == null) {
            errors.add(PlaylistError.MEDIA_DATA_WITHOUT_GROUP_ID);
        }

        if (mediaData.getName() == null) {
            errors.add(PlaylistError.MEDIA_DATA_WITHOUT_NAME);
        }

        if (mediaData.getType() == MediaType.CLOSED_CAPTIONS) {
            if (mediaData.hasUri()) {
                errors.add(PlaylistError.CLOSE_CAPTIONS_WITH_URI);
            }

            if (mediaData.getInStreamId() == null) {
                errors.add(PlaylistError.CLOSE_CAPTIONS_WITHOUT_IN_STREAM_ID);
            }
        } else {
            if (mediaData.getType() != MediaType.CLOSED_CAPTIONS && mediaData.getInStreamId() != null) {
                errors.add(PlaylistError.IN_STREAM_ID_WITHOUT_CLOSE_CAPTIONS);
            }
        }

        if (mediaData.isDefault() && !mediaData.isAutoSelect()) {
            errors.add(PlaylistError.DEFAULT_WITHOUT_AUTO_SELECT);
        }

        if (mediaData.getType() != MediaType.SUBTITLES && mediaData.isForced()) {
            errors.add(PlaylistError.FORCED_WITHOUT_SUBTITLES);
        }
    }

    private static void addTrackDataErrors(TrackData trackData, Set<PlaylistError> errors, boolean isExtended, ParsingMode parsingMode) {
        if (trackData.getUri() == null || trackData.getUri().isEmpty()) {
            errors.add(PlaylistError.TRACK_DATA_WITHOUT_URI);
        }

        if (isExtended && !trackData.hasTrackInfo()) {
            errors.add(PlaylistError.EXTENDED_TRACK_DATA_WITHOUT_TRACK_INFO);
        }

        if (trackData.hasEncryptionData()) {
            if (trackData.getEncryptionData().getMethod() == null) {
                errors.add(PlaylistError.ENCRYPTION_DATA_WITHOUT_METHOD);
            }
        }

        if (trackData.hasTrackInfo()) {
            if (!parsingMode.allowNegativeNumbers && trackData.getTrackInfo().duration < 0) {
                errors.add(PlaylistError.TRACK_INFO_WITH_NEGATIVE_DURATION);
            }
        }

        if (trackData.hasMapInfo()) {
            if (trackData.getMapInfo().getUri() == null || trackData.getMapInfo().getUri().isEmpty()) {
                errors.add(PlaylistError.MAP_INFO_WITHOUT_URI);
            }
        }
    }
}
