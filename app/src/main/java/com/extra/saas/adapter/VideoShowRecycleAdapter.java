package com.extra.saas.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.extra.saas.R;
import com.extra.saas.VideoShowOnItemClickListener;
import com.extra.saas.model.SmetaBean;
import com.extra.saas.model.VideoShowBean;
import com.extra.saas.util.DensityUtil;
import com.extra.saas.util.JsonUtil;
import com.extra.utils.Utils;

import java.util.List;

/**
 * Created by Extra on 2017/11/15.
 */

public class VideoShowRecycleAdapter extends BaseRecyclerAdapter<VideoShowRecycleAdapter.AdapterViewHolder>
{

    private List<VideoShowBean> list;
    private int largeCardHeight, smallCardHeight;
    VideoShowOnItemClickListener listener;
    public VideoShowRecycleAdapter(List<VideoShowBean> list, Context context, VideoShowOnItemClickListener listener) {
        this.list = list;
        this.listener =listener;
        largeCardHeight = DensityUtil.dip2px(context, 150);
        smallCardHeight = DensityUtil.dip2px(context, 100);
    }
    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    @Override
    public int getAdapterItemViewType(int position) {
        return 0;
    }

    @Override
    public AdapterViewHolder getViewHolder(View view) {
        return new AdapterViewHolder(view,false);
    }


    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.video_show, parent, false);
        AdapterViewHolder vh = new AdapterViewHolder(v, true);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder holder, int position, boolean isItem) {

        VideoShowBean videoShowBean = getItem(position);
        holder.mTv_hits.setText(videoShowBean.getPost_hits());
        holder.mTv_like.setText(videoShowBean.getPost_like());
        holder.mTv_title.setText(videoShowBean.getPost_title());
        SmetaBean smetaBean = (SmetaBean) JsonUtil.stringToObject(videoShowBean.getSmeta(),SmetaBean.class);
        Glide.with(Utils.getContext())
                .load(smetaBean.getThumb())
                .into( holder.mIv_thumb);
        holder.mRootView.setOnClickListener( c ->{
            listener.OnItemClick(videoShowBean);
        });
        holder.mTv_like.setOnClickListener( c ->{
            listener.OnClickLike(videoShowBean);
        });
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            holder.mRootView.getLayoutParams().height = position % 2 != 0 ? largeCardHeight : smallCardHeight;
        }

    }


    /**
     * 刷新数据
     * @param datas
     */
    public void refresh(List<VideoShowBean> datas){
        this.list.clear();
        this.list.addAll(datas);
        notifyDataSetChanged();
    }


    /**
     * 添加数据
     * @param datas
     */
    public void addData(List<VideoShowBean> datas){
        this.list.addAll(datas);
        notifyDataSetChanged();
    }



    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout mRootView;
        private ImageView mIv_thumb;
        private TextView mTv_title;
        private TextView mTv_hits;
        private TextView mTv_like;

        public AdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem){
                mRootView = (FrameLayout) itemView.findViewById(R.id.rootView);
                mIv_thumb = (ImageView)itemView. findViewById(R.id.iv_thumb);
                mTv_title = (TextView) itemView.findViewById(R.id.tv_title);
                mTv_hits = (TextView) itemView.findViewById(R.id.tv_hits);
                mTv_like = (TextView) itemView.findViewById(R.id.tv_like);
            }
        }
    }

    public VideoShowBean getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }
}
