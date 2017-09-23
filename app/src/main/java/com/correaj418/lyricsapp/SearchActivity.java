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

import com.correaj418.lyricsapi.Constants.HTTP_STATUS;
import com.correaj418.lyricsapi.LyricsApiService;
import com.correaj418.lyricsapi.LyricsApiService.SongSearchCallback;
import com.correaj418.lyricsapi.models.Lyrics;
import com.correaj418.lyricsapi.models.Song;
import com.correaj418.lyricsapi.models.Song.SongsListWrapper;
import com.correaj418.lyricsapi.utilities.Log;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, RecyclerViewAdapter.OnItemClickListener
{
    private static final String TAG = SearchActivity.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private RecyclerViewAdapter mAdapter;

    private ArrayList<Song> modelList = new ArrayList<>();

    //region lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        initToolbar("Lyrics");
        setAdapter();

        // TODO
        onQueryTextSubmit("brand new");
    }

    //endregion

    public void initToolbar(String title)
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));

        //changing edittext color
        EditText searchEdit = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        searchEdit.setTextColor(Color.WHITE);
        searchEdit.setHintTextColor(Color.WHITE);
        searchEdit.setBackgroundColor(Color.TRANSPARENT);
        searchEdit.setHint("Search");

        searchView.setOnQueryTextListener(this);

        return true;
    }

    private void setAdapter()
    {
        mAdapter = new RecyclerViewAdapter(modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(this);
    }

    //region SearchView.OnQueryTextListener

    @Override
    public boolean onQueryTextSubmit(String arQuery)
    {
        LyricsApiService.instance().searchForTracks(arQuery, new SongSearchCallback<SongsListWrapper>()
        {
            @Override
            public void onSongSearchCallback(HTTP_STATUS arHttpStatus,
                                             SongsListWrapper arSongsListModel)
            {
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

        return false;
    }

    @Override
    public boolean onQueryTextChange(String arNewText) { return false; }

    //endregion

    //region request handling

    private void handleSongSearchResult(SongsListWrapper arSongsListModel)
    {
        Log.v(TAG, "handleSongSearchResult() called with " + arSongsListModel.getResultCount() + " results");

        mAdapter.updateList(arSongsListModel.getSongResultsList());

        dismissKeyboard();
//        onItemClick(null, 0, arSongsListModel.getSongResultsList().get(0));
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
//                Toast.makeText(SearchActivity.this, "Hey " + model.getTitle(), Toast.LENGTH_SHORT).show();

        LyricsApiService.instance().searchForLyrics(arModel, new SongSearchCallback<Lyrics>()
        {
            @Override
            public void onSongSearchCallback(HTTP_STATUS arHttpStatus,
                                             Lyrics arLyricsModel)
            {
                Intent loLyricsActivityIntent = new Intent(SearchActivity.this, LyricsActivity.class)
                        .putExtra("lyrics", Parcels.wrap(arLyricsModel));

                startActivity(loLyricsActivityIntent);
            }
        });
    }

    //endregion

    private void dismissKeyboard()
    {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        getWindow().getCurrentFocus().clearFocus();

        inputManager.hideSoftInputFromWindow(
                getWindow().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }
}
