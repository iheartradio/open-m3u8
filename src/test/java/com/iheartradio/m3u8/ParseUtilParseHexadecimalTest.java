/*
 * Copyright (c) 2017, Spiideo
 */

package com.iheartradio.m3u8;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * @author Raniz
 * @since 02/08/17.
 */
@RunWith(Parameterized.class)
public class ParseUtilParseHexadecimalTest {

    @Parameterized.Parameters(name = "{index}: {1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList((byte) 0), "0x00"},
                {Arrays.asList((byte) 1), "0x01"},
                {Arrays.asList((byte) -1), "0xff"},
                {Arrays.asList((byte) -16), "0xf0"},
                {Arrays.asList((byte) 0, (byte) 1), "0x0001"},
                {Arrays.asList((byte) 1, (byte) 1), "0x0101"},
                {Arrays.asList((byte) -1, (byte) -1), "0xffff"},
                {Arrays.asList((byte) -121, (byte) -6), "0x87fa"},
                {Arrays.asList((byte) 75, (byte) 118), "0x4b76"},
        });
    }

    private final List<Byte> expected;
    private final String input;

    public ParseUtilParseHexadecimalTest(final List<Byte> expected, final String input) {
        this.expected = expected;
        this.input = input;
    }

    @Test
    public void parseHexadecimal() throws Exception {
        Assert.assertEquals(expected, ParseUtil.parseHexadecimal(input, ""));
    }

}
