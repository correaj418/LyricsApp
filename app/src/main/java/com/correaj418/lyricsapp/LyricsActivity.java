package com.correaj418.lyricsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.correaj418.lyricsapi.models.Lyrics;

public class LyricsActivity extends AppCompatActivity
{
    private Lyrics obLyricsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Lyrics loLyrics = (Lyrics) getIntent().getExtras().getBundle("lyrics").get("lyrics");
        obLyricsModel = loLyrics;

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
