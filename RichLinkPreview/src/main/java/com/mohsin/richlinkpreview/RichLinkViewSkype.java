package com.mohsin.richlinkpreview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mohsin.richlinkpreview.R;
import com.squareup.picasso.Picasso;

public class RichLinkViewSkype extends RelativeLayout {

    private View view;
    Context context;
    private MetaData meta;

    RelativeLayout relativeLayout;
    ImageView imageView;
    ImageView imageViewFavIcon;
    TextView textViewTitle;
    TextView textViewDesp;
    TextView textViewUrl;

    private String main_url;

    private boolean isDefaultClick = true;

    private RichLinkListener richLinkListener;


    public RichLinkViewSkype(Context context) {
        super(context);
        this.context = context;
    }

    public RichLinkViewSkype(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public RichLinkViewSkype(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public RichLinkViewSkype(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    public void initView() {

        if (findRelativeLayoutChild() != null) {
            this.view = findRelativeLayoutChild();
        } else {
            this.view = this;
            inflate(context, R.layout.skype_link_layout, this);
        }

        relativeLayout = findViewById(R.id.rich_link_card);
        imageView = findViewById(R.id.rich_link_image);
        imageViewFavIcon = findViewById(R.id.rich_link_favicon);
        textViewTitle = findViewById(R.id.rich_link_title);
        textViewDesp = findViewById(R.id.rich_link_desp);
        textViewUrl = findViewById(R.id.rich_link_url);


        if (meta.getImageUrl().isEmpty()) {
            imageView.setVisibility(GONE);
        } else {
            imageView.setVisibility(VISIBLE);
            Picasso.get()
                    .load(meta.getImageUrl())
                    .into(imageView);
        }

        if (meta.getFavicon().isEmpty()) {
            imageViewFavIcon.setVisibility(GONE);
        } else {
            imageViewFavIcon.setVisibility(VISIBLE);
            Picasso.get()
                    .load(meta.getFavicon())
                    .into(imageViewFavIcon);
        }

        if (meta.getTitle().isEmpty()) {
            textViewTitle.setVisibility(GONE);
        } else {
            textViewTitle.setVisibility(VISIBLE);
            textViewTitle.setText(meta.getTitle());
        }
        if (meta.getUrl().isEmpty()) {
            textViewUrl.setVisibility(GONE);
        } else {
            textViewUrl.setVisibility(VISIBLE);
            textViewUrl.setText(meta.getUrl());
        }
        if (meta.getDescription().isEmpty()) {
            textViewDesp.setVisibility(GONE);
        } else {
            textViewDesp.setVisibility(VISIBLE);
            textViewDesp.setText(meta.getDescription());
        }


        relativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDefaultClick) {
                    richLinkClicked();
                } else {
                    if (richLinkListener != null) {
                        richLinkListener.onClicked(view, meta);
                    } else {
                        richLinkClicked();
                    }
                }
            }
        });

    }

    private void richLinkClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(main_url));
        context.startActivity(intent);
    }

    protected RelativeLayout findRelativeLayoutChild() {
        if (getChildCount() > 0 && getChildAt(0) instanceof LinearLayout) {
            return (RelativeLayout) getChildAt(0);
        }
        return null;
    }

    public void setLinkFromMeta(MetaData metaData) {
        meta = metaData;
        initView();
    }

    public MetaData getMetaData() {
        return meta;
    }


    public void setDefaultClickListener(boolean isDefault) {
        isDefaultClick = isDefault;
    }

    public void setClickListener(RichLinkListener richLinkListener1) {
        richLinkListener = richLinkListener1;
    }


    public void setLink(String url, final ViewListener viewListener) {
        main_url = url;
        RichPreview richPreview = new RichPreview(new ResponseListener() {
            @Override
            public void onData(MetaData metaData) {
                meta = metaData;

                if (meta.getTitle().isEmpty()) {
                    viewListener.onSuccess(true);
                }

                initView();
            }

            @Override
            public void onError(Exception e) {
                viewListener.onError(e);
            }
        });
        richPreview.getPreview(url);
    }


}
