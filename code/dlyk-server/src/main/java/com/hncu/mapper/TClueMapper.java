package com.hncu.mapper;

import com.hncu.model.TClue;
import com.hncu.query.BaseQuery;
import com.hncu.result.NameValue;

import java.util.List;

public interface TClueMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TClue record);

    int insertSelective(TClue record);

    TClue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TClue record);

    int updateByPrimaryKey(TClue record);

    List<TClue> selectClueByPage(BaseQuery build);

    void saveClue(List<TClue> tClueList);

    int selectByCount(String phone);

    int selectCLueByCount();

    TClue selectByDetailPrimaryKey(Integer id);

    List<NameValue> selectBySource();
}