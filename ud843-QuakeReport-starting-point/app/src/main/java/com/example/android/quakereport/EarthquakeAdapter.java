package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by erlangga on 10/12/16.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake>{

    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item,parent,false);
        }

        Earthquake currentEarthquake = getItem(position);

        String magnitude = formatDecimal(currentEarthquake.getmMagnitude());
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);
        magnitudeView.setText(magnitude);


        String location = currentEarthquake.getmLocation();
        if(location.contains(",")) {
            String[] place = location.split(",");

            TextView nearly = (TextView) listItemView.findViewById(R.id.location_offset);
            nearly.setText(place[0]);

            TextView locationView = (TextView) listItemView.findViewById(R.id.primary_location);
            locationView.setText(place[1]);
        }else{
            TextView locationView = (TextView) listItemView.findViewById(R.id.location_offset);
            locationView.setText(location);
        }
        long time = currentEarthquake.getmTime();

        Date dateObject = new Date(time);
        String dateText = formatDate(dateObject);
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText(dateText);

        String clock = formatTime(dateObject);
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        timeView.setText(clock);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getmMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);
        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    public String formatDate(Date dateObject){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String datetime = simpleDateFormat.format(dateObject);
        return datetime;
    }

    public String formatTime(Date dateObject){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        return simpleDateFormat.format(dateObject);
    }

    public String formatDecimal(Double mag){
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(mag);
    }
}
