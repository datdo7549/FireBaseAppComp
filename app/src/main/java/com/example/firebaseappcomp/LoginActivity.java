package com.example.firebaseappcomp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static int LOG_STATUS=1;
    private EditText email;
    private EditText password;
    private EditText password_confirm;
    private TextView register;
    private Button login;
    private Button register_button;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;
    public static String UIID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Mapping();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null && LOG_STATUS==1) {
                    UIID=mAuth.getUid();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }
    public void Mapping()
    {
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        password_confirm=findViewById(R.id.password_confirm);
        register=findViewById(R.id.register);
        login=findViewById(R.id.login);
        register_button=findViewById(R.id.register_button);
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(LoginActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.login:
            {
                String mEmail=email.getText().toString();
                String mPassword=password.getText().toString();
                progressDialog.setTitle("Login Status");
                progressDialog.setMessage("Logging...");
                progressDialog.show();
                if(TextUtils.isEmpty(mEmail)||TextUtils.isEmpty(mPassword))
                {
                    Toast.makeText(LoginActivity.this,"Fields are empty ",Toast.LENGTH_SHORT).show();
                }
                else {
                   mAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if(!task.isSuccessful())
                           {
                               progressDialog.dismiss();
                           }
                       }
                   });
                }
                break;
            }
            case R.id.register:
            {
                password_confirm.setVisibility(View.VISIBLE);
                register_button.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);
                register.setVisibility(View.GONE);
                register_button.setOnClickListener(this);
                break;
            }
            case R.id.register_button:
            {
                String mEmailReg=email.getText().toString();
                String mPasswordReg=password.getText().toString();
                String mPasswordCon=password_confirm.getText().toString();
                if (TextUtils.isEmpty(mEmailReg) || TextUtils.isEmpty(mPasswordReg) || TextUtils.isEmpty(mPasswordCon) || !TextUtils.equals(mPasswordReg,mPasswordCon))
                {
                    Toast.makeText(LoginActivity.this,"Fields are empty",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mAuth.createUserWithEmailAndPassword(mEmailReg,mPasswordReg).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this,"Register Complete",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"Register Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("A","onStart");
        mAuth.addAuthStateListener(mAuthListener);

    }
}
