package com.correaj418.lyricsapi;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.correaj418.lyricsapi.Constants.APPLE_API_URL;

public class LyricsApiService
{
    private static final String TAG = LyricsApiService.class.getSimpleName();

    private static LyricsApiService sInstance;

    private OkHttpClient obOkHttpClient;
    private Gson obGson;

    private LyricsApiService()
    {
        obOkHttpClient = new OkHttpClient();
        obGson = new Gson();
    }

    public static LyricsApiService instance()
    {
        if (sInstance == null)
        {
            sInstance = new LyricsApiService();
        }

        return sInstance;
    }

    public void searchForTracks(String arSearchTerm,
                                final SongSearchCallback arCallback)
    {
        String loUrl = String.format(APPLE_API_URL, arSearchTerm);
        sendRequest(loUrl, new Callback()
        {
            @Override
            public void onFailure(Call arCall,
                                  IOException arException)
            {
                Log.e("", arException.getMessage());

                // TODO - status codes
                // TODO - error handling
                arCallback.onSongSearchCallback(-1, null);
            }

            @Override
            public void onResponse(Call arCall,
                                   Response arResponse) throws IOException
            {
                String loResponseJson = arResponse.body().string();

                SongsListModel loSongsModel = obGson.fromJson(loResponseJson, SongsListModel.class);

                // TODO - status codes
                arCallback.onSongSearchCallback(0, loSongsModel);
            }
        });
    }

    private void sendRequest(String arUrl,
                             Callback arCallback)
    {
        Request loRequest = new Request.Builder()
                .url(arUrl)
                .build();

        obOkHttpClient.newCall(loRequest).enqueue(arCallback);
    }

    public interface SongSearchCallback
    {
        void onSongSearchCallback(int arStatusCode,
                                  SongsListModel arSongsListModel);
    }
}