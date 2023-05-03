package com.example.music_service.adapters.artists;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_service.R;
import com.example.music_service.models.Author;
import com.example.music_service.models.globals.PlaylistSystem;
import com.example.music_service.views.ArtistInfoActivity;

import java.util.ArrayList;

public class ArtistsRecViewAdapter extends RecyclerView.Adapter<ArtistsRecViewAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<Author> artists = new ArrayList<>();

    public ArtistsRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modern_artist_block, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.artistName.setText(artists.get(pos).getAuthorName());
        holder.artistName.setSelected(true);

        Glide.with(holder.itemView)
                .load(PlaylistSystem.findArtistFirstSong(artists.get(pos).getAuthorName()))
                .thumbnail(0.05f)
                .into(holder.image);

        holder.parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                goToArtistInfo(holder, pos);
            }
        });
    }

    private void goToArtistInfo(ArtistsRecViewAdapter.ViewHolder holder, int pos) {
        PlaylistSystem.setCurrentAuthor(artists.get(pos));

        Intent intent = new Intent(context, ArtistInfoActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, Pair.create(holder.artistName, "name"), Pair.create(holder.artistImage, "artist_image"));
        context.startActivity(intent, options.toBundle());
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public void setAuthors(ArrayList<Author> artists) {
        this.artists = artists;

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView artistName;

        private final CardView parent;
        private final CardView artistImage;

        private final ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            artistName = itemView.findViewById(R.id.artist_name);

            parent = itemView.findViewById(R.id.parent);
            artistImage = itemView.findViewById(R.id.artist_image);

            image = itemView.findViewById(R.id.image);
        }
    }
}
