package com.example.tuprak8;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

public class AddNoteActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText;
    private Button submitButton;
    private DatabaseHelper db;
    private int noteId = -1;

//    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        db = new DatabaseHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);

        if(!getIntent().hasExtra("noteId")){
            getSupportActionBar().setTitle("Create Note");
        }else{
            noteId = getIntent().getIntExtra("noteId",-1);
            loadNote(noteId);
            getSupportActionBar().setTitle("Update Note");
        }

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String desc = descriptionEditText.getText().toString();

            if (title.isEmpty()){
                titleEditText.setError("Field tidak boleh kosong");
            }else{
                if(noteId == -1){
                    db.insertNote(title,desc);
                    Toast.makeText(this, "Note created", Toast.LENGTH_SHORT).show();
                }else{
                    Note note = db.getNote(noteId);
                    note.setTitle(title);
                    note.setDescription(desc);
                    db.updateNote(note);
                    Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();

                }
                finish();
            }
        });


    }

    private void loadNote(int noteId){
        Note note = db.getNote(noteId);
        titleEditText.setText(note.getTitle());
        descriptionEditText.setText(note.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_update_delete, menu);
        if (noteId == -1) {
            MenuItem deleteItem = menu.findItem(R.id.action_delete);
            deleteItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            showCancelConfirmationDialog();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            showDeleteConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        showCancelConfirmationDialog();
    }

    private void showCancelConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah anda ingin membatalkan penambahan atau perubahan pada form?")
                .setPositiveButton("Ya", (dialog, id) -> finish())
                .setNegativeButton("Tidak", (dialog, id) -> {
                    // User cancelled the dialog
                });
        builder.create().show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah anda yakin ingin menghapusnya?")
                .setPositiveButton("Ya", (dialog, id) -> {
                    db.deleteNote(noteId);
                    Toast.makeText(AddNoteActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Tidak", (dialog, id) -> {
                    // User cancelled the dialog
                });
        builder.create().show();
    }
}