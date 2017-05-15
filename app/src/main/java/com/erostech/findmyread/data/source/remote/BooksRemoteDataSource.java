package com.erostech.findmyread.data.source.remote;

import com.erostech.findmyread.Constants;
import com.erostech.findmyread.api.BookSearchApi;
import com.erostech.findmyread.api.BookSearchService;
import com.erostech.findmyread.data.models.Book;
import com.erostech.findmyread.data.models.BookSearchResult;
import com.erostech.findmyread.data.source.DataSource;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by erosgarciaponte on 15/05/2017.
 */

public class BooksRemoteDataSource implements DataSource<Book> {
    private BookSearchApi mApi;

    public BooksRemoteDataSource() {
        mApi = BookSearchService.getInstance().getApi();
    }

    @Override
    public void search(String term, int startIndex, final SearchCallback<Book> callback) {
        if (callback != null && term != null && !term.isEmpty()) {
            loadMovies(term, startIndex, new Observer<BookSearchResult>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    callback.onFailure(e);
                }

                @Override
                public void onNext(BookSearchResult bookSearchResult) {
                    callback.onSuccess(bookSearchResult.getBooks(), bookSearchResult.getTotalItems());
                }
            });
        }
    }

    private void loadMovies(String term, int startIndex, Observer<BookSearchResult> observer) {
                mApi.searchBooks(Constants.API_KEY, term, startIndex, Constants.MAX_PAGE_RESULTS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(observer);
    }
}
