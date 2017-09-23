package com.correaj418.lyricsapp.api.models;

import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class Lyric
{
    //region properties

    // 'song':'The Quiet Things That No One Ever Knows',
    @Expose
    @SerializedName("song")
    private final String obSongName;

    // 'artist':'Brand New',
    @Expose
    @SerializedName("artist")
    private final String obArtistName;

    // 'lyrics':'[...]'
    @Expose
    @SerializedName("lyrics")
    private final String obPartialLyrics;

    // 'url':'http://lyrics.wikia.com/Brand_New:The_Quiet_Things_That_No_One_Ever_Knows'
    @Expose
    @SerializedName("url")
    private final String obCompleteLyricsUrl;

    private String obCompleteLyrics;

    //endregion

    private Song obSongModel;

    //region constructor

    public Lyric(String arSongName,
                 String arArtistName,
                 String arPartialLyrics,
                 String arCompleteLyricsUrl)
    {
        this(arSongName, arArtistName, arPartialLyrics, arCompleteLyricsUrl, null);
    }

    @SuppressWarnings("WeakerAccess")
    @ParcelConstructor
    public Lyric(String obSongName,
                 String obArtistName,
                 String obPartialLyrics,
                 String obCompleteLyricsUrl,
                 @SuppressWarnings("SameParameterValue") String obCompleteLyrics)
    {
        this.obSongName = obSongName;
        this.obArtistName = obArtistName;
        this.obPartialLyrics = obPartialLyrics;
        this.obCompleteLyricsUrl = obCompleteLyricsUrl;
        this.obCompleteLyrics = obCompleteLyrics;
    }

    //endregion

    //region getters

    public String getSongName()
    {
        return obSongName;
    }

    public String getArtistName()
    {
        return obArtistName;
    }

    public String getPartialLyrics()
    {
        return obPartialLyrics;
    }

    public String getCompleteLyricsUrl()
    {
        return obCompleteLyricsUrl;
    }

    public Spanned getCompleteLyricsAsHtml()
    {
        return Html.fromHtml(obCompleteLyrics);
    }

    public Song getSongModel()
    {
        return obSongModel;
    }

    //region setters

    public void setCompleteLyrics(String arCompleteLyrics)
    {
        obCompleteLyrics = arCompleteLyrics;
    }

    public void setSongModel(Song arSongModel)
    {
        obSongModel = arSongModel;
    }

    //endregion
}
