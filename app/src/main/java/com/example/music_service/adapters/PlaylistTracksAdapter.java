package com.example.music_service.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.models.FavouriteMusic;
import com.example.music_service.models.Player;
import com.example.music_service.models.Song;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.models.globals.PlaylistSystem;
import com.example.music_service.models.globals.SongsProps;
import com.example.music_service.viewModels.LibraryFragmentViewModel;
import com.example.music_service.viewModels.PlaylistInfoViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class PlaylistTracksAdapter extends RecyclerView.Adapter<PlaylistTracksAdapter.ViewHolder> {

    private final Context context;
    private final PlaylistInfoViewModel playlistInfoViewModel;

    private ArrayList<Song> songs = new ArrayList<>();

    public PlaylistTracksAdapter(Context context, PlaylistInfoViewModel pv) {
        this.context = context;

        playlistInfoViewModel = pv;
    }

    @NonNull
    @Override
    public PlaylistTracksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_songs_item, parent, false);

        return new PlaylistTracksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.trackNameTxt.setText(songs.get(pos).getTitle());
        holder.authorNameTxt.setText(songs.get(pos).getArtist());

        holder.infoButton.setTag(holder.trackNameTxt.getText().toString());
        holder.trackNameTxt.setSelected(true);

        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSongInfo(view);
            }
        });

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trackName = holder.trackNameTxt.getText().toString();
                Toast.makeText(context, "(Playlist " + PlaylistSystem.getCurrentPlaylist().getPlaylistName() + ") " + trackName + " chosen", Toast.LENGTH_SHORT).show();
                playlistInfoViewModel.chooseTrack(trackName);
            }
        });
    }

    public void openSongInfo(View view) {
        String text = view.getTag().toString();
        Song song = SongsProps.getSongByName(text);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog((Activity) context, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(((Activity)context).getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_sheet,
                        (RelativeLayout) ((Activity)context).findViewById(R.id.bottom_sheet_container)
                );

        TextView title = bottomSheetView.findViewById(R.id.title_song);
        TextView artist = bottomSheetView.findViewById(R.id.author_song);

        Button playButton = bottomSheetView.findViewById(R.id.play_button);
        Button playNextButton = bottomSheetView.findViewById(R.id.queue_next_button);
        Button playLastButton = bottomSheetView.findViewById(R.id.queue_end_button);

        Button removeButton = bottomSheetView.findViewById(R.id.remove_button);
        removeButton.setVisibility(View.GONE);

        CardView favButton = bottomSheetView.findViewById(R.id.fav_button);

        title.setText(song.getTitle());
        artist.setText(song.getArtist());

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playlistInfoViewModel.chooseTrack(title.getText().toString());

                bottomSheetDialog.dismiss();
            }
        });

        playNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Globs.currentSongs.size() == 0) {
                    Toast.makeText(context, "No queue", Toast.LENGTH_SHORT).show();
                    return;
                }

                Player.addNextToQueue(title.getText().toString());

                bottomSheetDialog.dismiss();
            }
        });

        playLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Globs.currentSongs.size() == 0) {
                    Toast.makeText(context, "No queue", Toast.LENGTH_SHORT).show();
                    return;
                }

                Player.addToQueueEnd(title.getText().toString());

                bottomSheetDialog.dismiss();
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavouriteMusic.addToFavourites(title.getText().toString(), (Activity) context);
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
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
        private final ImageButton infoButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trackNameTxt = itemView.findViewById(R.id.track_title);
            authorNameTxt = itemView.findViewById(R.id.track_author);

            infoButton = itemView.findViewById(R.id.info_button);

            parent = itemView.findViewById(R.id.parent);
        }
    }
}
