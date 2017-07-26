package com.iheartradio.m3u8;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iheartradio.m3u8.data.IFrameStreamInfo;
import com.iheartradio.m3u8.data.IStreamInfo;
import com.iheartradio.m3u8.data.MasterPlaylist;
import com.iheartradio.m3u8.data.MediaData;
import com.iheartradio.m3u8.data.Playlist;
import com.iheartradio.m3u8.data.PlaylistData;
import com.iheartradio.m3u8.data.StreamInfo;

abstract class MasterPlaylistTagWriter extends ExtTagWriter {
    
    @Override
    public final void write(TagWriter tagWriter, Playlist playlist) throws IOException, ParseException {
        if (playlist.hasMasterPlaylist()) {
            doWrite(tagWriter, playlist, playlist.getMasterPlaylist());
        }
    }
    
    public void doWrite(TagWriter tagWriter,Playlist playlist, MasterPlaylist masterPlaylist) throws IOException, ParseException {
        tagWriter.writeTag(getTag());
    }
    
    // master playlist tags

    static final IExtTagWriter EXT_X_MEDIA = new MasterPlaylistTagWriter() {
        private final Map<String, AttributeWriter<MediaData>> HANDLERS = new HashMap<String, AttributeWriter<MediaData>>();

        {
            HANDLERS.put(Constants.TYPE, new AttributeWriter<MediaData>() {
                @Override
                public boolean containsAttribute(MediaData mediaData) {
                    return true;
                };
                
                @Override
                public String write(MediaData mediaData) throws ParseException {
                    return mediaData.getType().getValue();
                }
            });

            HANDLERS.put(Constants.URI, new AttributeWriter<MediaData>() {
                @Override
                public boolean containsAttribute(MediaData mediaData) {
                    return mediaData.hasUri();
                };

                @Override
                public String write(MediaData mediaData) throws ParseException {
                    return WriteUtil.writeQuotedString(mediaData.getUri(), getTag());
                }
            });

            HANDLERS.put(Constants.GROUP_ID, new AttributeWriter<MediaData>() {
                @Override
                public boolean containsAttribute(MediaData mediaData) {
                    return true;
                };
                
                @Override
                public String write(MediaData mediaData) throws ParseException {
                    return WriteUtil.writeQuotedString(mediaData.getGroupId(), getTag());
                }
                
            });

            HANDLERS.put(Constants.LANGUAGE, new AttributeWriter<MediaData>() {
                @Override
                public boolean containsAttribute(MediaData mediaData) {
                    return mediaData.hasLanguage();
                };

                @Override
                public String write(MediaData mediaData) throws ParseException {
                    return WriteUtil.writeQuotedString(mediaData.getLanguage(), getTag());
                }
            });

            HANDLERS.put(Constants.ASSOCIATED_LANGUAGE, new AttributeWriter<MediaData>() {
                @Override
                public boolean containsAttribute(MediaData mediaData) {
                    return mediaData.hasAssociatedLanguage();
                };

                @Override
                public String write(MediaData mediaData) throws ParseException {
                    return WriteUtil.writeQuotedString(mediaData.getAssociatedLanguage(), getTag());
                }
                
            });

            HANDLERS.put(Constants.NAME, new AttributeWriter<MediaData>() {
                @Override
                public boolean containsAttribute(MediaData mediaData) {
                    return true;
                };
                
                @Override
                public String write(MediaData mediaData) throws ParseException {
                    return WriteUtil.writeQuotedString(mediaData.getName(), getTag());
                }
            });

            HANDLERS.put(Constants.DEFAULT, new AttributeWriter<MediaData>() {
                @Override
                public boolean containsAttribute(MediaData mediaData) {
                    return true;
                }
                
                @Override
                public String write(MediaData mediaData) throws ParseException {
                    return WriteUtil.writeYesNo(mediaData.isDefault());
                }
            });

            HANDLERS.put(Constants.AUTO_SELECT, new AttributeWriter<MediaData>() {
                @Override
                public boolean containsAttribute(MediaData mediaData) {
                    return true;
                }
                
                @Override
                public String write(MediaData mediaData) throws ParseException {
                    return WriteUtil.writeYesNo(mediaData.isAutoSelect());
                }
            });

            HANDLERS.put(Constants.FORCED, new AttributeWriter<MediaData>() {
                @Override
                public boolean containsAttribute(MediaData mediaData) {
                    return true;
                }
                
                @Override
                public String write(MediaData mediaData) throws ParseException {
                    return WriteUtil.writeYesNo(mediaData.isForced());
                }
            });

            HANDLERS.put(Constants.IN_STREAM_ID, new AttributeWriter<MediaData>() {
                @Override
                public boolean containsAttribute(MediaData mediaData) {
                    return mediaData.hasInStreamId();
                }

                @Override
                public String write(MediaData mediaData) throws ParseException {
                    return WriteUtil.writeQuotedString(mediaData.getInStreamId(), getTag());
                }
            });

            HANDLERS.put(Constants.CHARACTERISTICS, new AttributeWriter<MediaData>() {
                @Override
                public boolean containsAttribute(MediaData mediaData) {
                    return mediaData.hasCharacteristics();
                }
                
                @Override
                public String write(MediaData mediaData) throws ParseException {
                    return WriteUtil.writeQuotedString(WriteUtil.join(mediaData.getCharacteristics(), Constants.COMMA), getTag());
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
        public void doWrite(TagWriter tagWriter, Playlist playlist, MasterPlaylist masterPlaylist) throws IOException, ParseException {
            if (masterPlaylist.getMediaData().size() > 0) {
                List<MediaData> mds = masterPlaylist.getMediaData();
                for(MediaData md : mds) {
                    writeAttributes(tagWriter, md, HANDLERS);
                }
            }
        }
    };

    static abstract class EXT_STREAM_INF<T extends IStreamInfo> extends MasterPlaylistTagWriter {
        final Map<String, AttributeWriter<T>> HANDLERS = new HashMap<>();

        {
            HANDLERS.put(Constants.BANDWIDTH, new AttributeWriter<T>() {
                @Override
                public boolean containsAttribute(T streamInfo) {
                    return true;
                }
                
                @Override
                public String write(T streamInfo) {
                    return Integer.toString(streamInfo.getBandwidth());
                }
            });

            HANDLERS.put(Constants.AVERAGE_BANDWIDTH, new AttributeWriter<T>() {
                @Override
                public boolean containsAttribute(T streamInfo) {
                    return streamInfo.hasAverageBandwidth();
                }
                
                @Override
                public String write(T streamInfo)  {
                    return Integer.toString(streamInfo.getAverageBandwidth());
                }
            });

            HANDLERS.put(Constants.CODECS, new AttributeWriter<T>() {
                @Override
                public boolean containsAttribute(T streamInfo) {
                    return streamInfo.hasCodecs();
                }
                
                @Override
                public String write(T streamInfo) throws ParseException {
                    return WriteUtil.writeQuotedString(WriteUtil.join(streamInfo.getCodecs(), Constants.COMMA), getTag());
                }
            });

            HANDLERS.put(Constants.RESOLUTION, new AttributeWriter<T>() {
                @Override
                public boolean containsAttribute(T streamInfo) {
                    return streamInfo.hasResolution();
                }
                
                @Override
                public String write(T streamInfo) throws ParseException {
                    return WriteUtil.writeResolution(streamInfo.getResolution());
                }
            });

            HANDLERS.put(Constants.FRAME_RATE, new AttributeWriter<T>() {
                @Override
                public boolean containsAttribute(T streamInfo) {
                    return streamInfo.hasFrameRate();
                }

                @Override
                public String write(T streamInfo) throws ParseException {
                    return String.valueOf(streamInfo.getFrameRate());
                }
            });

            HANDLERS.put(Constants.VIDEO, new AttributeWriter<T>() {
                @Override
                public boolean containsAttribute(T streamInfo) {
                    return streamInfo.hasVideo();
                }
                
                @Override
                public String write(T streamInfo) throws ParseException {
                    return WriteUtil.writeQuotedString(streamInfo.getVideo(), getTag());
                }
            });

            HANDLERS.put(Constants.PROGRAM_ID, new AttributeWriter<T>() {
                @Override
                public boolean containsAttribute(T streamInfo) {
                    return false;
                }
                
                @Override
                public String write(T streamInfo) {
                    // deprecated
                    return "";
                }
            });
        }

        @Override
        boolean hasData() {
            return true;
        }
        
        public abstract void doWrite(TagWriter tagWriter, Playlist playlist, MasterPlaylist masterPlaylist) throws IOException, ParseException;
    }
    
    static final IExtTagWriter EXT_X_I_FRAME_STREAM_INF = new EXT_STREAM_INF<IFrameStreamInfo>() {
        
        {
            HANDLERS.put(Constants.URI, new AttributeWriter<IFrameStreamInfo>() {
                @Override
                public boolean containsAttribute(IFrameStreamInfo streamInfo) {
                    return true;
                };
                
                @Override
                public String write(IFrameStreamInfo streamInfo) throws ParseException {
                    return WriteUtil.writeQuotedString(streamInfo.getUri(), getTag());
                }
            });
        }
        
        @Override
        public String getTag() {
            return Constants.EXT_X_I_FRAME_STREAM_INF_TAG;
        }
        
        @Override
        public void doWrite(TagWriter tagWriter, Playlist playlist, MasterPlaylist masterPlaylist) throws IOException, ParseException {
            for(IFrameStreamInfo streamInfo : masterPlaylist.getIFramePlaylists()) {
                writeAttributes(tagWriter, streamInfo, HANDLERS);
            }
        }
    };
    
    static final IExtTagWriter EXT_X_STREAM_INF = new EXT_STREAM_INF<StreamInfo>() {
        
        {
            HANDLERS.put(Constants.AUDIO, new AttributeWriter<StreamInfo>() {
                @Override
                public boolean containsAttribute(StreamInfo streamInfo) {
                    return streamInfo.hasAudio();
                }
                
                @Override
                public String write(StreamInfo streamInfo) throws ParseException {
                    return WriteUtil.writeQuotedString(streamInfo.getAudio(), getTag());
                }
            });
            
            HANDLERS.put(Constants.SUBTITLES, new AttributeWriter<StreamInfo>() {
                @Override
                public boolean containsAttribute(StreamInfo streamInfo) {
                    return streamInfo.hasSubtitles();
                }
                
                @Override
                public String write(StreamInfo streamInfo) throws ParseException {
                    return WriteUtil.writeQuotedString(streamInfo.getSubtitles(), getTag());
                }
            });

            HANDLERS.put(Constants.CLOSED_CAPTIONS, new AttributeWriter<StreamInfo>() {
                @Override
                public boolean containsAttribute(StreamInfo streamInfo) {
                    return streamInfo.hasClosedCaptions();
                }
                
                public String write(StreamInfo streamInfo) throws ParseException {
                    return WriteUtil.writeQuotedString(streamInfo.getClosedCaptions(), getTag());
                }
            });    
        }
        
        @Override
        public String getTag() {
            return Constants.EXT_X_STREAM_INF_TAG;
        }
        
        @Override
        public void doWrite(TagWriter tagWriter, Playlist playlist, MasterPlaylist masterPlaylist) throws IOException, ParseException {
            for(PlaylistData playlistData : masterPlaylist.getPlaylists()) {
                if (playlistData.hasStreamInfo()) {
                    writeAttributes(tagWriter, playlistData.getStreamInfo(), HANDLERS);
                    tagWriter.writeLine(playlistData.getUri());
                }
            }
        }
    };
}
