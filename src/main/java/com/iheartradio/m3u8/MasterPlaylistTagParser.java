package com.iheartradio.m3u8;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.iheartradio.m3u8.data.MediaData;
import com.iheartradio.m3u8.data.MediaType;
import com.iheartradio.m3u8.data.PlaylistData;
import com.iheartradio.m3u8.data.StreamInfo;
import com.iheartradio.m3u8.data.StreamInfo.Builder;

abstract class MasterPlaylistTagParser extends ExtTagParser {
    @Override
    public void parse(String line, ParseState state) throws ParseException {
        validateNotMedia(state);
        state.setMaster();
        super.parse(line, state);
    }

    private void validateNotMedia(ParseState state) throws ParseException {
        if (state.isMedia()) {
            throw ParseException.create(ParseExceptionType.MASTER_IN_MEDIA, getTag());
        }
    }

    // master playlist tags

    static final IExtTagParser EXT_X_MEDIA = new MasterPlaylistTagParser() {
        private final Map<String, AttributeParser<MediaData.Builder>> HANDLERS = new HashMap<String, AttributeParser<MediaData.Builder>>();

        {
            HANDLERS.put(Constants.TYPE, new AttributeParser<MediaData.Builder>() {
                @Override
                public void parse(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    final MediaType type = MediaType.fromValue(attribute.value);

                    if (type == null) {
                        throw ParseException.create(ParseExceptionType.INVALID_MEDIA_TYPE, getTag(), attribute.toString());
                    } else {
                        builder.withType(type);
                    }
                }
            });

            HANDLERS.put(Constants.URI, new AttributeParser<MediaData.Builder>() {
                @Override
                public void parse(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    builder.withUri(ParseUtil.decodeUrl(ParseUtil.parseQuotedString(attribute.value, getTag()), state.encoding));
                }
            });

            HANDLERS.put(Constants.GROUP_ID, new AttributeParser<MediaData.Builder>() {
                @Override
                public void parse(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    final String groupId = ParseUtil.parseQuotedString(attribute.value, getTag());

                    if (groupId.isEmpty()) {
                        throw ParseException.create(ParseExceptionType.EMPTY_MEDIA_GROUP_ID, getTag(), attribute.toString());
                    } else {
                        builder.withGroupId(groupId);
                    }
                }
            });

            HANDLERS.put(Constants.LANGUAGE, new AttributeParser<MediaData.Builder>() {
                @Override
                public void parse(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    builder.withLanguage(ParseUtil.parseQuotedString(attribute.value, getTag()));
                }
            });

            HANDLERS.put(Constants.ASSOCIATED_LANGUAGE, new AttributeParser<MediaData.Builder>() {
                @Override
                public void parse(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    builder.withAssociatedLanguage(ParseUtil.parseQuotedString(attribute.value, getTag()));
                }
            });

            HANDLERS.put(Constants.NAME, new AttributeParser<MediaData.Builder>() {
                @Override
                public void parse(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    final String name = ParseUtil.parseQuotedString(attribute.value, getTag());

                    if (name.isEmpty()) {
                        throw ParseException.create(ParseExceptionType.EMPTY_MEDIA_NAME, getTag(), attribute.toString());
                    } else {
                        builder.withName(name);
                    }
                }
            });

            HANDLERS.put(Constants.DEFAULT, new AttributeParser<MediaData.Builder>() {
                @Override
                public void parse(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    builder.withDefault(ParseUtil.parseYesNo(attribute.value, getTag()));
                }
            });

            HANDLERS.put(Constants.AUTO_SELECT, new AttributeParser<MediaData.Builder>() {
                @Override
                public void parse(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    builder.withAutoSelect(ParseUtil.parseYesNo(attribute.value, getTag()));
                }
            });

            HANDLERS.put(Constants.FORCED, new AttributeParser<MediaData.Builder>() {
                @Override
                public void parse(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    builder.withForced(ParseUtil.parseYesNo(attribute.value, getTag()));
                }
            });

            HANDLERS.put(Constants.IN_STREAM_ID, new AttributeParser<MediaData.Builder>() {
                @Override
                public void parse(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    final String inStreamId = ParseUtil.parseQuotedString(attribute.value, getTag());

                    if (Constants.EXT_X_MEDIA_IN_STREAM_ID_PATTERN.matcher(inStreamId).matches()) {
                        builder.withInStreamId(inStreamId);
                    } else {
                        throw ParseException.create(ParseExceptionType.INVALID_MEDIA_IN_STREAM_ID, getTag(), attribute.toString());
                    }
                }
            });

            HANDLERS.put(Constants.CHARACTERISTICS, new AttributeParser<MediaData.Builder>() {
                @Override
                public void parse(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    final String[] characteristicStrings = ParseUtil.parseQuotedString(attribute.value, getTag()).split(Constants.ATTRIBUTE_LIST_SEPARATOR);

                    if (characteristicStrings.length == 0) {
                        throw ParseException.create(ParseExceptionType.EMPTY_MEDIA_CHARACTERISTICS, getTag(), attribute.toString());
                    } else {
                        builder.withCharacteristics(Arrays.asList(characteristicStrings));
                    }
                }
            });
        }

        @Override
        public String getTag() {
            return Constants.EXT_X_MEDIA_TAG;
        }

        @Override
        boolean hasData() {
            return true;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            super.parse(line, state);

            final MediaData.Builder builder = new MediaData.Builder();
            parseAttributes(line, builder, state, HANDLERS);
            final MediaData mediaData = builder.build();

            if (mediaData.getType() == null) {
                throw ParseException.create(ParseExceptionType.MISSING_MEDIA_TYPE, getTag(), line);
            }

            if (mediaData.getGroupId() == null) {
                throw ParseException.create(ParseExceptionType.MISSING_MEDIA_GROUP_ID, getTag(), line);
            }

            if (mediaData.getName() == null) {
                throw ParseException.create(ParseExceptionType.MISSING_MEDIA_NAME, getTag(), line);
            }

            if (mediaData.getType() == MediaType.CLOSED_CAPTIONS && mediaData.getUri() != null) {
                throw ParseException.create(ParseExceptionType.CLOSE_CAPTIONS_WITH_URI, getTag(), line);
            }

            if (mediaData.getType() == MediaType.CLOSED_CAPTIONS && mediaData.getInStreamId() == null) {
                throw ParseException.create(ParseExceptionType.CLOSE_CAPTIONS_WITHOUT_IN_STREAM_ID, getTag(), line);
            }

            if (mediaData.getType() != MediaType.CLOSED_CAPTIONS && mediaData.getInStreamId() != null) {
                throw ParseException.create(ParseExceptionType.IN_STREAM_ID_WITHOUT_CLOSE_CAPTIONS, getTag(), line);
            }

            if (mediaData.isDefault() && builder.isAutoSelectSet() && !mediaData.isAutoSelect()) {
                throw ParseException.create(ParseExceptionType.DEFAULT_WITHOUT_AUTO_SELECT, getTag(), line);
            }

            if (mediaData.getType() != MediaType.SUBTITLES && builder.isForcedSet()) {
                throw ParseException.create(ParseExceptionType.FORCED_WITHOUT_SUBTITLES, getTag(), line);
            }

            state.getMaster().mediaData.add(mediaData);
        }
    };

