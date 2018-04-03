package com.zombietechinc.barhopforbusiness;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public String barName;
    Button plusOneButton;
    Button minusOneButton;

    Button editBar;
    String userId;
    String barAddress;
    int barCount;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth auth;
    private int barCap;
    private static final String ANONYMOUS = "anonymous";
    private String barKey;
    private ImageView barImage;
    private GoogleApiClient mGoogleApiClient;
    private String placeId;
    private String profilePic = "mUpdatePic.jpg";
    PlacePhotoMetadataBuffer photoMetadataBuffer;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference profilePicRef;
    private String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private EditText barCapET;
    ImageView plusOne;
    ImageView minusOne;
    Typeface typeface;
    //@BindView(R.id.addtion)LinearLayout addition;
    //@BindView(R.id.subtraction) LinearLayout subtraction;
    //@BindView(R.id.plusSignTV)TextView plusSignTV;
    @BindView(R.id.swipeLayout)LinearLayout swipeLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get firebase auth instance
        userId = ANONYMOUS;
        auth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        //get current user
        mFirebaseUser = auth.getCurrentUser();
        if (mFirebaseUser == null) {
            //user is not signed in, launch login activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else {
            userId = mFirebaseUser.getUid();
        }

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://bar-hop-b83f2.appspot.com");
        profilePicRef = storageRef.child(userId).child(profilePic);
            //set views
        setContentView(R.layout.swipe_counter);

        ButterKnife.bind(this);

        swipeLayout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){

            @Override
            public void onSwipeLeft(){

                barCount = barCount - 1;
                if (barCount < 0) {
                    barCount = 0;
                }
                mDatabaseReference.child("barCount").setValue(barCount);

            }

            @Override
            public void onSwipeRight(){

                if (barCap == 0) {
                    Toast.makeText(MainActivity.this, "Please set your Capacity!", Toast.LENGTH_SHORT).show();
                }

                barCount = barCount + 1;

                if (barCount >= barCap) {
                    barCount = barCap;
                }
                mDatabaseReference.child("barCount").setValue(barCount);

            }


        });

/*        final AlphaAnimation swellAnimation = new AlphaAnimation(0.5f, 0.7f );
        swellAnimation.setDuration(200);


        addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                plusSignTV.startAnimation(swellAnimation);

                if (barCap == 0) {
                    Toast.makeText(MainActivity.this, "Please set your Capacity!", Toast.LENGTH_SHORT).show();
                }

                barCount = barCount + 1;

                if (barCount >= barCap) {
                    barCount = barCap;
                }
                mDatabaseReference.child("barCount").setValue(barCount);

            }
        });

        subtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subtraction.startAnimation(swellAnimation);

                barCount = barCount - 1;
                if (barCount < 0) {
                    barCount = 0;
                }
                mDatabaseReference.child("barCount").setValue(barCount);

            }
        });*/

        typeface = ResourcesCompat.getFont(this, R.font.geosanslight);
        final TextView barNameTV = (TextView) findViewById(R.id.bar_name);
        final TextView barAddressTV = (TextView) findViewById(R.id.bar_notes);
        final TextView barCountTV = (TextView) findViewById(R.id.count);

        barNameTV.setTypeface(typeface);
        barAddressTV.setTypeface(typeface);
        barCountTV.setTypeface(typeface);
        //plusOneButton = (Button) findViewById(R.id.buttonIn);
        //minusOneButton = (Button) findViewById(R.id.buttonOut);
        plusOne = (ImageView) findViewById(R.id.plusOne);
        minusOne = (ImageView) findViewById(R.id.minusOne);


        barImage = (ImageView)findViewById(R.id.bar_image);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .build();

        }


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("bars").child(userId);
        barKey = mDatabaseReference.getKey();
        Log.d(TAG, "This is the KEY:" + barKey);

        //get database reference to bar information
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Bar bar = dataSnapshot.getValue(Bar.class);

                if (!dataSnapshot.exists()) {

                    Toast.makeText(MainActivity.this, "Error- Loading Info", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, PlacesActivity.class);
                    startActivity(intent);

                } else {

                    barName = bar.getBarName();
                    Log.d(TAG, bar.getBarName());
                    barAddress = bar.getBarAddress();
                    barAddressTV.setText(bar.getBarAddress());
                    barCount = bar.getBarCount();
                    barCap = bar.getBarCap();
                    placeId = bar.getPlaceId();

                    //Get Bar Image
                    final long ONE_MEGABYTE = 1024 * 1024;
                    profilePicRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            // Data for "profilepic.jpg" is returns, use this as needed
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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

                    barNameTV.setText(barName);
                    barCountTV.setText(String.valueOf(barCount));
                    //set up another click listener
                    //TODO make one lisetener for all click events
                    barNameTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, BarDetailsActivity.class);
                            intent.putExtra("barname", barName);
                            Log.d(TAG, "barname= " + barName);
                            intent.putExtra("baraddress", barAddress);
                            startActivity(intent);
                        }
                    });
                /*plusOneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (barCap == 0) {
                            Toast.makeText(MainActivity.this, "Please set your Capacity!", Toast.LENGTH_SHORT).show();
                        }
                        barCount = barCount + 1;
                        if (barCount >= barCap) {
                            barCount = barCap;
                        }
                        mDatabaseReference.child("barCount").setValue(barCount);

                    }
                });
                minusOneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        barCount = barCount - 1;
                        if (barCount < 0) {
                            barCount = 0;
                        }
                        mDatabaseReference.child("barCount").setValue(barCount);
                    }
                });*/

                    plusOne.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (barCap == 0) {
                                Toast.makeText(MainActivity.this, "Please set your Capacity!", Toast.LENGTH_SHORT).show();
                            }

                            barCount = barCount + 1;

                            if (barCount >= barCap) {
                                barCount = barCap;
                            }
                            mDatabaseReference.child("barCount").setValue(barCount);

                        }
                    });

                    minusOne.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            barCount = barCount - 1;
                            if (barCount < 0) {
                                barCount = 0;
                            }
                            mDatabaseReference.child("barCount").setValue(barCount);

                        }
                    });

                }

                }

                @Override
                public void onCancelled (DatabaseError databaseError){

                }

        };
        mDatabaseReference.addValueEventListener(valueEventListener);






    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {

        }
    }


    private String getBarName() {


        if (barName == null) {

            startActivity(new Intent(MainActivity.this, BarDetailsActivity.class));
        }
        return barName;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

                    Toast.makeText(MainActivity.this, "Upload Failed: Check Connection", Toast.LENGTH_SHORT).show();
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Toast.makeText(MainActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
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
        return image;
    }

}
