package com.iheartradio.m3u8;

interface ExtTagHandler extends LineHandler {
    String getTag();

    public static final ExtTagHandler EXTM3U_HANDLER = new ExtTagHandler() {
        @Override
        public String getTag() {
            return "EXTM3U";
        }

        @Override
        public void handle(String line, ParseState state) throws ParseException {
            state.setExtended();
        }
    };
}
