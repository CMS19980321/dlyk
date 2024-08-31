package com.hncu.service;

import com.github.pagehelper.PageInfo;
import com.hncu.model.TActivityRemark;
import com.hncu.model.TUser;
import com.hncu.query.ActivityRemarkQuery;

/**
 * @Author caimeisahng
 * @Date 2024/7/21 23:36
 * @Version 1.0
 */
public interface ActivityRemarkService {
    int saveActivityRemark(ActivityRemarkQuery remarkQuery);


    PageInfo<TActivityRemark> getActivityRemarkPage(Integer current,ActivityRemarkQuery activityRemarkQuery);

    TActivityRemark getActivityRemarkById(Integer id);

    int updateActivityRemark(ActivityRemarkQuery remarkQuery);

    int delActivityRemark(Integer id);
}
