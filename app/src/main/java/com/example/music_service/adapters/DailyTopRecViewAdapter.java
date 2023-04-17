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
import com.example.music_service.models.Song;
import com.example.music_service.viewModels.LibraryFragmentViewModel;

import java.util.ArrayList;

public class DailyTopRecViewAdapter extends RecyclerView.Adapter<DailyTopRecViewAdapter.ViewHolder> {

    private final Context context;
    private final LibraryFragmentViewModel libraryFragmentViewModel;

    private ArrayList<Song> songs = new ArrayList<>();

    public DailyTopRecViewAdapter(Context context, LibraryFragmentViewModel viewModel) {
        this.context = context;
        libraryFragmentViewModel = viewModel;
    }

    @NonNull
    @Override
    public DailyTopRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.best_item, parent, false);

        return new DailyTopRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyTopRecViewAdapter.ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.trackNameTxt.setText(songs.get(pos).getTitle());
        holder.authorNameTxt.setText(songs.get(pos).getArtist());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trackName = holder.trackNameTxt.getText().toString();
                Toast.makeText(context, "(Daily Top) " + trackName + " chosen", Toast.LENGTH_SHORT).show();
                libraryFragmentViewModel.chooseTrack(trackName);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trackNameTxt = itemView.findViewById(R.id.track_title);
            authorNameTxt = itemView.findViewById(R.id.track_author);

            parent = itemView.findViewById(R.id.parent);
        }
    }
}
