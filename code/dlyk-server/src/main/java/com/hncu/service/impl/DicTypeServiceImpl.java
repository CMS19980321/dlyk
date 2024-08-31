package com.hncu.service.impl;

import com.hncu.mapper.TDicTypeMapper;
import com.hncu.model.TDicType;
import com.hncu.service.DicTypeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/7/28 20:57
 * @Version 1.0
 */

@Service
public class DicTypeServiceImpl implements DicTypeService {

    @Resource
    private TDicTypeMapper tDicTypeMapper;

    @Override
    public List<TDicType> loadAllDicData() {
        return tDicTypeMapper.selectByAll();
    }
}
