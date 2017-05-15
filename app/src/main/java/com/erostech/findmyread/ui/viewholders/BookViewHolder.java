package com.erostech.findmyread.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.erostech.findmyread.R;
import com.erostech.findmyread.data.models.Book;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by erosgarciaponte on 15/05/2017.
 */

public class BookViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.book_title)
    TextView movieTitle;
    @BindView(R.id.book_desc)
    TextView movieDesc;
    @BindView(R.id.book_cover)
    ImageView posterImg;
    @BindView(R.id.book_progress)
    ProgressBar progress;

    public BookViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(Book book) {
        if (book != null) {
            movieTitle.setText(book.getVolumeInfo().getTitle());
            movieDesc.setText(book.getVolumeInfo().getDescription());

            if (book.getVolumeInfo().getImageLinks().getThumbnail() != null
                    && !book.getVolumeInfo().getImageLinks().getThumbnail().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(book.getVolumeInfo().getImageLinks().getThumbnail())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .crossFade()
                        .into(posterImg);
            }
        }
    }
}
