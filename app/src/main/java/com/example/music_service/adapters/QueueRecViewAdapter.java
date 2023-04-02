package com.example.music_service.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.QueueActivityViewModel;
import com.example.music_service.R;
import com.example.music_service.model.Song;

import java.util.ArrayList;

public class QueueRecViewAdapter extends RecyclerView.Adapter<QueueRecViewAdapter.ViewHolder> {

    private final Context context;
    private QueueActivityViewModel queueActivityViewModel;

    private ArrayList<Song> songs = new ArrayList<>();

    public QueueRecViewAdapter(Context context, QueueActivityViewModel viewModel) {
        this.context = context;
        queueActivityViewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.trackNameTxt.setText(songs.get(position).getTitle());
        holder.authorNameTxt.setText(songs.get(position).getArtist());

        holder.parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                queueActivityViewModel.chooseTrack(holder.trackNameTxt.getText().toString());
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
        private final TextView durationTxt;

        private final CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trackNameTxt = itemView.findViewById(R.id.track_title_txt);
            authorNameTxt = itemView.findViewById(R.id.author_txt);
            durationTxt = itemView.findViewById(R.id.track_length);

            parent = itemView.findViewById(R.id.parent);
        }
    }
}