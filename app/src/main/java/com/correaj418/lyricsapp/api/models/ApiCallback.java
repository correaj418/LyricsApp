package com.correaj418.lyricsapp.api.models;

import com.correaj418.lyricsapp.api.constants.Constants.HTTP_STATUS;

public interface ApiCallback<T>
{
    void onApiCallback(HTTP_STATUS arHttpStatus,
                       T arResult);
}
