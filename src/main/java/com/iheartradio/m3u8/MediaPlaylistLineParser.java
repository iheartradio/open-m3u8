package com.iheartradio.m3u8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import com.iheartradio.m3u8.data.EncryptionData;
import com.iheartradio.m3u8.data.EncryptionData.Builder;
import com.iheartradio.m3u8.data.EncryptionMethod;
import com.iheartradio.m3u8.data.PlaylistType;
import com.iheartradio.m3u8.data.StartData;
import com.iheartradio.m3u8.data.TrackInfo;

class MediaPlaylistLineParser implements LineParser {
    private final IExtTagParser tagParser;
    private final LineParser lineParser;

    MediaPlaylistLineParser(IExtTagParser parser) {
        this(parser, new ExtLineParser(parser));
    }

    MediaPlaylistLineParser(IExtTagParser tagParser, LineParser lineParser) {
        this.tagParser = tagParser;
        this.lineParser = lineParser;
    }

    @Override
    public void parse(String line, ParseState state) throws ParseException {
        if (state.isMaster()) {
            throw ParseException.create(ParseExceptionType.MEDIA_IN_MASTER, tagParser.getTag());
        }

        state.setMedia();
        lineParser.parse(line, state);
    }

    // media playlist tags
    
    static final IExtTagParser EXT_X_ENDLIST = new IExtTagParser() {
        private final LineParser lineParser = new MediaPlaylistLineParser(this);

        @Override
        public String getTag() {
            return Constants.EXT_X_ENDLIST_TAG;
        }

        @Override
        public boolean hasData() {
            return false;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            lineParser.parse(line, state);

            ParseUtil.match(Constants.EXT_X_ENDLIST_PATTERN, line, getTag());
            state.getMedia().endOfList = true;
        }
    };
    
    static final IExtTagParser EXT_X_I_FRAMES_ONLY = new IExtTagParser() {
        private final LineParser lineParser = new MediaPlaylistLineParser(this);

        @Override
        public String getTag() {
            return Constants.EXT_X_I_FRAMES_ONLY_TAG;
        }

        @Override
        public boolean hasData() {
            return false;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            lineParser.parse(line, state);
            
            ParseUtil.match(Constants.EXT_X_I_FRAMES_ONLY_PATTERN, line, getTag());
            
            if (state.getCompatibilityVersion() < 4) {
                throw ParseException.create(ParseExceptionType.REQUIRES_PROTOCOL_VERSION_4_OR_HIGHER, getTag());
            }
            
            state.setIsIframesOnly();
        }
    };
    
    static final IExtTagParser EXT_X_PLAYLIST_TYPE = new IExtTagParser() {
        private final LineParser lineParser = new MediaPlaylistLineParser(this);

        @Override
        public String getTag() {
            return Constants.EXT_X_PLAYLIST_TYPE_TAG;
        }

        @Override
        public boolean hasData() {
            return true;
        }
        
        @Override
        public void parse(String line, ParseState state) throws ParseException {
            lineParser.parse(line, state);

            final Matcher matcher = ParseUtil.match(Constants.EXT_X_PLAYLIST_TYPE_PATTERN, line, getTag());

            if (state.getMedia().playlistType != null) {
                throw ParseException.create(ParseExceptionType.MULTIPLE_EXT_TAG_INSTANCES, getTag(), line);
            }

            state.getMedia().playlistType = ParseUtil.parseEnum(matcher.group(1), PlaylistType.class, getTag());
        }
    };
    
    static final IExtTagParser EXT_X_PROGRAM_DATE_TIME = new IExtTagParser() {
        private final LineParser lineParser = new MediaPlaylistLineParser(this);

        @Override
        public String getTag() {
            return Constants.EXT_X_PROGRAM_DATE_TIME_TAG;
        }

        @Override
        public boolean hasData() {
            return true;
        }
        
        @Override
        public void parse(String line, ParseState state) throws ParseException {
            lineParser.parse(line, state);

            final Matcher matcher = ParseUtil.match(Constants.EXT_X_PROGRAM_DATE_TIME_PATTERN, line, getTag());

            if (state.getMedia().playlistDateTime != null) {
                throw ParseException.create(ParseExceptionType.MULTIPLE_EXT_TAG_INSTANCES, getTag(), line);
            }

            state.getMedia().playlistDateTime = ParseUtil.parseDateTime(line,getTag());
        }
    };
    
