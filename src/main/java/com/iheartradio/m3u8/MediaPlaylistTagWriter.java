package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

abstract class MediaPlaylistTagWriter extends ExtTagWriter {
    
    @Override
    public final void write(TagWriter tagWriter, Playlist playlist) throws IOException, ParseException {
        if (playlist.hasMediaPlaylist()) {
            doWrite(tagWriter, playlist, playlist.getMediaPlaylist());
        }
    }
    
    public void doWrite(TagWriter tagWriter,Playlist playlist, MediaPlaylist mediaPlaylist) throws IOException, ParseException {
        tagWriter.writeTag(getTag());
    }

    // media playlist tags
    
    static final IExtTagWriter EXT_X_ENDLIST = new MediaPlaylistTagWriter() {
        @Override
        public String getTag() {
            return Constants.EXT_X_ENDLIST_TAG;
        }

        @Override
        boolean hasData() {
            return false;
        }

        @Override
        public void doWrite(TagWriter tagWriter,Playlist playlist, MediaPlaylist mediaPlaylist) throws IOException {
            if (!mediaPlaylist.isOngoing()) {
                tagWriter.writeTag(getTag());
            }
        }
    };
    
    static final IExtTagWriter EXT_X_I_FRAMES_ONLY = new MediaPlaylistTagWriter() {
        @Override
        public String getTag() {
            return Constants.EXT_X_I_FRAMES_ONLY_TAG;
        }

        @Override
        boolean hasData() {
            return false;
        }
        
        @Override
        public void doWrite(TagWriter tagWriter,Playlist playlist, MediaPlaylist mediaPlaylist) throws IOException {
            if (mediaPlaylist.isIframesOnly()) {
                tagWriter.writeTag(getTag());
            }
        }
    };
    
    static final IExtTagWriter EXT_X_PLAYLIST_TYPE = new MediaPlaylistTagWriter() {
        @Override
        public String getTag() {
            return Constants.EXT_X_PLAYLIST_TYPE_TAG;
        }

        @Override
        boolean hasData() {
            return true;
        }

        @Override
        public void doWrite(TagWriter tagWriter, Playlist playlist, MediaPlaylist mediaPlaylist) throws IOException {
            if (mediaPlaylist.getPlaylistType() != null) {
                tagWriter.writeTag(getTag(), mediaPlaylist.getPlaylistType().getValue());
            }
        }
    };
    
