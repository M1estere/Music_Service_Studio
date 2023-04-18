package com.example.music_service.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.activities.PlaylistInfoActivity;
import com.example.music_service.models.globals.PlaylistSystem;
import com.example.music_service.viewModels.HomeFragmentViewModel;
import com.example.music_service.models.Playlist;
import com.example.music_service.R;

import java.util.ArrayList;

public class MorningRecViewAdapter extends RecyclerView.Adapter<MorningRecViewAdapter.ViewHolder> {

    private HomeFragmentViewModel homeFragmentViewModel;

    private final Context context;
    private ArrayList<Playlist> playlists = new ArrayList<>();

    public MorningRecViewAdapter(Context context, HomeFragmentViewModel viewModel) {
        this.context = context;
        homeFragmentViewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.morning_rec_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = playlists.get(pos).getPlaylistName();

                Toast.makeText(context, "(Morning) " + name + " was chosen", Toast.LENGTH_SHORT).show();
                goToPlaylistInfo(pos);
            }
        });
    }

    private void goToPlaylistInfo(int pos) {
        PlaylistSystem.setCurrentPlaylist(playlists.get(pos));

        Intent intent = new Intent(context, PlaylistInfoActivity.class);
        context.startActivity(intent);
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

        private final CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
        }
    }
}
