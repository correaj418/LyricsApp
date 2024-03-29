package com.correaj418.lyricsapp.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.correaj418.lyricsapp.R;
import com.correaj418.lyricsapp.api.LyricsApi;
import com.correaj418.lyricsapp.api.constants.Constants.HTTP_STATUS;
import com.correaj418.lyricsapp.api.models.ApiCallback;
import com.correaj418.lyricsapp.api.models.Lyric;
import com.correaj418.lyricsapp.api.models.Song;
import com.correaj418.lyricsapp.api.models.Song.SongsListWrapper;
import com.correaj418.lyricsapp.ui.adapters.RecyclerViewAdapter;
import com.correaj418.lyricsapp.ui.adapters.RecyclerViewAdapter.OnItemClickListener;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, OnItemClickListener
{
    private static final String TAG = SearchActivity.class.getSimpleName();

    public static final String LYRICS_PARCEL_KEY = "LYRICS_PARCEL_KEY";
    public static final String SONG_PARCEL_KEY = "SONG_PARCEL_KEY";

    @BindView(R.id.recycler_view)
    RecyclerView obRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar obToolbar;

    private RecyclerViewAdapter obRecyclerViewAdapter;

    private ProgressDialog obLoadingDialog;

    private LyricsApi obLyricsApi = new LyricsApi();

    //region lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        setSupportActionBar(obToolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        setAdapter();

//        onQueryTextSubmit("brand new");
    }

    //endregion

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));

        EditText searchEdit = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEdit.setTextColor(Color.WHITE);
        searchEdit.setHintTextColor(Color.WHITE);
        searchEdit.setBackgroundColor(Color.TRANSPARENT);
        searchEdit.setHint(R.string.search);

        searchView.setOnQueryTextListener(this);

        return true;
    }

    private void setAdapter()
    {
        obRecyclerViewAdapter = new RecyclerViewAdapter(SearchActivity.this);

        obRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        obRecyclerView.setLayoutManager(layoutManager);

        obRecyclerView.setAdapter(obRecyclerViewAdapter);

        obRecyclerViewAdapter.SetOnItemClickListener(this);
    }

    //region SearchView.OnQueryTextListener

    @Override
    public boolean onQueryTextSubmit(String arQuery)
    {
        // called after user hits search when the'yre done typing a query

        dismissKeyboard();

        obLyricsApi.getSongsForSearchTerm(arQuery, new ApiCallback<SongsListWrapper>()
        {
            @Override
            public void onApiCallback(final HTTP_STATUS arHttpStatus,
                                      final SongsListWrapper arSongsListModel)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        dismissLoadingDialog();

                        Log.d(TAG, "onApiCallback returned status code " + arHttpStatus);

                        if (arHttpStatus == HTTP_STATUS.OK)
                        {
                            handleSongSearchResult(arSongsListModel);
                        }
                        else
                        {
                            handleSongSearchFailure(arHttpStatus);
                        }
                    }
                });
            }
        });

        showLoadingDialog(getString(R.string.searching_for_songs), getString(R.string.please_wait));

        return false;
    }

    @Override
    public boolean onQueryTextChange(String arNewText) { return false; }

    //endregion

    //region request handling

    private void handleSongSearchResult(SongsListWrapper arSongsListModel)
    {
        Log.v(TAG, "handleSongSearchResult() called with " + arSongsListModel.getResultCount() + " results");

        obRecyclerView.scrollToPosition(0);

        obRecyclerViewAdapter.updateList(arSongsListModel.getSongResultsList());

        if (arSongsListModel.getResultCount() == 0)
        {
            // query didn't return any search results
            showMessageDialog(getString(R.string.no_results_found));
        }
    }

    private void handleSongSearchFailure(HTTP_STATUS arHttpStatus)
    {
        Log.e(TAG, "handleSongSearchFailure() called with status " + arHttpStatus);

        switch (arHttpStatus)
        {
            case NETWORK_ERROR:
                showMessageDialog(getString(R.string.check_internet_connection));
                break;
            case NOT_FOUND:
                showMessageDialog(getString(R.string.could_not_find_lyrics));
                break;
            default:
                showMessageDialog(getString(R.string.try_again_later));
                break;
        }
    }

    //endregion

    //region RecyclerViewAdapter.OnItemClickListener

    @Override
    public void onItemClick(View arView, int arPosition, final Song arSongModel)
    {
        // called when the user clicks on an individual song to view the lyrics

        Log.v(TAG, "onItemClick for song " + arSongModel.toString());

        obLyricsApi.getLyricsForSong(arSongModel, new ApiCallback<Lyric>()
        {
            @Override
            public void onApiCallback(final HTTP_STATUS arHttpStatus,
                                      final Lyric arLyricModel)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        dismissLoadingDialog();

                        if (arHttpStatus != HTTP_STATUS.OK)
                        {
                            handleSongSearchFailure(arHttpStatus);
                            return;
                        }

                        if (arLyricModel == null || // this shouldn't be null, but just in case
                                arLyricModel.getCompleteLyricsAsHtml().toString().isEmpty())
                        {
                            // if the lyrics html is empty it means that the wiki
                            // lyrics api didn't have lyrics for this specific song

                            showMessageDialog(getString(R.string.could_not_find_lyrics));

                            return;
                        }

                        Intent loLyricsActivityIntent = new Intent(SearchActivity.this, LyricsActivity.class)
                                .putExtra(LYRICS_PARCEL_KEY, Parcels.wrap(arLyricModel))
                                .putExtra(SONG_PARCEL_KEY, Parcels.wrap(arSongModel));

                        startActivity(loLyricsActivityIntent);
                    }
                });
            }
        });

        showLoadingDialog(getString(R.string.downloading_lyrics), getString(R.string.please_wait));
    }

    //endregion

    private void dismissKeyboard()
    {
        obRecyclerView.requestFocus();

        InputMethodManager loInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        loInputMethodManager.hideSoftInputFromWindow(obRecyclerView.getWindowToken(), 0);
    }

    //region dialogs

    private void showMessageDialog(String arMessage)
    {
        new Builder(this)
                .setCancelable(true)
                .setMessage(arMessage)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void showLoadingDialog(String arTitle, String arMessage)
    {
        obLoadingDialog = ProgressDialog
                .show(this, arTitle, arMessage, true);
    }

    private void dismissLoadingDialog()
    {
        if (obLoadingDialog == null)
        {
            return;
        }

        obLoadingDialog.dismiss();
    }

    //endregion
}
