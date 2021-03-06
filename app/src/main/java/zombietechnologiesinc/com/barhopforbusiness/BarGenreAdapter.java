package zombietechnologiesinc.com.barhopforbusiness;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    DatabaseReference dSpecial;
    int genreInt;


    public BarGenreAdapter(Context context, ArrayList<DailySpecial> arrayOfSpecials) {

        inflater=LayoutInflater.from(context);
        this.arrayOfSpecials = arrayOfSpecials;
        dailySpecial = new DailySpecial();
    }

    @Override
    public BarGenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(zombietechnologiesinc.com.barhopforbusiness.R.layout.bar_genre,parent, false);
        final BarGenreViewHolder holder = new BarGenreViewHolder(view);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            userID = firebaseUser.getUid();
        }
        context1 = holder.dayOfWeek.getContext();
        dailySpecialArrayRef = databaseReference.child("bars").child(userID).child("dailySpecialArrayList");
        dSpecial = databaseReference.child("bars_specials");



        return holder;
    }

    @Override
    public void onBindViewHolder(final BarGenreViewHolder holder, int position) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        final String dayOfTheWeek = sdf.format(d);


        viewGroup = holder.viewGroup;
        final DailySpecial dailySpecial = arrayOfSpecials.get(position);
        holder.dayOfWeek.setText(dailySpecial.getDateAsString());
        holder.specialTV.setText(dailySpecial.getMessage());

        switch (dailySpecial.getGenreInt()){
            case 1:
                holder.specialIV.setImageDrawable(context1.getResources().getDrawable(zombietechnologiesinc.com.barhopforbusiness.R.drawable.disco_ball_color2));
                holder.genreTV.setText("Dance Club");
                break;

            case 2:
                holder.specialIV.setImageDrawable(context1.getResources().getDrawable(zombietechnologiesinc.com.barhopforbusiness.R.drawable.mic_color_logo3));
                holder.genreTV.setText("Karaoke");
                break;

            case 3:
                holder.specialIV.setImageDrawable(context1.getResources().getDrawable(zombietechnologiesinc.com.barhopforbusiness.R.drawable.guitar_color_logo));
                holder.genreTV.setText("Live Music");
                break;

            case 4:
                holder.specialIV.setImageDrawable(context1.getResources().getDrawable(zombietechnologiesinc.com.barhopforbusiness.R.drawable.beermug_color_logo2));
                holder.genreTV.setText("Pub");
                break;

            case 5:
                holder.specialIV.setImageDrawable(context1.getResources().getDrawable(zombietechnologiesinc.com.barhopforbusiness.R.drawable.wine_color_logo2));
                holder.genreTV.setText("Winery");
                break;

            case 0:
                holder.specialIV.setImageDrawable(context1.getResources().getDrawable(zombietechnologiesinc.com.barhopforbusiness.R.drawable.disco_ball_color2));
                holder.genreTV.setText("");
                break;

            default:
                break;
        }


        /*holder.layoutBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dailySpecial.setGenreInt(1);
                DailySpecial dailySpecial1 = new DailySpecial(dailySpecial.getMessage(), dailySpecial.getDateAsString(), dailySpecial.getGenreInt(),dailySpecial.getDayInt());
                arrayOfSpecials.remove(dailySpecial.getDayInt());
                arrayOfSpecials.add(dailySpecial.getDayInt(), dailySpecial1);
                dailySpecialArrayRef.setValue(arrayOfSpecials);
                notifyDataSetChanged();

                //new database try

                dSpecial.child(userID).child(dayOfTheWeek).setValue(dailySpecial);

            }
        });*/

 /*       holder.layoutWine.setOnClickListener(new View.OnClickListener() {
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


*/






        //holder.specialTV.setText(dailySpecial.getMessage());
            Log.d("From ViewHolder: ", dailySpecial.getDateAsString());






        holder.layoutSpecial.setOnClickListener(new View.OnClickListener() {
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

        ForegroundColorSpan titleColorSpan = new ForegroundColorSpan(context.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.main_card_border_orange));
        ForegroundColorSpan messageColorSpan = new ForegroundColorSpan(context.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.main_top_grey));
        SpannableStringBuilder spannableTitle = new SpannableStringBuilder(dayOfWeek);
        SpannableStringBuilder spannableMessage = new SpannableStringBuilder("What is your special for " + dayOfWeek + "?");

        spannableTitle.setSpan(titleColorSpan, 0, spannableTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableMessage.setSpan(messageColorSpan, 0, spannableMessage.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



        builder.setTitle(spannableTitle);
        builder.setMessage(spannableMessage);


        View view = LayoutInflater.from(context).inflate(zombietechnologiesinc.com.barhopforbusiness.R.layout.special_dialog, (ViewGroup) viewGroup, false);
        final EditText specialET = (EditText) view.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.specialET);
        specialET.requestFocus();

        final LinearLayout danceLayout;
        final LinearLayout micLayout;
        final LinearLayout guitarLayout;
        final LinearLayout mugLayout;
        final LinearLayout wineLayout;

        danceLayout = view.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.layout_ball);
        micLayout = view.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.layout_mic);
        guitarLayout = view.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.layout_guitar);
        mugLayout = view.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.layout_mug);
        wineLayout = view.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.layout_wine);

        final TextView danceClubTV = view.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.dance_clubTV);
        final TextView micTV = view.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.micTV);
        final TextView guitarTV = view.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.live_musicTV);
        final TextView mugTV = view.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.pubTV);
        final TextView wineTV = view.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.wineTV);

        danceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dailySpecial.setGenreInt(1);
                danceLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.black));
                danceClubTV.setTextColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                wineLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                mugLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                micLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                guitarLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));

                genreInt = 1;

            }
        });

        micLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dailySpecial.setGenreInt(2);
                micLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.black));
                micTV.setTextColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                wineLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                mugLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                danceLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                guitarLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                genreInt = 2;

            }
        });

        guitarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dailySpecial.setGenreInt(3);
                guitarLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.black));
                guitarTV.setTextColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                wineLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                mugLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                micLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                danceLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                genreInt = 3;

            }
        });

        mugLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dailySpecial.setGenreInt(4);
                mugLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.black));
                mugTV.setTextColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                wineLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                danceLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                micLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                guitarLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                genreInt = 4;

            }
        });

        wineLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dailySpecial.setGenreInt(5);
                wineLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.black));
                wineTV.setTextColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                danceLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                mugLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                micLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                guitarLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                genreInt = 5;

            }
        });

        switch (dailySpecial.getGenreInt()){
            case 1:
                danceLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.black));
                danceClubTV.setTextColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                wineLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                mugLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                micLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                guitarLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                break;

            case 2:
                micLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.black));
                micTV.setTextColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                wineLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                mugLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                danceLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                guitarLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                break;

            case 3:
                guitarLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.black));
                guitarTV.setTextColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                wineLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                mugLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                micLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                danceLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                break;

            case 4:
                mugLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.black));
                mugTV.setTextColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                wineLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                danceLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                micLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                guitarLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                break;

            case 5:
                wineLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.black));
                wineTV.setTextColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                danceLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                mugLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                micLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                guitarLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                break;

            case 0:
                wineLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                danceLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                mugLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                micLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                guitarLayout.setBackgroundColor(context1.getResources().getColor(zombietechnologiesinc.com.barhopforbusiness.R.color.white));
                break;

            default:
                break;
        }


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

                    //new database try

                    dSpecial.child(dayOfWeek).child(userID).setValue(dailySpecial1);




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
