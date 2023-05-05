package com.example.music_service.views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_service.R;
import com.example.music_service.adapters.playlists.PlaylistsAddAdapter;
import com.example.music_service.models.Player;
import com.example.music_service.models.Playlist;
import com.example.music_service.models.Song;
import com.example.music_service.models.data.SongsProps;
import com.example.music_service.models.firebase.CustomPlaylists;
import com.example.music_service.models.firebase.FavouriteMusic;
import com.example.music_service.models.globals.Convert;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.models.globals.PlaylistSystem;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class BottomSheets {

    public static void openPlaylistsSection(Context context, String songName) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog((Activity) context, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(((Activity) context).getApplicationContext())
                .inflate(
                        R.layout.playlists_bottom_sheet,
                        (RelativeLayout) ((Activity) context).findViewById(R.id.bottom_sheet_container)
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

    public static void openPlaylistInfo(Context context, Playlist play, boolean userList) {
        PlaylistSystem.setCurrentPlaylist(play);
        Playlist playlist = PlaylistSystem.getCurrentPlaylist();

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
        CardView addToPlaylistButton = bottomSheetView.findViewById(R.id.add_to_list_button);

        CardView removeButton = bottomSheetView.findViewById(R.id.remove_button);
        if (!userList) removeButton.setVisibility(View.GONE);

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

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomPlaylists.deletePlaylist(playlist.getPlaylistName());
                bottomSheetDialog.dismiss();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlaylist(context);
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

                Player.addNextToQueue(PlaylistSystem.getCurrentPlaylist());

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

                Player.addToQueueEnd(PlaylistSystem.getCurrentPlaylist());

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public static void startPlaylist(Context context) {
        Playlist current = PlaylistSystem.getCurrentPlaylist();
        Player.updateQueue(current.getSongTitles(), 0);

        ((Activity) context).onBackPressed();
    }

    public static void openSongInfoNoRemoving(Context context, View view, Playlist toLook) {
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
                chooseTrackFromPlaylist(toLook, title.getText().toString());

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
                favButton.animate()
                        .scaleX(1.15f)
                        .scaleY(1.15f)
                        .setDuration(200)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                favButton.animate()
                                        .scaleX(1)
                                        .scaleY(1)
                                        .setDuration(150)
                                        .setInterpolator(new OvershootInterpolator())
                                        .start();
                            }
                        }).start();

                boolean added = FavouriteMusic.addToFavourites(title.getText().toString(), (Activity) context);
                heart.setImageDrawable(added ? AppCompatResources.getDrawable(context, R.drawable.heart_filled_40) : AppCompatResources.getDrawable(context, R.drawable.heart_unfilled_40));
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public static void openSongInfoNoFav(Context context, View view, Playlist toLook) {
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
        CardView addToPlaylistButton = bottomSheetView.findViewById(R.id.add_to_list_button);

        CardView favButton = bottomSheetView.findViewById(R.id.fav_button_whole);
        favButton.setVisibility(View.GONE);

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
                chooseTrackFromPlaylist(toLook, title.getText().toString());

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

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public static void openFullSongInfo(Context context, View view, Playlist toLook) {
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
                chooseTrackFromPlaylist(toLook, title.getText().toString());

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
                favButton.animate()
                        .scaleX(1.15f)
                        .scaleY(1.15f)
                        .setDuration(200)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                favButton.animate()
                                        .scaleX(1)
                                        .scaleY(1)
                                        .setDuration(150)
                                        .setInterpolator(new OvershootInterpolator())
                                        .start();
                            }
                        }).start();

                boolean added = FavouriteMusic.addToFavourites(title.getText().toString(), (Activity) context);
                heart.setImageDrawable(added ? AppCompatResources.getDrawable(context, R.drawable.heart_filled_40) : AppCompatResources.getDrawable(context, R.drawable.heart_unfilled_40));
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public static void chooseTrackFromPlaylist(Playlist toLook, String name) {
        int currentTrackIndex = 0;

        for (int i = 0; i < toLook.getSongsAmount(); i++) {
            String nameT = Convert.getTitleFromPath(toLook.getSongTitles().get(i));
            if (nameT.equals(name)) {
                currentTrackIndex = i;
                break;
            }
        }

        Player.updateQueue(toLook.getSongTitles(), currentTrackIndex);
    }

    public static void chooseTrackFromPlaylist(ArrayList<Song> toLook, String name, String listName) {
        int currentTrackIndex = 0;
        for (int i = 0; i < toLook.size(); i++) {
            String nameT = toLook.get(i).getTitle();
            if (nameT.equals(name)) currentTrackIndex = i;
        }

        Player.updateQueue(Convert.getPlaylistFromSongs(toLook, listName).getSongTitles(), currentTrackIndex);
    }

}
