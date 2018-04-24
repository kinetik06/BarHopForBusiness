package zombietechnologiesinc.com.barhopforbusiness;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference mDatabaseReference;
    public String barName, barAddress, userId;
    public static final String TAG = SignupActivity.class.getSimpleName();
    private double latitude;
    private double longitude;
    private LatLng mLatLng;
    private String placeId;
    private String barEvent = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(zombietechnologiesinc.com.barhopforbusiness.R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("bars");

        btnSignIn = (Button) findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.sign_in_button);
        btnSignUp = (Button) findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.sign_up_button);
        inputEmail = (EditText) findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.email);
        inputPassword = (EditText) findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.password);
        progressBar = (ProgressBar) findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.progressBar);
        btnResetPassword = (Button) findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.btn_reset_password);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                                Bar bar = null;
                                if (user != null) {
                                    userId = user.getUid();

                                    bar = new Bar(barName,0, 0,  barAddress, userId, latitude, longitude, userId, placeId, barEvent);
                                }

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    /*mDatabaseReference.child("bars").child(userId).setValue(bar);
                                    GeoFire geoFire = new GeoFire(mDatabaseReference.child("bars_location"));
                                    geoFire.setLocation(userId, new GeoLocation(latitude, longitude));*/
                                    Intent intent = new Intent(SignupActivity.this, PlacesActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


}