    static final IExtTagWriter EXT_X_START = new MediaPlaylistTagWriter() {
        private final Map<String, AttributeWriter<StartData>> HANDLERS = new HashMap<String, AttributeWriter<StartData>>();
        
        {
            HANDLERS.put(Constants.TIME_OFFSET, new AttributeWriter<StartData>() {
                @Override
                public boolean containsAttribute(StartData attributes) {
                    return true;
                }
                
                @Override
                public String write(StartData attributes) throws ParseException {
                    return Float.toString(attributes.getTimeOffset());
                }
            });
            
            HANDLERS.put(Constants.PRECISE, new AttributeWriter<StartData>() {
                @Override
                public boolean containsAttribute(StartData attributes) {
                    return true;
                }
                
                @Override
                public String write(StartData attributes) throws ParseException {
                    if (attributes.isPrecise()) {
                        return Constants.YES;
                    } else {
                        return Constants.NO;
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
        public void doWrite(TagWriter tagWriter, Playlist playlist, MediaPlaylist mediaPlaylist) throws IOException, ParseException {
            if (mediaPlaylist.hasStartData()) {
                StartData startData = mediaPlaylist.getStartData();
                writeAttributes(tagWriter, startData, HANDLERS);
            }
        }
    };
    
    static final IExtTagWriter EXT_X_TARGETDURATION = new MediaPlaylistTagWriter() {
        @Override
        public String getTag() {
            return Constants.EXT_X_TARGETDURATION_TAG;
        }

        @Override
        boolean hasData() {
            return true;
        }
        
        @Override
        public void doWrite(TagWriter tagWriter, Playlist playlist, MediaPlaylist mediaPlaylist) throws IOException, ParseException {
            tagWriter.writeTag(getTag(), Integer.toString(mediaPlaylist.getTargetDuration()));
        }
    };

    static final IExtTagWriter EXT_X_MEDIA_SEQUENCE = new MediaPlaylistTagWriter() {
        @Override
        public String getTag() {
            return Constants.EXT_X_MEDIA_SEQUENCE_TAG;
        }

        @Override
        boolean hasData() {
            return true;
        }
        
        @Override
        public void doWrite(TagWriter tagWriter, Playlist playlist, MediaPlaylist mediaPlaylist) throws IOException ,ParseException {
            tagWriter.writeTag(getTag(), Integer.toString(mediaPlaylist.getMediaSequenceNumber()));
        };
    };

    static final IExtTagWriter EXT_X_ALLOW_CACHE = new MediaPlaylistTagWriter() {
        @Override
        public String getTag() {
            return Constants.EXT_X_ALLOW_CACHE_TAG;
        }

        @Override
        boolean hasData() {
            return true;
        }
        
        @Override
        public void doWrite(TagWriter tagWriter, Playlist playlist, MediaPlaylist mediaPlaylist){

            // deprecated
        };
    };

    // media segment tags

    static final SectionWriter MEDIA_SEGMENTS = new SectionWriter() {
        @Override
        public void write(TagWriter tagWriter, Playlist playlist) throws IOException, ParseException {
            if (playlist.hasMediaPlaylist()) {
                KeyWriter keyWriter = new KeyWriter();
                MapInfoWriter mapInfoWriter = new MapInfoWriter();

                for (TrackData trackData : playlist.getMediaPlaylist().getTracks()) {
                    if (trackData.hasDiscontinuity()) {
                        tagWriter.writeTag(Constants.EXT_X_DISCONTINUITY_TAG);
                    }

                    keyWriter.writeTrackData(tagWriter, playlist, trackData);
                    mapInfoWriter.writeTrackData(tagWriter, playlist, trackData);

                    if (trackData.hasByteRange()) {
                        writeByteRange(tagWriter, trackData.getByteRange());
                    }

                    writeExtinf(tagWriter, playlist, trackData);
                    tagWriter.writeLine(trackData.getUri());
                }
            }
        }
    };

    private static void writeExtinf(TagWriter tagWriter, Playlist playlist, TrackData trackData) throws IOException {
        final StringBuilder builder = new StringBuilder();

        if (playlist.getCompatibilityVersion() < 3) {
            builder.append(Integer.toString((int) trackData.getTrackInfo().duration));
        } else {
            builder.append(Float.toString(trackData.getTrackInfo().duration));
        }

	 builder.append(Constants.COMMA);
        if (trackData.getTrackInfo().title != null) {
            builder.append(trackData.getTrackInfo().title);
        }

        tagWriter.writeTag(Constants.EXTINF_TAG, builder.toString());
    }

    private static void writeByteRange(TagWriter tagWriter, ByteRange byteRange) throws IOException {
        String value;

        if (byteRange.getOffset() != null) {
            value = String.valueOf(byteRange.getSubRangeLength())
                    + '@' + String.valueOf(byteRange.getOffset());
        } else {
            value = String.valueOf(byteRange.getSubRangeLength());
        }

        tagWriter.writeTag(Constants.EXT_X_BYTERANGE_TAG, value);
    }

    static class KeyWriter extends MediaPlaylistTagWriter {
        private final Map<String, AttributeWriter<EncryptionData>> HANDLERS = new HashMap<String, AttributeWriter<EncryptionData>>();

        private EncryptionData mEncryptionData;

        {
            HANDLERS.put(Constants.METHOD, new AttributeWriter<EncryptionData>() {
                @Override
                public boolean containsAttribute(EncryptionData attributes) {
                    return true;
                }
                
                @Override
                public String write(EncryptionData encryptionData) {
                    return encryptionData.getMethod().getValue();
                }
            });

            HANDLERS.put(Constants.URI, new AttributeWriter<EncryptionData>() {
                @Override
                public boolean containsAttribute(EncryptionData attributes) {
                    return true;
                }
                
                @Override
                public String write(EncryptionData encryptionData) throws ParseException {
                    return WriteUtil.writeQuotedString(encryptionData.getUri(), getTag());
                }
            });

            HANDLERS.put(Constants.IV, new AttributeWriter<EncryptionData>() {
                @Override
                public boolean containsAttribute(EncryptionData attribute) {
                    return attribute.hasInitializationVector();
                }
                
                @Override
                public String write(EncryptionData encryptionData) {
                    return WriteUtil.writeHexadecimal(encryptionData.getInitializationVector());
                }
            });

            HANDLERS.put(Constants.KEY_FORMAT, new AttributeWriter<EncryptionData>() {
                @Override
                public boolean containsAttribute(EncryptionData attributes) {
                    return true;
                }
                
                @Override
                public String write(EncryptionData encryptionData) throws ParseException {
                    //TODO check for version 5
                    return WriteUtil.writeQuotedString(encryptionData.getKeyFormat(), getTag(), true);
                }
            });

            HANDLERS.put(Constants.KEY_FORMAT_VERSIONS, new AttributeWriter<EncryptionData>() {
                @Override
                public boolean containsAttribute(EncryptionData attributes) {
                    return true;
                }
                
                @Override
                public String write(EncryptionData encryptionData) throws ParseException {
                    //TODO check for version 5
                    return WriteUtil.writeQuotedString(WriteUtil.join(encryptionData.getKeyFormatVersions(), Constants.LIST_SEPARATOR), getTag(), true);
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
        public void doWrite(TagWriter tagWriter, Playlist playlist, MediaPlaylist mediaPlaylist) throws IOException, ParseException {
            writeAttributes(tagWriter, mEncryptionData, HANDLERS);
        }

        void writeTrackData(TagWriter tagWriter, Playlist playlist, TrackData trackData) throws IOException, ParseException {
            if (trackData != null && trackData.hasEncryptionData()) {
                final EncryptionData encryptionData = trackData.getEncryptionData();

                if (!encryptionData.equals(mEncryptionData)) {
                    mEncryptionData = encryptionData;
                    write(tagWriter, playlist);
                }
            }
        }
    }

    static class MapInfoWriter extends MediaPlaylistTagWriter {
        private final Map<String, AttributeWriter<MapInfo>> HANDLERS = new LinkedHashMap<>();

        private MapInfo mMapInfo;

        {
            HANDLERS.put(Constants.URI, new AttributeWriter<MapInfo>() {
                @Override
                public String write(MapInfo attributes) throws ParseException {
                    return WriteUtil.writeQuotedString(attributes.getUri(), getTag());
                }

                @Override
                public boolean containsAttribute(MapInfo attributes) {
                    return true;
                }
            });

            HANDLERS.put(Constants.BYTERANGE, new AttributeWriter<MapInfo>() {
                @Override
                public String write(MapInfo attributes) throws ParseException {
                    ByteRange byteRange = attributes.getByteRange();
                    String value;
                    if (byteRange.hasOffset()) {
                        value = String.valueOf(byteRange.getSubRangeLength())
                                + '@' + String.valueOf(byteRange.getOffset());
                    } else {
                        value = String.valueOf(byteRange.getSubRangeLength());
                    }

                    return WriteUtil.writeQuotedString(value, getTag());
                }

                @Override
                public boolean containsAttribute(MapInfo mapInfo) {
                    return mapInfo.hasByteRange();
                }
            });
        }

        @Override
        boolean hasData() {
            return true;
        }

        @Override
        public String getTag() {
            return Constants.EXT_X_MAP;
        }

        @Override
        public void doWrite(TagWriter tagWriter, Playlist playlist, MediaPlaylist mediaPlaylist) throws IOException, ParseException {
            writeAttributes(tagWriter, mMapInfo, HANDLERS);
        }

        void writeTrackData(TagWriter tagWriter, Playlist playlist, TrackData trackData) throws IOException, ParseException {
            if (trackData != null && trackData.getMapInfo() != null) {
                final MapInfo mapInfo = trackData.getMapInfo();
                if (!mapInfo.equals(mMapInfo)) {
                    mMapInfo = mapInfo;
                    write(tagWriter, playlist);
                }
            }
        }
    }
}
