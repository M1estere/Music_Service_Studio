package com.example.music_service.views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.adapters.PlaylistsAddAdapter;
import com.example.music_service.models.CustomPlaylists;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.globals.Convert;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.models.globals.PlaylistSystem;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class BottomSheets {

    public static void openPlaylistsSection(Context context, String songName) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog((Activity) context, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(((Activity)context).getApplicationContext())
                .inflate(
                        R.layout.playlists_bottom_sheet,
                        (RelativeLayout) ((Activity)context).findViewById(R.id.bottom_sheet_container)
                );

        CardView addNewListButton = bottomSheetView.findViewById(R.id.new_list_button);
        addNewListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> titles = new ArrayList<>();
                titles.add(Convert.getPathFromTitle(songName));
                CustomPlaylists.savingPlaylist(context, titles);
                bottomSheetDialog.dismiss();
            }
        });

        RecyclerView playlistsList = bottomSheetView.findViewById(R.id.playlists_rec);
        PlaylistsAddAdapter playlistsAddAdapter = new PlaylistsAddAdapter(context, songName);

        playlistsAddAdapter.setPlaylists(CustomPlaylists.getPlaylists());
        playlistsList.setAdapter(playlistsAddAdapter);

        playlistsList.setLayoutManager(new LinearLayoutManager(context));

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public static void openPlaylistInfo(Context context, Playlist play) {
        PlaylistSystem.setCurrentPlaylist(play);
        Playlist playlist = PlaylistSystem.getCurrentPlaylist();

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

        CardView playButton = bottomSheetView.findViewById(R.id.play_button);
        CardView playNextButton = bottomSheetView.findViewById(R.id.queue_next_button);
        CardView playLastButton = bottomSheetView.findViewById(R.id.queue_end_button);
        CardView addToPlaylistButton = bottomSheetView.findViewById(R.id.add_to_list_button);

        CardView removeButton = bottomSheetView.findViewById(R.id.remove_button);
        removeButton.setVisibility(View.GONE);

        CardView favButton = bottomSheetView.findViewById(R.id.fav_button_whole);

        ImageView cover = bottomSheetView.findViewById(R.id.track_cover);

//        Glide.with(bottomSheetView)
//                .load(song.getCover())
//                .thumbnail(0.05f).
//                into(cover);

        ImageView heart = bottomSheetView.findViewById(R.id.fav_button);
        heart.setVisibility(View.GONE);

        title.setText(playlist.getPlaylistName());
        artist.setText(playlist.getSongsAmount() + " tracks");

        addToPlaylistButton.setVisibility(View.GONE);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

//                Player.addNextToQueue(title.getText().toString());

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

//                Player.addToQueueEnd(title.getText().toString());

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

}
