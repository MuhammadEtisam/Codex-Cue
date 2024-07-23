package com.example.notetakingapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.lifecycle.Observer;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });

        FloatingActionButton buttonAddNote = findViewById(R.id.addNoteButton);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddNoteDialog();
            }
        });

        adapter.setOnItemLongClickListener(new NoteAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(final Note note) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> noteViewModel.delete(note))
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
    }

    private void openAddNoteDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextNote = dialogView.findViewById(R.id.edit_text_note);
        Button buttonSave = dialogView.findViewById(R.id.button_save);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteText = editTextNote.getText().toString();
                if (!noteText.trim().isEmpty()) {
                    Note note = new Note(noteText);
                    noteViewModel.insert(note);
                    alertDialog.dismiss();
                }
            }
        });
    }
}
