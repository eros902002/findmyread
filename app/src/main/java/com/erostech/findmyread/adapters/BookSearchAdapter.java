package com.erostech.findmyread.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erostech.findmyread.R;
import com.erostech.findmyread.callbacks.LoadingViewHolderCallback;
import com.erostech.findmyread.data.models.Book;
import com.erostech.findmyread.ui.viewholders.BookViewHolder;
import com.erostech.findmyread.ui.viewholders.LoadingViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erosgarciaponte on 15/05/2017.
 */

public class BookSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LoadingViewHolderCallback {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private String errorMessage;

    private List<Book> mBooks;

    public BookSearchAdapter() {
        mBooks = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_book, parent, false);
        viewHolder = new BookViewHolder(v1);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mBooks.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Book book = mBooks.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                ((BookViewHolder) holder).bind(book);
                break;
            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.bind(this, retryPageLoad, errorMessage);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mBooks == null ? 0 : mBooks.size();
    }

    public void add(Book r) {
        mBooks.add(r);
        notifyItemInserted(mBooks.size() - 1);
    }

    public void addAll(List<Book> books) {
        for (Book book : books) {
            add(book);
        }
    }

    public void remove(Book book) {
        int position = mBooks.indexOf(book);
        if (position > -1) {
            mBooks.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public Book getItem(int position) {
        return mBooks.get(position);
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Book());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mBooks.size() - 1;
        Book book = getItem(position);

        if (book != null) {
            mBooks.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public void showRetry(boolean show, @Nullable String errorMessage) {
        retryPageLoad = show;
        notifyItemChanged(mBooks.size() - 1);

        if (errorMessage != null) {
            this.errorMessage = errorMessage;
        }
    }
}