    static final IExtTagParser EXT_X_START = new IExtTagParser() {
        private final LineParser lineParser = new MediaPlaylistLineParser(this);
        private final Map<String, AttributeParser<StartData.Builder>> HANDLERS = new HashMap<>();
        
        {
            HANDLERS.put(Constants.TIME_OFFSET, new AttributeParser<StartData.Builder>() {
                @Override
                public void parse(Attribute attribute, StartData.Builder builder, ParseState state) throws ParseException {
                    builder.withTimeOffset(ParseUtil.parseFloat(attribute.value, getTag()));
                }
            });
            
            HANDLERS.put(Constants.PRECISE, new AttributeParser<StartData.Builder>() {
                @Override
                public void parse(Attribute attribute, StartData.Builder builder, ParseState state) throws ParseException {
                    builder.withPrecise(ParseUtil.parseYesNo(attribute, getTag()));
                }
            });
        }

        @Override
        public String getTag() {
            return Constants.EXT_X_START_TAG;
        }

        @Override
        public boolean hasData() {
            return true;
        }
        
        @Override
        public void parse(String line, ParseState state) throws ParseException {
            lineParser.parse(line, state);

            final StartData.Builder builder = new StartData.Builder();
            ParseUtil.parseAttributes(line, builder, state, HANDLERS, getTag());
            final StartData startData = builder.build();

            state.getMedia().startData = startData;
        }
    };
    
    static final IExtTagParser EXT_X_TARGETDURATION = new IExtTagParser() {
        private final LineParser lineParser = new MediaPlaylistLineParser(this);

        @Override
        public String getTag() {
            return Constants.EXT_X_TARGETDURATION_TAG;
        }

        @Override
        public boolean hasData() {
            return true;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            lineParser.parse(line, state);

            final Matcher matcher = ParseUtil.match(Constants.EXT_X_TARGETDURATION_PATTERN, line, getTag());

            if (state.getMedia().targetDuration != null) {
                throw ParseException.create(ParseExceptionType.MULTIPLE_EXT_TAG_INSTANCES, getTag(), line);
            }

            state.getMedia().targetDuration = ParseUtil.parseInt(matcher.group(1), getTag());
        }
    };

    static final IExtTagParser EXT_X_MEDIA_SEQUENCE = new IExtTagParser() {
        private final LineParser lineParser = new MediaPlaylistLineParser(this);

        @Override
        public String getTag() {
            return Constants.EXT_X_MEDIA_SEQUENCE_TAG;
        }

        @Override
        public boolean hasData() {
            return true;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            lineParser.parse(line, state);

            final Matcher matcher = ParseUtil.match(Constants.EXT_X_MEDIA_SEQUENCE_PATTERN, line, getTag());

            if (state.getMedia().mediaSequenceNumber != null) {
                throw ParseException.create(ParseExceptionType.MULTIPLE_EXT_TAG_INSTANCES, getTag(), line);
            }

            state.getMedia().mediaSequenceNumber = ParseUtil.parseInt(matcher.group(1), getTag());
        }
    };

    static final IExtTagParser EXT_X_ALLOW_CACHE = new IExtTagParser() {
        private final LineParser lineParser = new MediaPlaylistLineParser(this);

        @Override
        public String getTag() {
            return Constants.EXT_X_ALLOW_CACHE_TAG;
        }

        @Override
        public boolean hasData() {
            return true;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            lineParser.parse(line, state);

            // deprecated
        }
    };

    // media segment tags

    static final IExtTagParser EXTINF = new IExtTagParser() {
        private final LineParser lineParser = new MediaPlaylistLineParser(this);

        @Override
        public String getTag() {
            return Constants.EXTINF_TAG;
        }

        @Override
        public boolean hasData() {
            return true;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            lineParser.parse(line, state);

            final Matcher matcher = ParseUtil.match(Constants.EXTINF_PATTERN, line, getTag());

            state.getMedia().trackInfo = new TrackInfo(ParseUtil.parseFloat(matcher.group(1), getTag()), matcher.group(2));
        }
    };

