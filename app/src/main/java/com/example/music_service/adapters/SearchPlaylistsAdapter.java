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
import com.example.music_service.models.Playlist;
import com.example.music_service.models.Song;

import java.util.ArrayList;

public class SearchPlaylistsAdapter extends RecyclerView.Adapter<SearchPlaylistsAdapter.ViewHolder> {

    private final Context context;

    private ArrayList<Playlist> playlists = new ArrayList<>();

    public SearchPlaylistsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SearchPlaylistsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_playlist_item, parent, false);

        return new SearchPlaylistsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPlaylistsAdapter.ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.listNameTxt.setText(playlists.get(pos).getPlaylistName());

//        Glide.with(holder.itemView)
//                .load(songs.get(pos).getCover())
//                .thumbnail(0.05f)
//                .into(holder.cover);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        private final TextView listNameTxt;

        private final CardView parent;
        private final ImageView cover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listNameTxt = itemView.findViewById(R.id.list_title);
            cover = itemView.findViewById(R.id.list_cover);

            parent = itemView.findViewById(R.id.parent);
        }
    }
}
