package com.example.music_service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MusicPlayerActivity extends AppCompatActivity {

    private TextView currentTrackTitle;
    private TextView currentTrackAuthor;
    private TextView currentTrackDuration;
    private TextView currentSecond;

    private Button homeButton;
    private Button pauseButton;
    private Button nextButton;
    private Button previousButton;

    private SeekBar progressBar;

    private ArrayList<Song> songs;
    private int currentTrackNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);

        songs = new ArrayList<>();

        currentTrackTitle = findViewById(R.id.song_title);
        currentTrackAuthor = findViewById(R.id.song_author);
        currentTrackDuration = findViewById(R.id.whole_time_text);
        currentSecond = findViewById(R.id.current_time_text);

        homeButton = findViewById(R.id.home_page_button);
        pauseButton = findViewById(R.id.pause_button);
        nextButton = findViewById(R.id.next_button);
        previousButton = findViewById(R.id.previous_button);
        progressBar = findViewById(R.id.progress_bar);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MusicPlayerActivity.this, QueueActivity.class);
                startActivity(intent);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Player.isPlay()) pauseSong();
                else playSong();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSong();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousSong();
            }
        });

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                return;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Player.goTo(progress * 1000);
                return;
            }
        });

        final boolean Set = true;
        currentTrackTitle.setSelected(true);

        Globs.fillAllSongs();
        initSongs();

        new Thread() {
            public void run() {
                while (Set == true) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progressBar.setProgress(Player.getCurrentPos() / 1000);
                                currentSecond.setText(Convert.GetTimeFromSeconds(Player.getCurrentPos()));

                                checkProgression();
                            }
                        });

                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        updateUI();
    }

    private void checkProgression() {
        if (Player.getCurrentPos() >= Player.getDuration() - 2000)
            nextSong();
    }

    private void initSongs() {
        Player.setContext(this);

        Playlist playlist = new Playlist("Start List");

        PlaylistSystem.fillOnePlaylist(20, playlist);

        Player.setQueue(playlist.getSongTitles());
    }

    private void nextSong() {
        currentTrackNumber = Player.nextSong();
        updateUI();
    }

    private void previousSong() {
        currentTrackNumber = Player.previousSong();
        updateUI();
    }

    private void updateUI() {
        songs.clear();

        for (String title : Player.getSongs())
            createSong(title, SongsProps.ids.get(SongsProps.songs.indexOf(title)));

        currentTrackTitle.setText(songs.get(currentTrackNumber).getTitle());
        currentTrackAuthor.setText(songs.get(currentTrackNumber).getArtist());

        currentTrackDuration.setText(Convert.GetTimeFromSeconds(Player.getDuration() - 1000));

        int max = Player.getDuration() / 1000;
        progressBar.setMax(max);
    }

    private void createSong(String title, int id) {
        Song songToAdd = new Song(title, id);

        songs.add(songToAdd);
    }

    public void playSong() {
        Player.playSong();
    }

    public void pauseSong() {
        Player.pause();
    }
}
