package c.cpen391.alarms.tabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import c.cpen391.alarms.CustomApplication;
import c.cpen391.alarms.CustomSharedPreference;
import c.cpen391.alarms.MainActivity;
import c.cpen391.alarms.R;
import c.cpen391.alarms.home;
import c.cpen391.alarms.models.UserObject;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class TabProfile extends Fragment {
    private CircleImageView proPic;
    private Button browse;
    private Button logout;
    private Bitmap bitmap;
    private String userBio;
    private Gson gson;
    private UserObject mUserObject;
    protected static CustomSharedPreference mPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.profile_fragment, container, false);

        proPic = (CircleImageView) rootview.findViewById(R.id.profilePicture);
        browse = (Button) rootview.findViewById(R.id.browse);
        logout = (Button) rootview.findViewById(R.id.logout);

        mUserObject = ((CustomApplication) getActivity().getApplicationContext()).getSomeVariable();

        String bio;

        if(mUserObject != null) {
            bio = "Name: " + mUserObject.getUsername() + "\n" +
                    "Location: Vancouver, Canada" + "\n" +
                    "email: " + mUserObject.getEmail() + "\n";
        } else { // fetch from db

        }
        TextView userTextValue = (TextView)rootview.findViewById(R.id.user_bio);
        userTextValue.setText(bio);

        Uri temp = mUserObject.getUri();
        if(temp != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), temp);
                proPic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            proPic.setImageResource(R.drawable.empty_pp);
        }

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MainActivity.class);
                getContext().startActivity(i);

                Toast.makeText(getContext(), "Logged Out Successfully", Toast.LENGTH_LONG).show();
            }
        });


        return rootview;
    }

    private void chooseFile() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                proPic.setImageBitmap(bitmap);
                mUserObject.putUri(filePath);
                ((CustomApplication) getActivity().getApplicationContext()).setSomeVariable(mUserObject);
                Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}