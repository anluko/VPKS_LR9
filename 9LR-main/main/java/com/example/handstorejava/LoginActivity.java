package com.example.handstorejava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.handstorejava.Model.Prevalent.Prevalent;
import com.example.handstorejava.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText loginMailInput,loginPasswordInput;
    private ProgressDialog loadingBar;
    private CheckBox checkBoxRememberMe;


    private String parentDbName ="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton=(Button) findViewById(R.id.login_btn);
        loginMailInput=(EditText) findViewById(R.id.login_mail_input);
        loginPasswordInput=(EditText) findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);
        checkBoxRememberMe= (CheckBox)findViewById(R.id.login_checkbox);
        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });


    }

    private void loginUser() {

        String mail = loginMailInput.getText().toString();
        String password = loginPasswordInput.getText().toString();
        if(TextUtils.isEmpty(mail))
        {
            Toast.makeText(this, "Введите почту", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
        }
        else
        {
        loadingBar.setTitle("Вход в аккаунт");
        loadingBar.setMessage("Пожалуйста, подождите...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        ValidateUser(mail, password);}
    }

    private void ValidateUser(String mail, String password) {

        if(checkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserMailKey, mail);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }


        final DatabaseReference RoofRef;
        RoofRef= FirebaseDatabase.getInstance().getReference();

        RoofRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(mail).exists())
                {
                    Users usersData = snapshot.child(parentDbName).child(mail).getValue(Users.class);

                    if(usersData.getMail().equals(mail))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            if(parentDbName.equals("Users")){
                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "Успешный вход!", Toast.LENGTH_SHORT).show();

                                Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(homeIntent);
                            }

                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Аккаунт с почтой " + mail + "не существует", Toast.LENGTH_SHORT).show();

                    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}