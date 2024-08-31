package com.hncu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hncu.constant.Constants;
import com.hncu.mapper.TActivityMapper;
import com.hncu.model.TActivity;
import com.hncu.model.TUser;
import com.hncu.query.ActivityQuery;
import com.hncu.query.BaseQuery;
import com.hncu.service.ActivityService;
import com.hncu.util.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/7/7 19:57
 * @Version 1.0
 */

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private TActivityMapper activityMapper;

    @Override
    public PageInfo<TActivity> getActivityByPage(Integer current , ActivityQuery activityQuery) {
        //1.设置PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        //2.查询
        List<TActivity> list = activityMapper.selectActivityByPage(activityQuery);
        //3.封装分页数据到PageInfo
        PageInfo<TActivity> info = new PageInfo<>(list);

        return info;

    }

    //增删改操作，异常时加上事务回滚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveActivity(ActivityQuery activityQuery) {
        TActivity tActivity = new TActivity();
        //将activityQuery对象中的属性数据复制到tActivity对象中(复制要求:两个对象的属性名相同，属性类型要相同，这样才能进行复制)
        BeanUtils.copyProperties(activityQuery,tActivity);

        //密码加密
        tActivity.setCreateTime(new Date());

        //解析token，获取登陆人id
        Integer loginUserId = JWTUtils.parseJWTByUserId(activityQuery.getToken());
        tActivity.setCreateBy(loginUserId);

        //对应sql语句存在空值判断，空值不插入
        return activityMapper.insertSelective(tActivity);
    }

    @Override
    public TActivity getActivityById(Integer id) {
        return activityMapper.selectDetailByPrimaryKey(id);
    }

    //增删改操作，异常时加上事务回滚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateActivity(ActivityQuery activityQuery) {
        TActivity tActivity = new TActivity();
        //将UserQuery对象中的属性数据复制到TUser对象中(复制要求:两个对象的属性名相同，属性类型要相同，这样才能进行复制)
        BeanUtils.copyProperties(activityQuery,tActivity);

        tActivity.setEditTime(new Date());


        //解析token，获取登陆人id
        Integer loginUserId = JWTUtils.parseJWTByUserId(activityQuery.getToken());
        tActivity.setEditBy(loginUserId);

        //传入空值不更新
        return activityMapper.updateByPrimaryKeySelective(tActivity);
    }

    @Override
    public List<TActivity> getOngoingActivity() {
        return activityMapper.selectOngoingActivity();
    }
}
