package com.example.skyreachadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class LoginActivity extends AppCompatActivity {
    private TextView fp;
    private Button signin;
    EditText txtEmail,txtPassword;
    ProgressBar progressbar;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth= FirebaseAuth.getInstance();
        firebaseUser= mAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            Intent i=new Intent(this,HomeActivity.class);
            startActivity(i);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fp=(TextView)findViewById(R.id.txtforgot);
        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,ForgetActivityAdmin.class);
                startActivity(i);
                finish();
            }
        });

        progressbar=(ProgressBar)findViewById(R.id.progressbar);
        txtEmail=(EditText)findViewById(R.id.email);
        txtPassword=(EditText)findViewById(R.id.pass);
        signin=(Button)findViewById(R.id.login_in);
        mAuth=FirebaseAuth.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                final String email=txtEmail.getText().toString().trim();
                final String pass=txtPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(LoginActivity.this,"Please Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(pass))
                {
                    Toast.makeText(LoginActivity.this,"Please Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.length()<6)
                {
                    Toast.makeText(LoginActivity.this,"Incorrect Email and Password",Toast.LENGTH_LONG).show();

                }

                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressbar.setVisibility(View.GONE);
                                if (task.isSuccessful())
                                {
                                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                    finish();

                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this,"Incorrect Email and Password",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                finish();
                            }
                        });

            }

        });

    }
}
