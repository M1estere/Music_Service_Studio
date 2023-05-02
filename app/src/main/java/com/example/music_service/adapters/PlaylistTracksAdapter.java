package com.example.music_service.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_service.R;
import com.example.music_service.models.Song;
import com.example.music_service.models.globals.PlaylistSystem;
import com.example.music_service.views.BottomSheets;

import java.util.ArrayList;

public class PlaylistTracksAdapter extends RecyclerView.Adapter<PlaylistTracksAdapter.ViewHolder> {

    private final Context context;

    private ArrayList<Song> songs = new ArrayList<>();

    public PlaylistTracksAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PlaylistTracksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_songs_item, parent, false);

        return new PlaylistTracksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.trackNameTxt.setText(songs.get(pos).getTitle());
        holder.authorNameTxt.setText(songs.get(pos).getArtist());

        holder.infoButton.setTag(holder.trackNameTxt.getText().toString());
        holder.trackNameTxt.setSelected(true);

        Glide.with(holder.itemView).load(songs.get(pos).getCover()).thumbnail(0.05f).into(holder.cover);

        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheets.openSongInfoNoRemoving(context, view, PlaylistSystem.getCurrentPlaylist());
            }
        });

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trackName = holder.trackNameTxt.getText().toString();
                Toast.makeText(context, "(Playlist " + PlaylistSystem.getCurrentPlaylist().getPlaylistName() + ") " + trackName + " chosen", Toast.LENGTH_SHORT).show();
                BottomSheets.chooseTrackFromPlaylist(PlaylistSystem.getCurrentPlaylist(), trackName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView trackNameTxt;
        private final TextView authorNameTxt;

        private final CardView parent;
        private final ImageButton infoButton;
        private final ImageView cover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trackNameTxt = itemView.findViewById(R.id.track_title);
            authorNameTxt = itemView.findViewById(R.id.track_author);

            infoButton = itemView.findViewById(R.id.info_button);
            cover = itemView.findViewById(R.id.song_cover);

            parent = itemView.findViewById(R.id.parent);
        }
    }
}
