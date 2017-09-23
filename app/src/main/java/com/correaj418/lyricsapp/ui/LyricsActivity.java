package com.correaj418.lyricsapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.correaj418.lyricsapp.R;
import com.correaj418.lyricsapp.api.models.Lyric;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LyricsActivity extends AppCompatActivity
{
    private static final String TAG = LyricsActivity.class.getSimpleName();

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

        // TODO - const
        Lyric loLyricModel = Parcels.unwrap(getIntent().getExtras().getParcelable("lyrics"));

        setSupportActionBar(obToolbar);
        getSupportActionBar().setTitle("Lyrics for " + loLyricModel.getSongName());

        Picasso.with(this)
                .load(loLyricModel.getSongModel().getAlbumArtUrl())
                .into(obAlbumCoverImageView);

        obSongNameTextView.setText(loLyricModel.getSongName());
        obArtistNameTextView.setText(loLyricModel.getArtistName());
        obAlbumNameTextView.setText(loLyricModel.getSongModel().getAlbumName());

        obLyricsTextView.setText(loLyricModel.getCompleteLyricsAsHtml());
    }
}
