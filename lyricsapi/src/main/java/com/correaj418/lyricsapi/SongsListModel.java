package com.correaj418.lyricsapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SongsListModel
{
    @Expose
    @SerializedName("resultCount")
    private int obResultCount;

    @Expose
    @SerializedName("results")
    private List<SongModel> obSongResultsList;
}
