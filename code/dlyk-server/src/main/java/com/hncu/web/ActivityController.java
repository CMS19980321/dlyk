package com.hncu.web;

import com.github.pagehelper.PageInfo;
import com.hncu.model.TActivity;
import com.hncu.model.TUser;
import com.hncu.query.ActivityQuery;
import com.hncu.query.UserQuery;
import com.hncu.result.R;
import com.hncu.service.ActivityService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;

/**
 * @Author caimeisahng
 * @Date 2024/7/7 19:50
 * @Version 1.0
 */

@RestController
public class ActivityController {

    @Resource
    private ActivityService activityService;


    /**
     * 用户信息分页查询
     * @param current
     * @return
     */
    @GetMapping("/api/activities")
    public R activityPage(@RequestParam(value = "current" ,required = false) Integer current,
                          ActivityQuery activityQuery){
        // 传入参数为非必须传入
        if (current == null) {
            current = 1;
        }

        PageInfo<TActivity> pageInfo = activityService.getActivityByPage(current , activityQuery);
        return R.OK(pageInfo);

    }

    @PostMapping("/api/activity")
    public R addActivity(ActivityQuery activityQuery , @RequestHeader(value = "Authorization") String token){
        activityQuery.setToken(token);
        int save = activityService.saveActivity(activityQuery);
        return save >= 1 ? R.OK() : R.FAIL();
    }

    @GetMapping("/api/activity/{id}")
    public R loadActivity(@PathVariable(value = "id") Integer id){
        TActivity tActivity = activityService.getActivityById(id);
        return R.OK(tActivity);
    }

    @PutMapping("/api/activity")
    public R editActivity(ActivityQuery activityQuery , @RequestHeader(value = "Authorization") String token){
        activityQuery.setToken(token);
        int update = activityService.updateActivity(activityQuery);
        return update >= 1 ? R.OK() : R.FAIL();
    }
}
