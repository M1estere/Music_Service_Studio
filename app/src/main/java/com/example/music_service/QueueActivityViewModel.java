package com.example.music_service;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.adapters.QueueRecViewAdapter;
import com.example.music_service.model.globals.Convert;
import com.example.music_service.model.globals.Globs;
import com.example.music_service.model.globals.SongsProps;
import com.example.music_service.model.Player;
import com.example.music_service.model.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class QueueActivityViewModel extends BaseObservable {

    private Activity mainActivity;

    private RecyclerView queueRecView;

    public QueueActivityViewModel(@NonNull Activity activity) {
        mainActivity = activity;

        queueRecView = activity.findViewById(R.id.queue_rec_view);

        QueueRecViewAdapter adapter = new QueueRecViewAdapter(activity, this);
        adapter.setSongs(Globs.currentSongs);

        queueRecView.setAdapter(adapter);
        queueRecView.setLayoutManager(new LinearLayoutManager(activity));

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();

                int toPosition = target.getAdapterPosition();
                int old = Globs.currentTrackNumber;

                String currentTitle = Globs.getTitles().get(Globs.currentTrackNumber);
                Collections.swap(Globs.currentSongs, fromPosition, toPosition);
                adapter.notifyItemMoved(fromPosition, toPosition);

                Globs.currentTrackNumber = findSong(currentTitle);

                System.out.printf("swapped, was: %d, now: %d\n", old, Globs.currentTrackNumber);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Toast.makeText(activity, "on Swiped ", Toast.LENGTH_SHORT).show();
                String currentTitle = Globs.getTitles().get(Globs.currentTrackNumber);

                int position = viewHolder.getAdapterPosition();
                if (position == Globs.currentTrackNumber) return;

                Globs.currentSongs.remove(position);
                adapter.notifyDataSetChanged();

                Globs.currentTrackNumber = findSong(currentTitle);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(queueRecView);
    }

    public void chooseTrack(String title) {
        if (title == null) return;

        int songIndex = findSong(title);

        if (Globs.currentTrackNumber == songIndex) return;

        Globs.currentTrackNumber = songIndex;

        Player.selectTrack(songIndex);
    }

    private int findSong(String title) {
        int songIndex = 0;
        for (int i = 0; i < Globs.currentSongs.size(); i++)
            if (Objects.equals(Globs.currentSongs.get(i).getTitle(), title)) songIndex = i;

        return songIndex;
    }

    public void backToPlayer() {
        mainActivity.onBackPressed();
    }
}
