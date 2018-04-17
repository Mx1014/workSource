package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.flow.FlowCase;
import com.everhomes.parking.*;
import com.everhomes.parking.qididaoding.QidiDaodingMonthCardEntity;
import com.everhomes.parking.qididaoding.QidiDaodingResponse;
import com.everhomes.parking.qididaoding.QidiDaodingSignUtil;
import com.everhomes.parking.qididaoding.QidiDaodingTokenEntity;
import com.everhomes.parking.yinxingzhijiexiaomao.*;
import com.everhomes.parking.zhongbaichang.ZhongBaiChangCardInfo;
import com.everhomes.parking.zhongbaichang.ZhongBaiChangData;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 启迪香山，启迪香山
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "QIDI_DAODING")
public class QiqiDaodingParkingVendorHandler extends DefaultParkingVendorHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(QiqiDaodingParkingVendorHandler.class);

    private static final String OPENAPI_TOKEN_GET = "/openapi/token/get";// 全局token获取
    private static final String OPENAPI_PARKING_INFO = "/openapi/parking/info";// 停车场详情
    private static final String OPENAPI_PARKING_MONTHCARD_GET = "/openapi/parking/monthcard/get";// 月卡（固定户）信息查询
    private static final String OPENAPI_PARKING_MONTHCARD_TYPE_GET = "/openapi/parking/monthcard/type/get";// 月卡类型获取
    private static final String OPENAPI_PARKING_MONTHCARD_RENEWALS = "/openapi/parking/monthcard/renewals";// 月卡充值（续费）
    private static final String OPENAPI_PARKING_MONTHCARD_CREATE = "/openapi/parking/monthcard/create";// 新增月卡用户
    private static final String OPENAPI_PARKING_TEMPORARY_GET = "/openapi/parking/temporary/get";// 临停车费用查询
    private static final String OPENAPI_PARKING_TEMPORARY_CREATE = "/openapi/parking/temporary/create";// 临停车创建缴费订单
    private static final String OPENAPI_PARKING_LOCKCAR_INFO = "/openapi/parking/lockCar/info";// 锁车状态查询
    private static final String PARKING_LOCKCAR_OPERATING = "/parking/lockCar/operating";// 锁车/解锁
    private static final String OPENAPI_PARKING_TEMPORARY_OPEN_ADD = "/openapi/parking/temporary/open/add";// 临时放行
    private static final String OPENAPI_PARKING_TEMPORARY_OPEN_LIST = "/openapi/parking/temporary/open/list";// 临时放行记录

    private static final String PARKING_QIDI_DAODING_TICKENT = "PARKING_QIDI_DAODING_TICKENT";//存token

    @Autowired
    public BigCollectionProvider bigCollectionProvider;


    @Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        QidiDaodingResponse<QidiDaodingMonthCardEntity> response = getCardInfo(plateNumber);
        List<ParkingCardDTO> resultList = new ArrayList<>();
        if (isRequestDataSuccess(response)) {
            ParkingCardDTO parkingCardDTO = new ParkingCardDTO();

            // 格式yyyyMMddHHmmss
            String validEnd = response.getData().getExpireDate();
            Long endTime = Utils.strToLong(validEnd, Utils.DateStyle.DATE_TIME);

            setCardStatus(parkingLot, endTime, parkingCardDTO);// 这里设置过期可用，正常可用

            parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
            parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
            parkingCardDTO.setParkingLotId(parkingLot.getId());

            parkingCardDTO.setPlateOwnerName(response.getData().getOwnerName());// 车主名称
            parkingCardDTO.setPlateNumber(response.getData().getPlateNo());// 车牌号
            parkingCardDTO.setEndTime(endTime);

            parkingCardDTO.setCardTypeId(response.getData().getTypeId());
            parkingCardDTO.setCardType(response.getData().getTypeName());
//            parkingCardDTO.setCardNumber(entity.getData().getMemberUuid());
            resultList.add(parkingCardDTO);
        }
        return resultList;
    }

    private QidiDaodingResponse<QidiDaodingMonthCardEntity> getCardInfo(String plateNumber) {
        String parkingId = configProvider.getValue("parking.qididaoding.parkingId", "");

        TreeMap map = new TreeMap();
        map.put("parkingId",parkingId);
        map.put("plateNo",plateNumber);

        return postWithToken(map,OPENAPI_PARKING_MONTHCARD_GET,QidiDaodingResponse.class);
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot, String plateNumber, String cardNo) {
        return null;
    }

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
        return null;
    }

    @Override
    public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
        return null;
    }

    @Override
    public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {

    }


    private <T extends QidiDaodingResponse> T postWithToken(TreeMap map, String context, Class<T> cls) {
        map.put("token",getToken(false));
        String result = post(map, context);
        T response = JSONObject.parseObject(result,cls);
        if(isTokenOutOfDate(response)){
            map.put("token",getToken(true));
            result = post(map,context);
            return JSONObject.parseObject(result,cls);
        }
        return response;
    }

    private boolean isRequestDataSuccess(QidiDaodingResponse cardInfo) {
        return cardInfo != null && cardInfo.isSuccess() && cardInfo.getData()!=null;
    }

    private boolean isRequestSuccess(QidiDaodingResponse delayMonthCardResult) {
        return delayMonthCardResult != null && delayMonthCardResult.isSuccess();
    }

    private <T extends QidiDaodingResponse> boolean isTokenOutOfDate(T response){
        return response!=null && response.isTokenOutOfDate();
    }


    /**
     *
     * @param refreshFlag 是否从第三方获取token,并缓存到redis
     * @return token
     */
    private String getToken(boolean refreshFlag){
        String rediskey = UserContext.getCurrentNamespaceId()+PARKING_QIDI_DAODING_TICKENT;
        Accessor acc = this.bigCollectionProvider.getMapAccessor(rediskey, "");
        final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Integer lifecycle = configProvider.getIntValue("parking.qididaoding.tokenlifecycle", 6500);
        if(refreshFlag) {
            String token = requestToken();
            redisTemplate.opsForValue().set(rediskey, token.toString(),lifecycle, TimeUnit.SECONDS);
            return token;
        }
        Object redisToken = redisTemplate.opsForValue().get(rediskey);
        if(redisToken != null) {
            return redisToken.toString();
        }
        String token = requestToken();
        if(token == null) {
            return null;
        }
        //这里存储6500秒，因为你大爷的，对面只给两个小时有效token时间
        redisTemplate.opsForValue().set(rediskey, token.toString(),lifecycle, TimeUnit.SECONDS);
        return token;


    }

    /**
     *
     * @return 从第三方获取token
     */
    private String requestToken() {
        String url = configProvider.getValue("parking.qididaoding.url", "");
        String code = configProvider.getValue("parking.qididaoding.code", "");
        String secret = configProvider.getValue("parking.qididaoding.secret", "");
        String key = configProvider.getValue("parking.qididaoding.key", "");
        String parkingId = configProvider.getValue("parking.qididaoding.parkingId", "");

//        String url = "http://test.daodingtech.com:9999/xdrpark-app";
//        String code = "qdxs001";
//        String secret = "ed62d4335a294932849415a4cc171e8c";
//        String key = "TdBMEZBxeRGQIRrN";
//        String parkingId = "20170104000000000002";

        TreeMap map = new TreeMap();
        map.put("code",code);
        map.put("secret",secret);
        map.put("parkingId",parkingId);
        map.put("sign", QidiDaodingSignUtil.getSign(map,key));

        int i = 0;
        for (;;) {
            String result = post(map,OPENAPI_TOKEN_GET);
            QidiDaodingResponse<QidiDaodingTokenEntity> response = JSONObject.parseObject(result, new TypeReference<QidiDaodingResponse<QidiDaodingTokenEntity>>() {
            });
            if (!response.isSuccess() || response.getData()==null || response.getData().getToken()==null) {
                i++;
                LOGGER.error("get token from elive failed, try again times {}",i);
                if(i>2) {
                    throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_GET_TOKEN,
                            "获取token失败");
                }
                continue;
            }
            return response.getData().getToken();
        }
    }

    private String post(TreeMap map, String conext){
        String url = configProvider.getValue("parking.qididaoding.url", "");
        String key = configProvider.getValue("parking.qididaoding.key", "");
        map.put("sign", QidiDaodingSignUtil.getSign(map,key));
        return  Utils.postUrlencoded(url+conext,map);
    }

//    public static void main(String[] args) {
//        System.out.println(new QiqiDaodingParkingVendorHandler().requestToken(OPENAPI_TOKEN_GET));
//    }

}