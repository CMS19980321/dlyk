package com.hncu.mapper;

import com.hncu.commons.DataScope;
import com.hncu.model.TActivityRemark;
import com.hncu.model.TClueRemark;
import com.hncu.query.ClueRemarkQuery;

import java.util.List;

public interface TClueRemarkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TClueRemark record);

    int insertSelective(TClueRemark record);

    TClueRemark selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TClueRemark record);

    int updateByPrimaryKey(TClueRemark record);

    @DataScope(tableAlias = "tcr" ,tableField = "create_by")
    List<TActivityRemark> selectClueRemarkByPage(ClueRemarkQuery clueRemarkQuery);
}