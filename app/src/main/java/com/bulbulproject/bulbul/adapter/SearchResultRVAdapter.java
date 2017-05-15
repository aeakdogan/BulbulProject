package com.bulbulproject.bulbul.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.activity.AlbumActivity;
import com.bulbulproject.bulbul.activity.ArtistActivity;
import com.bulbulproject.bulbul.activity.StreamActivity;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.Song;
import com.bulbulproject.bulbul.service.Globals;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by aeakdogan on 05/05/2017.
 */

public class SearchResultRVAdapter extends RecyclerView.Adapter {
    private ArrayList<Song> mSongs;
    private ArrayList<Artist> mArtists;
    private ArrayList<Album> mAlbums;
    Context context;
    final int HEADER_VIEW_TYPE = 0;
    final int SONG_VIEW_TYPE = 1;
    final int ARTIST_VIEW_TYPE = 2;
    final int ALBUM_VIEW_TYPE = 3;

    String[] header_strings = {"Songs", "Artists", "Albums"};

    public SearchResultRVAdapter(Context context, ArrayList<Song> mSongs, ArrayList<Artist> mArtists, ArrayList<Album> mAlbums) {
        this.context = context;
        this.mSongs = mSongs;
        this.mAlbums = mAlbums;
        this.mArtists = mArtists;
    }

    static class SongViewHolder extends RecyclerView.ViewHolder{
        CardView mCardView;
        TextView songTitle;
        TextView songArtists;
        ImageView songPhoto;

        SongViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cv);
            songTitle = (TextView) mCardView.findViewById(R.id.song_title);
            songArtists = (TextView) mCardView.findViewById(R.id.song_artists);
            songPhoto = (ImageView) mCardView.findViewById(R.id.song_photo);
        }
    }

    static class ArtistViewHolder extends RecyclerView.ViewHolder{
        CardView mCardView;
        TextView artistName;
        TextView artistMeta;
        ImageView artistPhoto;

        ArtistViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cv);
            artistName = (TextView) mCardView.findViewById(R.id.artist_name);
            artistMeta = (TextView) mCardView.findViewById(R.id.artist_meta);
            artistPhoto = (ImageView) mCardView.findViewById(R.id.artist_photo);
        }
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView albumName;
        TextView albumMeta;
        ImageView albumPhoto;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cv);
            albumName = (TextView) mCardView.findViewById(R.id.album_name);
            albumMeta = (TextView) mCardView.findViewById(R.id.album_meta);
            albumPhoto = (ImageView) mCardView.findViewById(R.id.album_photo);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder{
        CardView mCardView;
        TextView textViewHeader;

        HeaderViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cv);
            textViewHeader = (TextView) mCardView.findViewById(R.id.header_text);
        }
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if (0 < position && position <= mSongs.size())
            return SONG_VIEW_TYPE;
        if (mSongs.size() + 1 < position && position < mSongs.size() + mArtists.size() +2)
            return ARTIST_VIEW_TYPE;
        if (mSongs.size() + mArtists.size() +2 < position && position < mSongs.size() + mArtists.size() + mAlbums.size() +3)
            return ALBUM_VIEW_TYPE;
        return HEADER_VIEW_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == SONG_VIEW_TYPE)
            return new SongViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_song, parent, false));
        if(viewType == ARTIST_VIEW_TYPE)
            return new ArtistViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_artist, parent, false));
        if(viewType == ALBUM_VIEW_TYPE)
            return new AlbumViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_album, parent, false));
        return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_header, parent, false));
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case SONG_VIEW_TYPE:
                SongViewHolder songHolder = (SongViewHolder) holder;
                final Song tmpSong = mSongs.get(position -1);
                final ArrayList<String> songsList = new ArrayList<String>();
                final ArrayList<Integer> songIdsList = new ArrayList<>();
                for (Song song : mSongs) {
                    songsList.add(song.getSpotifyUrl());
                    songIdsList.add(song.getId());
                }

                songHolder.songTitle.setText(tmpSong.getName());
                songHolder.songArtists.setText(tmpSong.getFirstArtistName());
                Picasso.with(context)
                        .load(tmpSong.getImageUrl())
                        .placeholder(R.drawable.cover_picture)
                        .error(R.drawable.album)
                        .fit()
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(songHolder.songPhoto);
                songHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Globals.mSongs = mSongs;
                        Intent intent = new Intent(context.getApplicationContext(), StreamActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putStringArrayListExtra("songs", songsList);
                        intent.putIntegerArrayListExtra("songIds", songIdsList);
                        intent.putExtra("position", holder.getAdapterPosition() -1);
                        context.getApplicationContext().startActivity(intent);
                    }
                });
                break;
            case ARTIST_VIEW_TYPE:
                ArtistViewHolder artistHolder = (ArtistViewHolder) holder;
                Artist artist = mArtists.get(position - mSongs.size() -2);
                artistHolder.artistName.setText(artist.getName());
                artistHolder.artistMeta.setText("" + artist.getAlbumsCount() + " albums");
                if (artist.getImageUrl() != null && artist.getImageUrl().length() > 0) {
                    Picasso.with(context).load(artist.getImageUrl())
                            .placeholder(R.drawable.artist)
                            .error(R.drawable.artist)
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .fit()
                            .into(artistHolder.artistPhoto);
                }
                artistHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context.getApplicationContext(), ArtistActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("id", mArtists.get(holder.getAdapterPosition() - mSongs.size() -2).getId());
                        context.getApplicationContext().startActivity(intent);
                    }
                });
                break;
            case ALBUM_VIEW_TYPE:
                AlbumViewHolder albumHolder = (AlbumViewHolder) holder;

                final Album tmpAlbum = mAlbums.get(position - mSongs.size() - mArtists.size() - 3);
                albumHolder.albumName.setText(tmpAlbum.getName());
                String albumMeta = tmpAlbum.getArtistsString() + "\n" +
                        tmpAlbum.getSongsCount() + " songs" ;
                albumHolder.albumMeta.setText(albumMeta);
                Picasso.with(context).load(tmpAlbum.getImageUrl())
                        .placeholder(R.drawable.album)
                        .error(R.drawable.album)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .fit()
                        .into(albumHolder.albumPhoto);
                albumHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context.getApplicationContext(), AlbumActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("id",tmpAlbum.getId());
                        context.getApplicationContext().startActivity(intent);
                    }
                });
                break;
            case HEADER_VIEW_TYPE:
                HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
                if(position == 0)
                    headerHolder.textViewHeader.setText(header_strings[0]);
                if(position == mSongs.size() +1)
                    headerHolder.textViewHeader.setText(header_strings[1]);
                if(position == mSongs.size() + mArtists.size() +2)
                    headerHolder.textViewHeader.setText(header_strings[2]);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mSongs.size() + mArtists.size() + mAlbums.size() + header_strings.length;
    }
}
