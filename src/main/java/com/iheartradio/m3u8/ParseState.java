package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.Playlist;

class ParseState implements IParseState<Playlist> {
    private MasterParseState mMasterParseState;
    private MediaParseState mMediaParseState;
    private boolean mIsExtended;

    public void setExtended() throws ParseException {
        if (mIsExtended) {
            throw new ParseException(ParseExceptionType.MULTIPLE_EXTM3U);
        }

        mIsExtended = true;
    }

    public boolean isMaster() {
        return mMasterParseState != null;
    }

    public MasterParseState getMaster() {
        return mMasterParseState;
    }

    public void setMaster() throws ParseException {
        if (isMedia()) {
            throw new ParseException(ParseExceptionType.MASTER_IN_MEDIA);
        }

        if (mMasterParseState == null) {
            mMasterParseState = new MasterParseState();
        }
    }

    public boolean isMedia() {
        return mMediaParseState != null;
    }

    public MediaParseState getMedia() {
        return mMediaParseState;
    }

    public void setMedia() throws ParseException {
        if (isMaster()) {
            throw new ParseException(ParseExceptionType.MEDIA_IN_MASTER);
        }

        if (mMediaParseState == null) {
            mMediaParseState = new MediaParseState();
        }
    }

    @Override
    public Playlist buildPlaylist() throws ParseException {
        if (isMaster()) {
            return new Playlist(mMasterParseState.buildPlaylist());
        } else if (isMedia()) {
            return new Playlist(mMediaParseState.buildPlaylist());
        } else {
            throw new ParseException(ParseExceptionType.UNKNOWN_PLAYLIST_TYPE);
        }
    }
}
