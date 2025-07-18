package com.example.assignment_writer.Profile;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment_writer.Login.SignUp;
import com.example.assignment_writer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProfileActivity extends Fragment {

    ImageView imageView;
    TextView profileName,userGmail;
    ImageView changeProfile;
    Boolean permission;
    CardView edit_profile;
    LinearLayout LogOut,subscription,download,history;
    private  String key="";
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_profile_activity, container, false);

        sharedPreferences= getActivity().getSharedPreferences("MyPrefs",MODE_PRIVATE);

        storageReference = FirebaseStorage.getInstance().getReference("ProfileImages");

        if(key!=null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(key);
        }

        FindId(view);
        setProfileDatabase();
        UpdateProfile();
        SignOut();
        setSubscription();
        setEdit_profile();
        return view;
    }
    private void FindId(View view) {
        imageView=view.findViewById(R.id.profileImage);
        profileName=view.findViewById(R.id.profileName);
        changeProfile=view.findViewById(R.id.updateProfile);
        LogOut=view.findViewById(R.id.logOut);
        userGmail=view.findViewById(R.id.gmail);
        subscription=view.findViewById(R.id.subscription);
        download=view.findViewById(R.id.download);
        history=view.findViewById(R.id.history);
        edit_profile=view.findViewById(R.id.edit_profile);
    }
    private void setProfileDatabase(){

        key=sharedPreferences.getString("profileKey",null);
        permission=sharedPreferences.getBoolean("profilePermission",false);

        storageReference = FirebaseStorage.getInstance().getReference("ProfileImages");

        if(key!=null){
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(key);

        }

        if(permission) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(key);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String name=snapshot.child("fullName").getValue(String.class);
                    String imageUrl=snapshot.child("Photos").getValue(String.class);
                    String gmail=snapshot.child("email").getValue(String.class);
                    profileName.setText(name);
                    userGmail.setText(gmail);
                    Picasso.get().load(imageUrl).into(imageView);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Load Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void UpdateProfile() {
        changeProfile.setOnClickListener(v -> {
            openFileChooser();
        });
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);

            if (imageUri != null && permission == true) {
                StorageReference fileRef = storageReference.child(key + ".jpg");

                fileRef.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();

                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("Photos", imageUrl);

                                    databaseReference.updateChildren(map)
                                            .addOnSuccessListener(aVoid ->
                                                    Toast.makeText(getContext(), "Photo URL Updated", Toast.LENGTH_SHORT).show()
                                            );
                                }))
                        .addOnFailureListener(e ->
                                Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
            }
        }
    }
    private void SignOut(){

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CardView cardView,cardView2;

                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.logout_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();

                cardView=dialogView.findViewById(R.id.cardview);
                cardView2=dialogView.findViewById(R.id.cardview2);

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                cardView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("profileKey",null);
                        editor.putBoolean("profilePermission", false);
                        editor.apply();
                        dialog.dismiss();
                    }
                });


            }});
    }
    private void setSubscription(){
        subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "coming soon subscription", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setEdit_profile() {

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getContext(), Update_Profile.class);
                intent.putExtra("profileKey",key);
                startActivity(intent);
            }
        });
    }
}