package com.extra.saas.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.extra.retrofit.HttpBuilder;
import com.extra.saas.AdHeader;
import com.extra.saas.App;
import com.extra.saas.AppUrl;
import com.extra.saas.LoginActivity;
import com.extra.saas.MainActivity;
import com.extra.saas.R;
import com.extra.saas.VideoActivity;
import com.extra.saas.VideoShowOnItemClickListener;
import com.extra.saas.adapter.VideoShowAdapter;
import com.extra.saas.adapter.VideoShowRecycleAdapter;
import com.extra.saas.model.VideoShowBean;
import com.extra.saas.result.Result;
import com.extra.saas.result.ResultLogin;
import com.extra.saas.result.ResultVideoInfo;
import com.extra.saas.result.ResultVideoShow;
import com.extra.saas.util.GlideImageLoader;
import com.extra.saas.util.JsonUtil;
import com.extra.utils.AppManager;
import com.extra.utils.ContextUtils;
import com.extra.utils.DataUtils;
import com.extra.utils.SPUtils;
import com.extra.utils.ToastUtils;
import com.extra.view.listview.PullListView;
import com.extra.view.listview.PullToRefreshLayout;
import com.player.util.L;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FreeZoneFragment extends BaseFragment implements VideoShowOnItemClickListener {


    @BindView(R.id.custom_view)
    XRefreshView xRefreshView;
    @BindView(R.id.rv_list)
    RecyclerView recyclerView;
    @BindView(R.id.show_data)
    TextView showData;
    Unbinder unbinder;

    Banner banner;

    private View headerView;

    private int  page =1;
    private int  pageSize =10;

    private VideoShowRecycleAdapter adapter;
    private List<VideoShowBean> list = new ArrayList<>();


    public FreeZoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_free_zone, container, false);
        unbinder = ButterKnife.bind(this, view);
        loadData();
        adapter= new VideoShowRecycleAdapter(list,getContext(),this);
        headerView = adapter.setHeaderView(R.layout.bannerview, recyclerView);
        banner = headerView.findViewById(R.id.banner3);
        initView();

        return view;
    }

    private void initView() {

        xRefreshView.setPullLoadEnable(true);
        recyclerView.setHasFixedSize(true);

        banner.setImages(App.images)
                .setBannerTitles(App.titles)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setImageLoader(new GlideImageLoader())
                .start();
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        CustomGifHeader header = new CustomGifHeader(getContext());
        xRefreshView.setCustomHeaderView(header);


        xRefreshView.setCustomHeaderView(header);
        recyclerView.setAdapter(adapter);
        xRefreshView.setAutoLoadMore(false);
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setMoveForHorizontal(true);
        xRefreshView.setPullLoadEnable(true);
//        recyclerviewAdapter.setHeaderView(headerView, recyclerView);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(getContext()));

        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener(){
            @Override
            public void onRefresh(boolean isPullDown) {
                loadData();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
               loadMoreData();
            }
        });
    }

    private void loadMoreData() {
        new HttpBuilder(AppUrl.freeZone)
                .header("VOUCHER", SPUtils.getContent(getContext(),AppUrl.Voucher))
                .params("page", String.valueOf(page))
                .params("pagesize", String.valueOf(pageSize))
                .tag(this)
                .success( s ->{
                    xRefreshView.stopLoadMore(false);
                    L.d(s);
                    try {
                        ResultVideoShow result = (ResultVideoShow) JsonUtil.stringToObject(s,ResultVideoShow.class);
                        if (result.getCode()==1){
                            if (result.getData().size()==0){
                                xRefreshView.stopRefresh(true);
                            }else{
                                page++;
                            }

                            if (adapter ==null){
                                adapter= new VideoShowRecycleAdapter(result.getData(),getContext(),this);
                            }
                            else {
                                adapter.addData(result.getData());
                            };

                        }else {
                            showDialogToast(result.getMsg());
                        }
                    }catch (Exception e){
                        Result result = (Result) JsonUtil.stringToObject(s,Result.class);
                        showDialogToast(result.getMsg());
                    }

                })
                .error( e ->{
                    xRefreshView.stopLoadMore(false);
                    showDialogToast(getResources().getString(R.string.check_net));
                })
                .get();
    }

    private void loadData() {
        page =1;
        new HttpBuilder(AppUrl.freeZone)
                .header("VOUCHER", SPUtils.getContent(getContext(),AppUrl.Voucher))
                .params("page", String.valueOf(page))
                .params("pagesize", String.valueOf(pageSize))
                .tag(this)
                .success( s ->{
                    xRefreshView.stopRefresh();
                    L.d(s);
                    try {
                        ResultVideoShow result = (ResultVideoShow) JsonUtil.stringToObject(s,ResultVideoShow.class);
                        if (result.getCode()==1){
                            page++;
                           if (adapter ==null){
                               adapter= new VideoShowRecycleAdapter(result.getData(),getContext(),this);
                           }
                           else {
                               adapter.refresh(result.getData());
                           };

                        }else {
                            showDialogToast(result.getMsg());
                        }
                    }catch (Exception e){
                        Result result = (Result) JsonUtil.stringToObject(s,Result.class);
                        showDialogToast(result.getMsg());
                    }

                })
                .error( e ->{
                    xRefreshView.stopRefresh();
                    showDialogToast(getResources().getString(R.string.check_net));
                })
                .get();
    }

    @Override
    public void onResume() {
        super.onResume();
        xRefreshView.startRefresh();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void OnItemClick(VideoShowBean videoShowBean) {
        showProgressDialog(R.string.loading);
        new HttpBuilder(AppUrl.movies)
                .header("VOUCHER", SPUtils.getContent(getContext(),AppUrl.Voucher))
                .params("id", videoShowBean.getId())
                .success( s ->{
                    L.d(s);
                    cancleProgressDialog();
                    try {
                        ResultVideoInfo result = (ResultVideoInfo) JsonUtil.stringToObject(s,ResultVideoInfo.class);
                        if (result.getCode()==1){
                            Intent intent = new Intent(getActivity(), VideoActivity.class);
                            intent.putExtra("VIDEO",result.getData());
                            startActivity(intent);

                        }else {
                            showDialogToast(result.getMsg());
                        }
                    }catch (Exception e){
                        Result result = (Result) JsonUtil.stringToObject(s,Result.class);
                        showDialogToast(result.getMsg());
                    }


                })
                .error( e ->{
                    cancleProgressDialog();
                    showDialogToast(getResources().getString(R.string.check_net));
                })
                .post();


    }

    @Override
    public void OnClickLike(VideoShowBean videoShowBean) {
        if (DataUtils.isNullString(SPUtils.getString(getActivity(),AppUrl.Voucher)))
        {
            startActivity(new Intent(getActivity(),LoginActivity.class));
            return;
        }

    }
}
