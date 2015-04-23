package com.androidprogramming.will.htmlppexample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    List entries = null;
    TestHTMLParser thp = new TestHTMLParser();
    InputStream in = null;
    BufferedReader buffReader = null;
    StringBuilder text = new StringBuilder();
    FileInputStream fInStr = null;
    private final String TAG = "onCreate(); ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            fInStr = openFileInput("test.html");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Creating Input Stream passed!!");

        Thread thread = new Thread(null, startParsing, "Background");
        thread.start();
    }

    private Runnable startParsing = new Runnable() {
        @Override
        public void run() {
            try {
                entries = thp.parse(fInStr);
                Log.v(TAG, "after the call to .parse(); entries = " + entries + entries.size());
            }catch (XmlPullParserException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }//Need both catch clauses because the THP class throws those two exceptions.
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
