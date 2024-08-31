package com.hncu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hncu.config.listener.UploadDataListener;
import com.hncu.constant.Constants;
import com.hncu.mapper.TClueMapper;
import com.hncu.model.TClue;
import com.hncu.model.TUser;
import com.hncu.query.BaseQuery;
import com.hncu.query.ClueQuery;
import com.hncu.service.ClueService;
import com.hncu.util.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/7/22 21:53
 * @Version 1.0
 */

@Service
public class ClueServiceImpl implements ClueService {

    @Resource
    private TClueMapper tClueMapper;

    @Override
    public PageInfo<TClue> getClueByPage(Integer current) {
        //1.设置PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        //2.查询
        List<TClue> list = tClueMapper.selectClueByPage(BaseQuery.builder().build());
        //3.封装分页数据到PageInfo
        PageInfo<TClue> info = new PageInfo<>(list);

        return info;
    }

    @Override
    public void importExcel(InputStream inputStream , String token) {
        //3个参数。第一个是要读取的Excel文件，第二个参数是Excel模板，第三个参数是文件读取的监听器

        EasyExcel.read(inputStream, TClue.class, new UploadDataListener(tClueMapper , token)).sheet().doRead();
    }

    @Override
    public Boolean checkPhone(String phone) {
        int count = tClueMapper.selectByCount(phone);
        return count <= 0;
    }

    //增删改操作，异常时加上事务回滚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public  int saveClue(ClueQuery clueQuery) {
        int count = tClueMapper.selectByCount(clueQuery.getPhone());
        if (count <= 0) {
            TClue tClue = new TClue();
            //把前端提交过来的参数数据对象ClueQuery复制到TClue对象中
            //Spring框架有个工具类BeanUtils可以进行对象的复制,复制的条件要求是：两个对象的字段名要相同，字段的类型也相同，这样才可以复制
            BeanUtils.copyProperties(clueQuery, tClue);

            //解析jwt得到userId
            Integer loginUserId = JWTUtils.parseJWTByUserId(clueQuery.getToken());

            tClue.setCreateTime(new Date()); //创建时间
            tClue.setCreateBy(loginUserId); //创建人id

            return tClueMapper.insertSelective(tClue);
        } else {
            throw new RuntimeException("该手机号已经录入，不能再录入");
        }



    }

    @Override
    public TClue getClueById(Integer id) {
        return tClueMapper.selectByDetailPrimaryKey(id);
    }

    //增删改操作，异常时加上事务回滚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateClue(ClueQuery clueQuery) {
        TClue tClue = new TClue();

        //把前端提交过来的参数数据对象ClueQuery复制到TClue对象中
        //Spring框架有个工具类BeanUtils可以进行对象的复制,复制的条件要求是：两个对象的字段名要相同，字段的类型也相同，这样才可以复制
        BeanUtils.copyProperties(clueQuery, tClue);

        //解析jwt得到userId
        Integer loginUserId = JWTUtils.parseJWTByUserId(clueQuery.getToken());

        tClue.setEditTime(new Date()); //编辑时间
        tClue.setEditBy(loginUserId); //编辑人id

        return tClueMapper.updateByPrimaryKeySelective(tClue);
    }
}
