package com.example.music_service.adapters.songs.libraryFragment;

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
import com.example.music_service.models.data.LibraryFragmentData;
import com.example.music_service.models.globals.PlaylistSystem;
import com.example.music_service.views.BottomSheets;

import java.util.ArrayList;

public class BestTracksAdapter extends RecyclerView.Adapter<BestTracksAdapter.ViewHolder> {

    private final Context context;

    private ArrayList<Song> songs = new ArrayList<>();

    public BestTracksAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BestTracksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.best_item, parent, false);

        return new BestTracksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BestTracksAdapter.ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.trackNameTxt.setText(songs.get(pos).getTitle());
        holder.authorNameTxt.setText(songs.get(pos).getArtist());
        holder.infoButton.setTag(holder.trackNameTxt.getText().toString());

        Glide.with(holder.itemView)
                .load(songs.get(pos).getCover())
                .thumbnail(0.05f)
                .into(holder.cover);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trackName = holder.trackNameTxt.getText().toString();
                Toast.makeText(context, "(Best) " + trackName + " chosen", Toast.LENGTH_SHORT).show();
                BottomSheets.chooseTrackFromPlaylist(LibraryFragmentData.bestSongs, trackName, "Best Songs");
            }
        });

        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheets.openSongInfoNoRemoving(context, view, PlaylistSystem.getPlaylistOfSongsSongs("Best Songs", LibraryFragmentData.bestSongs));
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
        private final ImageView cover;
        private final ImageButton infoButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trackNameTxt = itemView.findViewById(R.id.track_title);
            authorNameTxt = itemView.findViewById(R.id.track_author);
            cover = itemView.findViewById(R.id.song_cover);
            infoButton = itemView.findViewById(R.id.info_button);

            parent = itemView.findViewById(R.id.parent);
        }
    }
}
