package com.example.music_service.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
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
import com.example.music_service.models.FavouriteMusic;
import com.example.music_service.models.Player;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.Song;
import com.example.music_service.models.data.SongsProps;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.models.globals.PlaylistSystem;
import com.example.music_service.views.BottomSheets;
import com.example.music_service.views.PlaylistInfoActivity;
import com.example.music_service.views.UserPlaylistInfoActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class UserPlaylistsRecViewAdapter extends RecyclerView.Adapter<UserPlaylistsRecViewAdapter.ViewHolder> {

    private final Context context;

    private ArrayList<Playlist> playlists = new ArrayList<>();

    public UserPlaylistsRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public UserPlaylistsRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_playlist_item, parent, false);

        return new UserPlaylistsRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPlaylistsRecViewAdapter.ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.playlistName.setText(playlists.get(pos).getPlaylistName());
        holder.tracksAmount.setText(playlists.get(pos).getSongsAmount() + " tracks");

        holder.parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = playlists.get(pos).getPlaylistName();

                goToPlaylistInfo(holder, pos);
            }
        });

        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheets.openPlaylistInfo(context, playlists.get(pos), true);
            }
        });
    }

    private void goToPlaylistInfo(UserPlaylistsRecViewAdapter.ViewHolder holder, int pos) {
        PlaylistSystem.setCurrentPlaylist(playlists.get(pos));

        Intent intent = new Intent(context, UserPlaylistInfoActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, Pair.create(holder.playlistName, "title"), Pair.create(holder.cover, "play_cover"));
        context.startActivity(intent, options.toBundle());
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
        private final TextView playlistName;
        private final TextView tracksAmount;

        private final CardView parent;
        private CardView cover;
        private ImageButton infoButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            playlistName = itemView.findViewById(R.id.playlist_name);
            tracksAmount = itemView.findViewById(R.id.playlist_tracks);
            cover = itemView.findViewById(R.id.cover);

            infoButton = itemView.findViewById(R.id.info_button);

            parent = itemView.findViewById(R.id.parent);
        }
    }

}
