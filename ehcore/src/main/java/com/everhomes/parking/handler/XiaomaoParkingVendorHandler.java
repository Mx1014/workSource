package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.flow.FlowCase;
import com.everhomes.parking.*;
import com.everhomes.parking.xiaomao.XiaomaoCard;
import com.everhomes.parking.xiaomao.XiaomaoCardType;
import com.everhomes.parking.xiaomao.XiaomaoJsonEntity;
import com.everhomes.parking.xiaomao.XiaomaoTempFee;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.MD5Utils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 小猫停车对接
 * Created by zhengsiting on 2017/8/16.
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "XIAOMAO")
public class XiaomaoParkingVendorHandler extends DefaultParkingVendorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(XiaomaoParkingVendorHandler.class);

    private static final String GET_CARD = "/park/getMonthCard";
    //办理月卡，续费
    private static final String OPEN_CARD = "/park/openMonthCard";

    //临时车缴费单查询
    private static final String CHARGING = "/park/charging";

    //缴费通知
    private static final String POSTCHARGE = "/park/postCharge";

 // 新增月卡接口
	private static final String HANDLER_MONTHCARD = "/park/handleMonthCard";

	// 获取月卡类型接口
	private static final String GET_MONTHCARD_TYPE = "/park/getMonthCardType";



    private static final int SUCCESS = 1;

    @Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        List<ParkingCardDTO> resultList = new ArrayList<>();

        XiaomaoCard card = getCard(plateNumber, parkingLot.getId());

        if(null != card){
            Date expireDate = card.getEndTime();

            long expireTime = expireDate.getTime();

            ParkingCardDTO parkingCardDTO = convertCardInfo(parkingLot);
            setCardStatus(parkingLot, expireTime, parkingCardDTO);

            parkingCardDTO.setPlateNumber(plateNumber);
            parkingCardDTO.setPlateOwnerName(card.getUserName());
            //parkingCardDTO.setStartTime(startTime);
            parkingCardDTO.setEndTime(expireTime);

            List<XiaomaoCardType> types = listCardTypes(parkingLot.getId());
            String cardTypeName = null;
            for (XiaomaoCardType type: types) {
                if (type.getStandardId().equals(card.getMemberType())) {
                    cardTypeName = type.getStandardType();
                }
            }
            parkingCardDTO.setCardType(cardTypeName);
            parkingCardDTO.setCardTypeId(card.getMemberType());

            resultList.add(parkingCardDTO);
        }

        return resultList;
    }

	@Override
	public ParkingExpiredRechargeInfoDTO getExpiredRechargeInfo(ParkingLot parkingLot, GetExpiredRechargeInfoCommand cmd) {
		XiaomaoCard xiaomaoCard = getCard(cmd.getPlateNumber(), cmd.getParkingLotId());
		if (xiaomaoCard == null) {
			return null;
		}
		List<ParkingRechargeRateDTO> parkingRechargeRates = getParkingRechargeRates(parkingLot, null, null);
		if(parkingRechargeRates==null || parkingRechargeRates.size()==0){
			return null;
		}

		ParkingRechargeRateDTO targetRateDTO = null;
		String cardTypeId = xiaomaoCard.getMemberType();
		for (ParkingRechargeRateDTO rateDTO : parkingRechargeRates) {
			if (rateDTO.getCardTypeId().equals(cardTypeId) && rateDTO.getMonthCount().intValue()==parkingLot.getExpiredRechargeMonthCount()) {
				targetRateDTO = rateDTO;
				break;
			}
		}

		if (null == targetRateDTO) {
			parkingRechargeRates.sort((r1,r2)->r1.getMonthCount().compareTo(r2.getMonthCount()));
			for (ParkingRechargeRateDTO rateDTO : parkingRechargeRates) {
				if (rateDTO.getCardTypeId().equals(cardTypeId)) {
					targetRateDTO = rateDTO;
					break;
				}
			}
		}
		if (null == targetRateDTO) {
			return null;
		}

		ParkingExpiredRechargeInfoDTO dto = ConvertHelper.convert(targetRateDTO,ParkingExpiredRechargeInfoDTO.class);
		dto.setCardTypeName(targetRateDTO.getCardType());
		if (xiaomaoCard != null  && xiaomaoCard.getEndTime() != null) {
			Date expireDate = xiaomaoCard.getEndTime();
			long newStartTime = expireDate.getTime();
			dto.setStartPeriod(newStartTime);
			Timestamp rechargeEndTimestamp = Utils.getTimestampByAddDistanceMonthV2(newStartTime, parkingLot.getExpiredRechargeMonthCount());
			dto.setEndPeriod(rechargeEndTimestamp.getTime());
			dto.setMonthCount(new BigDecimal(parkingLot.getExpiredRechargeMonthCount()));
			dto.setRateName(parkingLot.getExpiredRechargeMonthCount()+configProvider.getValue("parking.xiaomao.rateName","个月"));
			dto.setPrice(targetRateDTO.getPrice().divide(targetRateDTO.getMonthCount(),OPEN_CARD_RETAIN_DECIMAL, RoundingMode.HALF_UP)
				.multiply(new BigDecimal(parkingLot.getExpiredRechargeMonthCount())));
		}
		return dto;
	}

	private XiaomaoCard getCard(String plateNumber, Long parkingId){
        JSONObject param = new JSONObject();

        param.put("licenseNumber",plateNumber);
        String jsonString = post(param,GET_CARD, parkingId);

        XiaomaoCard card = JSONObject.parseObject(jsonString, XiaomaoCard.class);
        if (card.getFlag() == SUCCESS){
            return card;
        }
        return null;
    }

    /**
    * @Function: XiaomaoParkingVendorHandler.java
    * @Description: 月卡充值
    *
    * @version: v1.0.0
    * @author:	 黄明波
    * @date: 2018年5月10日 下午2:35:05
    *
    */
    private boolean rechargeMonthlyCard(ParkingRechargeOrder order){

        XiaomaoCard card = getCard(order.getPlateNumber(), order.getParkingLotId());

        if (null != card) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long expireTime = card.getEndTime().getTime();
			boolean baseNewestTime = configProvider.getBooleanValue("parking.xiaomao.baseNewestTime." + order.getParkingLotId(), false);
			if(baseNewestTime){
				Long now = System.currentTimeMillis();
				if(now>expireTime){
					expireTime=now;
				}
			}

			Timestamp timestampStart = Utils.addSecond(expireTime, 1);
            Timestamp timestampEnd = getCardEndTime(expireTime, order.getMonthCount().intValue());
            String validStart = sdf.format(timestampStart);
            String validEnd = sdf.format(timestampEnd);

            JSONObject param = new JSONObject();
            param.put("parkName", card.getParkName());
            param.put("memberType", card.getMemberType());
            param.put("licenseNumber", order.getPlateNumber());
            param.put("beginTime", validStart);
            param.put("endTime", validEnd);

            String json = post(param, OPEN_CARD, order.getParkingLotId());

            //将充值信息存入订单
            order.setErrorDescriptionJson(json);
            order.setStartPeriod(timestampStart);
            order.setEndPeriod(timestampEnd);

            XiaomaoJsonEntity<?> entity = JSONObject.parseObject(json, XiaomaoJsonEntity.class);
            if (entity.getFlag() == SUCCESS){
                return true;
            }
        }
        return false;
    }

    public String post(JSONObject param, String type, Long parkingLotId) {
        String url = configProvider.getValue("parking.xiaomao.url", "") + type;
        String parkId = configProvider.getValue("parking.xiaomao.parkId." + parkingLotId, "");
        String keyId = configProvider.getValue("parking.xiaomao.accessKeyId."+parkingLotId, "");
        String key = configProvider.getValue("parking.xiaomao.accessKeyValue."+parkingLotId, "");

        param.put("parkId", parkId);
        param.put("accessKeyId", keyId);

        String sign = getSign(param, key);
        param.put("sign",sign);
        String result = Utils.post(url, param);
        LOGGER.info("get card info param:"+param.toJSONString()+" result:"+result);
        return result;

    }

    public String getSign(JSONObject param,String key){
        StringBuilder stringBuilder = new StringBuilder();
        Iterator iterator = param.entrySet().iterator();
        TreeMap map = new TreeMap();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            map.put(entry.getKey(),entry.getValue());
        }
       Iterator iterator2 =  map.entrySet().iterator();
        while(iterator2.hasNext()){
            Map.Entry<String,String> entry = (Map.Entry)iterator2.next();
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        stringBuilder.append("accessKeyValue=").append(key);
        String s = stringBuilder.toString();
        return MD5Utils.getMD5(s);
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot, String plateNumber, String cardNo) {

        List<ParkingRechargeRate> parkingRechargeRateList;

        if(StringUtils.isBlank(plateNumber)) {
            parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(),
                    parkingLot.getId(), null);
        }else{
            XiaomaoCard card = getCard(plateNumber, parkingLot.getId());
            String cardType = card.getMemberType();
			parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(),
					parkingLot.getId(), cardType);
        }

        List<XiaomaoCardType> types = listCardTypes(parkingLot.getId());

        List<ParkingRechargeRateDTO> result = parkingRechargeRateList.stream().map(r->{
            ParkingRechargeRateDTO dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);

            populaterate(types, dto, r);
            return dto;
        }).collect(Collectors.toList());

        return result;
    }

    private void populaterate(List<XiaomaoCardType> types, ParkingRechargeRateDTO dto, ParkingRechargeRate r) {
    	XiaomaoCardType temp = null;
        for(XiaomaoCardType t: types) {
            if(t.getStandardId().equals(r.getCardType())) {
                temp = t;
                break;
            }
        }

        if (null == temp) {
        	return;
        }

        dto.setCardTypeId(temp.getStandardId());
        dto.setCardType(temp.getStandardType());
        dto.setRateToken(r.getId().toString());
        dto.setVendorName(ParkingLotVendor.XIAOMAO.getCode());
    }

    @Override
	public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		if (order.getOrderType().equals(ParkingOrderType.RECHARGE.getCode())) {
			if (order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode())) {
				//月卡缴费
				return rechargeMonthlyCard(order);
			}

			if (order.getRechargeType().equals(ParkingRechargeType.TEMPORARY.getCode())) {
				//临时车缴费
				return payTempCardFee(order);
			}

			return false;
		}

		// 开通月卡
		if (order.getOrderType().equals(ParkingOrderType.OPEN_CARD.getCode())) {
			return openMonthCard(order);
		}

		LOGGER.info("unknown type = " + order.getRechargeType());
		return false;
	}

    /**
    * @Function: XiaomaoParkingVendorHandler.java
    * @Description: 开通月卡
    *
    * @version: v1.0.0
    * @author:	 黄明波
    * @date: 2018年4月9日 下午6:41:07
    *
    */
    private boolean openMonthCard(ParkingRechargeOrder order){

        ParkingCardRequest request;
        if (null != order.getCardRequestId()) {
            request = parkingProvider.findParkingCardRequestById(order.getCardRequestId());

        }else {
            request = getParkingCardRequestByOrder(order);
            order.setCardRequestId(request.getId()); //补上id
        }
        long nowTime = getStartTimeMillis();
        Timestamp timestampStart = new Timestamp(nowTime);
		Timestamp timestampEnd = Utils.getTimestampByAddNatureMonth(nowTime, order.getMonthCount().intValue());
        order.setStartPeriod(timestampStart);
        order.setEndPeriod(timestampEnd);

        if(addMonthCard(order, request)) {
            updateFlowStatus(request);
            return true;
        }
        return false;
    }

    private boolean addMonthCard(ParkingRechargeOrder order, ParkingCardRequest request){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String validStart = sdf.format(order.getStartPeriod());
        String validEnd = sdf.format(order.getEndPeriod());

        JSONObject param = new JSONObject();
        param.put("standardId",request.getCardTypeId());
        param.put("beginTime",validStart);//月卡开始时间，按照现在时间来
        param.put("stopTime",validEnd);//结束时间
        param.put("licenseNumber", order.getPlateNumber());
        param.put("money", order.getPrice().setScale(2).toString());
        param.put("payType", VendorType.WEI_XIN.getCode().equals(order.getPaidType()) ? "wxpay" : "alipay");

        param.put("userName", request.getPlateOwnerName());
        param.put("userTel", request.getPlateOwnerPhone());
        param.put("companyName", checkCompanyName(request.getPlateOwnerEntperiseName()));

        String json = post(param, HANDLER_MONTHCARD, order.getParkingLotId());
        order.setErrorDescriptionJson(json);

        JSONObject jsonObject = JSONObject.parseObject(json);
        Object obj = jsonObject.get("flag");
        order.setErrorDescription(jsonObject.getString("message"));
        if(null != obj ) {
            int resCode = (int) obj;
            if(resCode == 1) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
		ListCardTypeResponse ret = new ListCardTypeResponse();

		do {

			// 获取当前的卡类型
			List<ParkingCardRequestType> typeList = parkingProvider.listParkingCardTypes(cmd.getOwnerType(),
					cmd.getOwnerId(), cmd.getParkingLotId());
			if (null == typeList || typeList.isEmpty()) {
				break;
			}

			// 获取所有卡类型
			List<XiaomaoCardType> listCardTypes = listCardTypes(cmd.getParkingLotId());
			if (listCardTypes.isEmpty()) {
				break;
			}

			// 获取费率支持的卡
			List<ParkingCardType> supportCardTypes = new ArrayList<>();
			for (ParkingCardRequestType type : typeList) {
				for (XiaomaoCardType cardType : listCardTypes) {
					if (type.getCardTypeId().equals(cardType.getStandardId())) {
						ParkingCardType temp = new ParkingCardType();
						temp.setTypeId(cardType.getStandardId());
						temp.setTypeName(cardType.getStandardType());
						supportCardTypes.add(temp);
						break;
					}
				}
			}

			ret.setCardTypes(supportCardTypes);

		} while (false);

		return ret;
	}


    @Override
    public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {
        updateParkingRechargeOrderRateInfo(parkingLot, order);

    }

    @Override
	public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {

		JSONObject param = new JSONObject();
		param.put("plateNo", plateNumber);
		String result = post(param, CHARGING, parkingLot.getId());
		XiaomaoTempFee tempFee = JSONObject.parseObject(result,
				new TypeReference<XiaomaoTempFee>() {
				});
		ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
		if (null == tempFee || !tempFee.isSuccess()) {
			return dto;
		}

		int freeParkingTime  = configProvider.getIntValue("parking.xiaomao.free.time", 15); //初始停车时长，单位分
		long startTime = Utils.strToLong(tempFee.getStartTime(), Utils.DateStyle.DATE_TIME);
		long chargeTime = Utils.strToLong(tempFee.getChargeTime(), Utils.DateStyle.DATE_TIME); //以小猫的计费时间为准
		int parkingTime = (int)((chargeTime - startTime) / (1000 * 60));


		dto.setPlateNumber(plateNumber);
		dto.setEntryTime(startTime);
		dto.setPayTime(chargeTime);
		dto.setParkingTime(parkingTime);
		dto.setPrice(new BigDecimal(tempFee.getShouldPay()));
		int delayTime = parkingTime <= freeParkingTime ? freeParkingTime : tempFee.getTimeOut();
		dto.setDelayTime(delayTime);
		dto.setOrderToken(tempFee.getOrderId());
		dto.setRemainingTime(tempFee.getTimeOut());

		return dto;

	}

    private boolean payTempCardFee(ParkingRechargeOrder order) {

        JSONObject params = new JSONObject();
        params.put("plateNo", order.getPlateNumber());
        params.put("orderId", order.getOrderToken());
        params.put("payMoney", order.getPrice().setScale(2).toString());
        String result = post(params, POSTCHARGE, order.getParkingLotId());
        order.setErrorDescriptionJson(result);//缴费结果储存

        JSONObject jsonObject = JSONObject.parseObject(result);
        Object obj = jsonObject.get("flag");
        order.setErrorDescription(jsonObject.getString("message"));
        if(null != obj ) {
            int resCode = (int) obj;
            if(resCode == 1) {
                return true;
            }
        }
        return false;
    }

	@Override
	public OpenCardInfoDTO getOpenCardInfo(GetOpenCardInfoCommand cmd) {

		 OpenCardInfoDTO cardInfoDTO = new OpenCardInfoDTO();

		do {

			//查询申请记录
			ParkingCardRequest parkingCardRequest = parkingProvider
					.findParkingCardRequestById(cmd.getParkingRequestId());
			if (null == parkingCardRequest) {
				break;
			}

			//获取工作流
			FlowCase flowCase = flowCaseProvider.getFlowCaseById(parkingCardRequest.getFlowCaseId());
			if (null == flowCase) {
				break;
			}

			// 读取月卡发放的限制参数
			ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(cmd.getOwnerType(), cmd.getOwnerId(),
					cmd.getParkingLotId(), flowCase.getFlowMainId());

			Integer requestMonthCount = REQUEST_MONTH_COUNT;
			Byte requestRechargeType = REQUEST_RECHARGE_TYPE;

			if (null != parkingFlow) {
				requestMonthCount = parkingFlow.getRequestMonthCount();
				requestRechargeType = parkingFlow.getRequestRechargeType();
			}

			ParkingLot parkingLot = ConvertHelper.convert(cmd, ParkingLot.class);
			parkingLot.setId(cmd.getParkingLotId());

			// 查询资费配置项
			List<ParkingRechargeRateDTO> rateDTOList = getParkingRechargeRates(parkingLot, null, null);
			if (null == rateDTOList || rateDTOList.isEmpty()) {
				break;
			}

			ParkingRechargeRateDTO targetRateDTO = null;
			String cardTypeId = parkingCardRequest.getCardTypeId();
			for (ParkingRechargeRateDTO rateDTO : rateDTOList) {
				if (rateDTO.getCardTypeId().equals(cardTypeId)) {
					targetRateDTO = rateDTO;
					break;
				}
			}

			if (null == targetRateDTO) {
				break;
			}

			cardInfoDTO.setOwnerId(cmd.getOwnerId());
			cardInfoDTO.setOwnerType(cmd.getOwnerType());
			cardInfoDTO.setParkingLotId(cmd.getParkingLotId());
			cardInfoDTO.setRateToken(targetRateDTO.getRateToken());
			cardInfoDTO.setRateName(targetRateDTO.getRateName());
			cardInfoDTO.setCardType(targetRateDTO.getCardType()); // 月卡类型显示字段
			cardInfoDTO.setMonthCount(BigDecimal.valueOf(requestMonthCount)); // 需要至少充值的月份
			//这里计算月份单价时，向上保留整数
			cardInfoDTO.setPrice(targetRateDTO.getPrice().divide(targetRateDTO.getMonthCount(),
					OPEN_CARD_RETAIN_DECIMAL, RoundingMode.UP));

			cardInfoDTO.setPlateNumber(cmd.getPlateNumber());
			long now = getStartTimeMillis();
			cardInfoDTO.setOpenDate(now);
			cardInfoDTO.setExpireDate(Utils.getTimestampByAddDistanceMonthV2(now, requestMonthCount).getTime());

			// 根据配置设定收费标准，默认按实际天数，即ParkingCardExpiredRechargeType.ACTUAL(2)
			if (requestRechargeType == ParkingCardExpiredRechargeType.ALL.getCode()) {
				cardInfoDTO.setPayMoney(cardInfoDTO.getPrice().multiply(new BigDecimal(requestMonthCount)));
			} else {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(now);
				int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				int today = calendar.get(Calendar.DAY_OF_MONTH);

				BigDecimal price = cardInfoDTO.getPrice().multiply(new BigDecimal(requestMonthCount - 1))
						.add(cardInfoDTO.getPrice().multiply(new BigDecimal(maxDay - today + 1))
								.divide(new BigDecimal(DAY_COUNT), OPEN_CARD_RETAIN_DECIMAL, RoundingMode.HALF_UP));
				cardInfoDTO.setPayMoney(price);
			}

			cardInfoDTO.setOrderType(ParkingOrderType.OPEN_CARD.getCode());

		} while (false);

		return cardInfoDTO;
	}

	/**
	* @Function: XiaomaoParkingVendorHandler.java
	* @Description: 获取月卡开始时间。(默认为系统当前时间戳)
	* 为了测试不同的开卡时间，可通过parking.xiaomao.openCardDate参数进行配置开始时间
	*
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年5月9日 下午3:38:32
	*
	*/
	private final long getStartTimeMillis() {
		String cardStartStr = configProvider.getValue("parking.xiaomao.openCardDate", "");
		if (StringUtils.isBlank(cardStartStr)) {
			return System.currentTimeMillis();
		}

		return Utils.strToLong(cardStartStr, Utils.DateStyle.DATE_TIME);
	}

	/**
	* @Function: XiaomaoParkingVendorHandler.java
	* @Description: 月卡充值时使用，像深圳湾生态园的机制一样。
	*
	*/
	private final static Timestamp getCardEndTime(long expireTime, int addMounthNum) {
		return Utils.getTimestampByAddDistanceMonthV2(expireTime, addMounthNum);
	}

	private final List<XiaomaoCardType> listCardTypes(Long parkingLot) {

		List<XiaomaoCardType> cardTypeList = new ArrayList<XiaomaoCardType>(0);

		do {
			// 向第三方获取卡类型
			String result = post(new JSONObject(), GET_MONTHCARD_TYPE, parkingLot);
			if (StringUtils.isBlank(result)) {
				break;
			}
			//issue 32675
			//原因 https://github.com/alibaba/fastjson/issues/569
			//fastjson1.2.4多级泛型转换偶发类型转换失败的情况，为避免此问题，修改为一级一级的转换
			// 解析
			XiaomaoJsonEntity entity = JSONObject.parseObject(result,
					new TypeReference<XiaomaoJsonEntity>() {
					});
			if (null == entity || !entity.isSuccess()) {
				break;
			}
			if(entity.getData()==null){
				break;
			}
			JSONArray array=null;
			try{
				array = JSONArray.parseArray(entity.getData().toString());
			}catch(Exception e){
				LOGGER.error("pasre error,",e);
				break;
			}
			
			cardTypeList = array.stream().map(r->{
				return JSONObject.parseObject(r==null?"{}":r.toString(),
						new TypeReference<XiaomaoCardType>() {
						});
			}).collect(Collectors.toList());

		} while (false);

		return cardTypeList;
	}


	/**
	* @Function: XiaomaoParkingVendorHandler.java
	* @Description: 传输的公司名不允许问号和逗号
	*
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年5月9日 下午4:56:31
	*
	*/
	private String checkCompanyName(String companyName) {
//		if (null != companyName) {
//			companyName = companyName.replaceAll("?", "").replaceAll(",", "");
//		}

		return companyName;
	}

}
