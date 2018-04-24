package zombietechnologiesinc.com.barhopforbusiness;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        danceClubTV = (TextView)itemView.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.dance_clubTV);
        micTV = itemView.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.micTV);
        pubTV = itemView.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.pubTV);
        wineTV = itemView.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.wineTV);
        liveMusicTV = itemView.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.live_musicTV);
        dayOfWeek = itemView.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.dayOfWeekTV);
        layoutBall = itemView.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.layout_ball);
        layoutMic = itemView.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.layout_mic);
        layoutGuitar = itemView.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.layout_guitar);
        layoutMug = itemView.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.layout_mug);
        layoutWine = itemView.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.layout_wine);
        specialTV = itemView.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.specialTV);
        viewGroup = itemView.findViewById(android.R.id.content);
        specialIV = itemView.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.special_picIV);
        genreTV = itemView.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.genreTV);
        layoutSpecial = itemView.findViewById(zombietechnologiesinc.com.barhopforbusiness.R.id.layout_special);
    }



}
