package com.example.zizo.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zizo.myapplication.Adapters.Place_adapter;
import com.example.zizo.myapplication.DataModel.placemodel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


///Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//setSupportActionBar(toolbar);
    //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    // getSupportActionBar().setDisplayShowHomeEnabled(true);

    //  https://stackoverflow.com/questions/35810229/how-to-display-and-set-click-event-on-back-arrow-on-toolbar
    EditText Edit_search;
    TextView searchTxt;
    ListView list_place;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);


        progressDialog = new ProgressDialog(MainActivity.this);
        Edit_search = (EditText) findViewById(R.id.edit_search);
        searchTxt = (TextView) findViewById(R.id.textView);
        list_place = (ListView) findViewById(R.id.listView);

        Edit_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetPlacesFormatAPI getPlacesFormatAPI = new GetPlacesFormatAPI();
//                maps.googleapis.com / maps / api / place / nearbysearch / jso
                String url = "n?location=-33.8670522,151.1957362&radius=500&type=restaurant&keyword=cruise&key=AIzaSyByy9wb2_lp8zUZSlDBVFgisRmyAVIsjXM";
                //executeWebService(url);
                getPlacesFormatAPI.execute(url);
            }
        });
    }


    class GetPlacesFormatAPI extends AsyncTask<String, Void, placemodel[]> {
        @Override
        protected void onPreExecute() {


            progressDialog.setMessage("Loading . . . ");
            progressDialog.show();
        }

        OkHttpClient client = new OkHttpClient();

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        @Override
        protected placemodel[] doInBackground(String... url) {

            // String url = "http://api.themoviedb.org/3/discover/movie?api_key=be32430c9f675ed7df41fbeda2a0525a&language=en-US&sort_by=" + sortByVarible + "&page=1";
//                executeWebService(url);
            try {
                String s = run(url[0]);
                Log.d("yarab", s);
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                //mTextView.setText(jsonArray.toString());

                final placemodel[] PlaceModels;
                PlaceModels = new Gson().fromJson(jsonArray.toString(), placemodel[].class);
                return PlaceModels;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;


        }

        @Override
        protected void onPostExecute(final placemodel[] PlaceModels) {

            progressDialog.dismiss();
            if (PlaceModels != null) {


                Place_adapter PlaceAdapter = new Place_adapter(MainActivity.this, PlaceModels);
                list_place.setAdapter(PlaceAdapter);
                list_place.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // Toast.makeText(MainActivity.this, PlaceModels[i].getTitle(), Toast.LENGTH_SHORT).show();
                    }


                });

            }
        }
    }

}

// AIzaSyByy9wb2_lp8zUZSlDBVFgisRmyAVIsjXM


////void setSubmitButtonEnabled (boolean enabled)

//http://mrbool.com/how-to-customize-different-buttons-in-android/27747
//https://drive.google.com/drive/folders/0BxCn49aM_sqMMDVqa1FPYWpsOXc
//https://html-color-codes.info/colors-from-image/
//https://developer.android.com/guide/topics/search/search-dialog.html