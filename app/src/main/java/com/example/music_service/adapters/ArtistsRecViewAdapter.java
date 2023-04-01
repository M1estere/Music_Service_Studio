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

import com.example.music_service.model.Author;
import com.example.music_service.R;

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
        holder.artistName.setText(artists.get(position).getAuthorName());

        holder.parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(context, artists.get(position).getAuthorName() + " chosen", Toast.LENGTH_SHORT).show();
            }
        });
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            artistName = itemView.findViewById(R.id.artist_name);

            parent = itemView.findViewById(R.id.parent);
        }
    }
}
