package com.example.task81candroidappexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText chatInputBox;
    private Button sendButton;
    private TextView responseText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatInputBox = findViewById(R.id.chatInputBox);
        sendButton = findViewById(R.id.sendButton);
        responseText = findViewById(R.id.responseText);
        progressBar = findViewById(R.id.progressBar);

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String userMessage = chatInputBox.getText().toString().trim();
        if (userMessage.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show ProgressBar and clear previous response
        progressBar.setVisibility(View.VISIBLE);
        responseText.setText("");
//        chatInputBox.setText("");

        String url = "http://10.0.2.2:5000/chat"; // Update to machine IP for physical device
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Hide ProgressBar and show response
                    progressBar.setVisibility(View.GONE);
                    String botMessage = response.trim();
                    responseText.setText(botMessage);
                },
                error -> {
                    // Hide ProgressBar and show error
                    progressBar.setVisibility(View.GONE);
                    String errorMessage = "Error connecting to server";
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userMessage", userMessage);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Volley.newRequestQueue(this).add(request);
    }
}