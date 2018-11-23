package com.everhomes.visitorsys;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.parking.handler.Utils;
import com.everhomes.parking.handler.haikangweishi.HaiKangWeiShiJinMaoVendorHandler;
import com.everhomes.rest.parking.handler.haikangweishi.ErrorCodeEnum;
import com.everhomes.rest.parking.handler.haikangweishi.HkwsThirdResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        params.put("startTime",visitor.getVisitorName());
        params.put("endTime",visitor.getVisitorName());
        params.put("identityType","0");//0为身份证
        params.put("certificateNo",visitor.getVisitorName());
        params.put("isVip",visitor.getVisitorName());
        params.put("purpose",visitor.getVisitorName());
        params.put("plateNo",visitor.getVisitorName());


        HKWSHandler.post(ADD_APPOINTMENT,params);
        return "";
    }

    public String delAppointment(){
        return "";
    }

}
