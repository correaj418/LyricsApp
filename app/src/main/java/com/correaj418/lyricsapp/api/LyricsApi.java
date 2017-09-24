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
import static com.correaj418.lyricsapp.api.constants.Constants.LYRICS_NOT_FOUND_RESPONSE;
import static com.correaj418.lyricsapp.api.constants.Constants.REQUEST_TYPE.APPLE_API_REQUEST;
import static com.correaj418.lyricsapp.api.constants.Constants.REQUEST_TYPE.COMPLETE_LYRICS_REQUEST;
import static com.correaj418.lyricsapp.api.constants.Constants.REQUEST_TYPE.LYRICS_METADATA_REQUEST;

public class LyricsApi
{
    private static final String TAG = LyricsApi.class.getSimpleName();

    private final OkHttpClient obOkHttpClient;
    private final Gson obGson;

    public LyricsApi()
    {
        obOkHttpClient = new OkHttpClient();
        obGson = new Gson();
    }

    /**
     * Get a list of songs matching the search term
     *
     * <b>Uses Apple's iTunes API</b>
     *
     * @param arSearchTerm The term to query
     * @param arCallback Callback for when the requests completes (success or failure)
     *
     * @see {@link com.correaj418.lyricsapp.api.models.ApiCallback}
     */
    public void getSongsForSearchTerm(String arSearchTerm,
                                      final ApiCallback<SongsListWrapper> arCallback)
    {
        String loUrl = String.format(Constants.APPLE_API_URL, arSearchTerm);
        sendRequest(loUrl, APPLE_API_REQUEST, arCallback);
    }

    /**
     * Get the lyrics for a song
     *
     * <b>Uses Wikia Lyric's API</b>
     *
     * <p>This method is a two step process:</p>
     *
     * <p>
     * Step 1) Send a request to {@link Constants#LYRICS_API_URL}.
     * This requests returns a JSON payload that contains a URL to
     * the web page that contains the lyrics in the "url" field
     *
     * @see {@link Lyric#obCompleteLyricsUrl}
     * </p>
     *
     * <p>
     * Step 2) Use the url obtained from step 1 to download the HTML
     * that contains the lyrics. This method requires us to parse HTML
     * and extract the lyrics by assuming that theyre always going to
     * be in the {@link Constants#LYRICS_HTML_CLASS_NAME} class. This
     * probably won't work well in the long run so we may want to find some alternative
     * </p>
     *
     * @param arSongModel The song that we're trying to get the lyrics for
     * @param arCallback Callback for when the requests completes (success or failure)
     *
     * @see {@link com.correaj418.lyricsapp.api.models.ApiCallback}
     */
    public void getLyricsForSong(final Song arSongModel,
                                 final ApiCallback<Lyric> arCallback)
    {
        // step 1 - request the lyrics metadata
        sendRequest(arSongModel.toLyricsUrl(), LYRICS_METADATA_REQUEST, new ApiCallback<Lyric>()
        {
            @Override
            public void onApiCallback(HTTP_STATUS arHttpStatus,
                                      final Lyric arLyricsModel)
            {
                if (arHttpStatus != HTTP_STATUS.OK)
                {
                    // error in step 1
                    // requests to the lyrics metadata returned an error
                    arCallback.onApiCallback(arHttpStatus, null);
                    return;
                }

                if (arLyricsModel.getPartialLyrics().equals(LYRICS_NOT_FOUND_RESPONSE))
                {
                    // when lyrics for a song are not available the "lyrics"
                    // field returns the string "Not found". we check for this
                    // so that we can avoid sending the extra request if we don't need to
                    arCallback.onApiCallback(arHttpStatus, arLyricsModel);
                    return;
                }

                // step 2 - request the html that contains the lyrics
                sendRequest(arLyricsModel.getCompleteLyricsUrl(), COMPLETE_LYRICS_REQUEST, new ApiCallback<String>()
                {
                    @Override
                    public void onApiCallback(HTTP_STATUS arHttpStatus,
                                              String arLyricsHtml)
                    {
                        if (arHttpStatus != HTTP_STATUS.OK)
                        {
                            // error in step 2
                            // requests to the lyrics HTML returned an error
                            arCallback.onApiCallback(arHttpStatus, arLyricsModel);
                            return;
                        }

                        arLyricsModel.setCompleteLyrics(arLyricsHtml);

                        arCallback.onApiCallback(arHttpStatus, arLyricsModel);
                    }
                });
            }
        });
    }

    /**
     * Helper method for sending GET requests
     *
     * @param arUrl The url that we're trying to send the request to
     * @param arRequestType The type of request
     * @param arCallback Callback for the reques
     */
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
                arCallback.onApiCallback(HTTP_STATUS.NETWORK_ERROR, null);
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

                arCallback.onApiCallback(loStatusCode, loParsedResponse);
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
