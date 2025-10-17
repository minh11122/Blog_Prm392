package com.example.blog.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.blog.R;
import com.example.blog.api.ApiClient;
import com.example.blog.api.ApiService;
import com.example.blog.model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends AppCompatActivity {

    EditText etTitle, etContent, etAuthor;
    Button btnSave;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        etAuthor = findViewById(R.id.etAuthor);
        btnSave = findViewById(R.id.btnSave);

        apiService = ApiClient.getClient().create(ApiService.class);

        btnSave.setOnClickListener(v -> {
            Post post = new Post(
                    etTitle.getText().toString(),
                    etContent.getText().toString(),
                    etAuthor.getText().toString()
            );
            apiService.createPost(post).enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddPostActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    Toast.makeText(AddPostActivity.this, "Lỗi mạng!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
