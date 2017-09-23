package com.correaj418.lyricsapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.correaj418.lyricsapi.SongModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<SongModel> obSongModels;

    private OnItemClickListener obItemClickListener;

    public RecyclerViewAdapter(ArrayList<SongModel> modelList)
    {
        this.obSongModels = modelList;
    }

    public void updateList(ArrayList<SongModel> arSongModels)
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
        //Here you can fill your row view
        if (arViewHolder instanceof ViewHolder)
        {
            final SongModel loSongModel = getItem(arPosition);
            ViewHolder loGenericViewHolder = (ViewHolder) arViewHolder;

            loGenericViewHolder.

//            loGenericViewHolder.itemTxtTitle.setText(loSongModel.getTitle());
//            loGenericViewHolder.itemTxtMessage.setText(loSongModel.getMessage());
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
        ImageView imgUser;
        @BindView(R.id.item_txt_title)
        TextView itemTxtTitle;
        @BindView(R.id.item_txt_message)
        TextView itemTxtMessage;

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

