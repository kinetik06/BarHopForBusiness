package com.zombietechinc.barhopforbusiness;

import android.support.v7.widget.RecyclerView;
import android.view.View;
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



    public BarGenreViewHolder(View itemView) {
        super(itemView);
        danceClubTV = (TextView)itemView.findViewById(R.id.dance_clubTV);
        micTV = itemView.findViewById(R.id.micTV);
        pubTV = itemView.findViewById(R.id.pubTV);
        wineTV = itemView.findViewById(R.id.wineTV);
        liveMusicTV = itemView.findViewById(R.id.live_musicTV);
    }



}
