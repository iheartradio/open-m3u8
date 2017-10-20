package com.iheartradio.m3u8;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class WriteUtilTest {

    @Test
    public void writeQuotedStringShouldIgnoreNullTagValueForOptionalFields() throws Exception {
            String outputString =WriteUtil.writeQuotedString(null,"some-key", true);

            assertThat(outputString,is("\"\""));
    }

    @Test(expected = NullPointerException.class)
    public void writeQuotedStringShouldNotIgnoreNullTagValue() throws Exception {
            WriteUtil.writeQuotedString(null,"some-key");
    }

    @Test
    public void writeQuotedStringShouldNotIgnoreSuppliedOptionalValue() throws Exception {
            assertThat(WriteUtil.writeQuotedString("blah","some-key"),is("\"blah\""));
    }

}