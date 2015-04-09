package com.iheartradio.m3u8;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.iheartradio.m3u8.data.Playlist;

abstract class ExtTagWriter implements IExtTagWriter {

    @Override
    public void write(TagWriter tagWriter, Playlist playlist) throws IOException, ParseException {
        if (!hasData()) {
            tagWriter.writeTag(getTag());
        }
    }
    
    abstract boolean hasData();

    <T> void writeAttributes(TagWriter tagWriter, T attributes, Map<String, ? extends AttributeWriter<T>> attributeWriters) throws IOException, ParseException {
        StringBuilder sb = new StringBuilder();
        
        for(Map.Entry<String, ? extends AttributeWriter<T>> entry : attributeWriters.entrySet()) {
            AttributeWriter<T> handler = entry.getValue();
            String attribute = entry.getKey();
            if (handler.containsAttribute(attributes)) {
                String value = handler.write(attributes);
                sb.append(attribute).append(Constants.ATTRIBUTE_SEPARATOR).append(value);
                sb.append(Constants.ATTRIBUTE_LIST_SEPARATOR);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        
        tagWriter.writeTag(getTag(), sb.toString());
    }

    static final IExtTagWriter EXTM3U_HANDLER = new ExtTagWriter()  {
        @Override
        public String getTag() {
            return Constants.EXTM3U_TAG;
        }

        @Override
        boolean hasData() {
            return false;
        }
    };
    
    static final IExtTagWriter EXT_UNKNOWN_HANDLER = new ExtTagWriter() {
        @Override
        public String getTag() {
            return null;
        }

        @Override
        boolean hasData() {
            return true;
        }

        @Override
        public void write(TagWriter tagWriter, Playlist playlist) throws IOException {
            List<String> unknownTags;
            if (playlist.hasMasterPlaylist() && playlist.getMasterPlaylist().hasUnknownTags()) {
                unknownTags = playlist.getMasterPlaylist().getUnknownTags();
            } else if (playlist.getMediaPlaylist().hasUnknownTags()) {
                unknownTags = playlist.getMediaPlaylist().getUnknownTags();
            } else {
                unknownTags = Collections.emptyList();
            }
            for(String line : unknownTags) {
                tagWriter.writeLine(line);
            }
        }
        
    };
    
    static final IExtTagWriter EXT_X_VERSION_HANDLER = new ExtTagWriter() {
        
        @Override
        public String getTag() {
            return Constants.EXT_X_VERSION_TAG;
        }

        @Override
        boolean hasData() {
            return true;
        }
        
        @Override
        public void write(TagWriter tagWriter, Playlist playlist) throws IOException {
            tagWriter.writeTag(getTag(), Integer.toString(playlist.getCompatibilityVersion()));
        }
        
    };
}
