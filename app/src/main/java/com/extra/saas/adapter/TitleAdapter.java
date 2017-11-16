package com.extra.saas.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.extra.adapter.BaseListAdapter;
import com.extra.saas.R;
import com.extra.saas.model.TermsBean;
import com.extra.utils.ContextUtils;

import java.util.List;

/**
 * Created by 戴尔 on 2017/11/16.
 */

public class TitleAdapter  extends BaseListAdapter<TermsBean>{
    public TitleAdapter(Context mContext, List<TermsBean> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = ContextUtils.inflate(mContext, R.layout.simple_list_item_1);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvContent.setText(getItem(position).getName());


        return convertView;
    }

    private class ViewHolder {
        TextView tvContent;
        public ViewHolder(View convertView) {
            tvContent = (TextView) convertView.findViewById(R.id.title);
        }

    }

}
