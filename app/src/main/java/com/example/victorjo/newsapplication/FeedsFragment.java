package com.example.victorjo.newsapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FeedsFragment extends Fragment{

    private static final String TAG = "FeedsFragment";
    private RecyclerView recyclerView;
    private FeedsAdapter mAdapter;
    private List<FeedItem> bookmarkedNews = new ArrayList<>();
    private List<FeedItem> mFeeds;
    private int initPosition = 0;
    private ShimmerFrameLayout shimmerFrameLayout;


    static Fragment newInstance(){
        return new FeedsFragment();
    }

    public List<FeedItem> getBookmarkedNews(){
        return bookmarkedNews;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        recyclerView.removeAllViews();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        String mQuery = "";
        new FeedsFetcherTask(null).execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.feeds_fragment, container, false);
        recyclerView = v.findViewById(R.id.news_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        shimmerFrameLayout = v.findViewById(R.id.shimmer);
//        final int defaultWidth = 600;
//        final int[] calculatedColunm = {1};
//
//
//        final ViewTreeObserver viewTreeObserver = recyclerView.getViewTreeObserver();
//        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void onGlobalLayout() {
//                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                int width = recyclerView.getWidth();
//                calculatedColunm[0] = width / defaultWidth;
//                Log.i(TAG, String.format("DefaultWidth %d Calculated %d", defaultWidth, calculatedColunm[0]));
//                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), calculatedColunm[0]));
//                setUpAdapter();
//            }
//        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
    }

    private void setUpAdapter(){
        if(isAdded()){
            mAdapter = new FeedsAdapter(mFeeds);
            recyclerView.setAdapter(mAdapter);
        }
    }


    private class FeedsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView im_bookmark;
        TextView author;
        TextView title;
        TextView content;
        TextView name;
        FeedItem  feedItem;
        ImageView newsImage;


        public FeedsHolder(LayoutInflater layoutInflater, ViewGroup group) {
            super(layoutInflater.inflate(R.layout.feed_item, group, false));
            im_bookmark = itemView.findViewById(R.id.im_bookmark);
            author = itemView.findViewById(R.id.author);
            title = itemView.findViewById(R.id.newsTitle);
            content = itemView.findViewById(R.id.newsContent);
            name = itemView.findViewById(R.id.newsName);
            newsImage = itemView.findViewById(R.id.newsImage);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


            //final int[] i = {1};
            im_bookmark.setOnClickListener(item ->{
                if(!feedItem.isBookmarked()){
                    feedItem.setBookmarked(true);
                    bookmarkNews();
                    bookmarkedNews.add(feedItem);
                }else{
                    feedItem.setBookmarked(false);
                    bookmarkNews();
                    bookmarkedNews.remove(feedItem);

                }
            });
            int position = getAdapterPosition();

//            Intent i = NewsReader.newIntent(getActivity(), feedItem.getNewsUri());
//            startActivity(i);

        }

        private void bookmarkNews(){

            if(feedItem.isBookmarked()){
                im_bookmark.setBackgroundResource(R.drawable.bookmarked_anim);
                AnimationDrawable animationDrawable = (AnimationDrawable) im_bookmark.getBackground();
                animationDrawable.start();
            }else{
                im_bookmark.setBackgroundResource(R.drawable.bookmarked_amin_reversed);
                AnimationDrawable animationDrawable = (AnimationDrawable) im_bookmark.getBackground();
                animationDrawable.start();
            }
        }

        public void bind(FeedItem feedItem, UUID id) {
            this.feedItem = feedItem;
            final int[] i = {1};

            bookmarkNews();

            title.setText(feedItem.getTitle());
            content.setText(feedItem.getDescription() == null ? feedItem.getTitle(): feedItem.getDescription());
            name.setText(feedItem.getName());
            author.setText(feedItem.getAuthor() == null ? "Breaking News" : feedItem.getAuthor());
            bindDrawable(feedItem.getUrlToImage());

        }


        public boolean newsSearch(FeedItem feedItem, UUID uuid){

            for(FeedItem item: bookmarkedNews){
                if(item.getId() == uuid){
                   return bookmarkedNews.contains(item);
                }
            }

            return false;
        }


        public void deleteSearch(FeedItem feedItem, UUID uuid){

            for(FeedItem item: bookmarkedNews){
                if(item.getId() == uuid){
                    bookmarkedNews.remove(feedItem);
                }
            }
        }

        public void bindDrawable(String url){
            newsImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(getActivity()).load(url)
                    .thumbnail(0.5f)
                    .error(getResources().getDrawable(R.drawable.news_stand_in))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(newsImage);
        }
    }

    private class FeedsAdapter extends RecyclerView.Adapter<FeedsHolder>{


        private List<FeedItem> feedItems;

        FeedsAdapter(List<FeedItem> feedItems){
            this.feedItems = feedItems;
        }

        @NonNull
        @Override
        public FeedsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FeedsHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FeedsHolder holder, int position) {
            FeedItem feedItem = feedItems.get(position);
            holder.bind(feedItem, feedItem.getId());
            Log.i(TAG, feedItem.toString());

            if(position > initPosition){

                AnimateUtil.animate(holder, true);


            }else{

                AnimateUtil.animate(holder, false);

            }
            initPosition = position;

        }

        @Override
        public int getItemCount() {
            return feedItems.size();
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class FeedsFetcherTask extends AsyncTask<Void, Void, List<FeedItem>>{

        private String mQuery;


        FeedsFetcherTask(String mQuery){

            this.mQuery = mQuery;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  shimmerFrameLayout.startShimmer();
        }

        @Override
        protected List<FeedItem> doInBackground(Void... voids) {
            if(mQuery == null){
                return new NewsFetcher().fetchRecentNews(mQuery);
            }else{
                return new NewsFetcher().searchNews(mQuery);
            }
        }

        @Override
        protected void onPostExecute(List<FeedItem> feedItems) {
            super.onPostExecute(feedItems);

            mFeeds = feedItems;
            new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };


            setUpAdapter();
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
            if(feedItems != null) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        }
    }
}
