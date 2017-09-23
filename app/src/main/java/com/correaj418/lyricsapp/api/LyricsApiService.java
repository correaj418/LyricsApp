package com.correaj418.lyricsapp.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.correaj418.lyricsapp.api.constants.Constants;
import com.correaj418.lyricsapp.api.constants.Constants.HTTP_STATUS;
import com.correaj418.lyricsapp.api.constants.Constants.REQUEST_TYPE;
import com.correaj418.lyricsapp.api.models.ApiCallback;
import com.correaj418.lyricsapp.api.models.Lyric;
import com.correaj418.lyricsapp.api.models.Song;
import com.correaj418.lyricsapp.api.models.Song.SongsListWrapper;
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

import static com.correaj418.lyricsapp.api.constants.Constants.LYRICS_HTML_CLASS_NAME;
import static com.correaj418.lyricsapp.api.constants.Constants.REQUEST_TYPE.APPLE_API_REQUEST;
import static com.correaj418.lyricsapp.api.constants.Constants.REQUEST_TYPE.COMPLETE_LYRICS_REQUEST;
import static com.correaj418.lyricsapp.api.constants.Constants.REQUEST_TYPE.LYRICS_METADATA_REQUEST;

public class LyricsApiService
{
    private static final String TAG = LyricsApiService.class.getSimpleName();

    private static LyricsApiService sInstance;

    private final OkHttpClient obOkHttpClient;
    private final Gson obGson;

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

    public void getSongsForSearchTerm(String arSearchTerm,
                                      final ApiCallback<SongsListWrapper> arCallback)
    {
        String loUrl = String.format(Constants.APPLE_API_URL, arSearchTerm);
        sendRequest(loUrl, APPLE_API_REQUEST, arCallback);
    }

    public void getLyricsForSong(final Song arSongModel,
                                 final ApiCallback<Lyric> arCallback)
    {
        sendRequest(arSongModel.toLyricsUrl(), LYRICS_METADATA_REQUEST, new ApiCallback<Lyric>()
        {
            @Override
            public void onSongSearchCallback(HTTP_STATUS arHttpStatus,
                                             final Lyric arSongsListModel)
            {
                if (arHttpStatus != HTTP_STATUS.OK)
                {
                    arCallback.onSongSearchCallback(arHttpStatus, null);
                    return;
                }

                sendRequest(arSongsListModel.getCompleteLyricsUrl(), COMPLETE_LYRICS_REQUEST, new ApiCallback<String>()
                {
                    @Override
                    public void onSongSearchCallback(HTTP_STATUS arHttpStatus,
                                                     String arLyricsHtml)
                    {
                        if (arHttpStatus != HTTP_STATUS.OK)
                        {
                            arCallback.onSongSearchCallback(arHttpStatus, arSongsListModel);
                            return;
                        }

                        arSongsListModel.setCompleteLyrics(arLyricsHtml);

                        arCallback.onSongSearchCallback(arHttpStatus, arSongsListModel);
                    }
                });
            }
        });
    }

    private void sendRequest(@NonNull String arUrl,
                             @NonNull final REQUEST_TYPE arRequestType,
                             @NonNull final ApiCallback arCallback)
    {
        Log.v(TAG, "sendRequest() called with url " + arUrl);

        Request loRequest = new Request.Builder()
                .url(arUrl)
                .tag(arRequestType)
                .build();

        obOkHttpClient.newCall(loRequest).enqueue(new Callback()
        {
            @Override
            public void onFailure(@NonNull Call arCall,
                                  @NonNull IOException arException)
            {
                // handle connectivity errors generally by
                // handing it directly back to the ui layer
                arCallback.onSongSearchCallback(HTTP_STATUS.NETWORK_ERROR, null);
            }

            @Override
            public void onResponse(@NonNull Call arCall,
                                   @NonNull Response arResponse) throws IOException
            {
                Object loParsedResponse = null;
                HTTP_STATUS loStatusCode = HTTP_STATUS.getHttpStatusForCode(arResponse.code());

                if (loStatusCode == HTTP_STATUS.OK)
                {
                    // only parse the response if the http code 200 (OK)
                    String loResponseBody = arResponse.body().string();

                    switch (arRequestType)
                    {
                        case APPLE_API_REQUEST:
                            loParsedResponse = parseAppleApiResponse(loResponseBody);
                            break;
                        case LYRICS_METADATA_REQUEST:
                            loParsedResponse = parseLyricMetadataResponse(loResponseBody);
                            break;
                        case COMPLETE_LYRICS_REQUEST:
                            loParsedResponse = parseLyricResponse(loResponseBody);
                            break;
                    }
                }

                arCallback.onSongSearchCallback(loStatusCode, loParsedResponse);
            }
        });
    }

    private Lyric parseLyricMetadataResponse(String arResponseBody)
    {
        // for whatever reason the json response contains
        // "song = " in front of it, so we need to remove it
        arResponseBody = arResponseBody.replace("song = ", "");

        final Lyric rvLyricModel = obGson.fromJson(arResponseBody, Lyric.class);

        return rvLyricModel;
    }

    private String parseLyricResponse(String arResponseBody)
    {
        Document doc = Jsoup.parse(arResponseBody);
        Elements loElements = doc.getElementsByClass(LYRICS_HTML_CLASS_NAME);

        String rvLyrics = "";

        if (loElements.size() != 1)
        {
            // zero or more than one
            Log.e(TAG, "Couldn't find lyrics");
        }
        else
        {
            rvLyrics = loElements.get(0).getAllElements().html();
        }

        return rvLyrics;
    }

    private SongsListWrapper parseAppleApiResponse(String arResponseBody)
    {
        SongsListWrapper rvSongsListWrapper = obGson.fromJson(arResponseBody, SongsListWrapper.class);

        return rvSongsListWrapper;
    }
}
