package com.example.skyreachadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

public class Achievements extends AppCompatActivity {
    StorageReference st;
    DatabaseReference db;
    InputStream inputStream = null;
    int image_selected_code = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        st = FirebaseStorage.getInstance().getReference();
        db = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.editbtn).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent,image_selected_code);
                    }
                }
        );
        findViewById(R.id.uploaditem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateinput();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==image_selected_code){
            if(resultCode==RESULT_OK){
                Uri uri=data.getData();
                try {
                    inputStream=(InputStream)getApplicationContext().getContentResolver().openInputStream(uri);
                    ImageView imageView=findViewById(R.id.itemimage);
                    Bitmap bitmap=BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void validateinput() {
        final String desc=((TextView)findViewById(R.id.desc)).getText().toString();
        if(!desc.isEmpty()){
            if(inputStream!=null){
                new AlertDialog.Builder(this)
                        .setTitle("Add Photo")
                        .setMessage("Are you sure you want to add this picture")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadimg(desc);
                                ((ProgressBar)findViewById(R.id.progcircle)).setVisibility(View.VISIBLE);
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
    }

    private void uploadimg(final String desc) {
        if(inputStream!=null){
            ImageView imageView=findViewById(R.id.itemimage);
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            st=st.child("Achievements").child(UUID.randomUUID().toString()+".jpg");
            st.putBytes(data).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  st.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                      @Override
                      public void onSuccess(Uri uri) {
                          HashMap<String,String> hashMap=new HashMap<>();
                          hashMap.put("url",uri.toString());
                          hashMap.put("desc",desc);
                          db.child("Admin").child("Achievements").child(UUID.randomUUID().toString()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void aVoid) {
                                  Toast.makeText(getApplicationContext(),"Picture Added successfully",Toast.LENGTH_SHORT).show();
                                  ((ProgressBar)findViewById(R.id.progcircle)).setVisibility(View.GONE);
                              }
                          });
                      }
                  });
                }
            });

        }
    }
}
