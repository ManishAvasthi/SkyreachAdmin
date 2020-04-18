package com.example.skyreachadmin;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AttandanceAdapter extends RecyclerView.Adapter<AttandanceHolder> {
    Context context=null;
    ArrayList<String> list=null;
    public AttandanceAdapter(Context context, ArrayList<String> list){
        this.context=context;
        this.list=list;
    }
    public void update(ArrayList<String> list)
    {
        this.list=list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override

    public AttandanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rowview,parent,false);
        return new AttandanceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttandanceHolder holder, final int position) {
        holder.t.setText(list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,AttandanceSheet.class);
                i.putExtra("ref",list.get(position));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class AttandanceHolder extends RecyclerView.ViewHolder {
    TextView t=null;
    public AttandanceHolder(View itemView){
        super(itemView);
        t=itemView.findViewById(R.id.attenname);
    }
}

class attandance{
    String email="";
    String uid="";
    String name="";
    String present="";

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }
}