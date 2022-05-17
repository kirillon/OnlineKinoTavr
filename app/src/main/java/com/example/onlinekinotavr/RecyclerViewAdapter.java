package com.example.onlinekinotavr;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
       holder.cardView.isClickable();
        System.out.println(holder.cardView.isClickable());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(holder.cardView.isClickable());
                Fragment fragment = new MovieDetailsFragment();
                Bundle bundle=new Bundle();
                bundle.putInt("kino_id", mData.get(position).getKino_id());
                System.out.println(mData.get(position).getKino_id());
                fragment.setArguments(bundle);
                AppCompatActivity activity =(AppCompatActivity) mContext;
                FragmentTransaction transaction =     activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView2, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView movieTitle;
        TextView movieCounties;
       ImageView moviePoster;
       CardView cardView;
        public MyViewHolder(View itemView){
            super(itemView);
            movieTitle = (TextView) itemView.findViewById(R.id.card_title);
            movieCounties = (TextView) itemView.findViewById(R.id.card_country);
            moviePoster = (ImageView) itemView.findViewById(R.id.card_poster);
            cardView = (CardView) itemView.findViewById(R.id.cardView);

        }
    }
}
