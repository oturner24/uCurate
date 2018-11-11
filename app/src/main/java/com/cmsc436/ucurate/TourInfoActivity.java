package com.cmsc436.ucurate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/*This Activity displays the tour information
*
* Still in progress
*
* */
public class TourInfoActivity extends AppCompatActivity {
    private Tour mTour;
    private static final String TOUR = "TOUR";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_info);

        mTour = getIntent().getParcelableExtra(TOUR);

        TextView title = findViewById(R.id.tour_name);
        title.setText(mTour.getTitle());

    }
}
