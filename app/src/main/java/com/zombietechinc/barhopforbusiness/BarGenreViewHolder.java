package com.zombietechinc.barhopforbusiness;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.ButterKnife;

/**
 * Created by emmaramos on 1/27/18.
 */

public class BarGenreViewHolder extends RecyclerView.ViewHolder {

    TextView danceClubTV;
    TextView micTV;
    TextView pubTV;
    TextView wineTV;
    TextView liveMusicTV;
    TextView dayOfWeek;
    LinearLayout layoutBall;
    LinearLayout layoutMic;
    LinearLayout layoutGuitar;
    LinearLayout layoutMug;
    LinearLayout layoutWine;
    TextView specialTV;
    ViewGroup viewGroup;
    ImageView specialIV;
    TextView genreTV;
    LinearLayout layoutSpecial;



    public BarGenreViewHolder(View itemView) {
        super(itemView);
        danceClubTV = (TextView)itemView.findViewById(R.id.dance_clubTV);
        micTV = itemView.findViewById(R.id.micTV);
        pubTV = itemView.findViewById(R.id.pubTV);
        wineTV = itemView.findViewById(R.id.wineTV);
        liveMusicTV = itemView.findViewById(R.id.live_musicTV);
        dayOfWeek = itemView.findViewById(R.id.dayOfWeekTV);
        layoutBall = itemView.findViewById(R.id.layout_ball);
        layoutMic = itemView.findViewById(R.id.layout_mic);
        layoutGuitar = itemView.findViewById(R.id.layout_guitar);
        layoutMug = itemView.findViewById(R.id.layout_mug);
        layoutWine = itemView.findViewById(R.id.layout_wine);
        specialTV = itemView.findViewById(R.id.specialTV);
        viewGroup = itemView.findViewById(android.R.id.content);
        specialIV = itemView.findViewById(R.id.special_picIV);
        genreTV = itemView.findViewById(R.id.genreTV);
        layoutSpecial = itemView.findViewById(R.id.layout_special);
    }



}
