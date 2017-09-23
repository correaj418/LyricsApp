package com.correaj418.lyricsapi;

import android.os.Handler;
import android.os.Looper;

import com.correaj418.lyricsapi.Constants.HTTP_STATUS;
import com.correaj418.lyricsapi.models.Lyrics;
import com.correaj418.lyricsapi.models.Song;
import com.correaj418.lyricsapi.models.Song.SongsListWrapper;
import com.correaj418.lyricsapi.utilities.Log;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.correaj418.lyricsapi.Constants.APPLE_API_URL;
import static com.correaj418.lyricsapi.Constants.HTTP_STATUS.OK;
import static com.correaj418.lyricsapi.Constants.HTTP_STATUS.UNKNOWN_ERROR;
import static com.correaj418.lyricsapi.LyricsApiService.REQUEST_TYPE.APPLE_API_REQUEST;
import static com.correaj418.lyricsapi.LyricsApiService.REQUEST_TYPE.FULL_LYRICS_REQUEST;
import static com.correaj418.lyricsapi.LyricsApiService.REQUEST_TYPE.LYRICS_REQUEST;

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
                                final SongSearchCallback<SongsListWrapper> arCallback)
    {
        String loUrl = String.format(APPLE_API_URL, arSearchTerm);
        sendRequest(loUrl, APPLE_API_REQUEST, new Callback()
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

                final SongsListWrapper loSongsModel = obGson.fromJson(loResponseJson, SongsListWrapper.class);

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

    public void searchForLyrics(Song arSongModel,
                                final SongSearchCallback<Lyrics> arCallback)
    {
        sendRequest(arSongModel.toLyricsUrl(), LYRICS_REQUEST, new Callback()
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
                REQUEST_TYPE loRequestType = (REQUEST_TYPE) arResponse.request().tag();

                switch (loRequestType)
                {
                    case LYRICS_REQUEST:
                        handleLyricsResponse(arResponse, arCallback);
                        break;
                    case FULL_LYRICS_REQUEST:
                        break;
                    default:
                        // TODO
                        Log.e(TAG, "Unknown request type with url " + arResponse.request().url());
                        break;
                }
            }
        });
    }

    private void handleLyricsResponse(Response arResponse,
                                      final SongSearchCallback<Lyrics> arCallback) throws IOException
    {
        // TODO - explain
        String loResponseJson = arResponse.body().string().replace("song = ", "");

        final Lyrics loLyricsModel = obGson.fromJson(loResponseJson, Lyrics.class);

        sendRequest(loLyricsModel.getCompleteLyricsUrl(), FULL_LYRICS_REQUEST, new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                // TODO
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                Log.i(TAG, "");

                if (loLyricsModel.getPartialLyrics().equals("Not found"))
                {
                    // TODO -
                    Log.e(TAG, "No lyrics available");
                }

                handleFullLyricsResponse(response, loLyricsModel, arCallback);
            }
        });
    }

    private void handleFullLyricsResponse(Response arResponse,
                                          final Lyrics loLyricsModel,
                                          final SongSearchCallback arCallback) throws IOException
    {
        final String loResponseHtml = arResponse.body().string();

        Document doc = Jsoup.parse(loResponseHtml);
        Elements loElements = doc.getElementsByClass("lyricbox");

        String loLyricsHtmlContent = "";

        if (loElements.isEmpty())
        {
            // todo
            Log.e(TAG, "Couldn't find lyrics");
        }
        else if (loElements.size() > 1)
        {
            // todo
            Log.e(TAG, "html changed todo");
        }
        else
        {
            loLyricsHtmlContent = loElements.get(0).getAllElements().html();
        }

        loLyricsModel.setCompleteLyrics(loLyricsHtmlContent);

        obMainThreadHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                // TODO - status codes

                arCallback.onSongSearchCallback(OK, loLyricsModel);
            }
        });
    }

    private void downloadCompleteLyrics(Lyrics loLyricsModel,
                                        final Callback arCallback)
    {
        // todo - check that lyrics are available

        sendRequest(loLyricsModel.getCompleteLyricsUrl(), FULL_LYRICS_REQUEST, new Callback()
        {
            @Override
            public void onFailure(Call arCall, IOException arException)
            {
                // TODO
                arCallback.onFailure(arCall, arException);
            }

            @Override
            public void onResponse(Call arCall,
                                   Response arResponse) throws IOException
            {
                arCallback.onResponse(arCall, arResponse);
            }
        });
    }

    private void sendRequest(String arUrl,
                             REQUEST_TYPE arRequestType,
                             Callback arCallback)
    {
        Log.v(TAG, "sendRequest() called with url " + arUrl);

        Request loRequest = new Request.Builder()
                .url(arUrl)
                .tag(arRequestType)
                .build();

        obOkHttpClient.newCall(loRequest).enqueue(arCallback);
    }

    public interface SongSearchCallback<T>
    {
//        void onSongSearchCallback(HTTP_STATUS arHttpStatus,
//                                  SongsListWrapper arSongsListModel);
        void onSongSearchCallback(HTTP_STATUS arHttpStatus,
                                  T arSongsListModel);
    }

    public enum REQUEST_TYPE
    {
        APPLE_API_REQUEST,
        LYRICS_REQUEST,
        FULL_LYRICS_REQUEST; // TODO - rename
    }
}
