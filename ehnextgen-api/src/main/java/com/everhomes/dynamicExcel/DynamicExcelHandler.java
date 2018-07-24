//@formatter:off
package com.everhomes.dynamicExcel;

import com.everhomes.rest.dynamicExcel.DynamicImportResponse;
import com.everhomes.rest.organization.ImportFileResultLog;

import java.util.List;
import java.util.Map;

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
     * 此方法应该用于插入schema等存储处理, 抛出的异常会导致该sheet的处理会被略过;storage1来自导入方法importMultiSheet
     * @param sheetClassObjs sheet页对应的类的对象的集合
     * @param sheetClass    sheet页对应的类
     */

//    void save2Schema(List<Object> sheetClassObjs, Class<?> sheetClass, Object storage1, Map<Object,Object> context);
    /**
     *
     * @param response 对返回结果进行后处理，不进行处理则只返回下载成功和失败的行数
     */
//    void postProcess(DynamicImportResponse response);

    /**
     * 注意：dynamicSheet中的list<DynamicField>的长度和顺序必须和 @param headers 相同
     * @param sheetName sheet的名字
     * @param params 在dynamicaService.exportDynamicExcel()和importDynamicExcel() 传递的调用者的参数,导出时dynamicField的前提参数
     * @param headers 导入时，获取dynamicField的前提参数
     * @param withData
     * @Return Dynamic实例对象集合
     */
    List<DynamicSheet> getDynamicSheet(String sheetName, Object params, List<String> headers, boolean isImport, boolean withData);

    /**
     * 标题行和返回的数据的list的下表必须保持对应
     * @param sheet 所在的动态sheet对象
     * @param context 上下文，遍历sheet时记录sheets之间的关系
     * @return
     */
    List<List<String>> getExportData(DynamicSheet sheet, Object params, Map<Object,Object> context);

//    void importData(String sheetName, List<String> headers, List<DynamicRowDTO> rowDatas, DynamicImportResponse response);

    /**
     * 导入数据，提供sheet的名字和行数据的集合，导入的结果应该置入response中
     * @param ds 动态sheet对象，参考DynamicSheet
     * @param rowDatas  sheet页中的行数据列表
     * @param context  上下文，遍历sheet导入时记录sheets之间的关系
     * @param params  在dynamicaService.importMultiSheet() 传递的调用者的参数
     * @param response  方法DynamicExcelService.importMultiSheet的返回，对于每一个sheet的导入结果都应该放入此返回中
     */
    void importData(DynamicSheet ds, List<DynamicRowDTO> rowDatas, Object params, Map<Object,Object> context, DynamicImportResponse response, List<ImportFileResultLog<Map<String,String>>> resultLogs);
}
