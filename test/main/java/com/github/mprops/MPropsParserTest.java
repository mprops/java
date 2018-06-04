package com.github.mprops;

import java.io.StringReader;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class MPropsParserTest {

    @Test
    public void testParse1() {
        String key = "key";
        String value = " Line 1 \n Line 2";
        String text = "~ " + key + " \n" + value;
        Map<String, String> props = new MPropsParser().parse(new StringReader(text));
        Assert.assertEquals(1, props.size());
        Assert.assertEquals(key, props.keySet().iterator().next());
        Assert.assertEquals(value, props.values().iterator().next());
    }

    @Test
    public void testParse2() {
        String key1 = "key1";
        String value1 = " Line 1-1 \n Line 1-2";
        String key2 = "key2";
        String value2 = " Line 1-2 \nLine 2-2";

        String text = "~ " + key1 + " \n" + value1 + "\r\n~" + key2 + "\r\n" + value2;
        Map<String, String> props = new MPropsParser().parse(new StringReader(text));
        Assert.assertEquals(2, props.size());
        Assert.assertEquals(value1, props.get(key1));
        Assert.assertEquals(value2, props.get(key2));
    }

    @Test
    public void testParseEmptyValue() {
        String key1 = "key1";
        String key2 = "key2";
        Map<String, String> result = new MPropsParser().parse(new StringReader("~" + key1 + "\n~" + key2 + "\n"));
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("", result.get(key1));
        Assert.assertEquals("", result.get(key2));
    }

    @Test
    public void testParseEmptyValueNoNewLine() {
        String key = "key";
        Map<String, String> result = new MPropsParser().parse(new StringReader("~" + key));
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("", result.get(key));
    }

    @Test
    public void testParseWithHeaderComment() {
        String key1 = "key1";
        String value1 = "Line 1-1 \n Line 1-2";

        String text = "here is a header comment\n with multiple lines and \n ~ inside\n\n.\n~ " + key1 + " \n" + value1;
        Map<String, String> props = new MPropsParser().parse(new StringReader(text));
        Assert.assertEquals(1, props.size());
        Assert.assertEquals(value1, props.get(key1));
    }


    @Test
    public void testParseKey1() {
        String keyLine = "~ good key ";
        String key = new MPropsParser().parseKey(keyLine, 1);
        Assert.assertEquals("good key", key);
    }

    @Test
    public void testParseKey2() {
        String keyLine = "~~ good key ";
        String key = new MPropsParser().parseKey(keyLine, 1);
        Assert.assertEquals("~ good key", key);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseKeyWrongContext1() {
        new MPropsParser().parseKey(" ~key", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseKeyWrongContext2() {
        new MPropsParser().parseKey("", 1);
    }

    @Test
    public void testParseKey3() {
        String keyLine = "~~ good key ~";
        String key = new MPropsParser().parseKey(keyLine, 1);
        Assert.assertEquals("~ good key ~", key);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseKeyBad1() {
        new MPropsParser().parseKey("bad key ", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseKeyBad2() {
        new MPropsParser().parseKey("~ ", 1);
    }

}