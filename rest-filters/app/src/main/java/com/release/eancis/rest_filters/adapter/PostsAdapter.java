package com.release.eancis.rest_filters.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.release.eancis.rest_filters.R;
import com.release.eancis.rest_filters.model.PostItem;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.release.eancis.rest_filters.tools.DateHandler.changeToEurFormat;

/**
 * Created by Enrico on 08/06/2018.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsAdapterViewHolder>{
    private final String TAG = PostsAdapter.class.getSimpleName();

    private Context mContext;
    private List<PostItem> mPostsList;
    private final PostsAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface PostsAdapterOnClickHandler {
        void onClick(String postDescription);
    }

    /**
     * Creates a PostsAdapter.
     *
     * @param cont         Application Context.
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public PostsAdapter(Context cont, PostsAdapterOnClickHandler clickHandler){
        mContext = cont;
        mClickHandler = clickHandler;
    }


    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new PostsAdapterViewHolder that holds the View for each list item
     */
    @Override
    public PostsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.posts_list_item;
        boolean shouldAttachToParentImmediately = false;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new PostsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostsAdapterViewHolder holder, int position) {
        PostItem postItem = null;
        String mDescText = "";
        try{
            postItem = mPostsList.get(position);


            holder.userID.setText(postItem.getUserId().toString());

            String postDate = changeToEurFormat( postItem.getPublishedAt() );

            holder.publishedAt.setText(postDate);
            holder.title.setText(postItem.getTitle());

            //To be preserved from exception if the string has not yet been loaded in case of quick recyclerview scrolling
            if(postItem.getDescription().length() >= 149){
                mDescText =  postItem.getDescription().substring(0, 150) + "...";
            }
            holder.description.setText(mDescText);

            Picasso.Builder builder = new Picasso.Builder(mContext);
            builder.downloader(new OkHttp3Downloader(mContext));
            builder.build().load(mPostsList.get(position).getImage())
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.image);

        } catch (Exception e) {
            Log.e("", e.getMessage());
        }

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in posts list
     */
    @Override
    public int getItemCount() {
        int result = 0;
        if(mPostsList != null){
            result = mPostsList.size();
        }
        return result;
    }

    /**
     * This method is used to set the Posts JSONArray on a c if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new PostsAdapter to display it.
     *
     * @param _postsList The new Posts JSONArray to be processed.
     */
    public void setPosts(List<PostItem> _postsList) {
        mPostsList = _postsList;
        //The adapter has new content
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a post list item.
     */
    public class PostsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView image;
        public final TextView userID;
        public final TextView publishedAt;
        public final TextView title;
        public final TextView description;

        public PostsAdapterViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.iv_image);
            userID = (TextView) itemView.findViewById(R.id.tv_user_id);
            publishedAt = (TextView) itemView.findViewById(R.id.tv_published_at);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            description = (TextView) itemView.findViewById(R.id.tv_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            PostItem mPostItem = null;
            String mDescText = "";
            try{
                mPostItem = mPostsList.get(adapterPosition);

                //To be preserved from exception if the string has not yet been loaded in case of quick recyclerview scrolling
                mDescText = mPostItem.getDescription();

            }
            catch (Exception e) {
                Log.e(TAG + " onClick", e.getMessage());
            }

            mClickHandler.onClick(mDescText);
        }
    }


}

