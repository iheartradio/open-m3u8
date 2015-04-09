package com.iheartradio.m3u8;

import java.io.IOException;
import java.io.OutputStreamWriter;

class TagWriter {
    private final OutputStreamWriter mWriter;
    
    public TagWriter(OutputStreamWriter outputStreamWriter) {
        mWriter = outputStreamWriter;
    }
    
    public void write(String str) throws IOException {
        mWriter.write(str);
    }
    
    public void writeLine(String line) throws IOException {
       write(line + Constants.WRITE_NEW_LINE);
    }

    public void writeTag(String tag) throws IOException {
        writeLine(Constants.COMMENT_PREFIX + tag);
    }
    
    public void writeTag(String tag, String value) throws IOException {
        writeLine(Constants.COMMENT_PREFIX + tag + Constants.EXT_TAG_END + value);
    }

    public void flush() throws IOException {
        mWriter.flush();
    }
    
}
