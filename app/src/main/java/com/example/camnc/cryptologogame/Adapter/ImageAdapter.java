package com.example.camnc.cryptologogame.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.camnc.cryptologogame.Common.Common;
import com.example.camnc.cryptologogame.ProgressDatabaseHelper;
import com.example.camnc.cryptologogame.R;
import com.example.camnc.cryptologogame.logoGridActivity;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private int offset = 0;
    private static final int LOGOS_PER_LEVEL = 15;

    public ImageAdapter(Context c, int levelSelected){
        context = c;
        offset = (levelSelected-1) * LOGOS_PER_LEVEL;
    }

    @Override
    public int getCount() {return LOGOS_PER_LEVEL;}//thumbs.length

    @Override
    public Object getItem(int position) {
        int positionL = position+offset;
        return Common.thumbs[positionL];
    }

    @Override
    public long getItemId(int position) {
        // not used in this example
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        position += offset;
        ImageView imageView;
        if (convertView == null) {
            // If view is not recycled, (re)initialise it
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(285, 285));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(4, 4, 4, 4);
        } else {
            // otherwise, use the original
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(Common.thumbs[position]);
        return imageView;
    }
}
