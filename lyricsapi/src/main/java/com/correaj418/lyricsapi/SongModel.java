package com.correaj418.lyricsapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SongModel
{
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

    public SongModel(String arSongName,
                     String arArtistName,
                     String arAlbumArtUrl,
                     String arReleaseDate,
                     String arAlbumName)
    {
        obSongName = arSongName;
        obArtistName = arArtistName;
        obAlbumArtUrl = arAlbumArtUrl;
        obReleaseDate = arReleaseDate;
        obAlbumName = arAlbumName;
    }

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

    @Override
    public String toString()
    {
        return "\nSong Name: " + obSongName +
                "\nArtist Name: " + obArtistName +
                "\nAlbum Name: " + obAlbumArtUrl +
                "\nRelease Date: " + obReleaseDate +
                "\nAlbum Cover URL: " + obAlbumArtUrl;
    }
}