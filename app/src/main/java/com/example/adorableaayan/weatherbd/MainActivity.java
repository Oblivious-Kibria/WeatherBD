package com.example.adorableaayan.weatherbd;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    // json object response url
    private String urlJsonObj = null;
    Marker dhakaMarker, chittagongMarker, rajshahiMarker, sylhetMarker, khulnaMarker, rangpurMarker, kishoreganjMarker;
    ImageView weather_Condition_Icon;
    private static String jsonWeatherResponse=null;
    Bitmap bitMapImage;
    TextView infoWindowHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);



        final  LatLng dhaka = new LatLng(23.7808874,90.2792374);
        dhakaMarker = googleMap.addMarker(new MarkerOptions()
                .position(dhaka)
                .title("Dhaka, Bangladesh."));


        final  LatLng chittagong = new LatLng(22.312967,91.2519282);
        chittagongMarker = googleMap.addMarker(new MarkerOptions()
                .position(chittagong)
                .title("Chittagong, Bangladesh."));

        final LatLng rajshahi = new LatLng(24.4909054,88.5689165);
        rajshahiMarker = googleMap.addMarker(new MarkerOptions()
                .title("Rajshahi")
                .position(rajshahi));

        final LatLng sylhet = new LatLng(24.9287071,91.8615006);
        sylhetMarker = googleMap.addMarker(new MarkerOptions()
                .title("Sylhet")
                .position(sylhet));


        final  LatLng khulna = new LatLng(22.8454448,89.4624607);
        khulnaMarker = googleMap.addMarker(new MarkerOptions()
                .position(khulna)
                .title("Khulna, Bangladesh."));

        final LatLng rangpur = new LatLng(25.7436397,89.2689792);
        rangpurMarker = googleMap.addMarker(new MarkerOptions()
                .position(rangpur)
                .title("Rangpur, Bangladesh."));

        final  LatLng kishoreganj = new LatLng(24.438133, 90.778613);
        kishoreganjMarker = googleMap.addMarker(new MarkerOptions()
                .position(kishoreganj)
                .title("Kishoreganj, Bangladesh"));

        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(dhaka , 7.0f) );

        // Setting a custom info window adapter for the google map
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker marker) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);

                // Getting reference to the TextView to set latitude
                TextView infoWindow = (TextView) v.findViewById(R.id.infoWindow);
                weather_Condition_Icon = (ImageView) v.findViewById(R.id.weather_Condition_Icon);
                infoWindowHeader = (TextView) v.findViewById(R.id.infoWindowHeader);

                if(marker.equals(dhakaMarker)){
                    infoWindowHeader.setText(dhakaMarker.getTitle());
                }
                else if(marker.equals(kishoreganjMarker)){
                    infoWindowHeader.setText(kishoreganjMarker.getTitle());
                }
                else if(marker.equals(khulnaMarker)){
                    infoWindowHeader.setText(khulnaMarker.getTitle());
                }
                else if(marker.equals(chittagongMarker)){
                    infoWindowHeader.setText(chittagongMarker.getTitle());
                }
                else if(marker.equals(sylhetMarker)){
                    infoWindowHeader.setText(sylhetMarker.getTitle());
                }
                else if(marker.equals(rajshahiMarker)){
                    infoWindowHeader.setText(rajshahiMarker.getTitle());
                }
                else if(marker.equals(rangpurMarker)){
                    infoWindowHeader.setText(rangpurMarker.getTitle());
                }
                infoWindow.setText(jsonWeatherResponse);
                weather_Condition_Icon.setImageBitmap(bitMapImage);

                // Returning the view containing InfoWindow contents
                return v;

            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.equals(dhakaMarker)){
            urlJsonObj = "http://api.openweathermap.org/data/2.5/weather?q=Dhaka,bd&APPID=2761fcafa81c6b5105148bfa92d3caf6";
            makeJsonObjectRequest(urlJsonObj, dhakaMarker);
        }
        else if(marker.equals(kishoreganjMarker)){
            urlJsonObj = "http://api.openweathermap.org/data/2.5/weather?q=Kishoreganj,bd&APPID=2761fcafa81c6b5105148bfa92d3caf6";
            makeJsonObjectRequest(urlJsonObj, kishoreganjMarker);
        }
        else if(marker.equals(chittagongMarker)){
            urlJsonObj = "http://api.openweathermap.org/data/2.5/weather?q=Chittagong,bd&APPID=2761fcafa81c6b5105148bfa92d3caf6";
            makeJsonObjectRequest(urlJsonObj, chittagongMarker);
        }
        else if(marker.equals(rajshahiMarker)){
            urlJsonObj = "http://api.openweathermap.org/data/2.5/weather?q=Rajshahi,bd&APPID=2761fcafa81c6b5105148bfa92d3caf6";
            makeJsonObjectRequest(urlJsonObj, rajshahiMarker);
        }
        else if(marker.equals(rangpurMarker)){
            urlJsonObj = "http://api.openweathermap.org/data/2.5/weather?q=Rangpur,bd&APPID=2761fcafa81c6b5105148bfa92d3caf6";
            makeJsonObjectRequest(urlJsonObj, rangpurMarker);
        }

        else if(marker.equals(sylhetMarker)){
            urlJsonObj = "http://api.openweathermap.org/data/2.5/weather?q=Sylhet,bd&APPID=2761fcafa81c6b5105148bfa92d3caf6";
            makeJsonObjectRequest(urlJsonObj, sylhetMarker);
        }
        else if(marker.equals(khulnaMarker)){
            urlJsonObj = "http://api.openweathermap.org/data/2.5/weather?q=Khulna,bd&APPID=2761fcafa81c6b5105148bfa92d3caf6";
            makeJsonObjectRequest(urlJsonObj, khulnaMarker);
        }
        return true;
    }

    /**
     * Method to make json object request where json response starts wtih {
     * */
    private void makeJsonObjectRequest(String urlJsonObj, final Marker marker) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    // response will be a json object

                      JSONArray weather = response.getJSONArray("weather");

                      JSONObject weather_Data = weather.getJSONObject(0);

                      String weather_status = weather_Data.getString("main");
                      String weather_description = weather_Data.getString("description");
                      String image_icon = weather_Data.getString("icon");

                      JSONObject humidityAndTemperature = response.getJSONObject("main");
                      String temperature = humidityAndTemperature.getString("temp");
                      String humidity = humidityAndTemperature.getString("humidity");

                        // For loading Image from url;
                        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                        String Image_URL = "http://openweathermap.org/img/w/"+image_icon+".png";
                        Log.d("IMAge _URL ", Image_URL);
                        imageLoader.get(Image_URL, new ImageLoader.ImageListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Image Error", "Image Load Error: " + error.getMessage());
                            }

                            @Override
                            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                if (response.getBitmap() != null) {
                                    // load image into imageview
                                     bitMapImage = response.getBitmap();
                                }
                            }
                        });

                    jsonWeatherResponse = "Weather Status: "+weather_status+"\n"+"Description: "+weather_description+"\n"+"Temperature: "+temperature+"F"+"\n"+"Humidity: "+humidity;
                    if(jsonWeatherResponse != null && bitMapImage !=null && marker.equals(dhakaMarker)){
                        dhakaMarker.showInfoWindow();
                    }
                    else if(jsonWeatherResponse != null && bitMapImage !=null && marker.equals(kishoreganjMarker)){
                        kishoreganjMarker.showInfoWindow();
                    }
                    else if(jsonWeatherResponse != null && bitMapImage !=null && marker.equals(khulnaMarker)){
                        khulnaMarker.showInfoWindow();
                    }
                    else if(jsonWeatherResponse != null && bitMapImage !=null && marker.equals(rangpurMarker)){
                        rangpurMarker.showInfoWindow();
                    }
                    else if(jsonWeatherResponse != null && bitMapImage !=null && marker.equals(rajshahiMarker)){
                        rajshahiMarker.showInfoWindow();
                    }
                    else if(jsonWeatherResponse != null && bitMapImage !=null && marker.equals(sylhetMarker)){
                        sylhetMarker.showInfoWindow();
                    }
                    else if(jsonWeatherResponse != null && bitMapImage !=null && marker.equals(rajshahiMarker)){
                        rajshahiMarker.showInfoWindow();
                    }
                    else if(jsonWeatherResponse != null && bitMapImage !=null && marker.equals(chittagongMarker)){
                        chittagongMarker.showInfoWindow();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
       AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
