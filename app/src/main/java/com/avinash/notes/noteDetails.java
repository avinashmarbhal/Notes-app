package com.avinash.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class noteDetails extends AppCompatActivity {
    private TextView mtitleofnotedetial,mcontentofnotedetail;
    FloatingActionButton mgotoeditnote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        mtitleofnotedetial = findViewById(R.id.tittlenotedetails);
        mcontentofnotedetail = findViewById(R.id.contentofnotedetails);
        mgotoeditnote = findViewById(R.id.gotoeditnote);
        Toolbar toolbar = findViewById(R.id.toolbarofnotedetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();

        mgotoeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),editnoteactivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteId",data.getStringExtra("noteId"));
                view.getContext().startActivity(intent);

            }
        });

        mcontentofnotedetail.setText(data.getStringExtra("content"));
        mtitleofnotedetial.setText(data.getStringExtra("title"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home){

            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}