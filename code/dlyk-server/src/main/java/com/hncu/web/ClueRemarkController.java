package com.hncu.web;

import com.github.pagehelper.PageInfo;
import com.hncu.model.TActivityRemark;
import com.hncu.query.ActivityRemarkQuery;
import com.hncu.query.ClueRemarkQuery;
import com.hncu.result.R;
import com.hncu.service.ClueRemarkService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author caimeisahng
 * @Date 2024/8/4 20:10
 * @Version 1.0
 */

@RestController
public class ClueRemarkController {

    @Resource
    private ClueRemarkService clueRemarkService;

    @PostMapping("/api/clue/remark")
    public R addActivityRemark(@RequestBody ClueRemarkQuery clueRemarkQuery
            , @RequestHeader(value = "Authorization") String token){
        //axiox提交的post请求，提交过来的是json数据，使用@RequestBody注解接收
        clueRemarkQuery.setToken(token);
        int save = clueRemarkService.saveClueRemark(clueRemarkQuery);
        return save >= 1 ? R.OK() : R.FAIL();
    }

    @GetMapping(value = "/api/clue/remark")
    public R clueRemarkPage(@RequestParam(value = "current" ,required = false) Integer current,
                                @RequestParam(value = "clueId" ) Integer clueId){
        ClueRemarkQuery clueRemarkQuery = new ClueRemarkQuery();
        clueRemarkQuery.setClueId(clueId);
        // 传入参数为非必须传入
        if (current == null) {
            current = 1;
        }

        PageInfo<TActivityRemark> pageInfo = clueRemarkService.getClueRemarkPage(current,clueRemarkQuery);
        return R.OK(pageInfo);
    }
}
