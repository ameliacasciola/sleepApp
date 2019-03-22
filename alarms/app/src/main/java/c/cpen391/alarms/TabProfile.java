package c.cpen391.alarms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

public class TabProfile extends Fragment {
    UserObject mUserObject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.profile_fragment, container, false);

        String userBio = getActivity().getIntent().getExtras().getString("USER_BIO");
        Gson gson = ((CustomApplication)getActivity().getApplication()).getGsonObject();
        UserObject mUserObject = gson.fromJson(userBio, UserObject.class);
        String bio = "Name: " + mUserObject.getUsername() + "\n" +
                "email: " + mUserObject.getEmail() + "\n";
        TextView userTextValue = (TextView)rootview.findViewById(R.id.user_bio);
        userTextValue.setText(bio);

        return rootview;
    }
}