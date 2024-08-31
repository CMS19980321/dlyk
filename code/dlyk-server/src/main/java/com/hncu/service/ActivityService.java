package com.hncu.service;

import com.github.pagehelper.PageInfo;
import com.hncu.model.TActivity;
import com.hncu.model.TUser;
import com.hncu.query.ActivityQuery;

import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/7/7 19:55
 * @Version 1.0
 */
public interface ActivityService {
    PageInfo<TActivity> getActivityByPage(Integer current , ActivityQuery activityQuery);

    int saveActivity(ActivityQuery activityQuery);

    TActivity getActivityById(Integer id);

    int updateActivity(ActivityQuery activityQuery);

    List<TActivity> getOngoingActivity();
}
