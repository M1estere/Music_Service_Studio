package com.example.music_service.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_service.models.FavouriteMusic;
import com.example.music_service.viewModels.QueueActivityViewModel;
import com.example.music_service.R;
import com.example.music_service.models.Player;
import com.example.music_service.models.Song;
import com.example.music_service.models.globals.Globs;
import com.example.music_service.models.globals.SongsProps;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class QueueRecViewAdapter extends RecyclerView.Adapter<QueueRecViewAdapter.ViewHolder> {

    private final Context context;
    private final QueueActivityViewModel queueActivityViewModel;

    private ArrayList<Song> songs = new ArrayList<>();

    public QueueRecViewAdapter(Context context, QueueActivityViewModel viewModel) {
        this.context = context;
        queueActivityViewModel = viewModel;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == songs.size()) ? R.layout.hold_to_move_text : R.layout.queue_item;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == R.layout.queue_item) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hold_to_move_text, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.trackNameTxt == null) return;
        int pos = holder.getAdapterPosition();

        holder.trackNameTxt.setText(songs.get(pos).getTitle());
        holder.authorNameTxt.setText(songs.get(pos).getArtist());
        holder.infoButton.setTag(holder.trackNameTxt.getText().toString());

        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSongInfo(view);
            }
        });

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String songName = holder.trackNameTxt.getText().toString();
                Toast.makeText(context, "(Queue) " + songName + " selected", Toast.LENGTH_SHORT).show();
                queueActivityViewModel.chooseTrack(songName);
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

        CardView favButton = bottomSheetView.findViewById(R.id.fav_button);

        title.setText(song.getTitle());
        artist.setText(song.getArtist());


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queueActivityViewModel.chooseTrack(title.getText().toString());

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
                FavouriteMusic.addToFavourites(title.getText().toString(), (Activity) context);
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    @Override
    public int getItemCount() {
        return songs.size() + 1;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView trackNameTxt;
        private final TextView authorNameTxt;

        private final Button infoButton;
        private final CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trackNameTxt = itemView.findViewById(R.id.track_title_txt);
            authorNameTxt = itemView.findViewById(R.id.author_txt);

            infoButton = itemView.findViewById(R.id.info_button);

            parent = itemView.findViewById(R.id.parent);

            if (trackNameTxt == null) return;

            trackNameTxt.setSelected(true);
            authorNameTxt.setSelected(true);
        }
    }
}
