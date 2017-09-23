package com.correaj418.lyricsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.correaj418.lyricsapi.models.Lyrics;
import com.correaj418.lyricsapi.utilities.Log;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LyricsActivity extends AppCompatActivity
{
    private static String TAG = LyricsActivity.class.getSimpleName();

    private Lyrics obLyricsModel;

    @BindView(R.id.lyrics_text_view)
    TextView obLyricsTextView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);

        ButterKnife.bind(this);

        initToolbar("Lyrics");

        obLyricsModel = Parcels.unwrap(getIntent().getExtras().getParcelable("lyrics"));

        obLyricsTextView.setText(obLyricsModel.getCompleteLyrics());
    }

    public void initToolbar(String title)
    {
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
    }
}
