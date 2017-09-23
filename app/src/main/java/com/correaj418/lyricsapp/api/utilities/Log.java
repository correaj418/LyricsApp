package com.correaj418.lyricsapp.api.utilities;

public class Log
{
    private static final boolean ENABLE_LOGGING = true;

    public static void v(String arTag, String arLogMessage)
    {
        if (ENABLE_LOGGING == false)
        {
            return;
        }

        android.util.Log.v(arTag, arLogMessage);
    }

    public static void v(String arTag, String arFormat, Object... arLogParams)
    {
        if (ENABLE_LOGGING == false)
        {
            return;
        }

        android.util.Log.v(arTag, String.format(arFormat, arLogParams));
    }

    public static void d(String arTag, String arLogMessage)
    {
        if (ENABLE_LOGGING == false)
        {
            return;
        }

        android.util.Log.d(arTag, arLogMessage);
    }

    public static void d(String arTag, String arFormat, Object... arLogParams)
    {
        if (ENABLE_LOGGING == false)
        {
            return;
        }

        android.util.Log.d(arTag, String.format(arFormat, arLogParams));
    }

    public static void i(String arTag, String arLogMessage)
    {
        if (ENABLE_LOGGING == false)
        {
            return;
        }

        android.util.Log.i(arTag, arLogMessage);
    }

    public static void i(String arTag, String arFormat, Object... arLogParams)
    {
        if (ENABLE_LOGGING == false)
        {
            return;
        }

        android.util.Log.i(arTag, String.format(arFormat, arLogParams));
    }

    public static void w(String arTag, String arLogMessage)
    {
        if (ENABLE_LOGGING == false)
        {
            return;
        }

        android.util.Log.w(arTag, arLogMessage);
    }

    public static void w(String arTag, String arFormat, Object... arLogParams)
    {
        if (ENABLE_LOGGING == false)
        {
            return;
        }

        android.util.Log.w(arTag, String.format(arFormat, arLogParams));
    }

    public static void e(String arTag, String arLogMessage)
    {
        if (ENABLE_LOGGING == false)
        {
            return;
        }

        android.util.Log.e(arTag, arLogMessage);
    }

    public static void e(String arTag, String arFormat, Object... arLogParams)
    {
        if (ENABLE_LOGGING == false)
        {
            return;
        }

        android.util.Log.e(arTag, String.format(arFormat, arLogParams));
    }
}
