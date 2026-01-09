package com.example.mynotez.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mynotez.R;
import com.example.mynotez.data.Note;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {
    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    private final OnNoteClickListener listener;

    public NoteAdapter(OnNoteClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getContent().equals(newItem.getContent()) &&
                    oldItem.getTimestamp() == newItem.getTimestamp();
        }
    };

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = getItem(position);
        holder.bind(note);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvContent;
        private final TextView tvDate;
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvDate = itemView.findViewById(R.id.tvDate);
            itemView.setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onNoteClick(getItem(pos));
                }
            });
        }

        void bind(Note note) {
            tvTitle.setText(note.getTitle());
            tvContent.setText(note.getContent());
            tvDate.setText(dateFormat.format(new Date(note.getTimestamp())));
        }
    }
}
