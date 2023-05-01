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
import com.example.music_service.models.Author;
import com.example.music_service.models.Song;

import java.util.ArrayList;

public class SearchArtistsAdapter extends RecyclerView.Adapter<SearchArtistsAdapter.ViewHolder> {

    private final Context context;

    private ArrayList<Author> artists = new ArrayList<>();

    public SearchArtistsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SearchArtistsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_artist_item, parent, false);

        return new SearchArtistsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchArtistsAdapter.ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.authorNameTxt.setText(artists.get(pos).getAuthorName());

//        Glide.with(holder.itemView)
//                .load(artists.get(pos).getCover())
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
        return artists.size();
    }

    public void setArtists(ArrayList<Author> authors) {
        artists = authors;

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView authorNameTxt;

        private final CardView parent;
        private final ImageView cover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            authorNameTxt = itemView.findViewById(R.id.artist_name);
            cover = itemView.findViewById(R.id.artist_image);

            parent = itemView.findViewById(R.id.parent);
        }
    }
}
