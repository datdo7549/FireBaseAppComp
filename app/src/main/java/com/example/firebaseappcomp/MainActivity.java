package com.example.firebaseappcomp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

import static com.example.firebaseappcomp.LoginActivity.LOG_STATUS;
import static com.example.firebaseappcomp.LoginActivity.UIID;

public class MainActivity extends AppCompatActivity {
    public static int count=0;
    private Button logout;
    private Button add;
    private EditText name;
    private EditText age;
    private EditText uid;
    private Button show;
    private TextView nameShow;
    private TextView ageShow;
    private Firebase mRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRoot=new Firebase("https://fir-appcomp.firebaseio.com/").child("user");
        logout=findViewById(R.id.logout);
        add=findViewById(R.id.add);
        name=findViewById(R.id.name);
        age=findViewById(R.id.age);
        uid=findViewById(R.id.uid);
        show=findViewById(R.id.show);
        nameShow=findViewById(R.id.nameView);
        ageShow=findViewById(R.id.ageView);
        final FirebaseUser user123=FirebaseAuth.getInstance().getCurrentUser();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LOG_STATUS=2;
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mName=name.getText().toString();
                String mAge=age.getText().toString();
                Firebase childUser=mRoot.child(user123.getUid());
                Firebase childName=childUser.child("Name");
                childName.setValue(mName);
                Firebase childAge=childUser.child("Age");
                childAge.setValue(mAge);
                count++;
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UID=uid.getText().toString();
                Firebase childShow=mRoot.child(UID);
                childShow.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String,String> map=dataSnapshot.getValue(Map.class);
                        nameShow.setText(map.get("Name"));
                        ageShow.setText(map.get("Age"));
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });
    }
}