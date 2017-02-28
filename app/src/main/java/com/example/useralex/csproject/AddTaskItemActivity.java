package com.example.useralex.csproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * this class is used to add a task item to the ListArray<TaskItem> when on a phone.
 */

public class AddTaskItemActivity extends AppCompatActivity implements OnMapReadyCallback  {

    public double[] coords = new double[2];
    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private Button mButtonAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mEditTextTitle = (EditText) findViewById(R.id.add_task_title);
        mEditTextDescription = (EditText) findViewById(R.id.add_task_description);
        mButtonAdd = (Button) findViewById(R.id.add_btn);

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Title", mEditTextTitle.getText().toString());
                returnIntent.putExtra("Description", mEditTextDescription.getText().toString());
                returnIntent.putExtra("Latitude", coords[0]);
                returnIntent.putExtra("Longitude", coords[1]);
                setResult(Activity.RESULT_OK, returnIntent);

                // Return to parent activity in onActivityResult
                finish();
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                map.clear();
                map.addMarker(new MarkerOptions().position(point));
                coords[0] = point.latitude;
                coords[1] = point.longitude;
            }
        });
     }

}
