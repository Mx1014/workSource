package com.everhomes.visitorsys;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.parking.handler.Utils;
import com.everhomes.parking.handler.haikangweishi.HaiKangWeiShiJinMaoVendorHandler;
import com.everhomes.rest.parking.handler.haikangweishi.ErrorCodeEnum;
import com.everhomes.rest.parking.handler.haikangweishi.HkwsThirdResponse;
import com.everhomes.visitorsys.hkws.HKWSDataSet;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class VisitorSysHKWSUtil {

    private final String GET_PERSON_INFO = "/openapi/service/base/person/getPersonInfosEx";

    private final String ADD_APPOINTMENT = "/openapi/service/rvs/reserve/addAppointment";

    private final String DELETE_APPOINTMENT = "/openapi/service/rvs/reserve/deleteAppointment";

    @Autowired
    HaiKangWeiShiJinMaoVendorHandler HKWSHandler;

    @Autowired
    HKWSUserProvider HKWSUserProvider;

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

    public void initHKWSUsers(Long startTime){

        Integer pageSize = 400;
        Integer pageNo = 1;

        JSONObject params = new JSONObject();

        params.put("pageNo",pageNo);
        params.put("pageSize",pageSize);
        params.put("opUserUuid",HKWSHandler.getOpUserUuid(false));
        params.put("startTime",startTime == null ? Long.MIN_VALUE : startTime);
        params.put("endTime",System.currentTimeMillis());
        HkwsThirdResponse resp = HKWSHandler.post(GET_PERSON_INFO,params);
        if(!HKWSHandler.isSuccess(resp)){

        }
        HKWSDataSet<HKWSUser> datas = JSONObject.parseObject(resp.getData(),new TypeReference<HKWSDataSet<HKWSUser>>(){});

        while (datas.getTotal() >= pageSize) {
            HKWSUserProvider.deleteUsers(datas.getList().stream().map(r-> r.getPersonId()).collect(Collectors.toList()));
            HKWSUserProvider.createUsers(datas.getList());
            params.put("pageNo",pageNo++);
            resp = HKWSHandler.post(GET_PERSON_INFO,params);
            if(!HKWSHandler.isSuccess(resp)){

            }
            datas = JSONObject.parseObject(resp.getData(),new TypeReference<HKWSDataSet<HKWSUser>>(){});

        }



    }

}
