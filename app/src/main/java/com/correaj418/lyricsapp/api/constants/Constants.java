package com.correaj418.lyricsapp.api.constants;

import android.util.SparseArray;

public class Constants
{
    // https://itunes.apple.com/search?term=tom+waits
    public static final String APPLE_API_URL = "https://itunes.apple.com/search?term=%s";

    // http://lyrics.wikia.com/api.php?func=getSong&amp;artist=Tom+Waits&amp;song=new+coat+of+paint&amp;fmt=json
    public static final String LYRICS_API_URL = "http://lyrics.wikia.com/api.php?func=getSong&artist=%s&song=%s&fmt=json";

    public static final String LYRICS_NOT_FOUND_RESPONSE = "Not found";
    public static final String LYRICS_HTML_CLASS_NAME = "lyricbox";

    public enum HTTP_STATUS
    {
        UNKNOWN_ERROR(-2),
        NETWORK_ERROR(-1),
        OK(200),
        NOT_FOUND(404);

        private static final SparseArray<HTTP_STATUS> obLookup =
                new SparseArray<HTTP_STATUS>(HTTP_STATUS.values().length);

        static
        {
            for (HTTP_STATUS c
                    : HTTP_STATUS.values())
            {
                obLookup.append(c.getHttpStatus(), c);
            }
        }


        private final int obHttpStatus;

        HTTP_STATUS(int arHttpStatus)
        {
            obHttpStatus = arHttpStatus;
        }

        public int getHttpStatus()
        {
            return obHttpStatus;
        }

        public static HTTP_STATUS getHttpStatusForCode(int arErrorCode)
        {
            HTTP_STATUS rvHttpStatus = obLookup.get(arErrorCode);

            if (rvHttpStatus  == null)
            {
                rvHttpStatus = UNKNOWN_ERROR;
            }

            return rvHttpStatus;
        }
    }
}
