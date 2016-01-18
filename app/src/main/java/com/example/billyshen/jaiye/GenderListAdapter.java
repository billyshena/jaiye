package com.example.billyshen.jaiye;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.billyshen.jaiye.models.Gender;
import com.koushikdutta.ion.Ion;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by billyshen on 09/01/2016.
 */
class GenderListAdapter extends ArrayAdapter<Gender> {

    private final Context ctx;
    private Display display;

    public GenderListAdapter(Context context, ArrayList<Gender> genders) {
        super(context, R.layout.gender_row, genders);
        ctx = context;
        display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = convertView;
        if(customView == null) {
            customView = inflater.inflate(R.layout.gender_row, parent, false);
        }

        // Initialize Gender object to display its information
        Gender gender = getItem(position);

        // Init UI elements
        TextView genderTitle = (TextView) customView.findViewById(R.id.genderTitle);
        genderTitle.setText(gender.getName().toUpperCase());
        ImageView genderImage = (ImageView) customView.findViewById(R.id.genderImage);

        Log.d("IMAGE URL", getContext().getResources().getString(R.string.picture_url) + "/" + gender.getPicture().getName());

        // Set Gender cover
        Ion.with(genderImage)
                .placeholder(R.drawable.gender1)
                .load(getContext().getResources().getString(R.string.picture_url) + "/" + gender.getPicture().getName());

        return customView;
    }




}
