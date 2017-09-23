package com.correaj418.lyricsapi.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Lyrics implements Parcelable
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
        obSongName = arSongName;
        obArtistName = arArtistName;
        obPartialLyrics = arPartialLyrics;
        obCompleteLyricsUrl = arCompleteLyricsUrl;
    }

    protected Lyrics(Parcel arParcelIn)
    {
        obSongName = arParcelIn.readString();
        obArtistName = arParcelIn.readString();
        obPartialLyrics = arParcelIn.readString();
        obCompleteLyricsUrl = arParcelIn.readString();
        obCompleteLyrics = arParcelIn.readString();
    }

    //endregion

    //region getters

    public static final Creator<Lyrics> CREATOR = new Creator<Lyrics>()
    {
        @Override
        public Lyrics createFromParcel(Parcel in)
        {
            return new Lyrics(in);
        }

        @Override
        public Lyrics[] newArray(int size)
        {
            return new Lyrics[size];
        }
    };

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

    //region Parcelable

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(obSongName);
        dest.writeString(obArtistName);
        dest.writeString(obPartialLyrics);
        dest.writeString(obCompleteLyricsUrl);
        dest.writeString(obCompleteLyrics);
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
