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
import com.bulbulproject.bulbul.activity.MyAlbumArtistPlaylistActivity;
import com.bulbulproject.bulbul.activity.PlaylistActivity;
import com.bulbulproject.bulbul.activity.StreamActivity;
import com.bulbulproject.bulbul.model.Album;
import com.bulbulproject.bulbul.model.Artist;
import com.bulbulproject.bulbul.model.Playlist;
import com.bulbulproject.bulbul.model.Song;
import com.bulbulproject.bulbul.service.Globals;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by aeakdogan on 05/05/2017.
 */

public class UserSummaryRVAdapter extends RecyclerView.Adapter {
    private ArrayList<Song> mSongs;
    private ArrayList<Artist> mArtists;
    private ArrayList<Album> mAlbums;
    private ArrayList<Playlist> mPlaylists;
    Context context;
    final int HEADER_VIEW_TYPE = 0;
    final int SONG_VIEW_TYPE = 1;
    final int ARTIST_VIEW_TYPE = 2;
    final int ALBUM_VIEW_TYPE = 3;
    final int PLAYLIST_VIEW_TYPE = 4;

    String[] header_strings = {"Playlists", "Songs", "Artists", "Albums"};

    public UserSummaryRVAdapter(Context context, ArrayList<Song> mSongs, ArrayList<Artist> mArtists, ArrayList<Album> mAlbums, ArrayList<Playlist> mPlaylists) {
        this.context = context;
        this.mSongs = mSongs;
        this.mAlbums = mAlbums;
        this.mArtists = mArtists;
        this.mPlaylists = mPlaylists;
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
    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView playlistName;
        public TextView playlistMeta;
        public ImageView playlistPhoto;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cv_playlist);
            playlistName = (TextView) mCardView.findViewById(R.id.playlist_name);
            playlistMeta = (TextView) mCardView.findViewById(R.id.playlist_meta);
            playlistPhoto = (ImageView) mCardView.findViewById(R.id.playlist_photo);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder{
        CardView mCardView;
        TextView textViewHeader;
        TextView textViewSeeMore;

        HeaderViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cv);
            textViewHeader = (TextView) mCardView.findViewById(R.id.header_text);
            textViewSeeMore = (TextView) mCardView.findViewById(R.id.header_see_more);
        }
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        if (0 < position && position <= mPlaylists.size())
            return PLAYLIST_VIEW_TYPE;
        if (mPlaylists.size() + 1 < position && position <= mPlaylists.size() + mSongs.size() +1)
            return SONG_VIEW_TYPE;
        if (mPlaylists.size() + mSongs.size() + 2 < position && position < mPlaylists.size() + mSongs.size() + mArtists.size() +3)
            return ARTIST_VIEW_TYPE;
        if (mPlaylists.size() + mSongs.size() + mArtists.size() +3 < position && position < mPlaylists.size() + mSongs.size() + mArtists.size() + mAlbums.size() +4)
            return ALBUM_VIEW_TYPE;
        return HEADER_VIEW_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == PLAYLIST_VIEW_TYPE)
            return new PlaylistViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_playlist, parent, false));
        if(viewType == SONG_VIEW_TYPE)
            return new SongViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_song, parent, false));
        if(viewType == ARTIST_VIEW_TYPE)
            return new ArtistViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_artist, parent, false));
        if(viewType == ALBUM_VIEW_TYPE)
            return new AlbumViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_album, parent, false));
        return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_header, parent, false));
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case PLAYLIST_VIEW_TYPE:
                PlaylistViewHolder playlistHolder = (PlaylistViewHolder) holder;
                Playlist tmpPlaylist = mPlaylists.get(position-1);
                playlistHolder.playlistName.setText(tmpPlaylist.getName());
                playlistHolder.playlistMeta.setText("" + tmpPlaylist.getSongsCount() + " songs");
                playlistHolder.playlistPhoto.setImageResource(tmpPlaylist.getPhotoId());
                playlistHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context.getApplicationContext(), PlaylistActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("id", mPlaylists.get(holder.getAdapterPosition() - 1).getId());
                        context.getApplicationContext().startActivity(intent);
                    }
                });
                break;
            case SONG_VIEW_TYPE:
                SongViewHolder songHolder = (SongViewHolder) holder;
                final Song tmpSong = mSongs.get(position - mPlaylists.size() -2);
                final ArrayList<String> songsList = new ArrayList<String>();
                for (Song song : mSongs) {
                    songsList.add(song.getSpotifyUrl());
                }

                songHolder.songTitle.setText(tmpSong.getName());
                songHolder.songArtists.setText(tmpSong.getFirstArtistName());
                Picasso.with(context)
                        .load(tmpSong.getImageUrl())
                        .placeholder(R.drawable.cover_picture)
                        .error(R.drawable.album)
                        .fit()
                        .into(songHolder.songPhoto);
                songHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Globals.mSongs = mSongs;
                        Intent intent = new Intent(context.getApplicationContext(), StreamActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putStringArrayListExtra("songs", songsList);
                        intent.putExtra("trackID", mSongs.get(holder.getAdapterPosition() - mPlaylists.size() -2).getId());
                        intent.putExtra("position", (holder.getAdapterPosition() - mPlaylists.size() -2));
                        context.getApplicationContext().startActivity(intent);
                    }
                });
                break;
            case ARTIST_VIEW_TYPE:
                ArtistViewHolder artistHolder = (ArtistViewHolder) holder;
                Artist artist = mArtists.get(position - mPlaylists.size() - mSongs.size() -3);
                artistHolder.artistName.setText(artist.getName());
                artistHolder.artistMeta.setText("" + artist.getAlbumsCount() + " albums");
                if (artist.getImageUrl() != null && artist.getImageUrl().length() > 0) {
                    Picasso.with(context).load(artist.getImageUrl())
                            .placeholder(R.drawable.artist)
                            .error(R.drawable.artist)
                            .fit()
                            .into(artistHolder.artistPhoto);
                }
                artistHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context.getApplicationContext(), ArtistActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("id", mArtists.get(holder.getAdapterPosition() - mPlaylists.size() - mSongs.size() -3).getId());
                        context.getApplicationContext().startActivity(intent);
                    }
                });
                break;
            case ALBUM_VIEW_TYPE:
                AlbumViewHolder albumHolder = (AlbumViewHolder) holder;

                final Album tmpAlbum = mAlbums.get(position - mPlaylists.size() - mSongs.size() - mArtists.size() - 4);
                albumHolder.albumName.setText(tmpAlbum.getName());
                String albumMeta = tmpAlbum.getArtistsString() + "\n" +
                        tmpAlbum.getSongsCount() + " songs" ;
                albumHolder.albumMeta.setText(albumMeta);
                Picasso.with(context).load(tmpAlbum.getImageUrl())
                        .placeholder(R.drawable.album)
                        .error(R.drawable.album)
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
                if(position == mPlaylists.size() +1)
                    headerHolder.textViewHeader.setText(header_strings[1]);
                if(position == mPlaylists.size() + mSongs.size() +2)
                    headerHolder.textViewHeader.setText(header_strings[2]);
                if(position == mPlaylists.size() + mSongs.size() + mArtists.size() +3)
                    headerHolder.textViewHeader.setText(header_strings[3]);
                headerHolder.textViewSeeMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context.getApplicationContext(), MyAlbumArtistPlaylistActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if(holder.getAdapterPosition() == 0)
                            intent.putExtra("selected_index", 0);
                        if(holder.getAdapterPosition() == mPlaylists.size() +1)
                            intent.putExtra("selected_index", 3);
                        if(holder.getAdapterPosition() == mPlaylists.size() + mSongs.size() +2)
                            intent.putExtra("selected_index", 1);
                        if(holder.getAdapterPosition() == mPlaylists.size() + mSongs.size() + mArtists.size() +3)
                            intent.putExtra("selected_index", 2);
                        context.getApplicationContext().startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mPlaylists.size() + mSongs.size() + mArtists.size() + mAlbums.size() + header_strings.length;
    }
}
