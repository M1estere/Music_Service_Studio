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
    private QueueActivityViewModel queueActivityViewModel;

    private ArrayList<Song> songs = new ArrayList<>();

    public QueueRecViewAdapter(Context context, QueueActivityViewModel viewModel) {
        this.context = context;
        queueActivityViewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.trackNameTxt.setText(songs.get(position).getTitle());
        holder.authorNameTxt.setText(songs.get(position).getArtist());
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
                queueActivityViewModel.chooseTrack(holder.trackNameTxt.getText().toString());
            }
        });
    }

    public void openSongInfo(View view) {
        String text = view.getTag().toString();
        Song song = SongsProps.getSongByName(text);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                ((Activity)context), R.style.BottomSheetDialogTheme
        );

        View bottomSheetView = LayoutInflater.from(((Activity)context).getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_sheet,
                        (RelativeLayout) ((Activity)context).findViewById(R.id.bottom_sheet_container)
                );

        TextView title = bottomSheetView.findViewById(R.id.title_song);
        title.setText(song.getTitle());

        TextView artist = bottomSheetView.findViewById(R.id.author_song);
        artist.setText(song.getArtist());

        Button playButton = bottomSheetView.findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queueActivityViewModel.chooseTrack(title.getText().toString());

                bottomSheetDialog.dismiss();
            }
        });

        Button playNextButton = bottomSheetView.findViewById(R.id.queue_next_button);
        playNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.addNextToQueue(title.getText().toString());

                bottomSheetDialog.dismiss();
            }
        });

        Button playLastButton = bottomSheetView.findViewById(R.id.queue_end_button);
        playLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.addToQueueEnd(title.getText().toString());

                bottomSheetDialog.dismiss();
            }
        });

        Button removeButton = bottomSheetView.findViewById(R.id.remove_button);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.deleteFromQueue(title.getText().toString());

                bottomSheetDialog.dismiss();
            }
        });

        CardView favButton = bottomSheetView.findViewById(R.id.fav_button);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, title.getText().toString() + " added to favs", Toast.LENGTH_SHORT).show();
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
//        private final TextView durationTxt;

        private final Button infoButton;
        private final CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trackNameTxt = itemView.findViewById(R.id.track_title_txt);
            authorNameTxt = itemView.findViewById(R.id.author_txt);
//            durationTxt = itemView.findViewById(R.id.track_length);

            infoButton = itemView.findViewById(R.id.info_button);

            parent = itemView.findViewById(R.id.parent);

            trackNameTxt.setSelected(true);
            authorNameTxt.setSelected(true);
        }
    }
}
