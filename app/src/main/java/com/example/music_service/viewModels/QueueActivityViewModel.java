package com.example.music_service.viewModels;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.R;
import com.example.music_service.adapters.QueueRecViewAdapter;
import com.example.music_service.models.FavouriteMusic;
import com.example.music_service.models.Player;
import com.example.music_service.models.globals.Globs;

import java.util.Collections;
import java.util.Objects;

public class QueueActivityViewModel extends BaseObservable {

    private final Activity mainActivity;
    private final QueueRecViewAdapter adapter;

    public QueueActivityViewModel(@NonNull Activity activity) {
        Player.setQueueActivityViewModel(this);

        mainActivity = activity;

        RecyclerView queueRecView = activity.findViewById(R.id.queue_rec_view);

        adapter = new QueueRecViewAdapter(activity, this);
        adapter.setSongs(Globs.currentSongs);

        queueRecView.setAdapter(adapter);
        queueRecView.setLayoutManager(new LinearLayoutManager(activity));

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();

                int toPosition = target.getAdapterPosition();

                String currentTitle = Globs.getTitles().get(Globs.currentTrackNumber);
                if (toPosition >= Globs.currentSongs.size()) return false;
                Collections.swap(Globs.currentSongs, fromPosition, toPosition);
                adapter.notifyItemMoved(fromPosition, toPosition);

                Globs.currentTrackNumber = findSong(currentTitle);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                String currentTitle = Globs.getTitles().get(Globs.currentTrackNumber);

                int position = viewHolder.getAdapterPosition();
                if (position >= Globs.currentTrackNumber) return;

                Globs.currentSongs.remove(position);
                adapter.notifyDataSetChanged();

                Globs.currentTrackNumber = findSong(currentTitle);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(queueRecView);
    }

    public void updateQueue() {
        adapter.notifyDataSetChanged();
    }

    public void addToFavourites(String title) {
        if (title == null) return;

        if (!FavouriteMusic.trackInFavourites(title))
            FavouriteMusic.addToFavourites(title, mainActivity);
        else
            FavouriteMusic.removeFromFavourites(title, mainActivity);
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
