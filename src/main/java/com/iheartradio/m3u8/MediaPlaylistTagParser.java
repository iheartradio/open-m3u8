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

abstract class MediaPlaylistTagParser extends ExtTagParser {
    @Override
    public void parse(String line, ParseState state) throws ParseException {
        validateNotMaster(state);
        state.setMedia();
        super.parse(line, state);
    }

    private void validateNotMaster(ParseState state) throws ParseException {
        if (state.isMaster()) {
            throw ParseException.create(ParseExceptionType.MEDIA_IN_MASTER, getTag());
        }
    }
    
    // media playlist tags
    
    static final IExtTagParser EXT_X_ENDLIST = new MediaPlaylistTagParser() {
        @Override
        public String getTag() {
            return Constants.EXT_X_ENDLIST_TAG;
        }

        @Override
        boolean hasData() {
            return false;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            super.parse(line, state);

            match(Constants.EXT_X_ENDLIST_PATTERN, line);
            //TODO we should ensure that no new items are added beyond this point to the playlist
        }
    };
    
    static final IExtTagParser EXT_X_I_FRAMES_ONLY = new MediaPlaylistTagParser() {
        @Override
        public String getTag() {
            return Constants.EXT_X_I_FRAMES_ONLY_TAG;
        }

        @Override
        boolean hasData() {
            return false;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            super.parse(line, state);
            
            match(Constants.EXT_X_I_FRAMES_ONLY_PATTERN, line);
            
            if (state.getCompatibilityVersion() < 4) {
                throw ParseException.create(ParseExceptionType.REQUIRES_PROTOCOL_VERSION_4_OR_HIGHER, getTag());
            }
            
            state.setIsIframesOnly();
        }
    };
    
    static final IExtTagParser EXT_X_PLAYLIST_TYPE = new MediaPlaylistTagParser() {
        @Override
        public String getTag() {
            return Constants.EXT_X_PLAYLIST_TYPE_TAG;
        }

        @Override
        boolean hasData() {
            return true;
        }
        
        @Override
        public void parse(String line, ParseState state) throws ParseException {
            super.parse(line, state);

            final Matcher matcher = match(Constants.EXT_X_PLAYLIST_TYPE_PATTERN, line);

            if (state.getMedia().playlistType != null) {
                throw ParseException.create(ParseExceptionType.MULTIPLE_EXT_TAG_INSTANCES, getTag(), line);
            }

            state.getMedia().playlistType = ParseUtil.parseEnum(matcher.group(1), PlaylistType.class, getTag());
        }
    };
    
    static final IExtTagParser EXT_X_START = new MediaPlaylistTagParser() {
        private final Map<String, AttributeParser<StartData.Builder>> HANDLERS = new HashMap<String, AttributeParser<StartData.Builder>>();
        
        {
            HANDLERS.put(Constants.TIME_OFFSET, new AttributeParser<StartData.Builder>() {
                @Override
                public void parse(Attribute attribute, StartData.Builder builder, ParseState state) throws ParseException {
                    try {
                        final float timeOffset = Float.parseFloat(attribute.value);
                        builder.withTimeOffset(timeOffset);
                    } catch (NumberFormatException e) {
                        throw ParseException.create(ParseExceptionType.INVALID_FLOATING_POINT, getTag(), attribute.toString());
                    }
                }
            });
            
            HANDLERS.put(Constants.PRECISE, new AttributeParser<StartData.Builder>() {
                @Override
                public void parse(Attribute attribute, StartData.Builder builder, ParseState state) throws ParseException {
                    try {
                        final boolean precise = ParseUtil.parseYesNo(attribute.value, getTag());
                        builder.withPrecise(precise);
                    } catch (ParseException e) {
                        throw ParseException.create(ParseExceptionType.INVALID_YES_NO, getTag(), attribute.toString());
                    }
                }
            });
        }

        @Override
        public String getTag() {
            return Constants.EXT_X_START_TAG;
        }

        @Override
        boolean hasData() {
            return true;
        }
        
        @Override
        public void parse(String line, ParseState state) throws ParseException {
            super.parse(line, state);

            final StartData.Builder builder = new StartData.Builder();
            parseAttributes(line, builder, state, HANDLERS);
            final StartData startData = builder.build();

            state.getMedia().startData = startData;
        }
    };
    
