package com.example.proaqi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AQIViewer extends Fragment {

    private TextView stationName;
    private TextView stationLink;
    private TextView stationLat;
    private TextView stationLong;

    private TextView temperature;
    private TextView humidity;
    private TextView pressure;
    private TextView windSpeed;

    private TextView CO_val;
    private TextView SO2_val;
    private TextView O3_val;
    private TextView NO2_val;
    private TextView PM25_val;
    private TextView PM10_val;
    private TextView aqi;

    private ArrayList<Object> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aqiviewer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stationName = getView().findViewById(R.id.stationName);
        stationLink = getView().findViewById(R.id.stationLink);
        stationLat = getView().findViewById(R.id.stationLat);
        stationLong = getView().findViewById(R.id.stationLong);

        temperature = getView().findViewById(R.id.temperature);
        humidity = getView().findViewById(R.id.humidity);
        pressure = getView().findViewById(R.id.pressure);
        windSpeed = getView().findViewById(R.id.windSpeed);

        CO_val = getView().findViewById(R.id.COValue);
        SO2_val = getView().findViewById(R.id.SO2Value);
        O3_val = getView().findViewById(R.id.O3Value);
        NO2_val = getView().findViewById(R.id.NO2Value);
        PM25_val = getView().findViewById(R.id.PM25);
        PM10_val = getView().findViewById(R.id.PM10);
        aqi = getView().findViewById(R.id.AQI);

        /* take data from Activity */
        data = UtilityClass.getInstance().getList();

        /* Set Value for layout elements */
        stationName.setText(String.valueOf(data.get(0)));
        stationLink.setText(String.valueOf(data.get(1)));
        stationLat.setText(String.valueOf(data.get(2)));
        stationLong.setText(String.valueOf(data.get(3)));

        temperature.setText(String.valueOf("Temperature: " + data.get(4)));
        humidity.setText(String.valueOf("Humidity: " + data.get(5)));
        pressure.setText(String.valueOf("Air Pressure: " + data.get(6)));
        windSpeed.setText(String.valueOf("Wind Speed: " + data.get(7)));

        CO_val.setText(String.valueOf("CO value: " + data.get(8)));
        SO2_val.setText(String.valueOf("SO2 value: " + data.get(9)));
        O3_val.setText(String.valueOf("O3 value: " + data.get(10)));
        NO2_val.setText(String.valueOf("NO2 value: " + data.get(11)));
        PM25_val.setText(String.valueOf("PM 2.5: " + data.get(12)));
        PM10_val.setText(String.valueOf("PM 10: " + data.get(13)));
        aqi.setText(String.valueOf("AQI: " + data.get(14)));
        aqi.setTextColor(getResources().getColor(R.color.purple_500, null));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}