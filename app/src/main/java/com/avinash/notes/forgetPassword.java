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
import com.google.firebase.auth.FirebaseAuth;

public class forgetPassword extends AppCompatActivity {

    private EditText mForgotPassword;
    private Button mPasswordRecoverButton;
    private TextView mGoBackToLogin;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().hide();

        mForgotPassword = findViewById(R.id.forgetPassword);
        mPasswordRecoverButton = findViewById(R.id.passwordRecoverButton);
        mGoBackToLogin = findViewById(R.id.goToLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        mGoBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(forgetPassword.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mPasswordRecoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mForgotPassword.getText().toString().trim();
                if (mail.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter your mail ",Toast.LENGTH_SHORT).show();
                }else {

                    //send mail
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful()){
                             Toast.makeText(getApplicationContext(),"Password reset link sent to your Mail",Toast.LENGTH_SHORT).show();
                             finish();
                             startActivity(new Intent(forgetPassword.this,MainActivity.class));
                         }else{
                             Toast.makeText(getApplicationContext(),"Enter Valid Email!",Toast.LENGTH_SHORT).show();

                         }
                        }
                    });
                }
            }
        });
    }
}