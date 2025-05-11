package com.example.task81candroidappexample;

import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {
    RecyclerView      chatRv;
    EditText          inputEt;
    ChatAdapter       adapter;
    List<ChatMessage> messages = new ArrayList<>();

    // extended timeouts for slow responses
    OkHttpClient http = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    // use emulator‐to‐host alias, no leading space!
    static final String ENDPOINT = "http://10.0.2.2:5050/chat";

    // will hold the logged-in user’s name
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 1) read the username from the Intent
        username = getIntent().getStringExtra("username");

        // 2) show it in the ActionBar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(username);
        }

        chatRv   = findViewById(R.id.chatRv);
        inputEt  = findViewById(R.id.inputEt);
        findViewById(R.id.sendBtn).setOnClickListener(v -> sendMessage());

        adapter = new ChatAdapter(messages);
        chatRv.setLayoutManager(new LinearLayoutManager(this));
        chatRv.setAdapter(adapter);
    }

    private void sendMessage() {
        String text = inputEt.getText().toString().trim();
        if (text.isEmpty()) return;

        // prefix the bubble with the user's name
        String userLine = username + ": " + text;

        // locally show the user message
        ChatMessage userMsg = new ChatMessage(userLine, true);
        messages.add(userMsg);
        adapter.notifyItemInserted(messages.size() - 1);
        chatRv.scrollToPosition(messages.size() - 1);
        inputEt.setText("");

        // build and send the form
        RequestBody body = new FormBody.Builder()
                .add("userMessage", text)  // send only the raw text to the API
                .build();

        Request req = new Request.Builder()
                .url(ENDPOINT)
                .post(body)
                .build();

        http.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    messages.add(new ChatMessage("Error: " + e.getMessage(), false));
                    adapter.notifyItemInserted(messages.size() - 1);
                });
            }

            @Override
            public void onResponse(Call call, Response resp) throws IOException {
                String reply = resp.body().string();
                runOnUiThread(() -> {
                    messages.add(new ChatMessage(reply, false));
                    adapter.notifyItemInserted(messages.size() - 1);
                });
            }
        });
    }
}