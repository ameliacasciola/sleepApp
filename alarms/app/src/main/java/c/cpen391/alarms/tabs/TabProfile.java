package c.cpen391.alarms.tabs;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.loader.content.CursorLoader;
import c.cpen391.alarms.CustomApplication;
import c.cpen391.alarms.CustomSharedPreference;
import c.cpen391.alarms.MainActivity;
import c.cpen391.alarms.R;
import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import c.cpen391.alarms.models.Profile;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class TabProfile extends Fragment {
    private CircleImageView proPic;
   // private Button logout;
    private Bitmap bitmap;
    private String userBio;
    private Gson gson;
    private EditText bioedit;
    private Button bioupdate;
    private ImageView logout;
    protected static CustomSharedPreference mPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.profile_fragment, container, false);

        proPic = (CircleImageView) rootview.findViewById(R.id.profilePicture);
       // logout = (Button) rootview.findViewById(R.id.logout);
        logout = (ImageView) rootview.findViewById(R.id.logout);
        bioedit = (EditText) rootview.findViewById(R.id.bioedit);
        bioupdate = (Button) rootview.findViewById(R.id.bioupdate);

        // GET info from database everytime
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
//                TextView userTextValue = (TextView)rootview.findViewById(R.id.user_bio);
                TextView userName = (TextView)rootview.findViewById(R.id.user_name);
                TextView userLocation = (TextView)rootview.findViewById(R.id.user_location);
                TextView userHighSc = (TextView)rootview.findViewById(R.id.user_highscore) ;
                Profile mProfile = response.body();

                userName.setText(mProfile.getName());
                userLocation.setText(mProfile.getLocation());
                userHighSc.setText(Integer.toString(mProfile.getPoints()));

                // grab photo from local storage
                Bitmap bm = mPref.getPic();
                if(bm != null) {
                    proPic.setImageBitmap(bm);
                } else { // grab image from db
                    String string = mProfile.getImage().toString();
                    Picasso.get().load(string).placeholder(R.drawable.empty_pp).into(proPic);
                }
                bioedit.setText(mProfile.getBio());
            }
            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Toast.makeText(getActivity(), "Profile User API Failure", Toast.LENGTH_SHORT).show();
                proPic.setImageResource(R.drawable.empty_pp);
            }
        });

        // change image
        proPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        // logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MainActivity.class);
                getContext().startActivity(i);

                Toast.makeText(getContext(), "Logged Out Successfully", Toast.LENGTH_LONG).show();
            }
        });


        // bio update button
        bioupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadBio(bioedit.getText().toString());
                bioedit.setFocusable(false);
                bioedit.setFocusable(true);
            }
        });

        return rootview;
    }

    // browse and choose from photo album
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

                // save browsed bitmap in local storage
                mPref = ((CustomApplication)getActivity().getApplicationContext()).getShared();
                mPref.setPic(bitmap);
                proPic.setImageBitmap(bitmap);

                //get image path:
                String[] projection = {MediaStore.Images.Media.DATA};
                CursorLoader loader = new CursorLoader(getContext(), filePath, projection, null, null, null);
                Cursor cursor = loader.loadInBackground();
                int column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String result = cursor.getString(column_idx);
                cursor.close();

                // upload to db (not working temporarily)
                uploadIMG(filePath);

                Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MultipartBody.FORM, descriptionString);
    }

    // trying to upload image to database
    private void uploadIMG(Uri filePath) {
        mPref = ((CustomApplication)getActivity().getApplicationContext()).getShared();

        Map<String, RequestBody> partmap = new HashMap<>();
        partmap.put("user", createPartFromString(Integer.toString(mPref.getUserID())));
        partmap.put("bio", createPartFromString("YOLO"));
        partmap.put("name", createPartFromString(mPref.getUserName()));
        partmap.put("location", createPartFromString("Vancouver, Canada"));

        File file = new File(filePath.toString());

        RequestBody requestBody = RequestBody.create(MediaType.parse(
                getContext().getContentResolver().getType(filePath)), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        Call<ResponseBody> call = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class)
                .uploadImage(body, partmap);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Upload Profile Picture No Response", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Upload Profile Picture Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // upload user bio to database
    private void uploadBio(String bio) {
        mPref = ((CustomApplication)getActivity().getApplicationContext()).getShared();

        Call<ResponseBody> call = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class)
                .updateBio(bio, Integer.toString(mPref.getUserID()));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Update Bio No Response", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity().getApplicationContext(), "Bio Updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Update Bio Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}