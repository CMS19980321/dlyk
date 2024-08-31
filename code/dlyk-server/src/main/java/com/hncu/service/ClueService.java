package com.hncu.service;

import com.github.pagehelper.PageInfo;
import com.hncu.model.TClue;
import com.hncu.query.ClueQuery;

import java.io.InputStream;

/**
 * @Author caimeisahng
 * @Date 2024/7/22 21:53
 * @Version 1.0
 */
public interface ClueService {
    PageInfo<TClue> getClueByPage(Integer current);

    void importExcel(InputStream inputStream , String token);

    Boolean checkPhone(String phone);

    int saveClue(ClueQuery clueQuery);

    TClue getClueById(Integer id);

    int updateClue(ClueQuery clueQuery);
}
