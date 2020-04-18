package com.example.skyreachadmin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

public class Notes extends AppCompatActivity {
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        db = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.puploaditem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatelink();
            }
        });
    }

    private void validatelink() {
        final String linkdesk=((TextView)findViewById(R.id.linkdesc)).getText().toString();
        final String link=((TextView)findViewById(R.id.link)).getText().toString();
        final String classlink=((TextView)findViewById(R.id.linkclass)).getText().toString();
        if(!linkdesk.isEmpty() && !link.isEmpty() && !classlink.isEmpty()){
            new AlertDialog.Builder(this)
                    .setTitle("Add Photo")
                    .setMessage("Are you sure you want to add this picture")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            uploaddata(linkdesk,link,classlink);
                            ((ProgressBar)findViewById(R.id.progcirclepackage)).setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            })
                    .create()
                    .show();
        }
    }

    private void uploaddata( String linkdesk, String link,String linkclass) {
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("link",link);
        hashMap.put("head",linkdesk);
        db.child("Admin").child("Links").child(linkclass)
                .child(UUID.randomUUID().toString()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"link Added successfully",Toast.LENGTH_SHORT).show();
                ((ProgressBar)findViewById(R.id.progcirclepackage)).setVisibility(View.GONE);
            }
        });
    }
}
