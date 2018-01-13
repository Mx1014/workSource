package com.everhomes.customer;

import com.everhomes.dynamicExcel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ying.xiong on 2018/1/12.
 */
@Component(DynamicExcelStrings.PREFIX + DynamicExcelStrings.CUSTOEMR)
public class CustomerDynamicExcelHandler implements DynamicExcelHandler {

    @Autowired
    private

    @Override
    public void save2Schema(List<Object> sheetClassObjs, Class<?> sheetClass) {

    }

    @Override
    public void postProcess(DynamicImportResponse response) {

    }

    @Override
    public DynamicSheet getDynamicSheet(String sheetName, Object storage) {

        return null;
    }

    @Override
    public List<List<String>> getExportData(List<DynamicField> fields, DynamicSheet sheet) {
        return null;
    }
}
