package com.everhomes.visitorsys;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.parking.handler.Utils;
import com.everhomes.parking.handler.haikangweishi.HaiKangWeiShiJinMaoVendorHandler;
import com.everhomes.rest.parking.handler.haikangweishi.ErrorCodeEnum;
import com.everhomes.rest.parking.handler.haikangweishi.HkwsThirdResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class VisitorSysHKWSUtil {

    private final String GET_PERSON_INFO = "/openapi/service/base/person/getPersonInfosEx";

    private final String ADD_APPOINTMENT = "/openapi/service/rvs/reserve/addAppointment";

    private final String DELETE_APPOINTMENT = "/openapi/service/rvs/reserve/deleteAppointment";

    @Autowired
    HaiKangWeiShiJinMaoVendorHandler HKWSHandler;

    public String addAppointment(VisitorSysVisitor visitor){
        JSONObject params = new JSONObject();

        params.put("visitorName",visitor.getVisitorName());
//        params.put("personId",);
        params.put("gender","1");
        params.put("phoneNo",visitor.getVisitorPhone());
        params.put("personNum","" + 1 + visitor.getFollowUpNumbers());

        Timestamp time = visitor.getPlannedVisitTime();
        DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        DateFormat edFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String st = sdFormat.format(time);
        String et = edFormat.format(time);
        params.put("startTime",Timestamp.valueOf(st).getTime());
        params.put("endTime",Timestamp.valueOf(et).getTime());
        params.put("identityType","0");//0为身份证
        params.put("certificateNo",visitor.getIdNumber());
        params.put("isVip","0");//0 非VIP 1 VIP
        params.put("purpose",visitor.getVisitReason());
        params.put("plateNo",visitor.getPlateNo());


        HkwsThirdResponse resp = HKWSHandler.post(ADD_APPOINTMENT,params);
        return "";
    }

    public String delAppointment(){
        return "";
    }

    public void initHKWSUsers(){

        JSONObject params = new JSONObject();

        params.put("pageNo",0);
        params.put("pageSize",400);
        params.put("opUserUuid",HKWSHandler.getOpUserUuid(false));
        params.put("startTime",Long.MIN_VALUE);
        params.put("endTime",Long.MIN_VALUE);
        HkwsThirdResponse resp = HKWSHandler.post(GET_PERSON_INFO,params);
        if(!HKWSHandler.isSuccess(resp)){

        }

//        while () {}



    }

}
