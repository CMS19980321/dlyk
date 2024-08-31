package com.hncu.config.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.hncu.DlykServerApplication;
import com.hncu.model.TDicType;
import com.hncu.model.TDicValue;
import com.hncu.result.DicEnum;

import java.util.Iterator;
import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/7/28 19:37
 * @Version 1.0
 *
 * 称呼转换器
 * Excel中的先生-->java类中是18
 * Excle中的女士， ---> java类中是41
 */
public class AppellationConverter implements Converter<Integer> {
    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        //cellData是Excel中读取到的数据，是‘先生’，‘女士’
        String cellAppellationName = cellData.getStringValue();


        List<TDicValue> tDicValueList= (List<TDicValue>)DlykServerApplication.casheMap.get(DicEnum.APPELLATION.getCode());

        for (TDicValue tDicValue : tDicValueList) {
            Integer id = tDicValue.getId();
            String name = tDicValue.getTypeValue();
            if (cellAppellationName.equals(name)) {
                return id;
            }
        }

        //未查到值
        return -1;
    }
}
