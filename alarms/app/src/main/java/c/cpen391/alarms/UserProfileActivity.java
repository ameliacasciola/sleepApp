package c.cpen391.alarms;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setTitle("Profile Page");
        String userBio = getIntent().getExtras().getString("USER_BIO");
        Gson gson = ((CustomApplication)getApplication()).getGsonObject();
        UserObject mUserObject = gson.fromJson(userBio, UserObject.class);
        String bio = "Name:" + mUserObject.getUsername() + "\n" +
                "email:" + mUserObject.getEmail() + "\n" +
                "Phone no.:" + mUserObject.getPhone() + "\n" +
                "Address:" + mUserObject.getAddress() + "\n";
        TextView userTextValue = (TextView)findViewById(R.id.user_bio);
        userTextValue.setText(bio);
    }
}
