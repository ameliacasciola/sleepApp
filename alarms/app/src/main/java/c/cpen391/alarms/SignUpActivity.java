package c.cpen391.alarms;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import c.cpen391.alarms.models.Post;
import c.cpen391.alarms.models.UserObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.Gson;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private EditText username;
    private EditText email;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Android Fingerprint Registration");
        username = (EditText)findViewById(R.id.username);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        Button signUpButton = (Button) findViewById(R.id.sign_up_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameValue = username.getText().toString();
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();
                if(TextUtils.isEmpty(usernameValue) || TextUtils.isEmpty(emailValue)|| TextUtils.isEmpty(passwordValue))
                {
                    Toast.makeText(SignUpActivity.this, "All input fields must be filled", Toast.LENGTH_LONG).show();
                }else{
                    UserObject userData = new UserObject(usernameValue, emailValue, passwordValue);
                    // set
                    ((CustomApplication) getApplication()).setSomeVariable(userData);
                    username.setText("");
                    email.setText("");
                    password.setText("");
                    sendPost(usernameValue,null,passwordValue,null,null);
                    //Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                    //startActivity(loginIntent);
                }
            }
        });
    }

    public void sendPost(String name, String email, String password, String first, String last) {
        Post post = new Post(name, email, password, first, last);
        Call<Post> call = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class)
                .createPost(post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Response Failure", Toast.LENGTH_SHORT).show();
                    return;
                }
                Post postResponse = response.body();
                username.setText(postResponse.toString());
                Toast.makeText(SignUpActivity.this, "Success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Sign Up Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}