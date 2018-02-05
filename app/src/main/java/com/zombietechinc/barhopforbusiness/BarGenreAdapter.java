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

    public BarGenreAdapter(Context context, ArrayList<DailySpecial> arrayOfSpecials) {

        inflater=LayoutInflater.from(context);
        this.arrayOfSpecials = arrayOfSpecials;
        dailySpecial = new DailySpecial();
    }

    @Override
    public BarGenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.bar_genre,parent, false);
        final BarGenreViewHolder holder = new BarGenreViewHolder(view);





        return holder;
    }

    @Override
    public void onBindViewHolder(final BarGenreViewHolder holder, int position) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        context1 = holder.dayOfWeek.getContext();
        viewGroup = holder.viewGroup;
        final DailySpecial dailySpecial = arrayOfSpecials.get(position);
        holder.dayOfWeek.setText(dailySpecial.getDateAsString());




            //holder.specialTV.setText(dailySpecial.getMessage());
            Log.d("From ViewHolder: ", dailySpecial.getDateAsString());






        holder.specialTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlertDialog(context1, dailySpecial.dateAsString,holder.specialTV, viewGroup).show();
                Log.d("Day CLicked: ", dailySpecial.getDateAsString());
                notifyDataSetChanged();

            }
        });




    }

    @Override
    public int getItemCount() {
        return arrayOfSpecials.size();
    }

    public AlertDialog.Builder getAlertDialog(final Context context, final String dayOfWeek, final TextView dayOfWeekTV, ViewGroup viewGroup){

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
                    dayOfWeekTV.setText(specialET.getText());
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
