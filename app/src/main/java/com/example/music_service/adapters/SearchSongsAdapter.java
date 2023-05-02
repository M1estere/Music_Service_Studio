package com.example.music_service.adapters;

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
import com.example.music_service.models.FavouriteMusic;
import com.example.music_service.models.Player;
import com.example.music_service.models.Song;
import com.example.music_service.models.data.SongsProps;
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
                searchViewModel.chooseTrack(holder.trackNameTxt.getText().toString());
            }
        });

        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSongInfo(view);
            }
        });
    }

    public void openSongInfo(View view) {
        String text = view.getTag().toString();
        Song song = SongsProps.getSongByName(text);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog((Activity) context, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(((Activity) context).getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_sheet,
                        (RelativeLayout) ((Activity) context).findViewById(R.id.bottom_sheet_container)
                );

        TextView title = bottomSheetView.findViewById(R.id.title_song);
        title.setSelected(true);
        TextView artist = bottomSheetView.findViewById(R.id.author_song);
        artist.setSelected(true);

        CardView playButton = bottomSheetView.findViewById(R.id.play_button);
        CardView playNextButton = bottomSheetView.findViewById(R.id.queue_next_button);
        CardView playLastButton = bottomSheetView.findViewById(R.id.queue_end_button);
        CardView removeButton = bottomSheetView.findViewById(R.id.remove_button);
        removeButton.setVisibility(View.GONE);
        CardView addToPlaylistButton = bottomSheetView.findViewById(R.id.add_to_list_button);

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
                searchViewModel.chooseTrack(title.getText().toString());

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
