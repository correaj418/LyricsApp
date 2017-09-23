package com.correaj418.lyricsapp.api.constants;

public class Constants
{
//    https://itunes.apple.com/search?term=tom+waits
    public static final String APPLE_API_URL = "https://itunes.apple.com/search?term=%s";
//    http://lyrics.wikia.com/api.php?func=getSong&amp;artist=Tom+Waits&amp;song=new+coat+of+paint&amp;fmt=json
    public static final String LYRICS_API_URL = "http://lyrics.wikia.com/api.php?func=getSong&artist=%s&song=%s&fmt=json";

    public enum HTTP_STATUS
    {
        UNKNOWN_ERROR(-1),
        OK(200),
        NOT_FOUND(401);

        private final int obHttpStatus;

        HTTP_STATUS(int arHttpStatus)
        {
            obHttpStatus = arHttpStatus;
        }

        public int getHttpStatus()
        {
            return obHttpStatus;
        }
    }
}
