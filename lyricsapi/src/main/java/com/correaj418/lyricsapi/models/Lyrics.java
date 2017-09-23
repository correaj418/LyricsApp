package com.correaj418.lyricsapi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

@Parcel
public class Lyrics
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

    // 'lyrics':'We saw the western coast\nI saw the hospital\nNurse the
    // shoreline like a wound\nReports of lover\'s tryst\nWere neither
    // clear nor descript\nWe kept it safe and slow\nThe qui[...]'
    @Expose
    @SerializedName("lyrics")
    private final String obPartialLyrics;

    // 'url':'http://lyrics.wikia.com/Brand_New:The_Quiet_Things_That_No_One_Ever_Knows'
    @Expose
    @SerializedName("url")
    private final String obCompleteLyricsUrl;

    private String obCompleteLyrics;

    //endregion

    //region constructor

    public Lyrics(String arSongName,
                  String arArtistName,
                  String arPartialLyrics,
                  String arCompleteLyricsUrl)
    {
        this(arSongName, arArtistName, arPartialLyrics, arCompleteLyricsUrl, null);
    }

    @SuppressWarnings("WeakerAccess")
    @ParcelConstructor
    public Lyrics(String obSongName,
                  String obArtistName,
                  String obPartialLyrics,
                  String obCompleteLyricsUrl,
                  String obCompleteLyrics)
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

    public String getCompleteLyrics()
    {
        return obCompleteLyrics;
    }

    //region setters

    public void setCompleteLyrics(String arCompleteLyrics)
    {
        obCompleteLyrics = arCompleteLyrics;
    }

    //endregion

    //endregion

    //region json list wrapper

    public static class LyricsListWrapperModel
    {
        @Expose
        @SerializedName("resultCount")
        private int obResultCount;

        @Expose
        @SerializedName("results")
        private List<Song> obSongResultsList;

        public int getResultCount()
        {
            return obResultCount;
        }

        public List<Song> getSongResultsList()
        {
            return obSongResultsList;
        }
    }

    //endregion
}
