package com.example.oneweekenglish.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oneweekenglish.R;
import com.example.oneweekenglish.adapter.MessageAdapter;
import com.example.oneweekenglish.model.Message;
import com.example.oneweekenglish.util.ChatBot;

import java.util.ArrayList;

public class ChatbotActivity extends AppCompatActivity {
    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private ImageView buttonSend;
    private ImageView buttonBack;
    private ArrayList<Message> messageList;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        // Khởi tạo các thành phần giao diện
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        buttonBack = findViewById(R.id.buttonBack);

        // Khởi tạo danh sách tin nhắn và adapter
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);

        // Thiết lập RecyclerView
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(messageAdapter);

        // Xử lý sự kiện khi nhấn nút gửi
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMessage = editTextMessage.getText().toString().trim();
                if (!userMessage.isEmpty()) {
                    // Thêm tin nhắn của người dùng
                    messageList.add(new Message(userMessage, false));
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerViewChat.scrollToPosition(messageList.size() - 1);
                    editTextMessage.setText("");
                    //goi api
                    ChatBot.sendToGeminiAPI(getApplicationContext(), userMessage, new ChatBot.ChatBotCallback() {
                        @Override
                        public void onResponse(String message) {

                            // Giả lập phản hồi của bot (thay thế bằng logic thật nếu có API)
                            String botResponse = message;
                            messageList.add(new Message(botResponse, true));
                            messageAdapter.notifyItemInserted(messageList.size() - 1);
                            recyclerViewChat.scrollToPosition(messageList.size() - 1);

                            // Xóa nội dung ô nhập liệu
                            editTextMessage.setText("");
                        }

                        @Override
                        public void onError(String errorMessage) {
                            // Giả lập phản hồi của bot (thay thế bằng logic thật nếu có API)
                            String botResponse = "Lỗi, vui lòng thử lại ... ";
                            messageList.add(new Message(botResponse, true));
                            messageAdapter.notifyItemInserted(messageList.size() - 1);
                            recyclerViewChat.scrollToPosition(messageList.size() - 1);

                            // Xóa nội dung ô nhập liệu
                            editTextMessage.setText("");
                        }
                    });
                }
            }
        });
        // Sự kiện nhấn nút back để quay về MainActivity
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatbotActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}