package com.extra.saas;

import com.extra.saas.model.VideoShowBean;

/**
 * Created by Extra on 2017/11/15.
 */

public interface VideoShowOnItemClickListener {
    void OnItemClick(VideoShowBean videoShowBean);
    void OnClickLike(VideoShowBean videoShowBean);
    void OnLongClick(VideoShowBean videoShowBean);

}
