package c.cpen391.alarms.tabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import c.cpen391.alarms.CustomApplication;
import c.cpen391.alarms.CustomSharedPreference;
import c.cpen391.alarms.MainActivity;
import c.cpen391.alarms.R;
import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import c.cpen391.alarms.models.Profile;
import c.cpen391.alarms.models.UserObject;
import de.hdodenhof.circleimageview.CircleImageView;
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
                proPic = (CircleImageView) rootview.findViewById(R.id.profilePicture);
                String temp = mProfile.getImage().toString();
                Picasso.get().load(temp).placeholder(R.drawable.empty_pp).into(proPic);
            }
            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Toast.makeText(getActivity(), "Profile User API Failure", Toast.LENGTH_SHORT).show();
                proPic = (CircleImageView) rootview.findViewById(R.id.profilePicture);
                proPic.setImageResource(R.drawable.empty_pp);
            }
        });

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