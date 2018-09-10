//@formatter:off
package com.everhomes.dynamicExcel;


import com.everhomes.rest.dynamicExcel.DynamicImportResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by Wentian Wang on 2018/1/12.
 */
@Service
public interface DynamicExcelService {

    /**
     *
     * @param response 输出流
     * @param code 业务码,bean名称的后缀，前缀参考 {@link com.everhomes.dynamicExcel.DynamicExcelStrings}
     * @param sheetNames 业务需要导出的sheet的名字集合
     * @param params 业务参数，可以再handler中的getDynamicSheet中使用
     * @param baseInfo 不填,则走默认的
     * @param enumSupport true：会将枚举值放入说明中； false：不会将枚举放入说明
     * @param excelName excel文件名字，没有则默认给一个“客户数据导出+时间戳”
     */
    public void exportDynamicExcel(HttpServletResponse response, String code, String baseInfo,List<String> sheetNames,
                                   Object params, boolean enumSupport, boolean withData, String excelName);


    /**
     *
     * @param code 业务码,bean名称的后缀，前缀参考 {@link com.everhomes.dynamicExcel.DynamicExcelStrings}
     * @param sheetNames 业务需要导出的sheet的名字集合
     * @param params 业务参数，可以再handler中的getDynamicSheet中使用
     * @param baseInfo 不填,则走默认的
     * @param enumSupport true：会将枚举值放入说明中； false：不会将枚举放入说明
     * @param excelName excel文件名字，没有则默认给一个“客户数据导出+时间戳”
     */
    OutputStream exportDynamicExcel(String code, String baseInfo, List<String> sheetNames,
                                           Object params, boolean enumSupport, boolean withData, String excelName);


    /**
     *
     * @param file 有效的excel文件
     * @param code 回调的bean的名称
     * @param headerRow 标题行为第几行，默认为第2行
     * @param params 业务参数，在handler中的importData方法中使用
     * @return DynamicImportResponse
     */
    public DynamicImportResponse importMultiSheet(MultipartFile file, String code, Integer headerRow, Object params);


}
