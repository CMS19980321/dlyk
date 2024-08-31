package com.hncu.service;

import com.github.pagehelper.PageInfo;
import com.hncu.model.TActivityRemark;
import com.hncu.query.ClueRemarkQuery;

/**
 * @Author caimeisahng
 * @Date 2024/8/4 20:16
 * @Version 1.0
 */
public interface ClueRemarkService {
    int saveClueRemark(ClueRemarkQuery clueRemarkQuery);

    PageInfo<TActivityRemark> getClueRemarkPage(Integer current, ClueRemarkQuery clueRemarkQuery);
}
