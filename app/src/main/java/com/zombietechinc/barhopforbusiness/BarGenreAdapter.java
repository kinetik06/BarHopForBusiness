package com.zombietechinc.barhopforbusiness;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by emmaramos on 1/27/18.
 */

public class BarGenreAdapter extends RecyclerView.Adapter<BarGenreViewHolder>{

    LayoutInflater inflater;
    ArrayList<DailySpecial> arrayOfSpecials;
    DailySpecial dailySpecial;

    public BarGenreAdapter(Context context, ArrayList<DailySpecial> arrayOfSpecials) {

        inflater=LayoutInflater.from(context);
        this.arrayOfSpecials = arrayOfSpecials;
        dailySpecial = new DailySpecial();
    }

    @Override
    public BarGenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.bar_genre,parent, false);
        BarGenreViewHolder holder = new BarGenreViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(BarGenreViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrayOfSpecials.size();
    }
}
