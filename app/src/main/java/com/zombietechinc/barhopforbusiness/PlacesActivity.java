package com.zombietechinc.barhopforbusiness;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PlacesActivity extends AppCompatActivity {

    private double latitude;
    private double longitude;
    private LatLng mLatLng;
    private String placeId;
    private FirebaseAuth auth;
    private DatabaseReference mDatabaseReference;
    public String barName, barAddress, userId;
    public static final String TAG = PlacesActivity.class.getSimpleName();
    private NumberPicker numberPicker;
    FirebaseAuth.AuthStateListener authStateListener;
    private String barEvent = "";
    private DatabaseReference placesRef;
    boolean barExists = false;
    TextView barEmailTV;
    BarPlace barPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }else {
            auth.signOut();
            Intent intent = new Intent(PlacesActivity.this, LoginActivity.class);
            startActivity(intent);
        }


        setContentView(R.layout.activity_places);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        placesRef = mDatabaseReference.child("bars_places");

        barEmailTV = (TextView)findViewById(R.id.emailTV);
        barEmailTV.setText("Welcome: " + user.getEmail());

        AlertDialog dialog = getWelcomeDialog(this).create();
        dialog.show();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                barName = place.getName().toString();
                barAddress = place.getAddress().toString();
                mLatLng = place.getLatLng();
                latitude = mLatLng.latitude;
                longitude = mLatLng.longitude;
                placeId = place.getId();

                //Create BarPlace Object

                barPlace = new BarPlace(userId, placeId, place.getName().toString(), place.getAddress().toString(),
                         place.getPriceLevel(), place.getRating());




                Query placesQuery = placesRef.orderByChild("placeId").equalTo(placeId);


                placesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            Log.d(TAG, "Bar Exists!" + dataSnapshot.getValue());
                            barExists = true;
                            Toast.makeText(PlacesActivity.this, "Bar has already been claimed. Please try again.", Toast.LENGTH_SHORT).show();
                        }else {
                            barExists = false;
                        }

                        if (!barExists) {
                            AlertDialog dialog1 = getPlaceDialog(PlacesActivity.this, barName, barAddress, place.getPhoneNumber().toString()).create();
                            dialog1.show();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    public AlertDialog.Builder getWelcomeDialog( Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //Try to change color

        ForegroundColorSpan titleColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.main_card_border_orange));
        ForegroundColorSpan messageColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.main_top_grey));
        SpannableStringBuilder spannableTitle = new SpannableStringBuilder("Welcome!");
        SpannableStringBuilder spannableMessage = new SpannableStringBuilder("We just need a few more details about your business. ");

        spannableTitle.setSpan(titleColorSpan, 0, spannableTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableMessage.setSpan(messageColorSpan, 0, spannableMessage.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



        builder.setTitle(spannableTitle);
        builder.setMessage(spannableMessage);


        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder;
    }

    public AlertDialog.Builder getPlaceDialog(Context context, final String barName, final String barAddress, String barNumber){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //Try to change color

        ForegroundColorSpan titleColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.main_card_border_orange));
        ForegroundColorSpan messageColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.main_top_grey));
        SpannableStringBuilder spannableTitle = new SpannableStringBuilder("Almost Done!");
        SpannableStringBuilder spannableMessage = new SpannableStringBuilder("We just have to verify your information:");

        spannableTitle.setSpan(titleColorSpan, 0, spannableTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableMessage.setSpan(messageColorSpan, 0, spannableMessage.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



        builder.setTitle(spannableTitle);
        builder.setMessage(spannableMessage);


        View view = LayoutInflater.from(context).inflate(R.layout.confirm_place_dialog, (ViewGroup)findViewById(android.R.id.content),false);
        TextView barNameTV = view.findViewById(R.id.bar_name);
        TextView barAddressTV = view.findViewById(R.id.bar_address);
        TextView barPhone = view.findViewById(R.id.bar_number);

        numberPicker = view.findViewById(R.id.number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(500);


        barNameTV.setText(barName);
        barAddressTV.setText(barAddress);
        barPhone.setText(barNumber);
        builder.setView(view);



        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Bar bar = new Bar(barName, 0, numberPicker.getValue(),barAddress,userId, latitude, longitude, userId, placeId, barEvent );
                mDatabaseReference.child("bars").child(userId).setValue(bar);
                placesRef.child(userId).setValue(barPlace);




                GeoFire geoFire = new GeoFire(mDatabaseReference.child("bars_location"));
                geoFire.setLocation(userId, new GeoLocation(latitude, longitude));
                Intent intent = new Intent(PlacesActivity.this, MainActivity.class);
                startActivity(intent);


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
