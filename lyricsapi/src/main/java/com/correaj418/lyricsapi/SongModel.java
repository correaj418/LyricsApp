package com.correaj418.lyricsapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SongModel
{
    // "artistName": "Tom Waits",
    @Expose
    @SerializedName("artistName")
    private final String obArtistName;

    // "trackName": "I Hope That I Don't Fall In Love With You",
    @Expose
    @SerializedName("trackName")
    private final String obSongName;

    // "artworkUrl100": "http://is5.mzstatic.com/image/thumb/Music/v4/f5/08/dd/f508ddf9-bd03-f1d5-6e57-41fc0680005a/source/100x100bb.jpg",
    @Expose
    @SerializedName("artworkUrl100")
    private final String obAlbumArtUrl;

    // "releaseDate": "1973-03-01T08:00:00Z",
    @Expose
    @SerializedName("releaseDate")
    private final String obReleaseDate;

    public SongModel(String arArtistName,
                     String arSongName,
                     String arAlbumArtUrl,
                     String arReleaseDate)
    {
        obArtistName = arArtistName;
        obSongName = arSongName;
        obAlbumArtUrl = arAlbumArtUrl;
        obReleaseDate = arReleaseDate;
    }

    public String getArtistName()
    {
        return obArtistName;
    }

    public String getSongName()
    {
        return obSongName;
    }

    public String getAlbumArtUrl()
    {
        return obAlbumArtUrl;
    }

    public String getReleaseDate()
    {
        return obReleaseDate;
    }
}
