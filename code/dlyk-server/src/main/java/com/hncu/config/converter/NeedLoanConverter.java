package com.hncu.config.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.hncu.DlykServerApplication;
import com.hncu.model.TDicValue;
import com.hncu.result.DicEnum;

import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/7/28 21:48
 * @Version 1.0
 */
public class NeedLoanConverter implements Converter<Integer> {
    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        //cellData是Excel中读取到的数据，是‘需要(对应java中的类是49)’
        String cellNeedLoanName = cellData.getStringValue();


        List<TDicValue> tDicValueList= (List<TDicValue>) DlykServerApplication.casheMap.get(DicEnum.NEEDLOAN.getCode());

        for (TDicValue tDicValue : tDicValueList) {
            Integer id = tDicValue.getId();
            String name = tDicValue.getTypeValue();
            if (cellNeedLoanName.equals(name)) {
                return id;
            }
        }

        //未查到值
        return -1;
    }
}
