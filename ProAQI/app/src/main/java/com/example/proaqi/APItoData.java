package com.example.proaqi;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * @link "https://api.waqi.info/feed/here/?token=a371d994b74489020d2361991b5cf80a1743e425"
 * */

public class APItoData extends AsyncTask<String, Void, Void> {
    private Double CO_val;
    private Double NO2_val;
    private Double O3_val;
    private Double SO2_val;
    private Double PM25_val;
    private Double PM10_val;
    private Double temperature;
    private Double windSpeed;
    private Double pressure;
    private Double humidity;
    private String station;
    private String link;
    private Double[] geoLocation = new Double[2];
    private Double aqi;

    private ArrayList<Object> allData = new ArrayList<>();

    /* Getter */
    public Double getCO_val() {
        return this.CO_val;
    }

    public Double getNO2_val() {
        return this.NO2_val;
    }

    public Double getO3_val() {
        return this.O3_val;
    }

    public Double getSO2_val() {
        return this.SO2_val;
    }

    public Double getPM25_val() {
        return this.PM25_val;
    }

    public Double getPM10_val() {
        return this.PM10_val;
    }

    public Double getTemperature() {
        return this.temperature;
    }

    public Double getWindSpeed() {
        return this.windSpeed;
    }

    public Double getAirPressure() {
        return this.pressure;
    }

    public Double getHumidity() {
        return this.humidity;
    }

    public String getStation() {
        return this.station;
    }

    public String getLink() {
        return this.link;
    }

    public Double getGeoLat() {
        return this.geoLocation[0];
    }

    public Double getGeoLong() {
        return this.geoLocation[1];
    }

    public Double getAqi() {
        return this.aqi;
    }

    public ArrayList<Object> getAllData() {
        /* assign value to array */
        this.allData.add(this.getStation());
        this.allData.add(getLink());
        this.allData.add(getGeoLat());
        this.allData.add(getGeoLong());

        this.allData.add(getTemperature());
        this.allData.add(getHumidity());
        this.allData.add(getAirPressure());
        this.allData.add(getWindSpeed());

        this.allData.add(getCO_val());
        this.allData.add(getSO2_val());
        this.allData.add(getO3_val());
        this.allData.add(getNO2_val());
        this.allData.add(getPM25_val());
        this.allData.add(getPM10_val());
        this.allData.add(getAqi());

        return this.allData;
    }


    /* Setter */
    private void setCO_val(Double CO_val) {
        this.CO_val = CO_val;
    }

    private void setNO2_val(Double NO2_val) {
        this.NO2_val = NO2_val;
    }

    private void setO3_val(Double O3_val) {
        this.O3_val = O3_val;
    }

    private void setSO2_val(Double SO2_val) {

        this.SO2_val = SO2_val;
    }

    private void setPM25_val(Double PM25_val) {

        this.PM25_val = PM25_val;
    }

    private void setPM10_val(Double PM10_val) {
        this.PM10_val = PM10_val;
    }

    private void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    private void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    private void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    private void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    private void setStation(String station) {
        this.station = station;
    }

    private void setLink(String link) {
        this.link = link;
    }

    private void setGeoLocation(Double[] geoLocation) {
        this.geoLocation = geoLocation;
    }

    private void setAqi(Double aqi) {
        this.aqi = aqi;
    }

    /**
     * @implSpec  parse JSON to variable from pulled API
     * @param responseBody JSON string
     */
    private void parseJSON(String responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody);

            /* City */
            JSONObject city = jsonObject.getJSONObject("data").getJSONObject("city");
            this.setStation(city.getString("name"));
            this.setLink(city.getString("url"));

            /* Location */
            JSONArray geo = (JSONArray) city.get("geo");
            Double[] location = new Double[2];
            for (int i = 0; i < geo.length(); i++) {
                location[i] = geo.getDouble(i);
            }
            this.setGeoLocation(location);

            /* AQI index */
            try {
                this.setAqi(jsonObject.getJSONObject("data").getDouble("aqi"));
            } catch (JSONException e) {
//                this.handleJSONException(e);
            }

            /* iAQI */
            JSONObject iaqi = jsonObject.getJSONObject("data").getJSONObject("iaqi");
            try {
                this.setCO_val(iaqi.getJSONObject("co").getDouble("v"));
            } catch (JSONException e) {
                this.setCO_val(null);
//                this.handleJSONException(e);
            }

            try {
                this.setNO2_val(iaqi.getJSONObject("no2").getDouble("v"));
            } catch (JSONException e) {
                this.setNO2_val(null);
//                this.handleJSONException(e);
            }

            try {
                this.setSO2_val(iaqi.getJSONObject("so2").getDouble("v"));
            } catch (JSONException e) {
                this.setSO2_val(null);
//                this.handleJSONException(e);
            }

            try {
                this.setO3_val(iaqi.getJSONObject("o3").getDouble("v"));
            } catch (JSONException e) {
                this.setO3_val(null);
//                this.handleJSONException(e);
            }

            try {
                this.setPM25_val(iaqi.getJSONObject("pm25").getDouble("v"));
            } catch (JSONException e) {
                this.setPM25_val(null);
//                this.handleJSONException(e);
            }

            try {
                this.setPM10_val(iaqi.getJSONObject("pm10").getDouble("v"));
            } catch (JSONException e) {
                this.setPM10_val(null);
//                this.handleJSONException(e);
            }

            try {
                this.setTemperature(iaqi.getJSONObject("t").getDouble("v"));
            } catch (JSONException e) {
                this.setTemperature(null);
//                this.handleJSONException(e);
            }

            try {
                this.setHumidity(iaqi.getJSONObject("h").getDouble("v"));
            } catch (JSONException e) {
                this.setHumidity(null);
//                this.handleJSONException(e);
            }

            try {
                this.setWindSpeed(iaqi.getJSONObject("w").getDouble("v"));
            } catch (JSONException e) {
                this.setWindSpeed(null);
//                this.handleJSONException(e);
            }

            try {
                this.setPressure(iaqi.getJSONObject("p").getDouble("v"));
            } catch (JSONException e) {
                this.setPressure(null);
//                this.handleJSONException(e);
            }

        } catch (JSONException e) {
//            this.handleJSONException(e);
        }
    }

    /**
     * @implSpec http GET and assign JSON into a String variable
     */
    public Void doInBackground(String... urls) {
        try {
            try {
                HttpURLConnection httpURLConnection;
                BufferedReader reader;
                String lineJSON;
                StringBuffer fullJSON = new StringBuffer();

                /* connect through URL with token */
                URL url = new URL(urls[0]);
                url.toURI();
                httpURLConnection = (HttpURLConnection) url.openConnection();

                // Request setup
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(5000); // 5s before timeout
                httpURLConnection.setReadTimeout(5000);

                // check if success Fetch
                int status = httpURLConnection.getResponseCode();

                if (status > 299) {
                    reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
                    while ((lineJSON = reader.readLine()) != null) {
                        fullJSON.append(lineJSON);
                    }
                    reader.close();
                } else {
                    reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    while ((lineJSON = reader.readLine()) != null) {
                        fullJSON.append(lineJSON);
                    }
                    reader.close();
                }

                /* parse JSON */
                parseJSON(fullJSON.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

}
