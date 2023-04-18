package com.example.music_service.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.models.FavouriteMusic;
import com.example.music_service.models.Player;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.Song;
import com.example.music_service.models.globals.Convert;

import java.util.ArrayList;

public class UserSongsRecViewAdapter extends RecyclerView.Adapter<UserSongsRecViewAdapter.ViewHolder> {

    private final Context context;

    private ArrayList<Song> songs = new ArrayList<>();

    public UserSongsRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_song_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.trackNameTxt.setText(songs.get(pos).getTitle());
        holder.authorNameTxt.setText(songs.get(pos).getArtist());

        holder.trackNameTxt.setSelected(true);

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavouriteMusic.removeFromFavourites(holder.trackNameTxt.getText().toString(), (Activity) context);
            }
        });

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trackName = holder.trackNameTxt.getText().toString();
                Toast.makeText(context, "(Favourites) " + trackName + " chosen", Toast.LENGTH_SHORT).show();
                chooseTrackFromFavs(trackName);
            }
        });
    }

    private void chooseTrackFromFavs(String title)
    {
        int currentTrackIndex = 0;
        for (int i = 0; i < FavouriteMusic.size(); i++)
        {
            String name = Convert.getTitleFromPath(FavouriteMusic.getArrayTitles().get(i));
            if (name.equals(title)) currentTrackIndex = i;
        }

        Playlist newPlaylist = new Playlist("Favourites");
        newPlaylist.setSongTitles(FavouriteMusic.getArrayTitles());

        Player.updateQueue(newPlaylist.getSongTitles(), currentTrackIndex);
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
        private final ImageButton removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trackNameTxt = itemView.findViewById(R.id.track_title);
            authorNameTxt = itemView.findViewById(R.id.track_author);
            removeButton = itemView.findViewById(R.id.remove_button);

            parent = itemView.findViewById(R.id.parent);
        }
    }
}
