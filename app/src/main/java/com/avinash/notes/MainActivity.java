package com.avinash.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mloginemail,mloginpassword;
    private Button mlogin,mgotosignup;
    private TextView mgotoforgotpassword;
    ProgressBar mprogressBar;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        mloginemail = findViewById(R.id.loginEmail);
        mloginpassword = findViewById(R.id.loginPassword);
        mlogin = findViewById(R.id.login);
        mgotosignup = findViewById(R.id.goToSignUp);
        mgotoforgotpassword = findViewById(R.id.gotoforgotPASSWORD);
        mprogressBar = findViewById(R.id.progressbarofmainactivity);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser!= null){
            finish();
            startActivity(new Intent(MainActivity.this,notesActivity.class));
        }

        mgotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,signup.class);
                startActivity(intent);
            }
        });

        mgotoforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,forgetPassword.class);
                startActivity(intent);

            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mloginemail.getText().toString().trim();
                String password = mloginpassword.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty()){

                    Toast.makeText(getApplicationContext(),"All Fields are Required",Toast.LENGTH_SHORT).show();
                }else {

                    //loin user
                    mprogressBar.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                checkMailVerification();
                            }else {
                                Toast.makeText(getApplicationContext(),"Wrong email or password ",Toast.LENGTH_SHORT).show();
                                mprogressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });


                }
            }
        });
    }

    private void checkMailVerification(){

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser.isEmailVerified()==true){

            Toast.makeText(getApplicationContext(),"Logged In",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,notesActivity.class));

        }else {

            mprogressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"Verify your Mail first!",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();

        }

    }
}