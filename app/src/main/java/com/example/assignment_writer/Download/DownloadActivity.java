package com.example.assignment_writer.Download;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment_writer.History.ModelAdapter;
import com.example.assignment_writer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DownloadActivity extends Fragment {

    RecyclerView recycler;
    List<DownloadModel> models;
    DatabaseReference reference;
    DownloadAdapter adapter;
    ProgressBar progressBar;
    TextView textView;
    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_download_activity, container, false);

        models=new ArrayList<>();

        sharedPreferences= getContext().getSharedPreferences("MyPrefs",MODE_PRIVATE);

        String key=sharedPreferences.getString("profileKey",null);

        recycler=view.findViewById(R.id.download_recycler);
        progressBar=view.findViewById(R.id.progress);
        textView=view.findViewById(R.id.text_view);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        if(key!=null) {

            reference = FirebaseDatabase.getInstance().getReference("Users").child(key).child("download");
            AddedDownloadDatabase();
        }
        else {
            textView.setVisibility(View.VISIBLE);
        }
        return view;
    }
    private void AddedDownloadDatabase() {

        progressBar.setVisibility(View.VISIBLE);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                models.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    DownloadModel item = dataSnapshot.getValue(DownloadModel.class);
                    if (item != null) {
                        item.setKey(dataSnapshot.getKey());
                        models.add(item);
                    }
                }
                if (models.size() == 0) {
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    recycler.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    recycler.setVisibility(View.VISIBLE);
                    adapter = new DownloadAdapter(getContext(),models);
                    recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "network not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

}