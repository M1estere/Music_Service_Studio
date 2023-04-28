package com.example.music_service.adapters;

import android.app.Activity;
import android.content.Context;
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
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_service.R;
import com.example.music_service.models.FavouriteMusic;
import com.example.music_service.models.Player;
import com.example.music_service.models.Song;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.models.globals.PlaylistSystem;
import com.example.music_service.models.data.SongsProps;
import com.example.music_service.viewModels.ArtistInfoViewModel;
import com.example.music_service.viewModels.PlaylistInfoViewModel;
import com.example.music_service.views.BottomSheets;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class ArtistTracksAdapter extends RecyclerView.Adapter<ArtistTracksAdapter.ViewHolder> {

    private final Context context;
    private final ArtistInfoViewModel artistInfoViewModel;

    private ArrayList<Song> songs = new ArrayList<>();

    public ArtistTracksAdapter(Context context, ArtistInfoViewModel pv) {
        this.context = context;
        artistInfoViewModel = pv;
    }

    @NonNull
    @Override
    public ArtistTracksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_songs_item, parent, false);

        return new ArtistTracksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.trackNameTxt.setText(songs.get(pos).getTitle());
        holder.authorNameTxt.setText(songs.get(pos).getArtist());

        holder.infoButton.setTag(holder.trackNameTxt.getText().toString());
        holder.trackNameTxt.setSelected(true);

        Glide.with(holder.itemView).load(songs.get(pos).getCover()).thumbnail(0.05f).into(holder.cover);

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
                Toast.makeText(context, "(Artist list " + PlaylistSystem.getCurrentAuthor().getAuthorName() + ") " + trackName + " chosen", Toast.LENGTH_SHORT).show();
                artistInfoViewModel.chooseTrack(trackName);
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
        title.setSelected(true);
        TextView artist = bottomSheetView.findViewById(R.id.author_song);
        artist.setSelected(true);

        Button playButton = bottomSheetView.findViewById(R.id.play_button);
        Button playNextButton = bottomSheetView.findViewById(R.id.queue_next_button);
        Button playLastButton = bottomSheetView.findViewById(R.id.queue_end_button);
        Button removeButton = bottomSheetView.findViewById(R.id.remove_button);
        Button addToPlaylistButton = bottomSheetView.findViewById(R.id.add_to_list_button);

        CardView favButton = bottomSheetView.findViewById(R.id.fav_button_whole);

        ImageView cover = bottomSheetView.findViewById(R.id.track_cover);

        Glide.with(bottomSheetView)
                .load(song.getCover())
                .thumbnail(0.05f).
                into(cover);

        title.setText(song.getTitle());
        artist.setText(song.getArtist());

        ImageView heart = bottomSheetView.findViewById(R.id.fav_button);
        heart.setImageDrawable(FavouriteMusic.contains(song.getTitle()) ? AppCompatResources.getDrawable(context, R.drawable.heart_filled_40) : AppCompatResources.getDrawable(context, R.drawable.heart_unfilled_40));

        addToPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheets.openPlaylistsSection(context, song.getTitle());
                bottomSheetDialog.dismiss();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artistInfoViewModel.chooseTrack(title.getText().toString());

                bottomSheetDialog.dismiss();
            }
        });

        playNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.addNextToQueue(title.getText().toString());

                bottomSheetDialog.dismiss();
            }
        });

        playLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.addToQueueEnd(title.getText().toString());

                bottomSheetDialog.dismiss();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.deleteFromQueue(title.getText().toString());

                bottomSheetDialog.dismiss();
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean added = FavouriteMusic.addToFavourites(title.getText().toString(), (Activity) context);
                heart.setImageDrawable(added ? AppCompatResources.getDrawable(context, R.drawable.heart_filled_40) : AppCompatResources.getDrawable(context, R.drawable.heart_unfilled_40));
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
        private final ImageView cover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trackNameTxt = itemView.findViewById(R.id.track_title);
            authorNameTxt = itemView.findViewById(R.id.track_author);

            infoButton = itemView.findViewById(R.id.info_button);
            cover = itemView.findViewById(R.id.song_cover);

            parent = itemView.findViewById(R.id.parent);
        }
    }
}
