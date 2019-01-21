package com.amniltech.filmstack.View.CustomAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;


import com.amniltech.filmstack.R;
import com.amniltech.filmstack.RetrofitService.ApiServiceClient;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieEntity;
import com.squareup.picasso.Picasso;

import io.reactivex.annotations.NonNull;

public class MovieCustomAdapter extends RecyclerView.Adapter<MovieCustomAdapter.MovieViewHolder> {
    private List<MovieEntity> movieList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_movie_item_row, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieEntity currentMovie = movieList.get(position);

        String posterUrl = ApiServiceClient.POSTER_IMAGE_BASE_URL + currentMovie.getPosterPath();
        String[] releaseDate = currentMovie.getReleaseDate().split("-");

        StringBuilder nameBuilder = new StringBuilder(currentMovie.getTitle()).append("(")
                .append(releaseDate[0]).append(")");

        StringBuilder ratingBuilder = new StringBuilder(String.valueOf(currentMovie.getVoteAverage()))
                .append("/10");


            Picasso.get().load(posterUrl)
                    .placeholder(R.drawable.test_image)
                     .error(R.drawable.test_image)
                    .into(holder.ivMoviePoster);

        holder.tvMovieName.setText(String.valueOf(nameBuilder));
        holder.tvTmdbRating.setText(String.valueOf(ratingBuilder));
//        holder.tvVotes.setText(String.valueOf(currentMovie.getVoteCount()));

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivMoviePoster;
        private TextView tvMovieName;
        private TextView tvTmdbRating;
//        private TextView tvVotes;
//        private CardView cvMovieItemContainer;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            ivMoviePoster = itemView.findViewById(R.id.iv_movie_item_poster);
            tvMovieName = itemView.findViewById(R.id.tv_movie_item_name);
            tvTmdbRating = itemView.findViewById(R.id.tv_movie_item_rating);
//            tvVotes = itemView.findViewById(R.id.tv_movie_item_votes);
//            cvMovieItemContainer = itemView.findViewById(R.id.cv_movie_item_container);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (mOnItemClickListener != null && position != RecyclerView.NO_POSITION) {
                        mOnItemClickListener.onItemClickListener(movieList.get(position));
                    }
                }
            });
        }
    }

    public void setMovieList(List<MovieEntity> newMovieList) {
        movieList.clear();
        movieList.addAll(newMovieList);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(MovieEntity movieEntity);
    }
}
