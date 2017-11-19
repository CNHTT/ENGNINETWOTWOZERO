package com.extra.saas.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.extra.adapter.BaseListAdapter;
import com.extra.saas.R;
import com.extra.saas.VideoShowOnItemClickListener;
import com.extra.saas.model.SmetaBean;
import com.extra.saas.model.VideoShowBean;
import com.extra.saas.util.JsonUtil;
import com.extra.utils.ContextUtils;
import com.extra.utils.Utils;

import java.util.List;

/**
 * Created by Extra on 2017/11/19.
 */

public class FavoriteAdapter extends BaseListAdapter<VideoShowBean> {


    VideoShowOnItemClickListener listener;

    public FavoriteAdapter(Context mContext, List<VideoShowBean> mDatas,VideoShowOnItemClickListener listener) {
        super(mContext, mDatas);
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = ContextUtils.inflate(mContext, R.layout.video_fravorite);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
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
        holder.mRootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.OnLongClick(videoShowBean);
                return false;
            }
        });

        return convertView;
    }

    private class ViewHolder {
        private LinearLayout mRootView;
        private ImageView mIv_thumb;
        private TextView mTv_title;
        private TextView mTv_hits;
        private TextView mTv_like;

        public ViewHolder(View itemView) {
            mRootView = (LinearLayout) itemView.findViewById(R.id.rootView);
            mIv_thumb = (ImageView)itemView. findViewById(R.id.iv_thumb);
            mTv_title = (TextView) itemView.findViewById(R.id.tv_title);
            mTv_hits = (TextView) itemView.findViewById(R.id.tv_hits);
            mTv_like = (TextView) itemView.findViewById(R.id.tv_like);
        }

    }
}
