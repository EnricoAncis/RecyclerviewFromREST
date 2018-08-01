package com.release.eancis.rest_filters.tools;

import java.util.Date;
import java.util.List;

import static com.release.eancis.rest_filters.tools.DateHandler.getPostDate;

/**
 * Created by Enrico on 08/06/2018.
 */

public class QuickSortDesc {
    private static final String TAG = QuickSortDesc.class.getSimpleName();

    public static  void sort(List postsDate, int lowIndex, int hightIndex ) {
        if ( lowIndex < hightIndex ) {

            int partIndex = partition(postsDate, lowIndex, hightIndex);

            sort(postsDate, lowIndex, partIndex);
            sort(postsDate, partIndex + 1, hightIndex);
        }
    }

    private static int partition(List postsDate, int lowIndex, int hightIndex) {
        Date pivotDate = getPostDate( (String) postsDate.get(lowIndex) );

        while (true) {

            while ( getPostDate( (String) postsDate.get(lowIndex)).after( pivotDate ) ) {
                lowIndex++;
            }

            while (getPostDate( (String) postsDate.get(hightIndex)).before( pivotDate )) {
                hightIndex--;
            }

            //Swap
            if (lowIndex < hightIndex) {
                String temp = (String) postsDate.get(lowIndex);
                postsDate.set(lowIndex, postsDate.get(hightIndex));
                postsDate.set(hightIndex, temp);

            } else {
                return hightIndex;
            }
        }
    }
}
