package com.example.music_service.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.models.CustomPlaylists;
import com.example.music_service.models.Playlist;

import java.util.ArrayList;

public class PlaylistsAddAdapter extends RecyclerView.Adapter<PlaylistsAddAdapter.ViewHolder> {

    private final Context context;

    private ArrayList<Playlist> playlists = new ArrayList<>();

    private String songTitle;

    public PlaylistsAddAdapter(Context context, String songName) {
        this.context = context;
        songTitle = songName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_playlists_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();

        holder.playlistName.setText(playlists.get(pos).getPlaylistName());
        holder.songsAmount.setText(playlists.get(pos).getSongsAmount() + " tracks");

        holder.status.setImageDrawable(CustomPlaylists.songInList(holder.playlistName.getText().toString(), songTitle) ?
                AppCompatResources.getDrawable(context, R.drawable.check_40) :
                AppCompatResources.getDrawable(context, R.drawable.plus_40));

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recheckStatus(playlists.get(pos).getPlaylistName(), holder);
            }
        });
    }

    private void recheckStatus(String playlistName, ViewHolder holder) {
        if (CustomPlaylists.songInList(playlistName, songTitle)) {
            removeFromList(playlistName, holder);
        } else {
            addToList(playlistName, holder);
        }
    }

    private void addToList(String playlistName, ViewHolder holder) {
        CustomPlaylists.addSongToPlaylist(context, playlistName, songTitle);

        holder.songsAmount.setText((playlists.get(holder.getAdapterPosition()).getSongsAmount() + 1) + " tracks");
        holder.status.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.check_40));
    }

    private void removeFromList(String playlistName, ViewHolder holder) {
        CustomPlaylists.removeSongFromPlaylist(context, playlistName, songTitle);

        holder.songsAmount.setText((playlists.get(holder.getAdapterPosition()).getSongsAmount()) + " tracks");
        holder.status.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.plus_40));
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public void setPlaylists(ArrayList<Playlist> playlists) {
        this.playlists = playlists;

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView playlistName;
        private TextView songsAmount;

        private CardView parent;

        private ImageView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            playlistName = itemView.findViewById(R.id.playlist_name);
            songsAmount = itemView.findViewById(R.id.playlist_tracks_amount);

            parent = itemView.findViewById(R.id.add_to_list);

            status = itemView.findViewById(R.id.status);
        }
    }
}
