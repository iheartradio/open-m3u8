package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class MasterPlaylistLineParser implements LineParser {
    private final IExtTagParser mTagParser;
    private final LineParser mLineParser;

    MasterPlaylistLineParser(IExtTagParser parser) {
        this(parser, new ExtLineParser(parser));
    }

    MasterPlaylistLineParser(IExtTagParser tagParser, LineParser lineParser) {
        mTagParser = tagParser;
        mLineParser = lineParser;
    }

    @Override
    public void parse(String line, ParseState state) throws ParseException {
        if (state.isMedia()) {
            throw ParseException.create(ParseExceptionType.MASTER_IN_MEDIA, mTagParser.getTag());
        }

        state.setMaster();
        mLineParser.parse(line, state);
    }

    // master playlist tags

    static final IExtTagParser EXT_X_MEDIA = new IExtTagParser() {
        private final LineParser mLineParser = new MasterPlaylistLineParser(this);
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
                    builder.withUri(ParseUtil.decodeUri(ParseUtil.parseQuotedString(attribute.value, getTag()), state.encoding));
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
                    boolean isDefault = ParseUtil.parseYesNo(attribute, getTag());

                    builder.withDefault(isDefault);
                    state.getMaster().isDefault = isDefault;

                    if (isDefault) {
                        if (state.getMaster().isNotAutoSelect) {
                            throw ParseException.create(ParseExceptionType.AUTO_SELECT_DISABLED_FOR_DEFAULT, getTag(), attribute.toString());
                        }

                        builder.withAutoSelect(true);
                    }
                }
            });

            HANDLERS.put(Constants.AUTO_SELECT, new AttributeParser<MediaData.Builder>() {
                @Override
                public void parse(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    final boolean isAutoSelect = ParseUtil.parseYesNo(attribute, getTag());

                    builder.withAutoSelect(isAutoSelect);
                    state.getMaster().isNotAutoSelect = !isAutoSelect;

                    if (state.getMaster().isDefault && !isAutoSelect) {
                        throw ParseException.create(ParseExceptionType.AUTO_SELECT_DISABLED_FOR_DEFAULT, getTag(), attribute.toString());
                    }
                }
            });

            HANDLERS.put(Constants.FORCED, new AttributeParser<MediaData.Builder>() {
                @Override
                public void parse(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    builder.withForced(ParseUtil.parseYesNo(attribute, getTag()));
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
                    final String[] characteristicStrings = ParseUtil.parseQuotedString(attribute.value, getTag()).split(Constants.COMMA);

                    if (characteristicStrings.length == 0) {
                        throw ParseException.create(ParseExceptionType.EMPTY_MEDIA_CHARACTERISTICS, getTag(), attribute.toString());
                    } else {
                        builder.withCharacteristics(Arrays.asList(characteristicStrings));
                    }
                }
            });

            HANDLERS.put(Constants.CHANNELS, new AttributeParser<MediaData.Builder>() {
                @Override
                public void parse(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    final String[] channelsStrings = ParseUtil.parseQuotedString(attribute.value, getTag()).split(Constants.LIST_SEPARATOR);

                    if (channelsStrings.length == 0 || channelsStrings[0].isEmpty()) {
                        throw ParseException.create(ParseExceptionType.EMPTY_MEDIA_CHANNELS, getTag(), attribute.toString());
                    } else {
                        final int channelsCount = ParseUtil.parseInt(channelsStrings[0], getTag());
                        builder.withChannels(channelsCount);
                    }
                }
            });
        }

        @Override
        public String getTag() {
            return Constants.EXT_X_MEDIA_TAG;
        }

        @Override
        public boolean hasData() {
            return true;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            mLineParser.parse(line, state);

            final MediaData.Builder builder = new MediaData.Builder();

            state.getMaster().clearMediaDataState();
            ParseUtil.parseAttributes(line, builder, state, HANDLERS, getTag());
            state.getMaster().mediaData.add(builder.build());
        }
    };

    static final IExtTagParser EXT_X_I_FRAME_STREAM_INF = new IExtTagParser() {
        private final LineParser mLineParser = new MasterPlaylistLineParser(this);
        private final Map<String, AttributeParser<IFrameStreamInfo.Builder>> HANDLERS = makeExtStreamInfHandlers(getTag());

        {
            HANDLERS.put(Constants.URI, new AttributeParser<IFrameStreamInfo.Builder>() {
                @Override
                public void parse(Attribute attribute, IFrameStreamInfo.Builder builder, ParseState state) throws ParseException {
                    builder.withUri(ParseUtil.parseQuotedString(attribute.value, getTag()));
                }
            });
        }

        @Override
        public String getTag() {
            return Constants.EXT_X_I_FRAME_STREAM_INF_TAG;
        }

        @Override
        public boolean hasData() {
            return true;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            mLineParser.parse(line, state);

            final IFrameStreamInfo.Builder builder = new IFrameStreamInfo.Builder();

            ParseUtil.parseAttributes(line, builder, state, HANDLERS, getTag());
            state.getMaster().iFramePlaylists.add(builder.build());
        }
    };
    
    static final IExtTagParser EXT_X_STREAM_INF = new IExtTagParser() {
        private final LineParser mLineParser = new MasterPlaylistLineParser(this);
        private final Map<String, AttributeParser<StreamInfo.Builder>> HANDLERS = makeExtStreamInfHandlers(getTag());

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

        @Override
        public String getTag() {
            return Constants.EXT_X_STREAM_INF_TAG;
        }

        @Override
        public boolean hasData() {
            return true;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            mLineParser.parse(line, state);

            final StreamInfo.Builder builder = new StreamInfo.Builder();

            ParseUtil.parseAttributes(line, builder, state, HANDLERS, getTag());
            state.getMaster().streamInfo = builder.build();
        }
    };

    static <T extends StreamInfoBuilder> Map<String, AttributeParser<T>> makeExtStreamInfHandlers(final String tag) {
        final Map<String, AttributeParser<T>> handlers = new HashMap<>();

        handlers.put(Constants.BANDWIDTH, new AttributeParser<T>() {
            @Override
            public void parse(Attribute attribute, T builder, ParseState state) throws ParseException {
                builder.withBandwidth(ParseUtil.parseInt(attribute.value, tag));
            }
        });

        handlers.put(Constants.AVERAGE_BANDWIDTH, new AttributeParser<T>() {
            @Override
            public void parse(Attribute attribute, T builder, ParseState state) throws ParseException {
                builder.withAverageBandwidth(ParseUtil.parseInt(attribute.value, tag));
            }
        });

        handlers.put(Constants.CODECS, new AttributeParser<T>() {
            @Override
            public void parse(Attribute attribute, T builder, ParseState state) throws ParseException {
                final String[] characteristicStrings = ParseUtil.parseQuotedString(attribute.value, tag).split(Constants.COMMA);

                if (characteristicStrings.length > 0) {
                    builder.withCodecs(Arrays.asList(characteristicStrings));
                }
            }
        });

        handlers.put(Constants.RESOLUTION, new AttributeParser<T>() {
            @Override
            public void parse(Attribute attribute, T builder, ParseState state) throws ParseException {
                builder.withResolution(ParseUtil.parseResolution(attribute.value, tag));
            }
        });

        handlers.put(Constants.FRAME_RATE, new AttributeParser<T>() {
            @Override
            public void parse(Attribute attribute, T builder, ParseState state) throws ParseException {
                builder.withFrameRate(ParseUtil.parseFloat(attribute.value, tag));
            }
        });

        handlers.put(Constants.VIDEO, new AttributeParser<T>() {
            @Override
            public void parse(Attribute attribute, T builder, ParseState state) throws ParseException {
                builder.withVideo(ParseUtil.parseQuotedString(attribute.value, tag));
            }
        });

        handlers.put(Constants.PROGRAM_ID, new AttributeParser<T>() {
            @Override
            public void parse(Attribute attribute, T builder, ParseState state) throws ParseException {
                // deprecated
            }
        });

        return handlers;
    }
}
