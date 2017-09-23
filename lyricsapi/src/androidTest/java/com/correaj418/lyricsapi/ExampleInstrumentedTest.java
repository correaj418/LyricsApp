package com.correaj418.lyricsapi;

import android.support.test.runner.AndroidJUnit4;

import com.correaj418.lyricsapi.LyricsApiService.SongSearchCallback;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest
{
    private CountDownLatch lock = new CountDownLatch(1);

    @Test
    public void useAppContext() throws Exception
    {
        LyricsApiService.instance()
                .searchForTracks("tom waits", new SongSearchCallback()
                {
                    @Override
                    public void onSongSearchCallback(int arStatusCode,
                                                     SongsListModel arSongsListModel)
                    {
                        // TODO - const
                        assertEquals(new Integer(arStatusCode), new Integer(0));
                    }
                });

        lock.await(30000, TimeUnit.MILLISECONDS);

//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//
//        assertEquals("com.correaj418.lyricsapi.test", appContext.getPackageName());
    }
}