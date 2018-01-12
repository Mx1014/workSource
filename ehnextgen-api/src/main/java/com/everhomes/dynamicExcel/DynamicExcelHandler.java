//@formatter:off
package com.everhomes.dynamicExcel;

import com.everhomes.dynamicExcel.DynamicField;
import com.everhomes.dynamicExcel.DynamicImportResponse;
import com.everhomes.dynamicExcel.DynamicSheet;

import java.util.List;

/**
 * Created by Wentian on 2018/1/12.
 */
public interface DynamicExcelHandler {
//
//    /**
//     *
//     * @param headerName 标题的名字
//     * @param ds sheet对应的dynamicSheet对象
//     * @return
//     */
//    DynamicField getDynamicFieldFromCell(String headerName,DynamicSheet ds);

    /**
     * 此方法应该用于插入schema等存储处理, 抛出的异常会导致该sheet的处理会被略过
     * @param sheetClassObjs sheet页对应的类的对象的集合
     * @param sheetClass    sheet页对应的类
     */
    void save2Schema(List<Object> sheetClassObjs, Class<?> sheetClass);
    /**
     *
     * @param response 对返回结果进行后处理，不进行处理则只返回下载成功和失败的行数
     */
    void postProcess(DynamicImportResponse response);

    /**
     *
     * @param sheetName sheet的名字
     * @param storage   传递的调用者的参数
     * @Return Dynamic实例对象
     */
    DynamicSheet getDynamicSheet(String sheetName, Object storage);

    /**
     * 标题行和返回的数据的list的下表必须保持对应
     * @param fields 标题行
     * @param sheet 所在的动态sheet对象
     * @return
     */
    List<List<String>> getExportData(List<DynamicField> fields, DynamicSheet sheet);
}
