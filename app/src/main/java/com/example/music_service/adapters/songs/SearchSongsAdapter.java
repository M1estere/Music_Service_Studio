package com.example.music_service.adapters.songs;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_service.R;
import com.example.music_service.models.Player;
import com.example.music_service.models.Song;
import com.example.music_service.models.data.SongsProps;
import com.example.music_service.models.firebase.FavouriteMusic;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.viewModels.SearchViewModel;
import com.example.music_service.views.BottomSheets;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class SearchSongsAdapter extends RecyclerView.Adapter<SearchSongsAdapter.ViewHolder> {

    private final Context context;
    private final SearchViewModel searchViewModel;

    private ArrayList<Song> songs = new ArrayList<>();

    public SearchSongsAdapter(Context context, SearchViewModel sv) {
        this.context = context;
        searchViewModel = sv;
    }

    @NonNull
    @Override
    public SearchSongsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_song_item, parent, false);

        return new SearchSongsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchSongsAdapter.ViewHolder holder, int position) {
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
                Toast.makeText(context, "(Search) " + holder.trackNameTxt.getText().toString() + " chosen", Toast.LENGTH_SHORT).show();
                searchViewModel.chooseTrack(holder.trackNameTxt.getText().toString());
            }
        });

        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheets.openSongInfoNoRemoving(context, view, searchViewModel.getTitles());
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
