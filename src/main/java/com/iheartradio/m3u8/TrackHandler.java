package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.TrackData;

class TrackHandler implements LineHandler {
    @Override
    public void handle(String line, ParseState state) throws ParseException {
        final TrackData.Builder builder = new TrackData.Builder();
        final MediaParseState mediaState = state.getMedia();

        if (state.isExtended() && mediaState.trackInfo == null) {
            throw new ParseException(ParseExceptionType.MISSING_TRACK_INFO);
        }

        if (Constants.URL_PATTERN.matcher(line).matches()) {
            builder.withUrl(line);
        } else {
            builder.withPath(line);
        }

        mediaState.tracks.add(builder
                .withTrackInfo(mediaState.trackInfo)
                .withEncryptionData(mediaState.encryptionData)
                .build());

        mediaState.trackInfo = null;
    }
}
