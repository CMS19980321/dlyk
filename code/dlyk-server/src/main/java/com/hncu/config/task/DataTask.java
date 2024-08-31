package com.hncu.config.task;

import com.hncu.DlykServerApplication;
import com.hncu.model.TActivity;
import com.hncu.model.TDicType;
import com.hncu.model.TDicValue;
import com.hncu.model.TProduct;
import com.hncu.result.DicEnum;
import com.hncu.service.ActivityService;
import com.hncu.service.DicTypeService;
import com.hncu.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author caimeisahng
 * @Date 2024/7/28 20:11
 * @Version 1.0
 */

@EnableScheduling //开启定时任务的支持
@Component
public class DataTask {

    @Resource
    private DicTypeService dicTypeService;

    @Resource
    private ProductService productService;

    @Resource
    private ActivityService activityService;

    //调度的意思
    //@Scheduled(fixedDelay = 3000, initialDelay = 15000) //单位默认是毫秒
    //@Scheduled(fixedDelay = 3000, initialDelayString = "15000") //单位默认是毫秒
    //@Scheduled(fixedDelayString = "3000") //单位默认是毫秒
    //@Scheduled(fixedRate = 3000) //单位默认是毫秒
    //cron表达式：包含6个或7个参数 ：
    /**
     * 秒（0-59）
     * 分钟（0-59）
     * 小时（0-23）
     * 日（1-31）
     * 月（1-12 或 JAN-DEC）
     * 星期几（0-6 或 SUN-SAT）
     */
    //@Scheduled(cron = "0 0 3 * * *") //每天3点执行
    @Scheduled(fixedDelayString = "${project.task.dalay}",zone = "Asia/Shanghai",timeUnit = TimeUnit.MILLISECONDS,initialDelay = 1000) //每2秒执行一次
    public void task(){
        //定时惹怒我业务代码
        System.out.println("定时任务业务逻辑执行中....." + new Date());
        List<TDicType> tDicTypeList = dicTypeService.loadAllDicData();

        tDicTypeList.forEach(tDicType ->{
            String typeCode = tDicType.getTypeCode();
            List<TDicValue> dicValueList = tDicType.getDicValueList();
            DlykServerApplication.casheMap.put(typeCode,dicValueList);
        });

        //查询所有产品
        List<TProduct> tProductList = productService.getAllOnSaleProdect();
        DlykServerApplication.casheMap.put(DicEnum.PRODUCT.getCode(),tProductList);

        //查询所有活动，存储进内存中
        List<TActivity> tActivityList =  activityService.getOngoingActivity();
        DlykServerApplication.casheMap.put(DicEnum.ACTIVITY.getCode(),tActivityList);

    }
}
