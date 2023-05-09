package com.example.music_service.adapters.playlists;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.globals.PlaylistSystem;
import com.example.music_service.views.PlaylistInfoActivity;

import java.util.ArrayList;

public class ProgramPlaylistsRecViewAdapter extends RecyclerView.Adapter<ProgramPlaylistsRecViewAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Playlist> playlists = new ArrayList<>();

    public ProgramPlaylistsRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_playlist_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.playlistTitle.setText(playlists.get(pos).getPlaylistName().substring(0, playlists.get(pos).getPlaylistName().lastIndexOf(" ")));

        holder.parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = playlists.get(pos).getPlaylistName();

                goToPlaylistInfo(holder, pos);
            }
        });
    }

    private void goToPlaylistInfo(ViewHolder holder, int pos) {
        PlaylistSystem.setCurrentPlaylist(playlists.get(pos));

        Intent intent = new Intent(context, PlaylistInfoActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, Pair.create(holder.playlistTitle, "title"), Pair.create(holder.cover, "play_cover"));
        context.startActivity(intent, options.toBundle());
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

        private final TextView playlistTitle;

        private final CardView cover;
        private final CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            playlistTitle = itemView.findViewById(R.id.playlist_title);
            cover = itemView.findViewById(R.id.content);

            parent = itemView.findViewById(R.id.parent);
        }
    }
}
