package com.erostech.findmyread.api;

import com.erostech.findmyread.data.models.BookSearchResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by erosgarciaponte on 15/05/2017.
 */

public interface BookSearchApi {
    @GET("volumes")
    Observable<BookSearchResult> searchBooks(
            @Query("key") String apiKey,
            @Query("q") String term,
            @Query("startIndex") int startIndex,
            @Query("maxResults") int maxResults);
}
