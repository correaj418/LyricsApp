package com.correaj418.lyricsapp.api.models;

import com.correaj418.lyricsapp.api.constants.Constants.HTTP_STATUS;

public interface ApiCallback<T>
{
    /**
     * Asynchronous callback for calls made to {@link com.correaj418.lyricsapp.api.LyricsApi}
     *
     * <b>This method is called on a BACKGROUND thread</b>
     *
     * @param arHttpStatus The http status code returned by the web service
     * @param arResult null if there was an error
     */
    void onApiCallback(HTTP_STATUS arHttpStatus,
                       T arResult);
}
