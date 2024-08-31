package com.hncu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hncu.constant.Constants;
import com.hncu.mapper.TClueMapper;
import com.hncu.mapper.TClueRemarkMapper;
import com.hncu.model.TActivityRemark;
import com.hncu.model.TClueRemark;
import com.hncu.query.ClueRemarkQuery;
import com.hncu.service.ClueRemarkService;
import com.hncu.util.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/8/4 20:17
 * @Version 1.0
 */

@Service
public class ClueRemarkServiceImpl implements ClueRemarkService {

    @Resource
    private TClueRemarkMapper tClueRemarkMapper;

    //增删改操作，异常时加上事务回滚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveClueRemark(ClueRemarkQuery clueRemarkQuery) {
        TClueRemark tClueRemark = new TClueRemark();
        //将activityQuery对象中的属性数据复制到tActivity对象中(复制要求:两个对象的属性名相同，属性类型要相同，这样才能进行复制)
        BeanUtils.copyProperties(clueRemarkQuery,tClueRemark);

        //密码加密
        tClueRemark.setCreateTime(new Date());

        //解析token，获取登陆人id
        Integer loginUserId = JWTUtils.parseJWTByUserId(clueRemarkQuery.getToken());
        tClueRemark.setCreateBy(loginUserId);

        //对应sql语句存在空值判断，空值不插入
        return tClueRemarkMapper.insertSelective(tClueRemark);
    }

    @Override
    public PageInfo<TActivityRemark> getClueRemarkPage(Integer current, ClueRemarkQuery clueRemarkQuery) {
        //1.设置PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        //2.查询
        List<TActivityRemark> list = tClueRemarkMapper.selectClueRemarkByPage(clueRemarkQuery);
        //3.封装分页数据到PageInfo
        PageInfo<TActivityRemark> info = new PageInfo<>(list);

        return info;
    }
}
