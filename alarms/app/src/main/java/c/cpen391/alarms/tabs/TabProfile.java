package c.cpen391.alarms.tabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import c.cpen391.alarms.CustomApplication;
import c.cpen391.alarms.CustomSharedPreference;
import c.cpen391.alarms.MainActivity;
import c.cpen391.alarms.R;
import c.cpen391.alarms.SignUpActivity;
import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import c.cpen391.alarms.home;
import c.cpen391.alarms.models.Alarm;
import c.cpen391.alarms.models.Post;
import c.cpen391.alarms.models.Profile;
import c.cpen391.alarms.models.UserObject;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        mPref = ((CustomApplication)getActivity().getApplicationContext()).getShared();
        SleepAPI service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class);
        Call<Profile> call = service.getProfileInfo(Integer.toString(mPref.getUserID()));
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Profile no Response", Toast.LENGTH_SHORT).show();
                    return;
                }
                TextView userTextValue = (TextView)rootview.findViewById(R.id.user_bio);
                Profile mProfile = response.body();
                userTextValue.setText("Name: " + mProfile.getName() + "\n"
                                    + "Bio: " + mProfile.getBio() + "\n"
                                    + "Location: " + mProfile.getLocation());

                // grab image
            }
            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Toast.makeText(getActivity(), "Profile User API Failure", Toast.LENGTH_SHORT).show();
            }
        });

        /*
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
        */

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