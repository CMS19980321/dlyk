package com.hncu.web;

import com.hncu.DlykServerApplication;
import com.hncu.model.TActivity;
import com.hncu.model.TDicValue;
import com.hncu.model.TProduct;
import com.hncu.result.DicEnum;
import com.hncu.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/7/29 21:59
 * @Version 1.0
 */

@RestController
public class DicController {
    @GetMapping(value = "/api/dicvalue/{typeCode}")
    public R dicData(@PathVariable(value = "typeCode") String typeCode){
        if (DicEnum.ACTIVITY.getCode().equals(typeCode)) {
            List<TActivity> tActivityList = (List<TActivity>)DlykServerApplication.casheMap.get(typeCode);
            return R.OK(tActivityList);
        } else if (DicEnum.PRODUCT.getCode().equals(typeCode)) {
            List<TProduct> tProductList = (List<TProduct>)DlykServerApplication.casheMap.get(typeCode);
            return R.OK(tProductList);
        } else {
            List<TDicValue> tDicValueList = (List<TDicValue>)DlykServerApplication.casheMap.get(typeCode);
            return R.OK(tDicValueList);
        }

    }
}
