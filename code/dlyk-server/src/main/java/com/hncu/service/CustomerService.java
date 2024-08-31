package com.hncu.service;

import com.github.pagehelper.PageInfo;
import com.hncu.model.TCustomer;
import com.hncu.query.CustomerQuery;
import com.hncu.result.CustomerExcel;

import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/8/5 10:53
 * @Version 1.0
 */
public interface CustomerService {
    Boolean convertCustomer(CustomerQuery customerQuery);

    PageInfo<TCustomer> getCustomerByPage(Integer current);

    List<CustomerExcel> getCustomerExcel(List<String> idList);
}
