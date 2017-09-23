package com.correaj418.lyricsapp.api.models;

import com.correaj418.lyricsapp.api.constants.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

@Parcel
public class Song
{
    //region properties

    // "trackName": "I Hope That I Don't Fall In Love With You",
    @Expose
    @SerializedName("trackName")
    private final String obSongName;

    // "artistName": "Tom Waits",
    @Expose
    @SerializedName("artistName")
    private final String obArtistName;

    // "artworkUrl100": "http://is5.mzstatic.com/image/thumb/Music/v4/f5/08/dd/f508ddf9-bd03-f1d5-6e57-41fc0680005a/source/100x100bb.jpg",
    @Expose
    @SerializedName("artworkUrl100")
    private final String obAlbumArtUrl;

    // "collectionName": "Closing Time",
    @Expose
    @SerializedName("releaseDate")
    private final String obReleaseDate;

    // "releaseDate": "1973-03-01T08:00:00Z",
    @Expose
    // TODO - verify that this is mapped correctly
    @SerializedName("collectionName")
    private final String obAlbumName;

    //endregion

    //region constructor

    @ParcelConstructor
    public Song(String obSongName,
                String obArtistName,
                String obAlbumArtUrl,
                String obReleaseDate,
                String obAlbumName)
    {
        this.obSongName = obSongName;
        this.obArtistName = obArtistName;
        this.obAlbumArtUrl = obAlbumArtUrl;
        this.obReleaseDate = obReleaseDate;
        this.obAlbumName = obAlbumName;
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

    public String getAlbumArtUrl()
    {
        return obAlbumArtUrl;
    }

    public String getReleaseDate()
    {
        return obReleaseDate;
    }

    public String getAlbumName()
    {
        return obAlbumName;
    }

    //endregion

    //region helpers

    public String toLyricsUrl()
    {
        // TODO - sanitize url
        return String.format(Constants.LYRICS_API_URL, obArtistName, obSongName);
    }

    @Override
    public String toString()
    {
        return "\nSong Name: " + obSongName +
                "\nArtist Name: " + obArtistName +
                "\nAlbum Name: " + obAlbumArtUrl +
                "\nRelease Date: " + obReleaseDate +
                "\nAlbum Cover URL: " + obAlbumArtUrl;
    }

    //endregion

    //region json list wrapper

    public static class SongsListWrapper
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