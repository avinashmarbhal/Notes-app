package com.avinash.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity {

    private EditText msignupEmail,msignuppassword;
    private Button msignup;
    private TextView mgotologin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();

        msignupEmail = findViewById(R.id.signupEmail);
        msignuppassword = findViewById(R.id.signUpPassword);
        msignup = findViewById(R.id.signup);
        mgotologin = findViewById(R.id.goToLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        mgotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this,MainActivity.class);
                startActivity(intent);
            }
        });

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = msignupEmail.getText().toString().trim();
                String password = msignuppassword.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty()){

                    Toast.makeText(getApplicationContext(),"All Fields are Required",Toast.LENGTH_SHORT).show();
                }else if (password.length()<7){

                    Toast.makeText(getApplicationContext(),"Password is to short",Toast.LENGTH_SHORT).show();
                }else {

                    //register to firebase

                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Registered Successful",Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }else {

                                Toast.makeText(getApplicationContext(),"Failed to Register",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }

    //send verification mail

    private  void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Verification email send Successful, verify your email",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(signup.this,MainActivity.class));
                }
            });
        }else {
            Toast.makeText(getApplicationContext()," Failed to send Verification email ",Toast.LENGTH_SHORT).show();

        }
    }
}