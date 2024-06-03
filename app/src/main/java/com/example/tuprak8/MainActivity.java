package com.example.tuprak8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewV;
    SearchView searchViewV;
    DatabaseHelper db;
    NotesAdapter notesAdapter;

    List<Note> notes;

    TextView textViewV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewV = findViewById(R.id.recyclerView);
        recyclerViewV.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);

        textViewV = findViewById(R.id.noDataTextView);
        searchViewV = findViewById(R.id.searchView);
        searchViewV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchNote(newText);
                return true;
            }
        });


        loadData();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> startActivity(new Intent(this,AddNoteActivity.class) ));
    }

    private void loadData(){
        notes = db.getAllNotes();
        if(notes.isEmpty()){
            recyclerViewV.setVisibility(View.GONE);
            textViewV.setVisibility(View.VISIBLE);
        }else{
            recyclerViewV.setVisibility(View.VISIBLE);
            textViewV.setVisibility(View.GONE);
            notesAdapter = new NotesAdapter(this,notes);
            recyclerViewV.setAdapter(notesAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void searchNote(String query){
        if(!TextUtils.isEmpty(query)){
            List<Note> filterNotes = new ArrayList<>();
            for (Note note : notes){
                if (note.getTitle().toLowerCase().contains(query)){
                    filterNotes.add(note);
                }
            }
            if(filterNotes.isEmpty()){
                recyclerViewV.setVisibility(View.GONE);
                textViewV.setVisibility(View.VISIBLE);
            }else{
                recyclerViewV.setVisibility(View.VISIBLE);
                textViewV.setVisibility(View.GONE);

            }
            notesAdapter.filterList(filterNotes);

        }else{
            recyclerViewV.setVisibility(View.VISIBLE);
            textViewV.setVisibility(View.GONE);
            notesAdapter.filterList(notes);
        }

    }
}