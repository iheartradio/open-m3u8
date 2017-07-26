package com.iheartradio.m3u8;

class Attribute {
    public final String name;
    public final String value;

    Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("(Attribute")
                .append(" name=").append(name)
                .append(" value=").append(value)
                .append(")").toString();
    }
}
