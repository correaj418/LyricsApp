package com.correaj418.lyricsapi;

import android.os.Handler;
import android.os.Looper;

import com.correaj418.lyricsapi.Constants.HTTP_STATUS;
import com.correaj418.lyricsapi.utilities.Log;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.correaj418.lyricsapi.Constants.APPLE_API_URL;
import static com.correaj418.lyricsapi.Constants.HTTP_STATUS.UNKNOWN_ERROR;

public class LyricsApiService
{
    private static final String TAG = LyricsApiService.class.getSimpleName();

    private static LyricsApiService sInstance;

    private final OkHttpClient obOkHttpClient;
    private final Gson obGson;

    private final Handler obMainThreadHandler;

    private LyricsApiService()
    {
        obOkHttpClient = new OkHttpClient();
        obGson = new Gson();

        obMainThreadHandler = new Handler(Looper.getMainLooper());
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
                Log.e(TAG, arException.getMessage());

                obMainThreadHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        // TODO - status codes
                        // TODO - error handling
                        arCallback.onSongSearchCallback(UNKNOWN_ERROR, null);
                    }
                });
            }

            @Override
            public void onResponse(Call arCall,
                                   Response arResponse) throws IOException
            {
                final String loResponseJson = arResponse.body().string();

                final SongsListModel loSongsModel = obGson.fromJson(loResponseJson, SongsListModel.class);

                obMainThreadHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // TODO - status codes
                        arCallback.onSongSearchCallback(HTTP_STATUS.OK, loSongsModel);
                    }
                });
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
        void onSongSearchCallback(HTTP_STATUS arHttpStatus,
                                  SongsListModel arSongsListModel);
    }
}
