package com.example.assignment_writer.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.assignment_writer.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Update_Profile extends AppCompatActivity {
    TextInputEditText user_email,user_name,user_phone;
    CardView cancel,save;
    DatabaseReference reference;
    boolean permission;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_profile);


        preferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);

        Intent intent=getIntent();
        String key=intent.getStringExtra("profileKey");


        if(key!=null) {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(key);
        }
        FindId();
        setClick();
    }
    private void FindId(){
        cancel=findViewById(R.id.cancel);
        save=findViewById(R.id.save);
        user_email=findViewById(R.id.user_email);
        user_name=findViewById(R.id.user_name);
        user_phone=findViewById(R.id.user_phone);
    }
    private void setClick() {

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permission=preferences.getBoolean("profilePermission",false);
                if(!permission){

                    Toast.makeText(Update_Profile.this, "Please SignIn Your Profile", Toast.LENGTH_SHORT).show();
                }
                else{
                    setUpdateProfile();
                    onBackPressed();
                }
            }
        });
    }
    private void setUpdateProfile(){

        String gmail=user_email.getText().toString();
        String name=user_name.getText().toString();
        String phone=user_phone.getText().toString();

        if(gmail.isEmpty()  || name.isEmpty() || phone.isEmpty()){
            Toast.makeText(this, "Write Text", Toast.LENGTH_SHORT).show();
        }
        else{

            Map<String, Object> updates = new HashMap<>();
            updates.put("userName", name);
            updates.put("email",gmail);
            updates.put("phone",phone);

            reference.updateChildren(updates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(Update_Profile.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Update_Profile.this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}