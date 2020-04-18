package com.example.skyreachadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;

public class AttandanceSheet extends AppCompatActivity {
    DatabaseReference database;
    ArrayList<attandance> list;
    TextView t;
    String currentref = "";
    TableLayout tablesheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attandance_sheet);
        currentref = Objects.requireNonNull(getIntent().getExtras()).getString("ref");
        list = new ArrayList();
        tablesheet= findViewById(R.id.tablesheet);
        tablesheet.setStretchAllColumns(true);
        database = FirebaseDatabase.getInstance().getReference();
        Log.d("dataref",currentref);
        database.child("Admin").child("Attendance").child(currentref).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                        database.child("users").child(ds.getKey().toString()).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot data) {
                                        if (data.exists()) {
                                            attandance d = new attandance();
                                            d.setEmail(data.child("email").getValue().toString());
                                            d.setName(data.child("username").getValue().toString());
                                            d.setPresent(ds.getValue().toString());
                                            d.setUid(data.getKey().toString());
                                            list.add(d);
                                            Log.d("data", d.getName());
                                            update();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                }
                        );
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void intial() {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp =
                new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        row.setBackgroundResource(R.drawable.border);
        row.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView qtv = getdesignText("Name", Color.WHITE, Color.GRAY);
        row.addView(qtv);
        qtv = getdesignText("Email", Color.WHITE, Color.GRAY);
        row.addView(qtv);
        qtv = getdesignText("Mark", Color.WHITE, Color.GRAY);
        row.addView(qtv);
        tablesheet.addView(row);
    }

    public void update() {
        tablesheet.removeAllViews();
        intial();
        for (attandance data:list) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp =
                    new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            row.setBackgroundResource(R.drawable.border);
            row.setGravity(Gravity.CENTER_HORIZONTAL);
            TextView qty =getdesignText(data.name,Color.BLACK,Color.WHITE);
            row.addView(qty);
            qty =getdesignText(data.email,Color.BLACK,Color.WHITE);
            qty.setText(data.email);
            row.addView(qty);
            qty = getdesignText(data.present,Color.BLACK,Color.WHITE);
            row.addView(qty);
            tablesheet.addView(row);
        }
    }

    TextView getdesignText(String text, int color, int bgcolor) {
        TextView qtv = new TextView(this);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(bgcolor);
//        gd.cornerRadius = 5f
        gd.setStroke(1, Color.BLACK);
        qtv.setText(text);
        qtv.setTextColor(color);
        qtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        qtv.setPadding(10, 10, 10, 20);
        qtv.setBackground(gd);
        TableRow.LayoutParams lp =
                new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        qtv.setLayoutParams(lp);
        return qtv;
    }
}

