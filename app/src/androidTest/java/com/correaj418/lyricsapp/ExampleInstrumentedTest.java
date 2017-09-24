package com.correaj418.lyricsapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.correaj418.lyricsapp.api.LyricsApi;
import com.correaj418.lyricsapp.api.constants.Constants.HTTP_STATUS;
import com.correaj418.lyricsapp.api.models.ApiCallback;
import com.correaj418.lyricsapp.api.models.Lyric;
import com.correaj418.lyricsapp.api.models.Song;
import com.correaj418.lyricsapp.api.models.Song.SongsListWrapper;

import org.junit.Assert;
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
    @Test
    public void useAppContext() throws Exception
    {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.correaj418.lyricsapp", appContext.getPackageName());
    }

    @Test
    public void testAppleApiSongSearch() throws InterruptedException
    {
        CountDownLatch loLock = new CountDownLatch(1);
        LyricsApi loLyricsApi = new LyricsApi();

        loLyricsApi.getSongsForSearchTerm("tom waits", new ApiCallback<SongsListWrapper>()
        {
            @Override
            public void onApiCallback(HTTP_STATUS arHttpStatus,
                                      SongsListWrapper arSongsListModel)
            {
                Assert.assertEquals(arHttpStatus, HTTP_STATUS.OK);

                Assert.assertNotEquals(arSongsListModel.getResultCount(), 0);
            }
        });

        loLock.await(1000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testWikiApiWithValidSong() throws InterruptedException
    {
        CountDownLatch loLock = new CountDownLatch(1);
        LyricsApi loLyricsApi = new LyricsApi();

        Song loSongWithLyrics = new Song(
                "Yesterday",
                "The Beatles",
                "http://is1.mzstatic.com/image/thumb/Music/v4/98/10/bd/9810bd86-9023-fb20-c6d8-d15e6a25222e/source/100x100bb.jpg",
                "Help!");

        loLyricsApi.getLyricsForSong(loSongWithLyrics, new ApiCallback<Lyric>()
        {
            @Override
            public void onApiCallback(HTTP_STATUS arHttpStatus,
                                      Lyric arSongsListModel)
            {
                Assert.assertEquals(arHttpStatus, HTTP_STATUS.OK);

                Assert.assertNotEquals(arSongsListModel.getCompleteLyricsAsHtml().toString().length(), 0);
            }
        });

        loLock.await(1000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testWikiApiWithInvalidSong()
    {
        // TODO
    }
}
