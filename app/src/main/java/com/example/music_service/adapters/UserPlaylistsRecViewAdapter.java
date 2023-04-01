package com.example.music_service.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.model.Playlist;

import java.util.ArrayList;

public class UserPlaylistsRecViewAdapter extends RecyclerView.Adapter<UserPlaylistsRecViewAdapter.ViewHolder> {

    private final Context context;

    private ArrayList<Playlist> playlists = new ArrayList<>();

    public UserPlaylistsRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public UserPlaylistsRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_playlist_item, parent, false);

        return new UserPlaylistsRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPlaylistsRecViewAdapter.ViewHolder holder, int position) {
        holder.playlistName.setText(playlists.get(position).getPlaylistName());
        holder.tracksAmount.setText(playlists.get(position).getSongsAmount() + " tracks");

        holder.parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(context, "shit chosen", Toast.LENGTH_SHORT).show();
            }
        });
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
        private TextView tracksAmount;

        private final CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            playlistName = itemView.findViewById(R.id.playlist_name);
            tracksAmount = itemView.findViewById(R.id.playlist_tracks);

            parent = itemView.findViewById(R.id.parent);
        }
    }

}
