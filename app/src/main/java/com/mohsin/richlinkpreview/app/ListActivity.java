package com.mohsin.richlinkpreview.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ListView;

import com.mohsin.richlinkpreview.app.R;
import com.mohsin.richlinkpreview.app.adapters.URLAdapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class ListActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.list);

        ArrayList<URL> urls = new ArrayList<>();

        try {
            urls.add(new URL("https://www.flipkart.com/"));
            urls.add(new URL("https://google.com"));
            urls.add(new URL("https://skype.com"));
            urls.add(new URL("https://twitter.com"));
            urls.add(new URL("https://www.flipkart.com/"));
            urls.add(new URL("https://google.com"));
            urls.add(new URL("https://skype.com"));
            urls.add(new URL("https://twitter.com"));
            urls.add(new URL("https://www.flipkart.com/"));
            urls.add(new URL("https://google.com"));
            urls.add(new URL("https://skype.com"));
            urls.add(new URL("https://twitter.com"));
            urls.add(new URL("https://www.flipkart.com/"));
            urls.add(new URL("https://google.com"));
            urls.add(new URL("https://skype.com"));
            urls.add(new URL("https://twitter.com"));

            URLAdapter urlAdapter = new URLAdapter(this, urls);

            listView.setAdapter(urlAdapter);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
