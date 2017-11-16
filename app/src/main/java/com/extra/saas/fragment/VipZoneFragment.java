package com.extra.saas.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.extra.retrofit.HttpBuilder;
import com.extra.saas.AppUrl;
import com.extra.saas.HorizontalListView;
import com.extra.saas.R;
import com.extra.saas.VideoActivity;
import com.extra.saas.VideoShowOnItemClickListener;
import com.extra.saas.adapter.TitleAdapter;
import com.extra.saas.adapter.VideoShowRecycleAdapter;
import com.extra.saas.model.VideoShowBean;
import com.extra.saas.result.Result;
import com.extra.saas.result.ResultTrems;
import com.extra.saas.result.ResultVideoInfo;
import com.extra.saas.result.ResultVideoShow;
import com.extra.saas.util.JsonUtil;
import com.extra.utils.SPUtils;
import com.player.util.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class VipZoneFragment extends BaseFragment implements VideoShowOnItemClickListener {


    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    @BindView(R.id.gv_vip_terms)
    HorizontalListView gvVipTerms;
    @BindView(R.id.rv_list)
    RecyclerView recyclerView;
    @BindView(R.id.show_data)
    TextView showData;
    @BindView(R.id.custom_view)
    XRefreshView xRefreshView;
    Unbinder unbinder;

    private int  page =1;
    private int  pageSize =10;
    private String path = "0-2";

    private VideoShowRecycleAdapter adapter;
    private List<VideoShowBean> list = new ArrayList<>();


    public VipZoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vip_zone, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        adapter= new VideoShowRecycleAdapter(list,getContext(),this);
        xRefreshView.setPullLoadEnable(true);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        loadTitle();
        loadData();
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

    private void loadTitle() {
        new HttpBuilder(AppUrl.vip_terms)
                .tag(this)
                .success( s ->{
                    L.d(s);
                    try {
                        ResultTrems result = (ResultTrems) JsonUtil.stringToObject(s,ResultTrems.class);
                        if (result.getCode()==1){
                          // 设置GirdView布局参数,横向布局的关键
                            TitleAdapter titleAdapter = new TitleAdapter(getContext(),result.getData());
                            gvVipTerms.setAdapter(titleAdapter);
                            gvVipTerms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    path=result.getData().get(position).getPath();
                                    loadData();
                                }
                            });


                        }else {
                            showDialogToast(result.getMsg());
                        }
                    }catch (Exception e){
                        Result result = (Result) JsonUtil.stringToObject(s,Result.class);
                        showDialogToast(result.getMsg());
                    }

                })
                .error( e ->{
                    showDialogToast(getResources().getString(R.string.check_net));
                })
                .get();
    }


    private void loadMoreData() {
        new HttpBuilder(AppUrl.vipZone)
                .header("VOUCHER", SPUtils.getContent(getContext(),AppUrl.Voucher))
                .params("page", String.valueOf(page))
                .params("pagesize", String.valueOf(pageSize))
                .params("path", String.valueOf(path))
                .tag(this)
                .success( s ->{
                    xRefreshView.stopLoadMore(true);
                    L.d(s);
                    try {
                        ResultVideoShow result = (ResultVideoShow) JsonUtil.stringToObject(s,ResultVideoShow.class);
                        if (result.getCode()==1){
                            if (result.getData().size()==0){
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
        new HttpBuilder(AppUrl.vipZone)
                .header("VOUCHER", SPUtils.getContent(getContext(),AppUrl.Voucher))
                .params("page", String.valueOf(page))
                .params("pagesize", String.valueOf(pageSize))
                .params("path", String.valueOf(path))
                .tag(this)
                .success( s ->{
                    xRefreshView.stopRefresh();
                    L.d(s);
                    try {
                        ResultVideoShow result = (ResultVideoShow) JsonUtil.stringToObject(s,ResultVideoShow.class);
                        if (result.getCode()==1){
                            if (result.getData().size()==0){
                                tvNoData.setVisibility(View.VISIBLE);
                            }else{
                                tvNoData.setVisibility(View.GONE);
                                page++;
                            }
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

    }
}
