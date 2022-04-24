package com.example.onlinekinotavr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Movie> mData;

    public RecyclerViewAdapter(Context mContext, List<Movie> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.list_item, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.movieTitle.setText(mData.get(position).getTitle());
        holder.movieCounties.setText(mData.get(position).getCountries());
        Picasso.with(mContext).setLoggingEnabled(true);
        Picasso.with(mContext)
                .load(mData.get(position).getPoster())
                .error(R.mipmap.ic_launcher).memoryPolicy(MemoryPolicy.NO_CACHE)


                .into(holder.moviePoster);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView movieTitle;
        TextView movieCounties;
       ImageView moviePoster;
        public MyViewHolder(View itemView){
            super(itemView);
            movieTitle = (TextView) itemView.findViewById(R.id.card_title);
            movieCounties = (TextView) itemView.findViewById(R.id.card_country);
            moviePoster = (ImageView) itemView.findViewById(R.id.card_poster);
        }
    }
}
