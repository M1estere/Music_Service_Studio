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

import com.example.music_service.viewModels.HomeFragmentViewModel;
import com.example.music_service.models.Playlist;
import com.example.music_service.R;

import java.util.ArrayList;

public class ProgramPlaylistsRecViewAdapter extends RecyclerView.Adapter<ProgramPlaylistsRecViewAdapter.ViewHolder> {

    private HomeFragmentViewModel homeFragmentViewModel;

    private final Context context;
    private ArrayList<Playlist> playlists = new ArrayList<>();

    public ProgramPlaylistsRecViewAdapter(Context context, HomeFragmentViewModel viewModel) {
        this.context = context;
        homeFragmentViewModel = viewModel;
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
        holder.playlistTitle.setText(playlists.get(pos).getPlaylistName());

        holder.parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = playlists.get(pos).getPlaylistName();

                Toast.makeText(context, "(Program) " + name + " was chosen", Toast.LENGTH_SHORT).show();
                homeFragmentViewModel.choosePlaylist(name);
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

        private final TextView playlistTitle;

        private final CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            playlistTitle = itemView.findViewById(R.id.playlist_title);

            parent = itemView.findViewById(R.id.parent);
        }
    }
}
