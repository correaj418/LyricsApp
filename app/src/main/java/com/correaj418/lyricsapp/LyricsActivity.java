package com.correaj418.lyricsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.correaj418.lyricsapi.models.Lyric;
import com.correaj418.lyricsapi.utilities.Log;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LyricsActivity extends AppCompatActivity
{
    private static String TAG = LyricsActivity.class.getSimpleName();

    private Lyric obLyricModel;

    @BindView(R.id.lyrics_text_view)
    TextView obLyricsTextView;

    @BindView(R.id.toolbar)
    Toolbar obToolbar;

    @BindView(R.id.album_cover_image_view)
    ImageView obAlbumCoverImageView;
    @BindView(R.id.song_name_text_view)
    TextView obSongNameTextView;
    @BindView(R.id.artist_name_text_view)
    TextView obArtistNameTextView;
    @BindView(R.id.album_name_text_view)
    TextView obAlbumNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);

        ButterKnife.bind(this);

        obLyricModel = Parcels.unwrap(getIntent().getExtras().getParcelable("lyrics"));

        initToolbar("Lyrics for " + obLyricModel.getSongName());

        Picasso.with(this)
                .load(obLyricModel.getSongModel().getAlbumArtUrl())
                .into(obAlbumCoverImageView);

        obSongNameTextView.setText(obLyricModel.getSongName());
        obArtistNameTextView.setText(obLyricModel.getArtistName());
        obAlbumNameTextView.setText(obLyricModel.getSongModel().getAlbumName());

        obLyricsTextView.setText(obLyricModel.getCompleteLyricsAsHtml());
    }

    public void initToolbar(String title)
    {
        setSupportActionBar(obToolbar);
        getSupportActionBar().setTitle(title);
    }
}
