package com.release.eancis.rest_filters.data;

import android.provider.BaseColumns;

/**
 * Created by Holystar on 11/06/18.
 */

public class PostContract {

    public static final class PostsEntry implements BaseColumns {

        public static final String TABLE_NAME = "posts";
        public static final String COLUMN_POST_REMOTE_ID = "remoteId";
        public static final String COLUMN_POST_USER_ID = "userId";
        public static final String COLUMN_POST_TITLE = "title";
        public static final String COLUMN_POST_DESCRIPTION = "description";
        public static final String COLUMN_POST_PUBLISHED_AT = "publishedAt";
        public static final String COLUMN_POST_IMAGE = "image";
    }

}
