package com.zombietechinc.barhopforbusiness;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * Created by emmaramos on 1/27/18.
 */

public class BarGenreAdapter extends RecyclerView.Adapter<BarGenreViewHolder>{

    LayoutInflater inflater;
    ArrayList<DailySpecial> arrayOfSpecials;
    DailySpecial dailySpecial;
    Context context1;
    ViewGroup viewGroup;
    int counter = 0;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    String userID;
    DatabaseReference dailySpecialRef;
    DatabaseReference dailySpecialArrayRef;


    public BarGenreAdapter(Context context, ArrayList<DailySpecial> arrayOfSpecials) {

        inflater=LayoutInflater.from(context);
        this.arrayOfSpecials = arrayOfSpecials;
        dailySpecial = new DailySpecial();
    }

    @Override
    public BarGenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.bar_genre,parent, false);
        final BarGenreViewHolder holder = new BarGenreViewHolder(view);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            userID = firebaseUser.getUid();
        }
        context1 = holder.dayOfWeek.getContext();
        dailySpecialArrayRef = databaseReference.child("bars").child(userID).child("dailySpecialArrayList");



        return holder;
    }

    @Override
    public void onBindViewHolder(final BarGenreViewHolder holder, int position) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);


        viewGroup = holder.viewGroup;
        final DailySpecial dailySpecial = arrayOfSpecials.get(position);
        holder.dayOfWeek.setText(dailySpecial.getDateAsString());
        holder.specialTV.setText(dailySpecial.getMessage());


        switch (dailySpecial.getGenreInt()){
            case 1:
                holder.layoutBall.setBackgroundColor(context1.getResources().getColor(R.color.black));
                holder.danceClubTV.setTextColor(context1.getResources().getColor(R.color.white));
                holder.layoutWine.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutMug.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutMic.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutGuitar.setBackgroundColor(context1.getResources().getColor(R.color.white));
                break;

            case 2:
                holder.layoutMic.setBackgroundColor(context1.getResources().getColor(R.color.black));
                holder.micTV.setTextColor(context1.getResources().getColor(R.color.white));
                holder.layoutWine.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutMug.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutBall.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutGuitar.setBackgroundColor(context1.getResources().getColor(R.color.white));
                break;

            case 3:
                holder.layoutGuitar.setBackgroundColor(context1.getResources().getColor(R.color.black));
                holder.liveMusicTV.setTextColor(context1.getResources().getColor(R.color.white));
                holder.layoutWine.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutMug.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutMic.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutBall.setBackgroundColor(context1.getResources().getColor(R.color.white));
                break;

            case 4:
                holder.layoutMug.setBackgroundColor(context1.getResources().getColor(R.color.black));
                holder.pubTV.setTextColor(context1.getResources().getColor(R.color.white));
                holder.layoutWine.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutBall.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutMic.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutGuitar.setBackgroundColor(context1.getResources().getColor(R.color.white));
                break;

            case 5:
                holder.layoutWine.setBackgroundColor(context1.getResources().getColor(R.color.black));
                holder.wineTV.setTextColor(context1.getResources().getColor(R.color.white));
                holder.layoutBall.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutMug.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutMic.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutGuitar.setBackgroundColor(context1.getResources().getColor(R.color.white));
                break;

            case 0:
                holder.layoutWine.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutBall.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutMug.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutMic.setBackgroundColor(context1.getResources().getColor(R.color.white));
                holder.layoutGuitar.setBackgroundColor(context1.getResources().getColor(R.color.white));
                break;

            default:
                break;
        }


        holder.layoutBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dailySpecial.setGenreInt(1);
                DailySpecial dailySpecial1 = new DailySpecial(dailySpecial.getMessage(), dailySpecial.getDateAsString(), dailySpecial.getGenreInt(),dailySpecial.getDayInt());
                arrayOfSpecials.remove(dailySpecial.getDayInt());
                arrayOfSpecials.add(dailySpecial.getDayInt(), dailySpecial1);
                dailySpecialArrayRef.setValue(arrayOfSpecials);
                notifyDataSetChanged();

            }
        });

        holder.layoutWine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dailySpecial.setGenreInt(5);
                DailySpecial dailySpecial1 = new DailySpecial(dailySpecial.getMessage(), dailySpecial.getDateAsString(), dailySpecial.getGenreInt(),dailySpecial.getDayInt());
                arrayOfSpecials.remove(dailySpecial.getDayInt());
                arrayOfSpecials.add(dailySpecial.getDayInt(), dailySpecial1);
                dailySpecialArrayRef.setValue(arrayOfSpecials);
                notifyDataSetChanged();
            }
        });

        holder.layoutMug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dailySpecial.setGenreInt(4);
                DailySpecial dailySpecial1 = new DailySpecial(dailySpecial.getMessage(), dailySpecial.getDateAsString(), dailySpecial.getGenreInt(),dailySpecial.getDayInt());
                arrayOfSpecials.remove(dailySpecial.getDayInt());
                arrayOfSpecials.add(dailySpecial.getDayInt(), dailySpecial1);
                dailySpecialArrayRef.setValue(arrayOfSpecials);
                notifyDataSetChanged();
            }
        });

        holder.layoutMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dailySpecial.setGenreInt(2);
                DailySpecial dailySpecial1 = new DailySpecial(dailySpecial.getMessage(), dailySpecial.getDateAsString(), dailySpecial.getGenreInt(),dailySpecial.getDayInt());
                arrayOfSpecials.remove(dailySpecial.getDayInt());
                arrayOfSpecials.add(dailySpecial.getDayInt(), dailySpecial1);
                dailySpecialArrayRef.setValue(arrayOfSpecials);
                notifyDataSetChanged();
            }
        });

        holder.layoutGuitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dailySpecial.setGenreInt(3);
                DailySpecial dailySpecial1 = new DailySpecial(dailySpecial.getMessage(), dailySpecial.getDateAsString(), dailySpecial.getGenreInt(),dailySpecial.getDayInt());
                arrayOfSpecials.remove(dailySpecial.getDayInt());
                arrayOfSpecials.add(dailySpecial.getDayInt(), dailySpecial1);
                dailySpecialArrayRef.setValue(arrayOfSpecials).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context1, "Special Updated!", Toast.LENGTH_SHORT).show();
                    }
                });
                notifyDataSetChanged();
            }
        });









        //holder.specialTV.setText(dailySpecial.getMessage());
            Log.d("From ViewHolder: ", dailySpecial.getDateAsString());






        holder.specialTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlertDialog(context1, dailySpecial.dateAsString,holder.specialTV, viewGroup, dailySpecial).show();
                Log.d("Day CLicked: ", dailySpecial.getDateAsString());


            }
        });




    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public AlertDialog.Builder getAlertDialog(final Context context, final String dayOfWeek, final TextView dayOfWeekTV, ViewGroup viewGroup,
                                              final DailySpecial dailySpecial){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //Try to change color

        ForegroundColorSpan titleColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.main_card_border_orange));
        ForegroundColorSpan messageColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.main_top_grey));
        SpannableStringBuilder spannableTitle = new SpannableStringBuilder(dayOfWeek);
        SpannableStringBuilder spannableMessage = new SpannableStringBuilder("What is your special for " + dayOfWeek + "?");

        spannableTitle.setSpan(titleColorSpan, 0, spannableTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableMessage.setSpan(messageColorSpan, 0, spannableMessage.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



        builder.setTitle(spannableTitle);
        builder.setMessage(spannableMessage);


        View view = LayoutInflater.from(context).inflate(R.layout.special_dialog, (ViewGroup) viewGroup, false);
        final EditText specialET = (EditText) view.findViewById(R.id.specialET);
        specialET.requestFocus();
        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (specialET.getText() != null && !specialET.getText().toString().equals("")){
                    dialog.dismiss();
                    Log.d("TextView Text Before: ", dayOfWeekTV.getText().toString());
                    Log.d("Reference Stuff: ", userID + " " + String.valueOf(dailySpecial.getGenreInt()));
                    dailySpecialRef = databaseReference.child("bars").child(userID).child("dailySpecialArrayList")
                            .child(String.valueOf(dailySpecial.getGenreInt()));

                    DailySpecial dailySpecial1 = new DailySpecial(specialET.getText().toString(), dailySpecial.getDateAsString(), dailySpecial.getGenreInt(),dailySpecial.getDayInt());
                    arrayOfSpecials.remove(dailySpecial.getDayInt());
                    arrayOfSpecials.add(dailySpecial.getDayInt(), dailySpecial1);
                    dailySpecialArrayRef.setValue(arrayOfSpecials);
                    notifyDataSetChanged();



                    //dailySpecialRef.setValue(specialET.getText());
                    //dayOfWeekTV.setText(specialET.getText());
                    Log.d("TextView Text After: ", dayOfWeekTV.getText().toString() + dayOfWeek);

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
        counter++;
        return builder;
    }


}
