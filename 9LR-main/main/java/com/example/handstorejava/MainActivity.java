package com.example.handstorejava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.handstorejava.Model.Prevalent.Prevalent;
import com.example.handstorejava.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
public class MainActivity extends AppCompatActivity {
    private Button registerButton,loginButton;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerButton = (Button) findViewById(R.id.main_register_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);
        loadingBar = new ProgressBar(this);

        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginintent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(loginintent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerintenr = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(registerintenr);
            }
        });


        String UserPhoneKey = Paper.book().read(Prevalent.UserMailKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if(UserPhoneKey != "" && UserPasswordKey != ""){
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey) ){
                ValidateUser(UserPhoneKey,UserPasswordKey);

            }
        }
}

    private void ValidateUser(String mail, String password) {

        final DatabaseReference RoofRef;
        RoofRef= FirebaseDatabase.getInstance().getReference();

        RoofRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").child(mail).exists())
                {
                    Users usersData = snapshot.child("Users").child(mail).getValue(Users.class);

                    if(usersData.getMail().equals(mail))
                    {
                        if(usersData.getPassword().equals(password))
                        {

                                Toast.makeText(MainActivity.this, "Успешный вход!", Toast.LENGTH_SHORT).show();

                                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(homeIntent);

                        }

                    }
                }
                else {

                    Toast.makeText(MainActivity.this, "Аккаунт с почтой " + mail + "не существует", Toast.LENGTH_SHORT).show();

                    Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    }