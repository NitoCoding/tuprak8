package com.example.tuprak8;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private Context context;
    private List<Note> noteList;

    public NotesAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.noteTitle.setText(note.getTitle());
        holder.noteDescription.setText(note.getDescription());

        if (note.getUpdatedAt() != null && !note.getUpdatedAt().isEmpty() && !note.getUpdatedAt().equals(note.getCreatedAt())) {
            holder.noteDate.setText("Updated at " + note.getUpdatedAt());
        } else {
            holder.noteDate.setText("Created at " + note.getCreatedAt());
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddNoteActivity.class);
            intent.putExtra("noteId", note.getId());
//            Log.e("noteid", String.valueOf(note.getId()));
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void filterList(List<Note> filteredList) {
        noteList = filteredList;
        notifyDataSetChanged();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteTitle;
        TextView noteDescription;
        TextView noteDate;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteDescription = itemView.findViewById(R.id.noteDescription);
            noteDate = itemView.findViewById(R.id.noteDate);
        }
    }
}
