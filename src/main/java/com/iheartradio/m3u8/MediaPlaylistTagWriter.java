package com.iheartradio.m3u8;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.iheartradio.m3u8.data.EncryptionData;
import com.iheartradio.m3u8.data.MediaPlaylist;
import com.iheartradio.m3u8.data.Playlist;
import com.iheartradio.m3u8.data.StartData;
import com.iheartradio.m3u8.data.TrackData;

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

    static final IExtTagWriter EXTINF = new MediaPlaylistTagWriter() {
        @Override
        public String getTag() {
            return Constants.EXTINF_TAG;
        }

        @Override
        boolean hasData() {
            return true;
        }
        
        @Override
        public void doWrite(TagWriter tagWriter, Playlist playlist, MediaPlaylist mediaPlaylist) throws IOException ,ParseException {
            for (TrackData trackData : mediaPlaylist.getTracks()) {
                StringBuilder sb = new StringBuilder();
                if (playlist.getCompatibilityVersion() <= 3) {
                    sb.append(Integer.toString((int)trackData.getTrackInfo().duration));
                } else {
                    sb.append(Float.toString(trackData.getTrackInfo().duration));
                }
                if (trackData.getTrackInfo().title != null) {
                    sb.append(Constants.COMMA).append(trackData.getTrackInfo().title);
                }
                tagWriter.writeTag(getTag(), sb.toString());
                tagWriter.writeLine(trackData.getUri());
            }
        };
    };

    static final ExtTagWriter EXT_X_KEY = new MediaPlaylistTagWriter() {
        private final Map<String, AttributeWriter<EncryptionData>> HANDLERS = new HashMap<String, AttributeWriter<EncryptionData>>();

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
                    return WriteUtil.writeQuotedString(encryptionData.getKeyFormat(), getTag());
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
                    return WriteUtil.writeQuotedString(WriteUtil.join(encryptionData.getKeyFormatVersions(), Constants.LIST_SEPARATOR), getTag());
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
            if (mediaPlaylist.getTracks().size() > 0) {
                TrackData td = mediaPlaylist.getTracks().get(0);
                if (td.hasEncryptionData()) {
                    EncryptionData ed = td.getEncryptionData();
                    writeAttributes(tagWriter, ed, HANDLERS);
                }
            }
        }
    };
}
