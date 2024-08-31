package com.hncu.manager;

import com.hncu.mapper.TClueMapper;
import com.hncu.mapper.TCustomerMapper;
import com.hncu.model.TClue;
import com.hncu.model.TCustomer;
import com.hncu.query.CustomerQuery;
import com.hncu.util.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author caimeisahng
 * @Date 2024/8/5 11:16
 * @Version 1.0
 *
 * 多个不同mapper调用，逻辑在Manager层中进行
 */


//@Component
@Component
public class CustomerManager {
    @Resource
    private TCustomerMapper tCustomerMapper;

    @Resource
    private TClueMapper tClueMapper;


    //增删改操作，异常时加上事务回滚
    @Transactional(rollbackFor = Exception.class)
    public Boolean convertCustomer(CustomerQuery customerQuery) {
        //验证该线索是否已经转过客户，转过就不能再进行转换
        TClue clue = tClueMapper.selectByPrimaryKey(customerQuery.getClueId());
        if (clue.getState() == -1){
            throw new RuntimeException("该线索已经转换为客户，不能再次转换");
        }

        //向客户表插入一条数据
        TCustomer tCustomer = new TCustomer();
        //将remarkQuery对象中的属性数据复制到tActivityRemark对象中(复制要求:两个对象的属性名相同，属性类型要相同，这样才能进行复制)
        BeanUtils.copyProperties(customerQuery,tCustomer);
        tCustomer.setCreateTime(new Date());
        //解析token，获取登陆人id
        Integer loginUserId = JWTUtils.parseJWTByUserId(customerQuery.getToken());
        tCustomer.setCreateBy(loginUserId);
        int inserted = tCustomerMapper.insertSelective(tCustomer);

        //把线索表的数据状态改为-1
        TClue clue1 = new TClue();
        clue1.setId(customerQuery.getClueId());
        clue1.setState(-1);//魔鬼数字，但是加了注解
        int updated = tClueMapper.updateByPrimaryKeySelective(clue1);
        return inserted >= 1 && updated >= 1 ;
    }


}
