package com.zombietechinc.barhopforbusiness;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class BarDetailsActivity extends AppCompatActivity {

    String barName;
    String barAddress;
    int barCap;
    int barCount;
    Button submit;
    String userId;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mFirebaseUser;
    private static final String TAG = BarDetailsActivity.class.getSimpleName();
    LatLng mLatLng;
    String photoURL;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button mUpdatePic;
    private Button signOut;
    private TextView barNameTV;
    private TextView barAddressTV;
    private EditText eventET;
    private String profilePic = "mUpdatePic.jpg";
    private String barEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_bar_detail_ui);
        Intent intent = getIntent();
        barName = intent.getStringExtra("barname");
        barAddress = intent.getStringExtra("baraddress");
        barNameTV = (TextView)findViewById(R.id.bar_name);
        barAddressTV = (TextView)findViewById(R.id.bar_address);
        barNameTV.setText(barName);
        barAddressTV.setText(barAddress);
        Log.d("CHECK:", barName + barAddress);
        signOut = (Button) findViewById(R.id.sign_out_button);
        final EditText barCapET = (EditText) findViewById(R.id.bar_cap);
        eventET = (EditText)findViewById(R.id.event_et);
        //submit = (Button) findViewById(R.id.update_bar_button);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser != null) {
            userId = mFirebaseUser.getUid();
        }
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://bar-hop-b83f2.appspot.com");
        final StorageReference profilePicRef = storageRef.child(userId).child(profilePic);

        /*submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                barCount = 0;

                if (TextUtils.isEmpty(barCapET.getText())) {
                    Toast.makeText(BarDetailsActivity.this, "PLEASE ENTER YOUR CAPACITY", Toast.LENGTH_SHORT).show();
                } else {

                    barEvent = eventET.getText().toString();
                    mDatabaseReference.child("bars").child(userId).child("barEvent").setValue(barEvent);
                    barCap = Integer.parseInt(barCapET.getText().toString());
                    mDatabaseReference.child("bars").child(userId).child("barCap").setValue(barCap);
                    Intent intent = new Intent(BarDetailsActivity.this, MainActivity.class);


                    startActivity(intent);
                }
            }
        });*/
        /*signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(BarDetailsActivity.this, LoginActivity.class));
            }
        });*/
    }

    /*private void addNewBar(String barName, String barAddress , int barCap, int barCount, String userId) {
        Bar bar = new Bar(barName,barAddress,barCap,barCount, userId);
        mDatabaseReference.child("bars").child(userId).setValue(bar);
    }*/
}
