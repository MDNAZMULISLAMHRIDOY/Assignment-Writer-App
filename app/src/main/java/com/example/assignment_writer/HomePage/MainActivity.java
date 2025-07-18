package com.example.assignment_writer.HomePage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment_writer.Download.DownloadActivity;
import com.example.assignment_writer.History.HistoryActivity;
import com.example.assignment_writer.Profile.ProfileActivity;
import com.example.assignment_writer.R;

public class MainActivity extends AppCompatActivity {

    private ImageView home,download,history,account;
    HomePage homePage=new HomePage();
    ProfileActivity profileActivity=new ProfileActivity();
    DownloadActivity downloadActivity=new DownloadActivity();
   HistoryActivity historyActivity=new HistoryActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        home=findViewById(R.id.home);
        download=findViewById(R.id.download);
        history=findViewById(R.id.history);
        account=findViewById(R.id.account);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,homePage).commit();

        setHomeActivity();
        setDownloadActivity();
        setHistoryActivity();
        setProfileActivity();
    }
    private void setHomeActivity(){
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,homePage).commit();
                home.setImageResource(R.drawable.home_select);
                download.setImageResource(R.drawable.download_b);
                history.setImageResource(R.drawable.history_b);
                account.setImageResource(R.drawable.user);
            }
        });
    }
    private void setDownloadActivity(){

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,downloadActivity).commit();
                home.setImageResource(R.drawable.home);
                download.setImageResource(R.drawable.download_c);
                history.setImageResource(R.drawable.history_b);
                account.setImageResource(R.drawable.user);
            }
        });
    }

    private void setHistoryActivity(){
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,historyActivity).commit();
                history.setImageResource(R.drawable.home);
                download.setImageResource(R.drawable.download_b);
                history.setImageResource(R.drawable.history_c);
                account.setImageResource(R.drawable.user);
            }
        });
    }

    private void setProfileActivity(){
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,profileActivity).commit();
                history.setImageResource(R.drawable.home);
                download.setImageResource(R.drawable.download_b);
                history.setImageResource(R.drawable.history_b);
                account.setImageResource(R.drawable.user_c);
            }
        });
    }



}
