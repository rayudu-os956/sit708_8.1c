package com.example.task81candroidappexample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText usernameEt;
    Button  proceedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEt = findViewById(R.id.usernameEt);
        proceedBtn = findViewById(R.id.proceedBtn);

        proceedBtn.setOnClickListener(v -> {
            String username = usernameEt.getText().toString().trim();
            if (!username.isEmpty()) {
                Intent i = new Intent(this, ChatActivity.class);
                // pass the username along
                i.putExtra("username", username);
                startActivity(i);
            } else {
                usernameEt.setError("Enter a username");
            }
        });
    }
}