    static final IExtTagParser EXT_X_DISCONTINUITY = new IExtTagParser() {
        private final LineParser lineParser = new MediaPlaylistLineParser(this);

        @Override
        public String getTag() {
            return Constants.EXT_X_DISCONTINUITY_TAG;
        }

        @Override
        public boolean hasData() {
            return false;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            lineParser.parse(line, state);
            final Matcher matcher = ParseUtil.match(Constants.EXT_X_DISCONTINUITY_PATTERN, line, getTag());
            state.getMedia().hasDiscontinuity = true;
        }
    };

    static final IExtTagParser EXT_X_KEY = new IExtTagParser() {
        private final LineParser lineParser = new MediaPlaylistLineParser(this);
        private final Map<String, AttributeParser<EncryptionData.Builder>> HANDLERS = new HashMap<>();

        {
            HANDLERS.put(Constants.METHOD, new AttributeParser<EncryptionData.Builder>() {
                @Override
                public void parse(Attribute attribute, Builder builder, ParseState state) throws ParseException {
                    final EncryptionMethod method = EncryptionMethod.fromValue(attribute.value);

                    if (method == null) {
                        throw ParseException.create(ParseExceptionType.INVALID_ENCRYPTION_METHOD, getTag(), attribute.toString());
                    } else {
                        builder.withMethod(method);
                    }
                }
            });

            HANDLERS.put(Constants.URI, new AttributeParser<EncryptionData.Builder>() {
                @Override
                public void parse(Attribute attribute, Builder builder, ParseState state) throws ParseException {
                    builder.withUri(ParseUtil.decodeUri(ParseUtil.parseQuotedString(attribute.value, getTag()), state.encoding));
                }
            });

            HANDLERS.put(Constants.IV, new AttributeParser<EncryptionData.Builder>() {
                @Override
                public void parse(Attribute attribute, Builder builder, ParseState state) throws ParseException {
                    final List<Byte> initializationVector = ParseUtil.parseHexadecimal(attribute.value, getTag());

                    if ((initializationVector.size() != Constants.IV_SIZE) && 
                        (initializationVector.size() != Constants.IV_SIZE_ALTERNATIVE)) {
                        throw ParseException.create(ParseExceptionType.INVALID_IV_SIZE, getTag(), attribute.toString());
                    }

                    builder.withInitializationVector(initializationVector);
                }
            });

            HANDLERS.put(Constants.KEY_FORMAT, new AttributeParser<EncryptionData.Builder>() {
                @Override
                public void parse(Attribute attribute, Builder builder, ParseState state) throws ParseException {
                    builder.withKeyFormat(ParseUtil.parseQuotedString(attribute.value, getTag()));
                }
            });

            HANDLERS.put(Constants.KEY_FORMAT_VERSIONS, new AttributeParser<EncryptionData.Builder>() {
                @Override
                public void parse(Attribute attribute, Builder builder, ParseState state) throws ParseException {
                    final String[] versionStrings = ParseUtil.parseQuotedString(attribute.value, getTag()).split(Constants.LIST_SEPARATOR);
                    final List<Integer> versions = new ArrayList<>();

                    for (String version : versionStrings) {
                        try {
                            versions.add(Integer.parseInt(version));
                        } catch (NumberFormatException exception) {
                            throw ParseException.create(ParseExceptionType.INVALID_KEY_FORMAT_VERSIONS, getTag(), attribute.toString());
                        }
                    }
                    
                    builder.withKeyFormatVersions(versions);
                }
            });
        }

        @Override
        public String getTag() {
            return Constants.EXT_X_KEY_TAG;
        }

        @Override
        public boolean hasData() {
            return true;
        }
        
        @Override
        public void parse(String line, ParseState state) throws ParseException {
            lineParser.parse(line, state);

            final EncryptionData.Builder builder = new EncryptionData.Builder()
                    .withKeyFormat(Constants.DEFAULT_KEY_FORMAT)
                    .withKeyFormatVersions(Constants.DEFAULT_KEY_FORMAT_VERSIONS);

            ParseUtil.parseAttributes(line, builder, state, HANDLERS, getTag());

            final EncryptionData encryptionData = builder.build();

            if (encryptionData.getMethod() != EncryptionMethod.NONE && encryptionData.getUri() == null) {
                throw ParseException.create(ParseExceptionType.MISSING_ENCRYPTION_URI, getTag(), line);
            }

            state.getMedia().encryptionData = encryptionData;
        }
    };
}
