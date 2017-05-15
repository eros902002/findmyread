package com.erostech.findmyread.data;

import com.erostech.findmyread.data.models.Book;
import com.erostech.findmyread.data.source.DataSource;
import com.erostech.findmyread.data.source.remote.BooksRemoteDataSource;

/**
 * Created by erosgarciaponte on 15/05/2017.
 */

public class BookRepository implements DataSource<Book> {

    private BooksRemoteDataSource mRemoteSource;

    public BookRepository() {
        mRemoteSource = new BooksRemoteDataSource();
    }

    @Override
    public void search(String term, int startIndex, SearchCallback<Book> callback) {
        if (callback != null) {
            if (mRemoteSource != null) {
                mRemoteSource.search(term, startIndex, callback);
            } else {
                Throwable throwable = new Throwable("There's no connected data source");
                callback.onFailure(throwable);
            }
        }
    }
}
