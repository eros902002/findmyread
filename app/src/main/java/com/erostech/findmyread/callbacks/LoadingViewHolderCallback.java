package com.erostech.findmyread.callbacks;

import android.support.annotation.Nullable;

/**
 * Created by erosgarciaponte on 15/05/2017.
 */

public interface LoadingViewHolderCallback {
    void showRetry(boolean show, @Nullable String errorMessage);
}
