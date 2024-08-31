package com.hncu.config.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.hncu.DlykServerApplication;
import com.hncu.model.TDicValue;
import com.hncu.model.TProduct;
import com.hncu.result.DicEnum;

import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/7/28 22:27
 * @Version 1.0
 *
 * 意向产品转换器
 */
public class IntentionProductConverter implements Converter<Integer> {
    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        //cellData是Excel中读取到的数据，是‘需要(对应java中的类是49)’
        String cellIntentionProductName = cellData.getStringValue();


        List<TProduct> tDicValueList= (List<TProduct>) DlykServerApplication.casheMap.get(DicEnum.PRODUCT.getCode());

        for (TProduct tProduct : tDicValueList) {
            Integer id = tProduct.getId();
            String name = tProduct.getName();
            if (cellIntentionProductName.equals(name)) {
                return id;
            }
        }

        //未查到值
        return -1;
    }

}
