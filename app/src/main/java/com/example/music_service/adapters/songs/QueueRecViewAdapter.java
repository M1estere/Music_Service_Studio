package com.example.music_service.adapters.songs;

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
import com.example.music_service.models.globals.Globs;
import com.example.music_service.models.globals.PlaylistSystem;
import com.example.music_service.viewModels.QueueActivityViewModel;
import com.example.music_service.views.BottomSheets;

import java.util.ArrayList;

public class QueueRecViewAdapter extends RecyclerView.Adapter<QueueRecViewAdapter.ViewHolder> {

    private final Context context;
    private final QueueActivityViewModel queueActivityViewModel;

    private ArrayList<Song> songs = new ArrayList<>();

    public QueueRecViewAdapter(Context context, QueueActivityViewModel viewModel) {
        this.context = context;
        queueActivityViewModel = viewModel;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == songs.size()) ? R.layout.hold_to_move_text : R.layout.queue_item;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == R.layout.queue_item) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hold_to_move_text, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.trackNameTxt == null) return;
        int pos = holder.getAdapterPosition();

        holder.trackNameTxt.setText(songs.get(pos).getTitle());
        holder.authorNameTxt.setText(songs.get(pos).getArtist());
        holder.infoButton.setTag(holder.trackNameTxt.getText().toString());

        Glide.with(holder.itemView)
                .load(songs.get(pos).getCover())
                .thumbnail(0.05f).
                into(holder.cover);

        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheets.openFullSongInfo(context, view, PlaylistSystem.getPlaylistOfSongs("Queue", Globs.getTitlesPaths()));
            }
        });

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String songName = holder.trackNameTxt.getText().toString();
                Toast.makeText(context, "(Queue) " + songName + " selected", Toast.LENGTH_SHORT).show();
                BottomSheets.chooseTrackFromPlaylist(PlaylistSystem.getPlaylistOfSongs("Queue", Globs.getTitlesPaths()), songName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size() + 1;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView trackNameTxt;
        private final TextView authorNameTxt;

        private final ImageButton infoButton;

        private final CardView parent;
        private final ImageView cover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trackNameTxt = itemView.findViewById(R.id.track_title_txt);
            authorNameTxt = itemView.findViewById(R.id.author_txt);

            infoButton = itemView.findViewById(R.id.info_button);

            parent = itemView.findViewById(R.id.parent);
            cover = itemView.findViewById(R.id.song_cover);

            if (trackNameTxt == null) return;

            trackNameTxt.setSelected(true);
            authorNameTxt.setSelected(true);
        }
    }
}
