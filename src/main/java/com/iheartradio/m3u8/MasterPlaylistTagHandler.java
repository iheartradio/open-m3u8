package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.MediaData;
import com.iheartradio.m3u8.data.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

abstract class MasterPlaylistTagHandler extends ExtTagHandler {
    @Override
    public void handle(String line, ParseState state) throws ParseException {
        validateNotMaster(state);
        state.setMaster();
        super.handle(line, state);
    }

    private void validateNotMaster(ParseState state) throws ParseException {
        if (state.isMedia()) {
            throw new ParseException(ParseExceptionType.MASTER_IN_MEDIA, getTag());
        }
    }

    // master playlist tags

    static final IExtTagHandler EXT_X_MEDIA = new MasterPlaylistTagHandler() {
        private final Map<String, AttributeHandler<MediaData.Builder>> HANDLERS = new HashMap<String, AttributeHandler<MediaData.Builder>>();
        private final String TYPE = "TYPE";
        private final String URI = "URI";
        private final String GROUP_ID = "GROUP-ID";
        private final String LANGUAGE = "LANGUAGE";
        private final String ASSOCIATED_LANGUAGE = "ASSOC-LANGUAGE";
        private final String NAME = "NAME";
        private final String DEFAULT = "DEFAULT";
        private final String AUTO_SELECT = "AUTOSELECT";
        private final String FORCED = "FORCED";
        private final String IN_STREAM_ID = "INSTREAM-ID";
        private final String CHARACTERISTICS = "CHARACTERISTICS";

        {
            HANDLERS.put(TYPE, new AttributeHandler<MediaData.Builder>() {
                @Override
                public void handle(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    final MediaType type = MediaType.fromValue(attribute.value);

                    if (type == null) {
                        throw new ParseException(ParseExceptionType.INVALID_MEDIA_TYPE, getTag());
                    } else {
                        builder.withType(type);
                    }
                }
            });

            HANDLERS.put(URI, new AttributeHandler<MediaData.Builder>() {
                @Override
                public void handle(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    builder.withUri(ParseUtil.decodeUrl(ParseUtil.parseQuotedString(attribute.value, getTag()), state.encoding));
                }
            });

            HANDLERS.put(GROUP_ID, new AttributeHandler<MediaData.Builder>() {
                @Override
                public void handle(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    final String groupId = ParseUtil.parseQuotedString(attribute.value, getTag());

                    if (groupId.isEmpty()) {
                        throw new ParseException(ParseExceptionType.EMPTY_MEDIA_GROUP_ID, getTag());
                    } else {
                        builder.withGroupId(groupId);
                    }
                }
            });

            HANDLERS.put(LANGUAGE, new AttributeHandler<MediaData.Builder>() {
                @Override
                public void handle(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    builder.withLanguage(ParseUtil.parseQuotedString(attribute.value, getTag()));
                }
            });

            HANDLERS.put(ASSOCIATED_LANGUAGE, new AttributeHandler<MediaData.Builder>() {
                @Override
                public void handle(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    builder.withAssociatedLanguage(ParseUtil.parseQuotedString(attribute.value, getTag()));
                }
            });

            HANDLERS.put(NAME, new AttributeHandler<MediaData.Builder>() {
                @Override
                public void handle(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    final String name = ParseUtil.parseQuotedString(attribute.value, getTag());

                    if (name.isEmpty()) {
                        throw new ParseException(ParseExceptionType.EMPTY_MEDIA_NAME, getTag());
                    } else {
                        builder.withName(name);
                    }
                }
            });

            HANDLERS.put(DEFAULT, new AttributeHandler<MediaData.Builder>() {
                @Override
                public void handle(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    builder.withDefault(ParseUtil.parseYesNo(attribute.value, getTag()));
                }
            });

            HANDLERS.put(AUTO_SELECT, new AttributeHandler<MediaData.Builder>() {
                @Override
                public void handle(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    builder.withAutoSelect(ParseUtil.parseYesNo(attribute.value, getTag()));
                }
            });

            HANDLERS.put(FORCED, new AttributeHandler<MediaData.Builder>() {
                @Override
                public void handle(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    builder.withForced(ParseUtil.parseYesNo(attribute.value, getTag()));
                }
            });

            HANDLERS.put(IN_STREAM_ID, new AttributeHandler<MediaData.Builder>() {
                @Override
                public void handle(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    final String inStreamId = ParseUtil.parseQuotedString(attribute.value, getTag());

                    if (Constants.EXT_X_MEDIA_IN_STREAM_ID_PATTERN.matcher(inStreamId).matches()) {
                        builder.withInStreamId(inStreamId);
                    } else {
                        throw new ParseException(ParseExceptionType.INVALID_MEDIA_IN_STREAM_ID, getTag());
                    }
                }
            });

            HANDLERS.put(CHARACTERISTICS, new AttributeHandler<MediaData.Builder>() {
                @Override
                public void handle(Attribute attribute, MediaData.Builder builder, ParseState state) throws ParseException {
                    final String[] characteristicStrings = ParseUtil.parseQuotedString(attribute.value, getTag()).split(",");

                    if (characteristicStrings.length == 0) {
                        throw new ParseException(ParseExceptionType.EMPTY_MEDIA_CHARACTERISTICS, getTag());
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
        boolean hasAttributes() {
            return true;
        }

        @Override
        public void handle(String line, ParseState state) throws ParseException {
            super.handle(line, state);

            final MediaData.Builder builder = new MediaData.Builder();
            parseAttributes(line, builder, state, HANDLERS);
            final MediaData mediaData = builder.build();

            if (mediaData.getType() == null) {
                throw new ParseException(ParseExceptionType.MISSING_MEDIA_TYPE, getTag());
            }

            if (mediaData.getGroupId() == null) {
                throw new ParseException(ParseExceptionType.MISSING_MEDIA_GROUP_ID, getTag());
            }

            if (mediaData.getName() == null) {
                throw new ParseException(ParseExceptionType.MISSING_MEDIA_NAME, getTag());
            }

            if (mediaData.getType() == MediaType.CLOSED_CAPTIONS && mediaData.getUri() != null) {
                throw new ParseException(ParseExceptionType.CLOSE_CAPTIONS_WITH_URI, getTag());
            }

            if (mediaData.getType() == MediaType.CLOSED_CAPTIONS && mediaData.getInStreamId() == null) {
                throw new ParseException(ParseExceptionType.CLOSE_CAPTIONS_WITHOUT_IN_STREAM_ID, getTag());
            }

            if (mediaData.getType() != MediaType.CLOSED_CAPTIONS && mediaData.getInStreamId() != null) {
                throw new ParseException(ParseExceptionType.IN_STREAM_ID_WITHOUT_CLOSE_CAPTIONS, getTag());
            }

            if (mediaData.isDefault() && builder.isAutoSelectSet() && !mediaData.isAutoSelect()) {
                throw new ParseException(ParseExceptionType.DEFAULT_WITHOUT_AUTO_SELECT, getTag());
            }

            if (mediaData.getType() != MediaType.SUBTITLES && builder.isForcedSet()) {
                throw new ParseException(ParseExceptionType.FORCED_WITHOUT_SUBTITLES, getTag());
            }

            state.getMaster().mediaData = mediaData;
        }
    };
}
