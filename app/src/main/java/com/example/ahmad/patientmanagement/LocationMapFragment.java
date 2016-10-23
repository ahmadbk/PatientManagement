package com.example.ahmad.patientmanagement;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationMapFragment extends Fragment {
    ArrayList<Location> locationArrayList = new ArrayList<Location>();
    String locations_url = "http://"+StaffLogin.serverAdd+"/Locations.php";
    String jsonPersonal;
    JSONObject jsonObject;
    JSONArray jsonArray;

    public LocationMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_map, container, false);
        new Background(getContext()).execute();
        return view;
    }

    protected void draw(){

        int x = 0, y = 0;
        String location = StaffLogin.patientDetails.getLocationArrayList().get(0).ward_name;
        switch(location){
            case "ICU": x = 325; y = 100; break;
            case "General Ward": x = 775; y = 250; break;
            case "Cancer Ward": x = 560; y = 580; break;
            case "X-Ray Room": x = 400; y = 505; break;
        }
        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hospital_floor_plan,myOptions);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);


        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);


        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawCircle(x, y, 15, paint);
        System.out.println(canvas.getWidth()+","+canvas.getHeight());

        ImageView imageView = (ImageView)getView().findViewById(R.id.hospitalMap);
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(mutableBitmap);
    }


    private class Background extends AsyncTask<Void, Void, Void> {

        Context context;

        public Background(Context ctx){
            context = ctx;
        }


        @Override
        protected Void doInBackground(Void... voids) {

            //Locations
//----------------------------------------------------------------------------------------------------------------------
            String dummy = getData(locations_url,""+StaffLogin.patientDetails.getTagID());
            try {
                jsonPersonal = AES.decrypt(dummy);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObject = new JSONObject(jsonPersonal);
                jsonArray = jsonObject.getJSONArray("server_response");

                int count = 0;
                String time_stamp,ward_name;
                while (count<jsonArray.length())
                {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    time_stamp = JO.getString("time_stamp");
                    ward_name = JO.getString("ward_name");
                    Location location = new Location(time_stamp,ward_name);
                    locationArrayList.add(location);
                    count++;
                }
                StaffLogin.patientDetails.setLocationArrayList(locationArrayList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            draw();
        }


        public String getData(String link,String tag_id)
        {
            String data = "";
            String jsonString = "";
            try {
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("tag_id", "UTF-8") + "=" + URLEncoder.encode(tag_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                while ((jsonString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(jsonString + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                data = stringBuilder.toString().trim();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

    }


}