    static abstract class EXT_STREAM_INF extends MasterPlaylistTagParser {
        final Map<String, AttributeParser<StreamInfo.Builder>> HANDLERS = new HashMap<String, AttributeParser<StreamInfo.Builder>>();

        {
            HANDLERS.put(Constants.BANDWIDTH, new AttributeParser<StreamInfo.Builder>() {
                @Override
                public void parse(Attribute attribute, StreamInfo.Builder builder, ParseState state) throws ParseException {
                    builder.withBandwidth(ParseUtil.parseInt(attribute.value, getTag()));
                }
            });

            HANDLERS.put(Constants.AVERAGE_BANDWIDTH, new AttributeParser<StreamInfo.Builder>() {
                @Override
                public void parse(Attribute attribute, StreamInfo.Builder builder, ParseState state) throws ParseException {
                    builder.withAverageBandwidth(ParseUtil.parseInt(attribute.value, getTag()));
                }
            });

            HANDLERS.put(Constants.CODECS, new AttributeParser<StreamInfo.Builder>() {
                @Override
                public void parse(Attribute attribute, StreamInfo.Builder builder, ParseState state) throws ParseException {
                    final String[] characteristicStrings = ParseUtil.parseQuotedString(attribute.value, getTag()).split(Constants.ATTRIBUTE_LIST_SEPARATOR);

                    if (characteristicStrings.length > 0) {
                        builder.withCodecs(Arrays.asList(characteristicStrings));
                    }
                }
            });

            HANDLERS.put(Constants.RESOLUTION, new AttributeParser<StreamInfo.Builder>() {
                @Override
                public void parse(Attribute attribute, StreamInfo.Builder builder, ParseState state) throws ParseException {
                    builder.withResolution(ParseUtil.parseResolution(attribute.value, getTag()));
                }
            });

            HANDLERS.put(Constants.VIDEO, new AttributeParser<StreamInfo.Builder>() {
                @Override
                public void parse(Attribute attribute, StreamInfo.Builder builder, ParseState state) throws ParseException {
                    builder.withVideo(ParseUtil.parseQuotedString(attribute.value, getTag()));
                }
            });

            HANDLERS.put(Constants.PROGRAM_ID, new AttributeParser<StreamInfo.Builder>() {
                @Override
                public void parse(Attribute attribute, StreamInfo.Builder builder, ParseState state) throws ParseException {
                    // deprecated
                }
            });
        }

