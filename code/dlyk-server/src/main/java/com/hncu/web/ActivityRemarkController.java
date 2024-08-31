package com.hncu.web;

import com.github.pagehelper.PageInfo;
import com.hncu.model.TActivityRemark;
import com.hncu.model.TUser;
import com.hncu.query.ActivityRemarkQuery;
import com.hncu.result.R;
import com.hncu.service.ActivityRemarkService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author caimeisahng
 * @Date 2024/7/21 23:20
 * @Version 1.0
 */

@RestController
public class ActivityRemarkController {

    @Resource
    private ActivityRemarkService activityRemarkService;

    @PostMapping("/api/activity/remark")
    public R addActivityRemark(@RequestBody ActivityRemarkQuery remarkQuery
            ,@RequestHeader(value = "Authorization") String token){
        //axiox提交的post请求，提交过来的是json数据，使用@RequestBody注解接收
        remarkQuery.setToken(token);
        int save = activityRemarkService.saveActivityRemark(remarkQuery);
        return save >= 1 ? R.OK() : R.FAIL();
    }


    @GetMapping(value = "/api/activity/remark")
    public R activityRemarkPage(@RequestParam(value = "current" ,required = false) Integer current,
                                @RequestParam(value = "activityId" ) Integer activityId){
        ActivityRemarkQuery activityRemarkQuery = new ActivityRemarkQuery();
        activityRemarkQuery.setActivityId(activityId);
        // 传入参数为非必须传入
        if (current == null) {
            current = 1;
        }

        PageInfo<TActivityRemark> pageInfo = activityRemarkService.getActivityRemarkPage(current,activityRemarkQuery);
        return R.OK(pageInfo);
    }

    @GetMapping(value = "/api/activity/remark/{id}")
    public R activityRemark(@PathVariable(value = "id") Integer id){

        TActivityRemark tActivityRemark = activityRemarkService.getActivityRemarkById(id);
        return R.OK(tActivityRemark);
    }


    @PutMapping("/api/activity/remark")
    public R editActivityRemark(@RequestBody ActivityRemarkQuery remarkQuery
            ,@RequestHeader(value = "Authorization") String token){
        //axiox提交的post请求，提交过来的是json数据，使用@RequestBody注解接收
        remarkQuery.setToken(token);
        int update = activityRemarkService.updateActivityRemark(remarkQuery);
        return update >= 1 ? R.OK() : R.FAIL();
    }

    @DeleteMapping(value = "/api/activity/remark/{id}")
    public R delActivityRemark(@PathVariable(value = "id") Integer id){
        int del = activityRemarkService.delActivityRemark(id);
        return del >= 1 ? R.OK() : R.FAIL();
    }

}


