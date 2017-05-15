package com.erostech.findmyread.ui.fragments;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.erostech.findmyread.Constants;
import com.erostech.findmyread.R;
import com.erostech.findmyread.adapters.BookSearchAdapter;
import com.erostech.findmyread.data.BookRepository;
import com.erostech.findmyread.data.models.Book;
import com.erostech.findmyread.data.source.DataSource;
import com.erostech.findmyread.listeners.PaginationScrollListener;

import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {
    public static final String TAG = MainFragment.class.getSimpleName();

    private static final int INDEX_START = 0;

    @BindView(R.id.book_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.search_input)
    EditText mSearchInput;

    private BookSearchAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private BookRepository mRepository;

    private Unbinder mUnbinder;

    private String currentTerm;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int totalItems = 50;
    private int currentIndex = INDEX_START;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new BookSearchAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {

            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentIndex += Constants.MAX_PAGE_RESULTS;
                performSearch();
            }

            @Override
            public int getTotalPageCount() {
                return totalItems;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        mRepository = new BookRepository();

        mSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ( (actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN ))) {
                    if (mSearchInput.getText().toString().trim().isEmpty()) {
                        if (getView() != null) {
                            Snackbar.make(getView(), "No search term has been provided", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        performSearch();
                    }
                    return true;
                }
                else {
                    return false;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void performSearch() {
        String term = mSearchInput.getText().toString();

        if (!term.equalsIgnoreCase(currentTerm)) {
            mAdapter.clear();
            currentIndex = INDEX_START;
            currentTerm = term;
        }

        term = term.toLowerCase();
        term = Normalizer.normalize(term, Normalizer.Form.NFD);
        term = term.replaceAll("[^\\p{ASCII}]", "");
        term = Uri.encode(term);

        mRepository.search(term, currentIndex, new DataSource.SearchCallback<Book>() {
            @Override
            public void onSuccess(List<Book> list, int itemCount) {
                mAdapter.clear();
                if (list != null) {
                    mAdapter.addAll(list);
                    totalItems = itemCount;
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                // Sometimes Rx return throwables with null messages so checking is worth it.
                if (throwable.getMessage() != null) {
                    Log.e(TAG, throwable.getMessage());
                } else {
                    Log.e(TAG, getString(R.string.error_msg));
                }
                if (getView() != null) {
                    Snackbar.make(getView(),
                            "An error ocurred while performing the search. Please ty again in a few moments",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
