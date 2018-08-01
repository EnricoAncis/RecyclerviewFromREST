package com.release.eancis.rest_filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.release.eancis.rest_filters.adapter.PostsAdapter;
import com.release.eancis.rest_filters.data.PostDbHelper;
import com.release.eancis.rest_filters.model.PostItem;
import com.release.eancis.rest_filters.model.PostList;
import com.release.eancis.rest_filters.network.GetDataService;
import com.release.eancis.rest_filters.network.RetrofitClientInstance;
import com.release.eancis.rest_filters.tools.QuickSortDesc;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PostsAdapter.PostsAdapterOnClickHandler {
    private final String TAG = MainActivity.class.getSimpleName();

    private static final int FILTER_USER_ID = 1;

    private RecyclerView mPostsRecyclerView;
    private PostsAdapter mPostsAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private List<PostItem> mPostsListCache = null;

    Switch mFilterSwitch;
    private boolean isChecked = false;
    private boolean isLoadedOnlyFilterd = false;

    private PostDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mPostsRecyclerView = (RecyclerView) findViewById(R.id.post_recyclerview);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mPostsRecyclerView.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mPostsRecyclerView.setHasFixedSize(true);

        /*
         * The PostsAdapter is responsible for linking our weather data with the Views that
         * will end up displaying our weather data.
         */
        mPostsAdapter = new PostsAdapter(this, this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mPostsRecyclerView.setAdapter(mPostsAdapter);


        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mDbHelper = new PostDbHelper(MainActivity.this);

    }

    /**
     * Create handler for the RetrofitInstance interface, request data and load postlist
     * */
    private void loadPostsFromNetwork() {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<PostList> call = service.getAllPosts();
        call.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
               generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                showErrorMessage();
                Log.d(TAG, t.getMessage());
            }
        });
    }

    /**
     * Generate List Posts using RecyclerView
     * */
    private void generateDataList(PostList photoList) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        showPostDataView();
        mPostsListCache = photoList.getPosts();
        mPostsRecyclerView = findViewById(R.id.post_recyclerview);
        mPostsAdapter = new PostsAdapter(this, this);
        mPostsAdapter.setPosts(photoList.getPosts());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        mPostsRecyclerView.setLayoutManager(layoutManager);
        mPostsRecyclerView.setAdapter(mPostsAdapter);
        postFilter(isChecked);
    }

    /**
     * This method will make the Posts RecyclerView  visible and hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showPostDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mPostsRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the Posts
     * RecyclerView.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mPostsRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu_main, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        MenuItem item = (MenuItem) menu.findItem(R.id.action_filter);

        item.setActionView(R.layout.filter_switch);
        mFilterSwitch = (Switch) item.getActionView().findViewById(R.id.filter_switch);
        mFilterSwitch.setChecked(isChecked);

        mFilterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean switchIsChecked) {

            postFilter(switchIsChecked);
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.

        switch (item.getItemId()) {
            case R.id.action_refresh:
                mPostsAdapter.setPosts(null);
                mFilterSwitch.setChecked(false);
                loadPostsFromNetwork();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param postDescription The weather for the day that was clicked
     */
    @Override
    public void onClick(String postDescription) {
        Context context = this;
        Toast.makeText(context, postDescription, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
        * Save all current posts data.
        * */

        if(mPostsListCache.size() > 0){
            mDbHelper.deletePosts();
            for(PostItem post: mPostsListCache){
                mDbHelper.insertPost(post);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoadingIndicator.setVisibility(View.VISIBLE);

        /*
        * Check if Posts are present on DB, il that case it load post from it
        * otherwise it load post from network
        * */
        List<PostItem> postList = new ArrayList<PostItem>();
        /*
        * To improve performance if the filter is currently enable only Post of filter user
        * are loaded.
        * This is anyway a implementing choice to evaluate because if filter is now desabled
        * all posts are to be loades from network
        *  */
        if(isChecked){
            postList = mDbHelper.loadFilteredPosts(FILTER_USER_ID);
            isLoadedOnlyFilterd = true;
        }
        else{
            postList = mDbHelper.loadAllPosts();
        }

        if(postList.size() > 0){
            generateDataList(new PostList(postList));
        }
        else{
            loadPostsFromNetwork();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
        * This is the easest way to handle the DB: closing it on Activity distruction.
        * The best way is define the DB helper as a Singleton to prevent multiple manipulations
        * to the database and save your application from a potential crash
        * */
        mDbHelper.closeDB();
    }

    //Save our Array Lists of Messages for if the user navigates away
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("isChecked", isChecked);
    }

    //Load our Array Lists of Messages for when the user navigates back
    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isChecked = savedInstanceState.getBoolean("isChecked", false);
    }

    public void postFilter(boolean switchIsChecked){

        isChecked = switchIsChecked;

        if(mPostsListCache != null && mPostsListCache.size() > 0) {

            if (switchIsChecked) {

                List<PostItem> filteredList = new ArrayList<PostItem>();
                List<PostItem> orderedList = new ArrayList<PostItem>();
                List<String> postsDate = new ArrayList<String>();

                //Filtering post by user ID
                for (int index = 0; index < mPostsListCache.size(); index++) {

                    try {
                        PostItem _post = mPostsListCache.get(index);

                        long userID = _post.getUserId();
                        if (userID == FILTER_USER_ID) {
                            filteredList.add(_post);
                            postsDate.add(_post.getPublishedAt());
                        }

                    } catch (Exception e) {
                        Log.e(TAG + " Filtering", e.getMessage());
                    }
                }

                //Ordering dates
                QuickSortDesc.sort(postsDate, 0, postsDate.size() - 1);

                //Ordering post by dates Desc
                for (String dateOdered : postsDate) {
                    for (int index = 0; index < filteredList.size(); index++) {
                        try {
                            //String dateOderedStr = dateOdered;
                            PostItem _post = filteredList.get(index);
                            String postFilteredDate = _post.getPublishedAt();

                            if (postFilteredDate.equals(dateOdered)) {
                                orderedList.add(_post);
                            }

                        } catch (Exception e) {
                            Log.e(TAG + " Ordering", e.getMessage());
                        }
                    }
                }

                mPostsAdapter.setPosts(null);
                mPostsAdapter.setPosts(orderedList);

            } else {

                if(isLoadedOnlyFilterd){
                    loadPostsFromNetwork();
                    isLoadedOnlyFilterd = false;
                }
                mPostsAdapter.setPosts(null);
                mPostsAdapter.setPosts(mPostsListCache);

            }
        }
    }
}
