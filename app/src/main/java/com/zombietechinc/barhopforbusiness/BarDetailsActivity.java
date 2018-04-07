package com.zombietechinc.barhopforbusiness;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BarDetailsActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, View.OnClickListener{

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
    private DatabaseReference dailySpecialReference;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference profilePicRef;
    Typeface typeface;
    TextView barhopTV1;
    TextView barhopTV2;
    TextView forBusinessTV;
    PopupMenu popupMenu;
    DatabaseReference barRef;
    private RecyclerView.Adapter adapterBars;

    /*@BindView(R.id.mondayTV) TextView mondayTV;
    @BindView(R.id.tuedayTV) TextView tuesdayTV;
    @BindView(R.id.wednesdayTV)TextView wednesdayTV;
    @BindView(R.id.thursdayTV)TextView thursdayTV;
    @BindView(R.id.fridayTV)TextView fridayTV;
    @BindView(R.id.saturdayTV)TextView saturdayTV;
    @BindView(R.id.sundayTV) TextView sundayTV;*/
    //@BindView(R.id.saveChangeTV) TextView saveChangeTV;
    @BindView(R.id.barGenreRV)RecyclerView mRecyclerView;

    ArrayList<String> dailySpecials;
    String dayOfWeek;
    Context context;
    ArrayList<DailySpecial> dailySpecialsArray;
    LinearLayoutManager mLinearLayoutManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.main_bar_detail_ui);
        dailySpecialReference = FirebaseDatabase.getInstance().getReference().child("bars_specials");


        dailySpecialsArray = new ArrayList<>(7);

        popupMenu = new PopupMenu(this, findViewById(R.id.menu_iconIV));
        popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "Log Out");
        popupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "Zero Count");
        popupMenu.setOnMenuItemClickListener(this);
        findViewById(R.id.menu_iconIV).setOnClickListener(this);

        //set resources for Daily TextVIews

        ButterKnife.bind(this);

        adapterBars = new BarGenreAdapter(BarDetailsActivity.this, dailySpecialsArray);
        mLinearLayoutManager = new LinearLayoutManager(context);
        mLinearLayoutManager.setStackFromEnd(false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(adapterBars);
        mRecyclerView.hasFixedSize();

        /*DailySpecial dailySpecial = new DailySpecial("Sunday FunDay", new Date(), "Sunday", 6);
        DailySpecial dailySpecial1 = new DailySpecial("Monday Madness", new Date(), "Monday", 1);
        dailySpecialsArray.add(dailySpecial);
        dailySpecialsArray.add(dailySpecial1);*/


//        mondayTV.setOnClickListener(this);
//        tuesdayTV.setOnClickListener(this);
//        wednesdayTV.setOnClickListener(this);
//        thursdayTV.setOnClickListener(this);
//        fridayTV.setOnClickListener(this);
//        saturdayTV.setOnClickListener(this);
//        sundayTV.setOnClickListener(this);

        dailySpecials = new ArrayList<>();







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

        //Pull current Bar Object

        barRef = FirebaseDatabase.getInstance().getReference().child("bars").child(userId);

        //Daily Special code
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
            }
        }, 1000);
        barRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Bar bar = dataSnapshot.getValue(Bar.class);
                if (bar.getDailySpecialArrayList() == null){
                    Log.d(TAG, "Special array empty or does not exist");

                    DailySpecial mondaySpecial = new DailySpecial("Tap to edit special", "Monday",0, 0);
                    DailySpecial tuesdaySpecial = new DailySpecial("Tap to edit special", "Tuesday",0, 1);
                    DailySpecial wednesdaySpecial = new DailySpecial("Tap to edit special", "Wednesday",0, 2);
                    DailySpecial thursdaySpecial = new DailySpecial("Tap to edit special", "Thursday",0, 3);
                    DailySpecial fridaySpecial = new DailySpecial("Tap to edit special", "Friday",0, 4);
                    DailySpecial saturdaySpecial = new DailySpecial("Tap to edit special", "Saturday",0, 5);
                    DailySpecial sundaySpecial = new DailySpecial("Tap to edit special", "Sunday",0, 6);
                    dailySpecialsArray.add(mondaySpecial);
                    dailySpecialsArray.add(tuesdaySpecial);
                    dailySpecialsArray.add(wednesdaySpecial);
                    dailySpecialsArray.add(thursdaySpecial);
                    dailySpecialsArray.add(fridaySpecial);
                    dailySpecialsArray.add(saturdaySpecial);
                    dailySpecialsArray.add(sundaySpecial);
                    dailySpecialsArray.trimToSize();



                    adapterBars.notifyDataSetChanged();

                    //Update Bar

                    Bar barUpdate = new Bar(bar.getBarName(), bar.getBarCount(), bar.getBarCap(), bar.getBarAddress(), bar.getBarPhotoURI(),
                            bar.getLatitude(), bar.getLongitude(), bar.getUserId(), bar.getPlaceId(), dailySpecialsArray, bar.getBarEvent());

                    barRef.setValue(barUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(BarDetailsActivity.this, "Specials successfully created on server", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {


                    Log.d(TAG, "Array of Specials: " + bar.getDailySpecialArrayList().toString());

                    for (int i = 0; i < bar.getDailySpecialArrayList().size(); i++){
                        dailySpecialsArray.add(bar.getDailySpecialArrayList().get(i));
                    }

//TODO array list still hangs at 13 items need to reduce to 7
                    dailySpecialsArray.trimToSize();
                    Log.d(TAG, String.valueOf(dailySpecialsArray.size()));
                    if (dailySpecialsArray.size() > 7){
                        ArrayList<DailySpecial> updateList = new ArrayList<DailySpecial>(dailySpecialsArray.subList(0,6));
                        dailySpecialsArray = updateList;
                    }

                    for (int i = 0; i < dailySpecialsArray.size(); i++){
                        DailySpecial testSpecial = dailySpecialsArray.get(i);
                        Log.d(TAG, testSpecial.getDateAsString());
                        Log.d(TAG, testSpecial.getMessage());
                        Log.d(TAG, String.valueOf(testSpecial.getGenreInt()));
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
/*


        barRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Bar bar = dataSnapshot.getValue(Bar.class);
                if (bar.getSpecialsArray() == null || bar.getSpecialsArray().size() > 7){
                    Log.d(TAG, "String Array is empty or does not exist -- Or is too big");
                }else {
                    for (int j = 0; j < bar.getSpecialsArray().size(); j++){
                        dailySpecials.add(bar.getSpecialsArray().get(j));
                        Log.d(TAG, bar.getSpecialsArray().get(j));
                        //TODO update Text Views for Days

                        mondayTV.setText(bar.getSpecialsArray().get(0));
                        tuesdayTV.setText(bar.getSpecialsArray().get(1));
                        wednesdayTV.setText(bar.getSpecialsArray().get(2));
                        thursdayTV.setText(bar.getSpecialsArray().get(3));
                        fridayTV.setText(bar.getSpecialsArray().get(4));
                        saturdayTV.setText(bar.getSpecialsArray().get(5));
                        sundayTV.setText(bar.getSpecialsArray().get(6));


                    }




                }
                saveChangeTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dailySpecials = new ArrayList<>();

                        Log.d(TAG, "Daily Special Arraylist: " + dailySpecials);

                        Log.d(TAG, "SAVED");

                        String[] daysArray =  {mondayTV.getText().toString(), tuesdayTV.getText().toString(), wednesdayTV.getText().toString(),
                                thursdayTV.getText().toString(), fridayTV.getText().toString(),
                                saturdayTV.getText().toString(), sundayTV.getText().toString()};

                        Log.d(TAG, "String Array: " + Arrays.toString(daysArray));

                        dailySpecials.addAll(Arrays.asList(daysArray));

                        Bar barUpdate = new Bar(bar.getBarName(), bar.getBarCount(), bar.getBarCap(), bar.getBarAddress(), bar.getBarPhotoURI(),
                                bar.getLatitude(), bar.getLongitude(), bar.getUserId(), bar.getPlaceId(), bar.getBarEvent(), dailySpecials);

                        barRef.setValue(barUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(BarDetailsActivity.this, "Changes Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(BarDetailsActivity.this, "Update Failed: Try Again.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
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

        barNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(BarDetailsActivity.this, MainActivity.class);
                startActivity(intent1);
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()){
            case 1:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(BarDetailsActivity.this, LoginActivity.class);
                startActivity(intent);

            case 2:
                barRef.child("barCount").setValue(0);

        }

        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.menu_iconIV:
                popupMenu.show();
                break;

            /*case R.id.mondayTV:
                Log.d(TAG, "Monday");
                dayOfWeek = "Monday";
                getAlertDialog(this, dayOfWeek, mondayTV).show();
                break;

            case R.id.tuedayTV:
                Log.d(TAG, "Tuesday");
                dayOfWeek = "Tuesday";
                getAlertDialog(this, dayOfWeek, tuesdayTV).show();
                break;

            case R.id.wednesdayTV:
                Log.d(TAG, "Wednesday");
                dayOfWeek = "Wednesday";
                getAlertDialog(this, dayOfWeek, wednesdayTV).show();
                break;

            case R.id.thursdayTV:
                Log.d(TAG, "Thursday");
                dayOfWeek = "Thursday";
                getAlertDialog(this, dayOfWeek, thursdayTV).show();
                break;

            case R.id.fridayTV:
                Log.d(TAG, "Friday");
                dayOfWeek = "Friday";
                getAlertDialog(this, dayOfWeek, fridayTV).show();
                break;

            case R.id.saturdayTV:
                Log.d(TAG, "Saturday");
                dayOfWeek = "Saturday";
                getAlertDialog(this, dayOfWeek, saturdayTV).show();
                break;

            case R.id.sundayTV:
                Log.d(TAG, "Sunday");
                dayOfWeek = "Sunday";
                getAlertDialog(this, dayOfWeek, sundayTV).show();
                break;*/

            default:
                break;
        }



    }

    public boolean isEmptyStringArray(ArrayList<String> array){
        for(int i=0; i<array.size(); i++){
            if(array.get(i)!=null){
                return false;
            }
        }
        return true;
    }

    public AlertDialog.Builder getAlertDialog(final Context context, final String dayOfWeek, final TextView dayOfWeekTV){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //Try to change color

        ForegroundColorSpan titleColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.main_card_border_orange));
        ForegroundColorSpan messageColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.main_top_grey));
        SpannableStringBuilder spannableTitle = new SpannableStringBuilder(dayOfWeek);
        SpannableStringBuilder spannableMessage = new SpannableStringBuilder("What is your special for " + dayOfWeek + "?");

        spannableTitle.setSpan(titleColorSpan, 0, spannableTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableMessage.setSpan(messageColorSpan, 0, spannableMessage.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



        builder.setTitle(spannableTitle);
        builder.setMessage(spannableMessage);


        View view = LayoutInflater.from(context).inflate(R.layout.special_dialog, (ViewGroup)findViewById(android.R.id.content),false);
        final EditText specialET = (EditText) view.findViewById(R.id.specialET);
        specialET.requestFocus();
        builder.setView(view);



        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (specialET.getText() != null && !specialET.getText().toString().equals("")){
                    dialog.dismiss();
                    dayOfWeekTV.setText(specialET.getText());
                }else {
                    Toast.makeText(context, "Please enter a Special for " + dayOfWeek, Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder;
    }
}

