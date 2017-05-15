package com.erostech.findmyread.data.source;

import java.util.List;

/**
 * Created by erosgarciaponte on 15/05/2017.
 */

public interface DataSource<T> {

    void search(String term, int startIndex, SearchCallback<T> callback);

    interface SearchCallback<T> {
        void onSuccess(List<T> list, int itemCount);
        void onFailure(Throwable throwable);
    }
}
