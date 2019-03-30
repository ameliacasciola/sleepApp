package c.cpen391.alarms;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
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

import java.net.URL;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private EditText username;
    private EditText email;
    private EditText password;
    private int userID;
    private String userNAME;

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
                    createPostAPICalling(usernameValue,null, passwordValue,null,null);
                    profilePostAPICalling(null, "Vancouver, Canada", null);

                    Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
            }
        });
    }

    public void profilePostAPICalling(String bio, String location, URL image) {
        SharedPreferences id = getSharedPreferences("UserID", Activity.MODE_PRIVATE);
        int myID = id.getInt("USERID", -1);
        String myName = id.getString("USERNAME", "NONE");

        email.setText(myName + Integer.toString(myID));


        Call<ResponseBody> call = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class)
                .profilePost(myID, bio, null, location, image);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Create Profile API Failure", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Create Profile Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createPostAPICalling(String name, String email, String password, String first, String last) {
        Post post = new Post(name, email, password, first, last);
        Call<Post> call = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class)
                .createPost(post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Create User API Failure", Toast.LENGTH_SHORT).show();
                    return;
                }
                Post postResponse = response.body();
                userID = postResponse.getid();
                userNAME = postResponse.getUsername();

                username.setText(userNAME + Integer.toString(userID));

                //saving userid
                SharedPreferences userid = getSharedPreferences("UserID", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = userid.edit();
                editor.putInt("USERID", userID);
                editor.putString("USERNAME", userNAME);
                editor.apply();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Sign Up Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}