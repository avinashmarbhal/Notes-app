package com.avinash.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class createNote extends AppCompatActivity {
     EditText mcreatetittlenode,mcreatecontentofnote;
     FloatingActionButton msavenote;
     FirebaseAuth firebaseAuth;
     FirebaseUser firebaseUser;
     FirebaseFirestore firebaseFirestore;
     ProgressBar mprogressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        msavenote = findViewById(R.id.saveNote);
        mcreatetittlenode = findViewById(R.id.createtittlenode);
        mcreatecontentofnote = findViewById(R.id.createcontentofnote);
        Toolbar toolbar = findViewById(R.id.toolbarofcreatenote);
        mprogressbar = findViewById(R.id.progressbarofcreatenote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        msavenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tittle = mcreatetittlenode.getText().toString();
                String content= mcreatecontentofnote.getText().toString();
                if (tittle.isEmpty() || content.isEmpty()){

                    Toast.makeText(getApplicationContext(), "Both filed are Required", Toast.LENGTH_SHORT).show();
                }else {

                    mprogressbar.setVisibility(View.VISIBLE);
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNote").document();
                    Map<String,Object> note= new HashMap<>();
                    note.put("tittle",tittle);
                    note.put("content",content);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Note created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(createNote.this,notesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mprogressbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Failed to create note", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(createNote.this,notesActivity.class));
                        }
                    });




                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home){

            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}