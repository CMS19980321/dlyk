package com.hncu.mapper;

import com.hncu.commons.DataScope;
import com.hncu.model.TActivity;
import com.hncu.query.ActivityQuery;
import com.hncu.query.BaseQuery;

import java.util.List;

public interface TActivityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TActivity record);

    int insertSelective(TActivity record);

    TActivity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TActivity record);

    int updateByPrimaryKey(TActivity record);

    //owner_id:负责人id
    @DataScope(tableField = "owner_id",tableAlias = "ta")
    List<TActivity> selectActivityByPage(ActivityQuery  build);

    TActivity selectDetailByPrimaryKey(Integer id);

    List<TActivity> selectOngoingActivity();

    Integer selectByCount();
}