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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText registerMailInput,registerPasswordInput,registerNameInput;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton=(Button) findViewById(R.id.register_btn);
        registerMailInput=(EditText) findViewById(R.id.register_mail_input);
        registerPasswordInput=(EditText) findViewById(R.id.register_password_input);
        registerNameInput=(EditText) findViewById(R.id.register_username_input);
        loadingBar = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String username = registerNameInput.getText().toString();
        String mail = registerMailInput.getText().toString();
        String password = registerPasswordInput.getText().toString();

        if(TextUtils.isEmpty(username))
        { Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show();}
        else if(TextUtils.isEmpty(mail))
        {
            Toast.makeText(this, "Введите почуту", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Создание аккаунта");
            loadingBar.setMessage("Пожалуйста, подождите...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateMail(username, mail, password);
        }
    }

    private void ValidateMail(String username, String mail, String password) {

       final DatabaseReference RoofRef;
       RoofRef=FirebaseDatabase.getInstance().getReference();

       RoofRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Usera").child(mail).exists()))
                {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("name", username );
                    userDataMap.put("mail", mail );
                    userDataMap.put("password", password );

                    RoofRef.child("Users").child(mail).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Регистрация прошла успешно.", Toast.LENGTH_SHORT).show();

                                        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(loginIntent);
                                }
                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Ошибка.", Toast.LENGTH_SHORT).show();
                                    }
                                };
                            });
                }
                else{
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Почта" + mail + "уже зарегистрирована", Toast.LENGTH_SHORT).show();

                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(loginIntent); }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }


}