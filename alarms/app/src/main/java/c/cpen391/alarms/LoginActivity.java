package c.cpen391.alarms;


import android.Manifest;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import c.cpen391.alarms.models.UserObject;

import com.google.gson.Gson;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;



public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintHandler fingerprintHandler;
    private static final String FINGERPRINT_KEY = "AndroidKey";
    private static final int REQUEST_USE_FINGERPRINT = 300;
    protected static CustomSharedPreference mPref;
    private static UserObject mUser;
    private static Application application;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        application = this.getApplication();

        setTitle("Android Fingerprint Login");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mPref = ((CustomApplication)getApplication()).getShared();

        // android version greater than marshmallow
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if(fingerprintManager != null) {
                // check support for android fingerprint on device
                checkDeviceFingerprintSupport();

                //instantiate Cipher class
                if(instantiateCipher()) {
                    cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    fingerprintHandler = new FingerprintHandler(this);
                    fingerprintHandler.startAuth(fingerprintManager, cryptoObject);
                }
            } else {
                Toast.makeText(this, "fingerprintManager is null", Toast.LENGTH_SHORT).show();
            }
        }


        ImageView fingerprintImage = (ImageView)findViewById(R.id.fingerprint_image);
        fingerprintImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fingerprintHandler.completeFingerAuthentication(fingerprintManager, cryptoObject);
            }
        });

    }

    private void checkDeviceFingerprintSupport() {
        if (!fingerprintManager.isHardwareDetected()) {
            Toast.makeText(LoginActivity.this, "Fingerprint is not supported in this device", Toast.LENGTH_LONG).show();
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
            Toast.makeText(this, "Permission not granted to use fingerprint scanner.", Toast.LENGTH_LONG).show();
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT}, REQUEST_USE_FINGERPRINT);
        else if (!keyguardManager.isKeyguardSecure()) {
            Toast.makeText(LoginActivity.this, "Screen lock is not secure and enable", Toast.LENGTH_LONG).show();
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            Toast.makeText(LoginActivity.this, "Fingerprint not yet configured", Toast.LENGTH_LONG).show();
        } else {
            //generate fingerprint keystore
            generateFingerprintKeyStore();
        }
    }
    private void generateFingerprintKeyStore(){
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(FINGERPRINT_KEY, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (KeyStoreException | IOException | CertificateException
                | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }
    private boolean instantiateCipher(){
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }
        try {
            keyStore.load(null);
            SecretKey secretKey = (SecretKey)keyStore.getKey(FINGERPRINT_KEY, null);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_USE_FINGERPRINT){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // check support for android fingerprint on device
                checkDeviceFingerprintSupport();
                //generate fingerprint keystore
                generateFingerprintKeyStore();

                if(instantiateCipher()) {
                    cryptoObject = new FingerprintManager.CryptoObject(cipher);
                }
            }
            else{
                Toast.makeText(this, R.string.permission_refused, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, getString(R.string.Unknown_permission_request), Toast.LENGTH_LONG).show();
        }
    }






    public static class FingerprintHandler extends FingerprintManager.AuthenticationCallback{
        private static final String TAG = FingerprintHandler.class.getSimpleName();

        private Context context;
        public FingerprintHandler(Context context){
            this.context = context;
        }


        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            Log.d(TAG, "Error message " + errorCode + ": " + errString);
            Toast.makeText(context, context.getString(R.string.authenticate_fingerprint), Toast.LENGTH_LONG).show();
        }


        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            super.onAuthenticationHelp(helpCode, helpString);
            Toast.makeText(context, R.string.auth_successful, Toast.LENGTH_LONG).show();
        }


        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            mUser = ((CustomApplication) application).getSomeVariable();
            int userID = mPref.getUserID();
            String userName = mPref.getUserName();

            if(mUser != null || userID != -1 || userName != "NONE"){
                Toast.makeText(context, context.getString(R.string.auth_successful), Toast.LENGTH_LONG).show();
                // login with only fingerprint
                Intent homeIntent = new Intent(context, home.class);
                context.startActivity(homeIntent);
            }else{
                Toast.makeText(context, "You must register before login with fingerprint", Toast.LENGTH_LONG).show();
            }
        }


        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
        }


        public void completeFingerAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            try{
                fingerprintManager.authenticate(cryptoObject, new CancellationSignal(), 0, this, null);
            }catch (SecurityException ex) {
                Log.d(TAG, "An error occurred:\n" + ex.getMessage());
            } catch (Exception ex) {
                Log.d(TAG, "An error occurred\n" + ex.getMessage());
            }
        }

        public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }
        /*
        private static void showPasswordAuthentication(Context context){
            final Dialog openDialog = new Dialog(context);
            openDialog.setContentView(R.layout.password_layout);
            openDialog.setTitle("Enter Password");
            final EditText passwordDialog = (EditText)openDialog.findViewById(R.id.password);
            Button loginWithPasswordButton = (Button)openDialog.findViewById(R.id.login_button);
            loginWithPasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String authPassword = passwordDialog.getText().toString();
                    if(TextUtils.isEmpty(authPassword)){
                        Toast.makeText(view.getContext(), "Password field must be filled", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(mUser.getPassword().equals(authPassword)){
                        Intent homeIntent = new Intent(view.getContext(), home.class);
                        view.getContext().startActivity(homeIntent);
                    }else{
                        Toast.makeText(view.getContext(), "Incorrect password! Try again", Toast.LENGTH_LONG).show();
                        return;
                    }
                    openDialog.dismiss();
                }
            });
            openDialog.show();
        }
        */
    }
}