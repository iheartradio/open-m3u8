package com.iheartradio.m3u8;

interface IExtTagParser extends LineParser {
    String getTag();
    boolean hasData();
}
