package com.everhomes.visitorsys;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.parking.handler.Utils;
import com.everhomes.parking.handler.haikangweishi.HaiKangWeiShiJinMaoVendorHandler;
import com.everhomes.rest.parking.handler.haikangweishi.ErrorCodeEnum;
import com.everhomes.rest.parking.handler.haikangweishi.HkwsThirdResponse;
import com.everhomes.rest.visitorsys.VisitorsysConstant;
import com.everhomes.uniongroup.UniongroupService;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.visitorsys.hkws.HKWSDataSet;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VisitorSysHKWSUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorSysHKWSUtil.class);

    private final String GET_PERSON_INFO = "/openapi/service/base/person/getPersonInfosEx";

    private final String ADD_APPOINTMENT = "/openapi/service/rvs/reserve/addAppointment";

    private final String DELETE_APPOINTMENT = "/openapi/service/rvs/reserve/deleteAppointment";

    @Autowired
    private HaiKangWeiShiJinMaoVendorHandler HKWSHandler;

    @Autowired
    private HKWSUserProvider HKWSUserProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private VisitorSysThirdMappingProvider thirdMappingProvider;

    @Autowired
    private UserProvider userProvider;

    public HkwsThirdResponse addAppointment(VisitorSysVisitor visitor){
        JSONObject params = new JSONObject();

        params.put("visitorName",visitor.getVisitorName());

        Integer personId = 0;

        UserIdentifier userIdentifier = userProvider.findUserIdentifiersOfUser(UserContext.current().getUser().getId(),UserContext.getCurrentNamespaceId());
        List<HKWSUser> users = HKWSUserProvider.findUserByPhone(userIdentifier.getIdentifierToken());
        if(users == null || users.size() == 0){
            Long startTime = configProvider.getLongValue("HKWS.user.syncTime", 0L);
            syncHKWSUsers(startTime);
            users = HKWSUserProvider.findUserByPhone(UserContext.current().getUser().getIdentifierToken());
            if(users == null || users.size() == 0){
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE,VisitorsysConstant.ERROR_NOT_EXIST,"third user not exist");
            }
        }
        personId = users.get(0).getPersonId();
        params.put("personId",personId);
        params.put("gender","1");
        params.put("phoneNo",visitor.getVisitorPhone());
        params.put("personNum","" + 1 + visitor.getFollowUpNumbers());

        Timestamp time = visitor.getPlannedVisitTime();
        Timestamp now = new Timestamp(System.currentTimeMillis() - 50000L);
        if (time.before(now)){
            time.setTime(now.getTime() + 300000L);
        }
//        DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        LOGGER.info("HKWS response:" + resp.toString());

        if(HKWSHandler.isSuccess(resp)){
            JSONObject data = JSONObject.parseObject(resp.getData());
            VisitorSysThirdMapping mapping = new VisitorSysThirdMapping();
            mapping.setVisitorId(visitor.getId());
            mapping.setThirdType("HKWSVISITOR");
            mapping.setThirdValue(data.getString("appointmentUuid"));
            mapping.setNamespaceId(visitor.getNamespaceId());
            mapping.setOwnerType(visitor.getOwnerType());
            mapping.setOwnerId(visitor.getOwnerId());
            thirdMappingProvider.createMapping(mapping);
        }

        return resp;
    }

    public void delAppointment(VisitorSysVisitor visitor){
        JSONObject params = new JSONObject();

        Integer personId = 0;
        List<HKWSUser> users = HKWSUserProvider.findUserByPhone(UserContext.current().getUser().getIdentifierToken());
        if(users == null || users.size() == 0){
            Long startTime = configProvider.getLongValue("HKWS.user.syncTime", 0L);
            syncHKWSUsers(startTime);
            users = HKWSUserProvider.findUserByPhone(UserContext.current().getUser().getIdentifierToken());
            if(users == null || users.size() == 0){
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE,VisitorsysConstant.ERROR_NOT_EXIST,"third user not exist");
            }
            personId = users.get(0).getPersonId();
        }
        params.put("personId",personId);

        String appointmentUuid;
        List<VisitorSysThirdMapping> mappings = thirdMappingProvider.findMappingByVisitorId(visitor.getId());

        if(mappings == null || mappings.size() == 0){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE,VisitorsysConstant.ERROR_NOT_EXIST,"third mapping id not exist");
        }
        appointmentUuid = mappings.get(0).getThirdValue();

        params.put("appointmentUuid",appointmentUuid);

        HkwsThirdResponse resp = HKWSHandler.post(DELETE_APPOINTMENT,params);
        LOGGER.info("HKWS response:" + resp.toString());
        if(!HKWSHandler.isSuccess(resp)){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE,VisitorsysConstant.ERROR_REQUEST_THIRD_INTERFACE,resp.getErrorMessage());
        }
    }

    public void syncHKWSUsers(Long startTime){

        Long now = System.currentTimeMillis();

        Integer pageSize = 400;
        Integer pageNo = 1;

        Integer total = 0;

        JSONObject params = new JSONObject();

        params.put("pageSize",pageSize);
        params.put("opUserUuid",HKWSHandler.getOpUserUuid(false));
        params.put("startTime",startTime == null ? 0L : startTime);
        params.put("endTime",now);
        while (true) {
            params.put("pageNo",pageNo++);
            HkwsThirdResponse resp = HKWSHandler.post(GET_PERSON_INFO,params);
            if(!HKWSHandler.isSuccess(resp)){
                throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE,VisitorsysConstant.ERROR_REQUEST_THIRD_INTERFACE,resp.getErrorMessage());
            }
            HKWSDataSet<HKWSUser> datas = JSONObject.parseObject(resp.getData(),new TypeReference<HKWSDataSet<HKWSUser>>(){});
            HKWSUserProvider.deleteUsers(datas.getList().stream().map(r-> r.getPersonId()).collect(Collectors.toList()));
            HKWSUserProvider.createUsers(datas.getList());
            total += datas.getPageSize();
            if(datas.getTotal() < total || datas.getPageSize() == 0){
                break;
            }
        }

        configProvider.setValue("HKWS.user.syncTime",String.valueOf(now));

    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void syncHKWSUsersdaily(){
        Long startTime = configProvider.getLongValue("HKWS.user.syncTime", 0L);
        syncHKWSUsers(startTime);
    }
}
