package com.hncu.service.impl;

import com.hncu.mapper.TProductMapper;
import com.hncu.model.TProduct;
import com.hncu.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/7/29 11:47
 * @Version 1.0
 */

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private TProductMapper tProductMapper;

    @Override
    public List<TProduct> getAllOnSaleProdect() {
        return tProductMapper.selectAllOnSaleProduct();
    }
}
