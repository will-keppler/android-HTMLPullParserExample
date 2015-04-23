package com.androidprogramming.will.htmlppexample;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by will on 3/22/15.
 */
public class TestHTMLParser {

    private static final String ns = null;
    private static final String TAG = "TestHTMLParser(); ";
    List entries = new ArrayList();

    public List parse (InputStream in) throws XmlPullParserException, IOException {
        Log.i(TAG, "parse();");

        try{
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            Log.i(TAG, "parse(); parser.getName = " + parser.getName());
            return readFeed(parser);

        }finally {
            in.close();
        }
    }//end parse()

    private List readFeed (XmlPullParser parser) throws XmlPullParserException, IOException {

        Log.i(TAG, "readFeed();");
        parser.require(XmlPullParser.START_TAG, ns, "html");
        while (parser.next() != XmlPullParser.END_TAG) {
            Log.i(TAG, "readFeed(); in while; parser.getName() = " + parser.getName());
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("body")) {
                parser.nextTag();
                //Log.d(TAG, "readFeed(); if body; parser.getName() = " + parser.getName());

            }else if (name.equals("div")) {
                handle_div(parser);

            }else if (name.equals("p")) {
                entries.add(readText(parser));
            }else {
                Log.i(TAG, "Calling skip();");
                skip(parser);
            }
        }//end while
        return entries;
    }

    /*
    * This Definition class has to change....I can probably remove it
    * I was going to build one of these from the parsed html...but now i dont see the point...
    * the entries[] in MainActivity contains all of the text i wanted to parse out of the
    * test.html file.
    *
    public static class Definition {
        public final String h1_;
        public final String h2;
        public final String title;

        private Definition(String title, String h1_, String h2) {
            this.title = title;
            this.h1_ = h1_;
            this.h2 = h2;
        }
        public String toString() {
            String s = "Title: " + this.title + " | H1: " + this.h1_ + " | H2: " + this.h2;
            return s;
        }
    }//end Definition object class
*/
    private void handle_div(XmlPullParser parser) throws IOException, XmlPullParserException {
        String class_atr = parser.getAttributeValue(null, "class");
        String id_atr = parser.getAttributeValue(null, "id");
        if (class_atr != null) {
            Log.d(TAG, "handle_div(); class_atr != null; = " + class_atr);
            if (class_atr.equals("content-holder")) {
                //call nextTag() and it should be h1, call getText(parser)
                //Log.d(TAG, "name.equals(div); class_atr != null; class_atr == content-holder");
                parser.nextTag();
                entries.add(readText(parser));
                //Log.d(TAG, "after call to readText; entries = " + entries);
            }
        }else if(id_atr != null) {
            Log.d(TAG, "handle_div(); id_atr != null; = " + id_atr);
            if(id_atr.equals("Definition")) {
                parser.nextTag();
                entries.add(readText(parser));
            }//end if
        }
    }

    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.d(TAG, "readText()");
        String tag_text = "";
        if (parser.next() == XmlPullParser.TEXT) {
            tag_text = parser.getText();
            parser.nextTag();
        }
        return tag_text;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        Log.i(TAG, "skip()");

        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    //Log.i("StackOverflowXMLP(); ", "skip(); xmlpp.end tag depth--");
                    //Log.i("StackOverflowXMLP(); ", "skip(); parser = " + parser);
                    //Log.i(TAG, "skip(); parser.getName = " + parser.getName());
                    Log.d(TAG, "skip(); endTag; parser.getName() = " + parser.getName());
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    Log.d(TAG, "skip(); StartTag; parser.getName() = " + parser.getName());
                    depth++;
                    break;
            }
        }
    }//end skip()
}