    static final IExtTagParser EXT_X_TARGETDURATION = new MediaPlaylistTagParser() {
        @Override
        public String getTag() {
            return Constants.EXT_X_TARGETDURATION_TAG;
        }

        @Override
        boolean hasData() {
            return true;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            super.parse(line, state);

            final Matcher matcher = match(Constants.EXT_X_TARGETDURATION_PATTERN, line);

            if (state.getMedia().targetDuration != null) {
                throw ParseException.create(ParseExceptionType.MULTIPLE_EXT_TAG_INSTANCES, getTag(), line);
            }

            state.getMedia().targetDuration = ParseUtil.parseInt(matcher.group(1), getTag());
        }
    };

    static final IExtTagParser EXT_X_MEDIA_SEQUENCE = new MediaPlaylistTagParser() {
        @Override
        public String getTag() {
            return Constants.EXT_X_MEDIA_SEQUENCE_TAG;
        }

        @Override
        boolean hasData() {
            return true;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            super.parse(line, state);

            final Matcher matcher = match(Constants.EXT_X_MEDIA_SEQUENCE_PATTERN, line);

            if (state.getMedia().mediaSequenceNumber != null) {
                throw ParseException.create(ParseExceptionType.MULTIPLE_EXT_TAG_INSTANCES, getTag(), line);
            }

            state.getMedia().mediaSequenceNumber = ParseUtil.parseInt(matcher.group(1), getTag());
        }
    };

    static final IExtTagParser EXT_X_ALLOW_CACHE = new MediaPlaylistTagParser() {
        @Override
        public String getTag() {
            return Constants.EXT_X_ALLOW_CACHE_TAG;
        }

        @Override
        boolean hasData() {
            return true;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            super.parse(line, state);

            // deprecated
        }
    };

    // media segment tags

    static final IExtTagParser EXTINF = new MediaPlaylistTagParser() {
        @Override
        public String getTag() {
            return Constants.EXTINF_TAG;
        }

        @Override
        boolean hasData() {
            return true;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            super.parse(line, state);

            final Matcher matcher = match(Constants.EXTINF_PATTERN, line);

            state.getMedia().trackInfo = new TrackInfo(ParseUtil.parseFloat(matcher.group(1), getTag()), matcher.group(2));
        }
    };

    static final IExtTagParser EXT_X_KEY = new MediaPlaylistTagParser() {
        private final Map<String, AttributeParser<EncryptionData.Builder>> HANDLERS = new HashMap<String, AttributeParser<EncryptionData.Builder>>();

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
                    builder.withUri(ParseUtil.decodeUrl(ParseUtil.parseQuotedString(attribute.value, getTag()), state.encoding));
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
                    String[] versionStrings = ParseUtil.parseQuotedString(attribute.value, getTag()).split(Constants.LIST_SEPARATOR);
                    final List<Integer> versions = new ArrayList<Integer>();

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
        boolean hasData() {
            return true;
        }
        
        @Override
        public void parse(String line, ParseState state) throws ParseException {
            super.parse(line, state);

            final EncryptionData.Builder builder = new EncryptionData.Builder()
                    .withKeyFormat(Constants.DEFAULT_KEY_FORMAT)
                    .withKeyFormatVersions(Constants.DEFAULT_KEY_FORMAT_VERSIONS);

            parseAttributes(line, builder, state, HANDLERS);
            final EncryptionData encryptionData = builder.build();

            if (encryptionData.getMethod() != EncryptionMethod.NONE && encryptionData.getUri() == null) {
                throw ParseException.create(ParseExceptionType.MISSING_ENCRYPTION_URI, getTag(), line);
            }

            state.getMedia().encryptionData = encryptionData;
        }
    };
}
