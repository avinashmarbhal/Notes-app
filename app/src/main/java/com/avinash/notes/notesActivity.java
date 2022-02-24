package com.avinash.notes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class notesActivity extends AppCompatActivity {

    FloatingActionButton mceatenotefab;
    FirebaseAuth firebaseAuthl;
    RecyclerView mrecyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<firebasemodel,NoteViewHolder> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        mceatenotefab = findViewById(R.id.createNoteFab);
        firebaseAuthl = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle("Notes");

        mceatenotefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(notesActivity.this,createNote.class));
            }
        });

        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNote").orderBy("tittle",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<firebasemodel> allusernotes = new  FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusernotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebasemodel model) {

                ImageView popupbutton = holder.itemView.findViewById(R.id.menupopbutton);

                holder.notetitle.setText(model.getTittle());
                holder.notecontent.setText(model.getContent());

                String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(view.getContext(),noteDetails.class);
                        intent.putExtra("title",model.getTittle());
                        intent.putExtra("content",model.getContent());
                        intent.putExtra("noteId",docId);
                        startActivity(intent);
                        //note detail
                       // Toast.makeText(getApplicationContext(), "noteDetails", Toast.LENGTH_SHORT).show();
                    }
                });

                popupbutton.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                Intent intent = new Intent(view.getContext(),editnoteactivity.class);
                                intent.putExtra("title",model.getTittle());
                                intent.putExtra("content",model.getContent());
                                intent.putExtra("noteId",docId);
                                startActivity(intent);
                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNote").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(view.getContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(view.getContext(), "Failed To Delete", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                return false;
                            }
                        });

                        popupMenu.show();


                    }
                });


            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
              View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
              return new NoteViewHolder(view);
            }
        };

        mrecyclerView = findViewById(R.id.recyclerview);
        mrecyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager= new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerView.setAdapter(noteAdapter);
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder
    {
        private TextView notetitle;
        private TextView notecontent;
        LinearLayout mnote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            notetitle = itemView.findViewById(R.id.noteTittle);
            notecontent = itemView.findViewById(R.id.notecontent);
            mnote = itemView.findViewById(R.id.note);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                firebaseAuthl.signOut();
                finish();
                startActivity(new Intent(notesActivity.this,MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter != null){
            noteAdapter.stopListening();
        }
    }
}