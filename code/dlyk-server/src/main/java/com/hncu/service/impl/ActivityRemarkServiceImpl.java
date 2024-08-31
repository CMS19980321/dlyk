package com.hncu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hncu.constant.Constants;
import com.hncu.mapper.TActivityRemarkMapper;
import com.hncu.model.TActivity;
import com.hncu.model.TActivityRemark;
import com.hncu.model.TUser;
import com.hncu.query.ActivityRemarkQuery;
import com.hncu.query.BaseQuery;
import com.hncu.service.ActivityRemarkService;
import com.hncu.service.ActivityService;
import com.hncu.util.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/7/21 23:37
 * @Version 1.0
 */

@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {

    @Resource
    private TActivityRemarkMapper activityRemarkMapper;

    //增删改操作，异常时加上事务回滚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveActivityRemark(ActivityRemarkQuery remarkQuery) {

        TActivityRemark tActivityRemark = new TActivityRemark();
        //将activityQuery对象中的属性数据复制到tActivity对象中(复制要求:两个对象的属性名相同，属性类型要相同，这样才能进行复制)
        BeanUtils.copyProperties(remarkQuery,tActivityRemark);

        //密码加密
        tActivityRemark.setCreateTime(new Date());

        //解析token，获取登陆人id
        Integer loginUserId = JWTUtils.parseJWTByUserId(remarkQuery.getToken());
        tActivityRemark.setCreateBy(loginUserId);

        //对应sql语句存在空值判断，空值不插入
        return activityRemarkMapper.insertSelective(tActivityRemark);
    }




    @Override
    public PageInfo<TActivityRemark> getActivityRemarkPage(Integer current,ActivityRemarkQuery activityRemarkQuery ) {
        //1.设置PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        //2.查询
        List<TActivityRemark> list = activityRemarkMapper.selectActivityRemarkByPage(activityRemarkQuery);
        //3.封装分页数据到PageInfo
        PageInfo<TActivityRemark> info = new PageInfo<>(list);

        return info;
    }

    @Override
    public TActivityRemark getActivityRemarkById(Integer id) {
        return activityRemarkMapper.selectByPrimaryKey(id);
    }


    //增删改操作，异常时加上事务回滚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateActivityRemark(ActivityRemarkQuery remarkQuery) {
        TActivityRemark tActivityRemark = new TActivityRemark();
        //将remarkQuery对象中的属性数据复制到tActivityRemark对象中(复制要求:两个对象的属性名相同，属性类型要相同，这样才能进行复制)
        BeanUtils.copyProperties(remarkQuery,tActivityRemark);

        tActivityRemark.setEditTime(new Date());


        //解析token，获取登陆人id
        Integer loginUserId = JWTUtils.parseJWTByUserId(remarkQuery.getToken());
        tActivityRemark.setEditBy(loginUserId);

        //传入空值不更新
        return activityRemarkMapper.updateByPrimaryKeySelective(tActivityRemark);
    }


    //增删改操作，异常时加上事务回滚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int delActivityRemark(Integer id) {
        //逻辑删除，修改数据状态
        //物理删除：真正的将表数据从表中删除
        TActivityRemark tActivityRemark = new TActivityRemark();
        tActivityRemark.setId(id);
        tActivityRemark.setDeleted(1);
        return activityRemarkMapper.updateByPrimaryKeySelective(tActivityRemark);
    }
}
