package com.example.onlinekinotavr;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
@SuppressLint("SetJavaScriptEnabled")
public class MovieDetailsFragment extends Fragment {
    List<Movie> lstMovie;
    LinearLayoutManager mLayoutManager;
    RecViewAdapter myAdapter;
    int kinopoisk_id;
    String iframeSrc;
    Handler h;
    Handler h_movie;
    private WebView webView;
    String Title_txt = "";
    String Description_txt = "";
    String Countries_txt = "";
    String year_txt = "";
    String genres = "";
    String actors_txt = "";
    String directors_txt = "";
    String time_txt = "";
    Double rate_txt = 0.0;
    Handler h_get;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstMovie = new ArrayList<Movie>();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        WebView webView = (WebView) rootView.findViewById(R.id.webView);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled( true );
        webView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );
        //webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);


        webView.getSettings().setUseWideViewPort(true);
        //webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
       // webView.getSettings().setAllowFileAccess(true);


        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        TextView title = (TextView)rootView.findViewById(R.id.title);
        TextView countries = (TextView)rootView.findViewById(R.id.countries);
        TextView desc = (TextView)rootView.findViewById(R.id.description);
        TextView year = (TextView)rootView.findViewById(R.id.year);
        TextView actors = (TextView)rootView.findViewById(R.id.actors);
        TextView directors = (TextView)rootView.findViewById(R.id.directors);
        TextView time = (TextView)rootView.findViewById(R.id.time);
        TextView rate = (TextView)rootView.findViewById(R.id.rate);
        kinopoisk_id = this.getArguments().getInt("kino_id");
        RecyclerView myrv = rootView .findViewById(R.id.recyclerview);
        System.out.println(rootView.getContext());
        myAdapter = new RecViewAdapter(rootView.getContext(),lstMovie);

        mLayoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        myrv.setLayoutManager(mLayoutManager);
        myrv.setAdapter(myAdapter);
        h_movie = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                myAdapter.notifyDataSetChanged();
                return true;
            }
        });
        h = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                System.out.println(1000);
                iframeSrc = "https:" + iframeSrc;
                System.out.println(iframeSrc);
                webView.loadUrl(iframeSrc);
                return true;

            }

        });
        h_get = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                System.out.println(1000);
                title.setText(Title_txt);
                countries.setText(Countries_txt);
                desc.setText(Description_txt);
                year.setText(year_txt);
                directors.setText(directors_txt);
                actors.setText(actors_txt);
                time.setText(time_txt);
                rate.setText(String.valueOf(rate_txt));
                if (rate_txt >5){
                    rate.setBackgroundResource(R.drawable.rate_good_background);
                }
                else{
                    rate.setBackgroundResource(R.drawable.rate_bad_background);
                }



                return true;
            }

        });


        System.out.println(kinopoisk_id);

        getInformation();
        getRec();
        getPersons();
        openMovie();



        return rootView;
    }

    protected void openMovie() {
        JSONObject data = null;
        OkHttpClient client = new OkHttpClient();
        String URL = String.format("http://videocdn.tv/api/short?api_token=fHEc7Whm1cy8rNlULnYVxvFvyDkgWYpo&kinopoisk_id=%s", kinopoisk_id);
        System.out.println(URL);
        Request request = new Request.Builder()

                .url(URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = Objects.requireNonNull(response.body()).string();
                    System.out.println(jsonData);

                    JSONObject Jobject = null;
                    try {
                        Jobject = new JSONObject(jsonData);
                        System.out.println(Jobject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject data = null;
                    try {
                        assert Jobject != null;
                        data = Jobject.getJSONArray("data").getJSONObject(0);
                        System.out.println(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {

                        if (data != null){
                        iframeSrc = data.getString("iframe_src");
                        System.out.println(iframeSrc);}
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    h.sendEmptyMessage(200);


                }
            }
        });
    }
    private void getInformation(){
        OkHttpClient client = new OkHttpClient();
        String URL = String.format("https://kinopoiskapiunofficial.tech/api/v2.2/films/%s", kinopoisk_id);


        Request request = new Request.Builder()

                .url(URL)
                .addHeader("accept", "application/json")
                .addHeader("x-api-key", "a14712c4-725a-4d3b-901d-8f3bbcb9b372")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String jsonData = Objects.requireNonNull(response.body()).string();
                    System.out.println(jsonData);

                    JSONObject Jobject = null;
                    try {
                        Jobject = new JSONObject(jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                        try {
                            JSONObject object = Jobject;

                            JSONArray jsnArray = object.getJSONArray("countries");

                            List<String> list = new ArrayList<String>();
                            for(int j = 0; j < jsnArray.length(); j++){

                                list.add(jsnArray.getJSONObject(j).getString("country"));
                            }

                            Title_txt = object.getString("nameRu");
                            Countries_txt = String.join(", ",list);
                            Description_txt = object.getString("description");
                            year_txt = object.getString("year");
                            time_txt = object.getString("filmLength") + " мин.";
                            rate_txt = object.getDouble("ratingKinopoisk");



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    h_get.sendEmptyMessage(200);


                }
            }

        });
    }
    private void getPersons() {
        OkHttpClient client = new OkHttpClient();
        String URL = String.format("https://kinopoiskapiunofficial.tech/api/v1/staff?filmId=%s", kinopoisk_id);
        System.out.println(URL);

        Request request = new Request.Builder()

                .url(URL)
                .addHeader("accept", "application/json")
                .addHeader("x-api-key", "a14712c4-725a-4d3b-901d-8f3bbcb9b372")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = Objects.requireNonNull(response.body()).string();
                    ArrayList<String> directorList = new ArrayList<String>();
                    ArrayList<String> actorList =  new ArrayList<String>();;

                    JSONArray Jobject = null;
                    try {
                        Jobject = new JSONArray(jsonData);
                        System.out.println(Jobject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    for (int i = 0; i < Jobject.length(); i++) {

                        try {
                            JSONObject object = Jobject.getJSONObject(i);

                            if(object.getString("professionKey").equals("DIRECTOR") && !object.getString("nameRu").equals("") && directorList.size()<=10)
                            {
                                directorList.add(object.getString("nameRu"));
                            }
                            if(object.getString("professionKey").equals("ACTOR") && !object.getString("nameRu").equals("") && actorList.size()<=10)
                            {
                                actorList.add(object.getString("nameRu"));
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }
                    System.out.println(actorList);
                    actors_txt = String.join(", ",actorList);
                    directors_txt = String.join(", ",directorList);
                    h_get.sendEmptyMessage(200);
                }


            }

            ;
        });
    }
    private void getRec() {
        OkHttpClient client = new OkHttpClient();
        String URL = String.format("https://kinopoiskapiunofficial.tech/api/v2.2/films/%s/similars", kinopoisk_id);
        System.out.println(URL);

        Request request = new Request.Builder()

                .url(URL)
                .addHeader("accept", "application/json")
                .addHeader("x-api-key", "a14712c4-725a-4d3b-901d-8f3bbcb9b372")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = Objects.requireNonNull(response.body()).string();
                    ArrayList<String> directorList = new ArrayList<String>();
                    ArrayList<String> actorList =  new ArrayList<String>();;

                    JSONArray Jobject = null;
                    try {
                        Jobject = new JSONObject(jsonData).getJSONArray("items");
                        System.out.println(Jobject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    for (int i = 0; i < Jobject.length(); i++) {

                        try {
                            JSONObject object = Jobject.getJSONObject(i);

                           System.out.println(object);
                            lstMovie.add(new Movie(object.getString("nameRu"),"",object.getString("posterUrlPreview"),object.getInt("filmId")));




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }
                    h_movie.sendEmptyMessage(200);



            }


        }});
    }


    private View mCustomView;

    private class MyWebChromeClient extends WebChromeClient {
        private int mOriginalOrientation;
        private FullscreenHolder mFullscreenContainer;
        private CustomViewCallback mCustomViewCollback;

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {

            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            mOriginalOrientation = getActivity().getRequestedOrientation();

            FrameLayout decor = (FrameLayout) getActivity().getWindow().getDecorView();

            mFullscreenContainer = new FullscreenHolder(getActivity());
            mFullscreenContainer.addView(view, ViewGroup.LayoutParams.MATCH_PARENT);
            decor.addView(mFullscreenContainer, ViewGroup.LayoutParams.MATCH_PARENT);
            mCustomView = view;
            mCustomViewCollback = callback;
            getActivity().setRequestedOrientation(mOriginalOrientation);

        }

        @Override
        public void onHideCustomView() {
            if (mCustomView == null) {
                return;
            }

            FrameLayout decor = (FrameLayout) getActivity().getWindow().getDecorView();
            decor.removeView(mFullscreenContainer);
            mFullscreenContainer = null;
            mCustomView = null;
            mCustomViewCollback.onCustomViewHidden();
            // show the content view.

            getActivity().setRequestedOrientation(mOriginalOrientation);
        }


        class FullscreenHolder extends FrameLayout {

            public FullscreenHolder(Context ctx) {
                super(ctx);
                setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
            }

            @Override
            public boolean onTouchEvent(MotionEvent evt) {
                return true;
            }
        }

    }



}