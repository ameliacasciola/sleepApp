package c.cpen391.alarms;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import c.cpen391.alarms.custom.LoginLoop;
import su.levenetc.android.textsurface.Debug;
import su.levenetc.android.textsurface.TextSurface;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import static su.levenetc.android.textsurface.animations.Just.show;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView pic;
    private TextSurface textSurface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getSupportActionBar();

        if(null != actionBar){
            actionBar.hide();
        }

        Button signInButton = (Button)findViewById(R.id.sign_in);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        Button signUpButton = (Button)findViewById(R.id.sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(signInIntent);
            }
        });

        textSurface = (TextSurface) findViewById(R.id.text_surface);

        textSurface.postDelayed(new Runnable() {
            @Override public void run() {
                show();
            }
        }, 1000);

        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                show();
            }
        });

        CheckBox checkDebug = (CheckBox) findViewById(R.id.check_debug);
        checkDebug.setChecked(Debug.ENABLED);
        checkDebug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Debug.ENABLED = isChecked;
                textSurface.invalidate();
            }
        });
    }

    private void show() {
        textSurface.reset();
        LoginLoop.play(textSurface, getAssets(), getApplicationContext());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent a = new Intent(this, MainActivity.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(a);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}