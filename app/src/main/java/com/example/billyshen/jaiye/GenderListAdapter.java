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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.billyshen.jaiye.models.Gender;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by billyshen on 09/01/2016.
 */
class GenderListAdapter extends ArrayAdapter<Gender> {

    private final Context ctx;

    public GenderListAdapter(Context context, ArrayList<Gender> genders) {
        super(context, R.layout.gender_row, genders);
        ctx = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = convertView;
        if(customView == null) {
            Log.d("custom view empty", "empty");
            customView = inflater.inflate(R.layout.gender_row, parent, false);
        }
        Gender gender = getItem(position);
        Log.d("gender", gender.getTitle());
        TextView genderTitle = (TextView) customView.findViewById(R.id.genderTitle);
        genderTitle.setText("This is my name bro" + position);
        ImageView genderImage = (ImageView) customView.findViewById(R.id.genderImage);
        Bitmap bp = crop(375, 100);
        genderImage.setImageBitmap(bp);

        return customView;
    }



    private Bitmap crop(int width, int height)
    {
        Bitmap bmp = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.gender1);
        Bitmap resizedBmp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        return resizedBmp;
    }
}
