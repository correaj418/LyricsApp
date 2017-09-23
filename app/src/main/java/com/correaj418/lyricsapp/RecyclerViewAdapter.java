package com.correaj418.lyricsapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.correaj418.lyricsapi.SongModel;
import com.correaj418.lyricsapi.utilities.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    private List<SongModel> obSongModels;

    private OnItemClickListener obItemClickListener;

    public RecyclerViewAdapter(List<SongModel> modelList)
    {
        this.obSongModels = modelList;
    }

    public void updateList(List<SongModel> arSongModels)
    {
        this.obSongModels = arSongModels;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup arViewGroup,
                                         int arViewType)
    {
        View rvView = LayoutInflater.from(arViewGroup.getContext()).inflate(R.layout.item_recycler_list, arViewGroup, false);

        return new ViewHolder(rvView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder arViewHolder,
                                 final int arPosition)
    {
        if (arViewHolder instanceof ViewHolder)
        {
            final SongModel loSongModel = getItem(arPosition);

            Log.v(TAG, "Binding view for song " + loSongModel.toString());

            ViewHolder loGenericViewHolder = (ViewHolder) arViewHolder;

//            loGenericViewHolder.obAlbumCoverImageView = TODO
            loGenericViewHolder.obSongNameTextView.setText(loSongModel.getSongName());
            loGenericViewHolder.obArtistNameTextView.setText(loSongModel.getArtistName());
            loGenericViewHolder.obAlbumNameTextView.setText(loSongModel.getAlbumName());
        }
    }

    @Override
    public int getItemCount()
    {
        return obSongModels.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.obItemClickListener = mItemClickListener;
    }

    private SongModel getItem(int position)
    {
        return obSongModels.get(position);
    }

    public interface OnItemClickListener
    {
        void onItemClick(View arView,
                         int arPosition,
                         SongModel arModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.album_cover_image_view)
        ImageView obAlbumCoverImageView;
        @BindView(R.id.song_name_text_view)
        TextView obSongNameTextView;
        @BindView(R.id.artist_name_text_view)
        TextView obArtistNameTextView;
        @BindView(R.id.album_name_text_view)
        TextView obAlbumNameTextView;

        public ViewHolder(final View arItemView)
        {
            super(arItemView);

            ButterKnife.bind(this, arItemView);

            arItemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    obItemClickListener.onItemClick(
                            arItemView,
                            getAdapterPosition(),
                            obSongModels.get(getAdapterPosition()));
                }
            });
        }
    }
}

