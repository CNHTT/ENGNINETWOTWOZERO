package com.extra.saas.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.extra.saas.App;
import com.extra.saas.R;
import com.extra.saas.util.GlideImageLoader;
import com.extra.utils.ContextUtils;
import com.extra.view.listview.PullListView;
import com.extra.view.listview.PullToRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FreeZoneFragment extends Fragment {


    private View listHeader;
    private Banner banner;
    @BindView(R.id.mPullListView)
    PullListView mPullListView;
    @BindView(R.id.mRefreshLayout)
    PullToRefreshLayout mRefreshLayout;
    Unbinder unbinder;

    public FreeZoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_free_zone, container, false);
        unbinder = ButterKnife.bind(this, view);
        listHeader = ContextUtils.inflate(getContext(),R.layout.list_heade);
        banner = listHeader.findViewById(R.id.banner3);
        initView();

        return view;
    }

    private void initView() {
        banner.setImages(App.images)
                .setBannerTitles(App.titles)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setImageLoader(new GlideImageLoader())
                .start();
        mPullListView.addHeaderView(listHeader);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }
}
