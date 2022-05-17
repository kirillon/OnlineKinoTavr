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
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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


public class SearchFragment extends Fragment {

    List<Movie> lstMovie;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    Handler h;
    int firstVisibleItem, visibleItemCount, totalItemCount,pageCount,itemCount;
    LinearLayoutManager mLayoutManager;
    RecyclerViewAdapter myAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        lstMovie = new ArrayList<Movie>();
        super.onCreate(savedInstanceState);
        h = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                myAdapter.notifyItemRangeChanged(itemCount, lstMovie.size());

                loading = false;
                return true;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Add the following lines to create RecyclerView
        RecyclerView myrv = view .findViewById(R.id.recyclerview);
        myAdapter = new RecyclerViewAdapter(view.getContext(),lstMovie);

        mLayoutManager = new GridLayoutManager(view.getContext(),1);
        myrv.setLayoutManager(mLayoutManager);
        myrv.setAdapter(myAdapter);
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                 System.out.println(searchView.getQuery().toString());
                searchMovie(searchView.getQuery().toString());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }
    protected void searchMovie(String res){

        OkHttpClient client = new OkHttpClient();
        String URL = String.format("https://kinopoiskapiunofficial.tech/api/v2.1/films/search-by-keyword?keyword=%s", res);

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

                    JSONObject Jobject = null;
                    try {
                        Jobject = new JSONObject(jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray Jarray = null;
                    try {
                        assert Jobject != null;
                        Jarray = Jobject.getJSONArray("films");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < Jarray.length(); i++) {

                        try {
                            JSONObject object = Jarray.getJSONObject(i);

                            JSONArray jsnArray = object.getJSONArray("countries");

                            List<String> list = new ArrayList<String>();
                            for(int j = 0; j < jsnArray.length(); j++){

                                list.add(jsnArray.getJSONObject(j).getString("country"));
                            }
                            lstMovie.add(new Movie(object.getString("nameRu"),String.join(", ",list),object.getString("posterUrl"),object.getInt("filmId")));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }



                    h.sendEmptyMessage(200);


                }
            }

        });
}}