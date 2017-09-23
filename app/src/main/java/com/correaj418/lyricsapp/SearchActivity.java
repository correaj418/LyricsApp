package com.correaj418.lyricsapp;

import android.app.SearchManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.correaj418.lyricsapi.Constants.HTTP_STATUS;
import com.correaj418.lyricsapi.LyricsApiService;
import com.correaj418.lyricsapi.LyricsApiService.SongSearchCallback;
import com.correaj418.lyricsapi.SongModel;
import com.correaj418.lyricsapi.SongsListModel;
import com.correaj418.lyricsapi.utilities.Log;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener
{
    private static final String TAG = SearchActivity.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private RecyclerViewAdapter mAdapter;

    private ArrayList<SongModel> modelList = new ArrayList<>();

    //region lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);
        initToolbar("Lyrics");
        setAdapter();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

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

        SearchManager searchManager = (SearchManager) this.getSystemService(this.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

        //changing edittext color
        EditText searchEdit = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        searchEdit.setTextColor(Color.WHITE);
        searchEdit.setHintTextColor(Color.WHITE);
        searchEdit.setBackgroundColor(Color.TRANSPARENT);
        searchEdit.setHint("Search");

        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(40);
        fArray[1] = new InputFilter()
        {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                for (int i = start; i < end; i++)
                {
                    if (!Character.isLetterOrDigit(source.charAt(i)))
                    {
                        return "";
                    }
                }

                return null;
            }
        };

        searchEdit.setFilters(fArray);
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);

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

        mAdapter.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position, SongModel model)
            {
                //handle item click events here
                // TODO - handle click
//                Toast.makeText(SearchActivity.this, "Hey " + model.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //region SearchView.OnQueryTextListener

    @Override
    public boolean onQueryTextSubmit(String arQuery)
    {
        LyricsApiService.instance().searchForTracks(arQuery, new SongSearchCallback()
        {
            @Override
            public void onSongSearchCallback(HTTP_STATUS arHttpStatus,
                                             SongsListModel arSongsListModel)
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

    private void handleSongSearchResult(SongsListModel arSongsListModel)
    {
        Log.v(TAG, "handleSongSearchResult() called with " + arSongsListModel.getResultCount() + " results");

        mAdapter.updateList(arSongsListModel.getSongResultsList());
    }

    private void handleSongSearchFailure(HTTP_STATUS arHttpStatus)
    {
        Log.e(TAG, "handleSongSearchFailure() called with status " + arHttpStatus);

        // TODO
    }

    //endregion
}