        @Override
        boolean hasData() {
            return true;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            super.parse(line, state);

            final StreamInfo.Builder builder = new StreamInfo.Builder();
            parseAttributes(line, builder, state, HANDLERS);
            final StreamInfo streamInfo = builder.build();

            if (!builder.isBandwidthSet()) {
                throw new ParseException(ParseExceptionType.MISSING_STREAM_BANDWIDTH, getTag());
            }
            
            handle(builder, state, streamInfo);
        }
        
        protected abstract void handle(Builder builder, ParseState state, StreamInfo streamInfo) throws ParseException;

    }
    
    static final IExtTagParser EXT_X_I_FRAME_STREAM_INF = new EXT_STREAM_INF() {

        {
            HANDLERS.put(Constants.URI, new AttributeParser<StreamInfo.Builder>() {
                @Override
                public void parse(Attribute attribute, StreamInfo.Builder builder, ParseState state) throws ParseException {
                    builder.withUri(ParseUtil.parseQuotedString(attribute.value, getTag()));
                }
            });
        }
        
        protected void handle(Builder streamBuilder, ParseState state, StreamInfo streamInfo) throws ParseException {

            if (!streamBuilder.isUriSet()) {
                throw new ParseException(ParseExceptionType.MISSING_STREAM_URI, getTag());
            }
            String line = streamInfo.getUri();
            
            final PlaylistData.Builder builder = new PlaylistData.Builder();

            if (Constants.URL_PATTERN.matcher(line).matches()) {
                builder.withUrl(line);
            } else {
                builder.withPath(line);
            }
            
            final MasterParseState masterState = state.getMaster();

            masterState.playlists.add(builder
                    .withStreamInfo(masterState.streamInfo)
                    .build());
        };
        
        @Override
        public String getTag() {
            return Constants.EXT_X_I_FRAME_STREAM_INF_TAG;
        } 
    };
    
    static final IExtTagParser EXT_X_STREAM_INF = new EXT_STREAM_INF() {
        
        {
            HANDLERS.put(Constants.AUDIO, new AttributeParser<StreamInfo.Builder>() {
                @Override
                public void parse(Attribute attribute, StreamInfo.Builder builder, ParseState state) throws ParseException {
                    builder.withAudio(ParseUtil.parseQuotedString(attribute.value, getTag()));
                }
            });
            
            HANDLERS.put(Constants.SUBTITLES, new AttributeParser<StreamInfo.Builder>() {
                @Override
                public void parse(Attribute attribute, StreamInfo.Builder builder, ParseState state) throws ParseException {
                    builder.withSubtitles(ParseUtil.parseQuotedString(attribute.value, getTag()));
                }
            });

            HANDLERS.put(Constants.CLOSED_CAPTIONS, new AttributeParser<StreamInfo.Builder>() {
                @Override
                public void parse(Attribute attribute, StreamInfo.Builder builder, ParseState state) throws ParseException {
                    if (!attribute.value.equals(Constants.NO_CLOSED_CAPTIONS)) {
                        builder.withClosedCaptions(ParseUtil.parseQuotedString(attribute.value, getTag()));
                    }
                }
            });    
        }
        
        protected void handle(Builder builder, ParseState state, StreamInfo streamInfo) {
            state.getMaster().streamInfo = streamInfo;
        };
        
        @Override
        public String getTag() {
            return Constants.EXT_X_STREAM_INF_TAG;
        }
    };
}
