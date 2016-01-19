package com.example.billyshen.jaiye;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by billyshen on 19/01/2016.
 */
public class GenderViewHolder extends RecyclerView.ViewHolder{

    TextView genderTitle;
    ImageView genderImage;


    public GenderViewHolder(View itemView) {

        super(itemView);

        // Fetch UI elements
        genderTitle = (TextView) itemView.findViewById(R.id.genderTitle);
        genderImage = (ImageView) itemView.findViewById(R.id.genderImage);



    }

}
