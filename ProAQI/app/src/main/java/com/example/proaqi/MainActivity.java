package com.example.proaqi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapController;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /* Map */
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapController mapController;
    private TextView etLat, etLong;
    private MapView mapApp;
    private Button btnCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Pull and handle API */
        APItoData apItoData = new APItoData();
//        new APItoData().execute("https://api.waqi.info/feed/here/?token=a371d994b74489020d2361991b5cf80a1743e425");
        apItoData.execute(getResources().getString(R.string.link));

        /* Check prerequisites before run the app */
        AccessPermission();

        /* Assign layout ID for variable */
        etLat = findViewById(R.id.latitudeInput);
        etLong = findViewById(R.id.longitudeInput);
        mapApp = findViewById(R.id.mapView);
        btnCheck = findViewById(R.id.buttonCheck);

        /* Set map features */
        mapApp.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        Configuration.getInstance().setUserAgentValue(getPackageName());
        //zoom map enable
        mapApp.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        mapApp.setClickable(true);
        mapApp.setMultiTouchControls(true);
        mapController = (MapController) mapApp.getController();
        mapController.setZoom(15);

        /* Rotation the map enable */
        RotationGestureOverlay rotationGestureOverlay = new RotationGestureOverlay(mapApp);
        rotationGestureOverlay.setEnabled(true);
        mapApp.getOverlays().add(rotationGestureOverlay);


        /* Set start point center on map */
        GeoPoint pointCenter = new GeoPoint(21.038200606048672, 105.8347319666011);

        /* Set first center view base on Geo Point */
        mapController.setCenter(pointCenter);

        /* make a marker to point with UI */
        GeoPoint markerPoint = new GeoPoint(21.038200606048672, 105.8347319666011);
        Marker myMarkerPoint = new Marker(mapApp);
        myMarkerPoint.setPosition(markerPoint);
        myMarkerPoint.setTitle("My Location");
        // deploy point Location On to overlay of map
        Drawable locationOnIcon = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_location_on_24);
        myMarkerPoint.setIcon(locationOnIcon);
        myMarkerPoint.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapApp.getOverlays().add(myMarkerPoint);

        /* Click event handle on map */
        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                double clickLat = p.getLatitude();
                double clickLong = p.getLongitude();
//                Toast.makeText(getApplicationContext(),
//                        clickLat + " - " + clickLong, Toast.LENGTH_SHORT).show();

                etLat.setText(String.valueOf(clickLat));
                etLong.setText(String.valueOf(clickLong));

                /* Set Location On icon to click point */
                myMarkerPoint.setPosition(new GeoPoint(clickLat, clickLong));
                myMarkerPoint.setIcon(locationOnIcon);
                myMarkerPoint.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                mapApp.getOverlays().add(myMarkerPoint);

                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }


        };

        /* Set up click events on map then do receiver */
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mapEventsReceiver);
        mapApp.getOverlays().add(mapEventsOverlay);

        /* Button Check click event */
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new AQIViewer());
                UtilityClass.getInstance().setList(apItoData.getAllData());
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        mapApp.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        mapApp.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    void AccessPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);

        }

        /* Check INTERNET */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.INTERNET}, 1002);

        }

        /* Network status */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1003);

        }

        /* Read External */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1004);

        }

        /* Write External */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1005);

        }

        /* Wifi */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 1006);

        }
    }

    /* Add Permissions if app request */
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    permissionsToRequest.toArray(new String[0]), REQUEST_PERMISSIONS_REQUEST_CODE
            );
        }
    }

    /* Return result when request Permissions */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1001:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getApplicationContext(), "Permission Granted Fine Location", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Permission Denied Fine Location", Toast.LENGTH_SHORT).show();
                break;
            case 1002:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getApplicationContext(), "Permission Granted Access Internet", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Permission Denied Access Internet", Toast.LENGTH_SHORT).show();
                break;
            case 1003:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getApplicationContext(), "Permission Granted Access Network State", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Permission Denied Access Network State", Toast.LENGTH_SHORT).show();
                break;
            case 1004:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getApplicationContext(), "Permission Granted Read External Storage", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Permission Denied Read External Storage", Toast.LENGTH_SHORT).show();
                break;
            case 1005:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getApplicationContext(), "Permission Granted Write External Storage", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Permission Denied Write External Storage", Toast.LENGTH_SHORT).show();
                break;
            case 1006:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getApplicationContext(), "Permission Granted Access Wifi State", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Permission Denied Access Wifi State", Toast.LENGTH_SHORT).show();
                break;
        }


    }
}