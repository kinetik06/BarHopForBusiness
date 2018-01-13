package com.zombietechinc.barhopforbusiness;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
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
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private ImageView barImage;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference profilePicRef;
    Typeface typeface;
    TextView barhopTV1;
    TextView barhopTV2;
    TextView forBusinessTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_bar_detail_ui);

        Intent intent = getIntent();
        barName = intent.getStringExtra("barname");
        barAddress = intent.getStringExtra("baraddress");
        typeface = ResourcesCompat.getFont(this, R.font.geosanslight);
        forBusinessTV = findViewById(R.id.forBusinessTV);
        forBusinessTV.setTypeface(typeface);
        barhopTV1 = findViewById(R.id.barhopTV1);
        barhopTV2 = findViewById(R.id.barhopTV2);
        barhopTV1.setTypeface(typeface);
        barhopTV2.setTypeface(typeface);

        Log.d("Barname: ", barName + "");
        barImage = (ImageView)findViewById(R.id.bar_image);
        barNameTV = (TextView)findViewById(R.id.bar_name);
        barNameTV.setTypeface(typeface);
        //barAddressTV = (TextView)findViewById(R.id.bar_address);
        barNameTV.setText(barName);
        //barAddressTV.setText(barAddress);
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
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://bar-hop-b83f2.appspot.com");
        profilePicRef = storageRef.child(userId).child(profilePic);

        final long ONE_MEGABYTE = 1024 * 1024;
        profilePicRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "profilepic.jpg" is returns, use this as needed
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                barImage.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });



        barImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);

            barImage.setImageBitmap(imageBitmap);
            barImage.setDrawingCacheEnabled(true);
            barImage.buildDrawingCache();
            Bitmap bitmap = barImage.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            byte[] datas = baos.toByteArray();

            UploadTask uploadTask = profilePicRef.putBytes(datas);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                    Toast.makeText(BarDetailsActivity.this, "Upload Failed: Check Connection", Toast.LENGTH_SHORT).show();
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Toast.makeText(BarDetailsActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });

        }


    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        Log.d("Path: ", mCurrentPhotoPath);
        return image;
    }
}

