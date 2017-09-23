package com.correaj418.lyricsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.correaj418.lyricsapi.Constants.HTTP_STATUS;
import com.correaj418.lyricsapi.LyricsApiService;
import com.correaj418.lyricsapi.LyricsApiService.SongSearchCallback;
import com.correaj418.lyricsapi.models.Lyric;
import com.correaj418.lyricsapi.models.Song;
import com.correaj418.lyricsapi.models.Song.SongsListWrapper;
import com.correaj418.lyricsapi.utilities.Log;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, RecyclerViewAdapter.OnItemClickListener
{
    private static final String TAG = SearchActivity.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView obRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar obToolbar;

    private RecyclerViewAdapter obRecyclerViewAdapter;

    private MaterialDialog obLoadingDialog;

    //region lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        initToolbar();

        setAdapter();
    }

    //endregion

    private void showLoadingDialog(String arTitle, String arContent)
    {
        obLoadingDialog = new MaterialDialog.Builder(this)
                .cancelable(false)
                .progress(true, 0)
                .title(arTitle)
                .content(arContent)
                .show();
    }

    private void dismissLoadingDialog()
    {
        if (obLoadingDialog == null)
        {
            return;
        }

        obLoadingDialog.dismiss();
    }

    public void initToolbar()
    {
        setSupportActionBar(obToolbar);
        getSupportActionBar().setTitle("Lyrics");
    }

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
        searchEdit.setHint("Search");

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
        dismissKeyboard();

        LyricsApiService.instance().searchForTracks(arQuery, new SongSearchCallback<SongsListWrapper>()
        {
            @Override
            public void onSongSearchCallback(HTTP_STATUS arHttpStatus,
                                             SongsListWrapper arSongsListModel)
            {
                dismissLoadingDialog();

                Log.d(TAG, "onSongSearchCallback returned status code " + arHttpStatus);

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

        showLoadingDialog("Searching For Songs", "Please wait...");

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
    }

    private void handleSongSearchFailure(HTTP_STATUS arHttpStatus)
    {
        Log.e(TAG, "handleSongSearchFailure() called with status " + arHttpStatus);

        // TODO
    }

    //endregion

    //region RecyclerViewAdapter.OnItemClickListener

    @Override
    public void onItemClick(View arView, int arPosition, Song arModel)
    {
        //handle item click events here
        // TODO - handle click
        Log.v(TAG, "onItemClick for song " + arModel.toString());

        LyricsApiService.instance().searchForLyrics(arModel, new SongSearchCallback<Lyric>()
        {
            @Override
            public void onSongSearchCallback(HTTP_STATUS arHttpStatus,
                                             Lyric arLyricModel)
            {
                dismissLoadingDialog();

                Intent loLyricsActivityIntent = new Intent(SearchActivity.this, LyricsActivity.class)
                        .putExtra("lyrics", Parcels.wrap(arLyricModel));

                startActivity(loLyricsActivityIntent);
            }
        });

        showLoadingDialog("Downloading lyrics", "Please wait...");
    }

    //endregion

    private void dismissKeyboard()
    {
        obRecyclerView.requestFocus();

        InputMethodManager loInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        loInputMethodManager.hideSoftInputFromWindow(obRecyclerView.getWindowToken(), 0);
    }
}
