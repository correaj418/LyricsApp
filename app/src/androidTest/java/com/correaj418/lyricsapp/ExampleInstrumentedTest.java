package com.correaj418.lyricsapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.correaj418.lyricsapp.api.LyricsApiService;
import com.correaj418.lyricsapp.api.LyricsApiService.SongSearchCallback;
import com.correaj418.lyricsapp.api.constants.Constants.HTTP_STATUS;
import com.correaj418.lyricsapp.api.models.Song.SongsListWrapper;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest
{
    private CountDownLatch obLock = new CountDownLatch(1);

    @Test
    public void useAppContext() throws Exception
    {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.correaj418.lyricsapp", appContext.getPackageName());
    }

    public void request()
    {
        LyricsApiService.instance()
                .searchForTracks("tom waits", new SongSearchCallback<SongsListWrapper>()
                {
                    @Override
                    public void onSongSearchCallback(HTTP_STATUS arHttpStatus, SongsListWrapper arSongsListModel)
                    {
                        // TODO
                    }
                });

        try
        {
            // TODO
            obLock.await(30000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException arE)
        {
            arE.printStackTrace();
        }
    }
}
