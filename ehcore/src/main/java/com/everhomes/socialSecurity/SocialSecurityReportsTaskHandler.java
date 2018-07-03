package com.everhomes.socialSecurity;


import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.Task;
import com.everhomes.filedownload.TaskService;
import com.everhomes.incubator.IncubatorApply;
import com.everhomes.incubator.IncubatorApplyAttachment;
import com.everhomes.incubator.IncubatorProvider;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.filedownload.TaskStatus;
import com.everhomes.rest.incubator.ApplyType;
import com.everhomes.rest.incubator.IncubatorApplyAttachmentType;
import com.everhomes.rest.incubator.IncubatorApplyDTO;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.excel.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class SocialSecurityReportsTaskHandler implements FileDownloadTaskHandler {

    @Autowired
    FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private SocialSecurityReportProvider socialSecurityReportProvider;

    @Autowired
    TaskService taskService;

    @Autowired
    SocialSecurityService socialSecurityService;

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {


        Long ownerId = null;
        if (params.get("ownerId") != null) {
            ownerId = Long.valueOf(String.valueOf(params.get("ownerId")));
        }

        String payMonth = null;
        if (params.get("payMonth") != null) {
            payMonth = String.valueOf(params.get("payMonth"));
        }

        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");


//        List<SocialSecurityReport> result = socialSecurityReportProvider.listSocialSecurityReport(ownerId,
//                payMonth, null, Integer.MAX_VALUE - 1);
//
//        ,
//        ExcelUtils excelUtils = new ExcelUtils(fileName, "");
//        List<String> propertyNames = new ArrayList<String>(Arrays.asList("projectName", "projectType", "teamName", "businessLicenceText", "planBookAttachmentText", "chargerName", "chargerPhone", "chargerEmail", "applyTypeText", "createTimeText"));
//        List<String> titleNames = new ArrayList<String>(Arrays.asList("姓名","入职日期","手机号","身份证号","学历","开户行","银行卡号","部门","社保电脑号","公积金账号","离职日期","户籍类型","参保城市","社保月份","社保基数","社保合计","社保企业","社保个人","公积金城市","公积金月份","公积金基数","公积金企业基数","公积金企业比例","公积金个人基数","公积金个人比例","公积金合计","公积金企业","公积金个人","公积金需纳税额","养老企业基数","养老企业比例","养老企业","养老个人基数","养老个人比例","养老个人","失业企业基数","失业企业比例","失业企业","失业个人基数","失业个人比例","失业个人","医疗企业基数","医疗企业比例","医疗企业","医疗个人基数","医疗个人比例","医疗个人","工伤企业基数","工伤企业比例","工伤企业","生育企业基数","生育企业比例","生育企业","大病企业基数","大病企业比例","大病企业","大病个人基数","大病个人比例","大病个人","社保补缴企业","社保补缴个人","公积金补缴企业","公积金补缴个人","养老补缴企业","养老补缴个人","失业补缴企业","失业补缴个人","医疗补缴企业","医疗补缴个人","工伤补缴企业","生育补缴企业","大病补缴企业","大病补缴个人","残障金","商业保险"));
//        List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(10, 10, 20, 10, 10, 10, 10, 20, 10, 10, 10, 10));
//        excelUtils.setNeedSequenceColumn(true);
//        OutputStream outputStream = excelUtils.getOutputStream(propertyNames, titleNames, titleSizes, result);

        OutputStream outputStream = null;

        String reportType = null;
        if (params.get("reportType") != null) {
            reportType = String.valueOf(params.get("reportType"));
        }
        if (reportType.equals("exportSocialSecurityReports")) {
            outputStream = socialSecurityService.getSocialSecurityReportsOutputStream(ownerId, payMonth);
        } else if (reportType.equals("exportSocialSecurityDepartmentSummarys")) {
            outputStream = socialSecurityService.getSocialSecurityDepartmentSummarysOutputStream(ownerId, payMonth);
        } else if (reportType.equals("exportSocialSecurityInoutReports")) {
            outputStream = socialSecurityService.getSocialSecurityInoutReportsOutputStream(ownerId, payMonth);
        }
        CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream, taskId);
        taskService.processUpdateTask(taskId, fileLocationDTO);
    }


    @Override
    public void commit(Map<String, Object> params) {

    }

    @Override
    public void afterExecute(Map<String, Object> params) {

    }
}
