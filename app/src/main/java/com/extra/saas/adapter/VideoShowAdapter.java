package com.extra.saas.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.extra.adapter.BaseRecycleAdapter;
import com.extra.saas.R;
import com.extra.saas.VideoShowOnItemClickListener;
import com.extra.saas.model.SmetaBean;
import com.extra.saas.model.VideoShowBean;
import com.extra.saas.util.JsonUtil;
import com.extra.utils.Utils;

import java.util.List;

/**
 * Created by Extra on 2017/11/15.
 */

public class VideoShowAdapter extends BaseRecycleAdapter<VideoShowBean> {

    private VideoShowOnItemClickListener listener;

    public VideoShowAdapter(List<VideoShowBean> data, VideoShowOnItemClickListener listener) {
        super(data);
        this.listener =listener;
    }

    @Override
    protected void bindData(BaseViewHolder holder, int position) {
        ((TextView)holder.getView(R.id.tv_title)).setText(datas.get(position).getPost_title());
        ((TextView)holder.getView(R.id.tv_like)).setText(datas.get(position).getPost_like());
        ((TextView)holder.getView(R.id.tv_hits)).setText(datas.get(position).getPost_hits());
        SmetaBean smetaBean = (SmetaBean) JsonUtil.stringToObject(datas.get(position).getSmeta(),SmetaBean.class);
        ImageView iv = (ImageView) holder.getView(R.id.iv_thumb);
        Glide.with(Utils.getContext())
                .load(smetaBean.getThumb())
                .into(iv);
        holder.getView(position).setOnClickListener( c ->{
            listener.OnItemClick(datas.get(position));
        });
        ((TextView)holder.getView(R.id.tv_like)).setOnClickListener( c ->{
            listener.OnClickLike(datas.get(position));
        });
    }



    @Override
    public int getLayoutId() {
        return R.layout.video_show;
    }
}
