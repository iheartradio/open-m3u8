package com.iheartradio.m3u8;

interface IParseState<T> {
    T buildPlaylist() throws ParseException;
}
