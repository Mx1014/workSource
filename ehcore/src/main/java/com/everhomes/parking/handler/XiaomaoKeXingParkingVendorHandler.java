// @formatter:off
package com.everhomes.parking.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.flow.FlowCase;
import com.everhomes.parking.ParkingCardRequest;
import com.everhomes.parking.ParkingCardRequestType;
import com.everhomes.parking.ParkingFlow;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingRechargeRate;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.kexinxiaomao.KexinXiaomaoCard;
import com.everhomes.parking.kexinxiaomao.KexinXiaomaoCardType;
import com.everhomes.parking.kexinxiaomao.KexinXiaomaoJsonEntity;
import com.everhomes.parking.kexinxiaomao.KexinXiaomaoSignUtil;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.GetOpenCardInfoCommand;
import com.everhomes.rest.parking.ListCardTypeCommand;
import com.everhomes.rest.parking.ListCardTypeResponse;
import com.everhomes.rest.parking.OpenCardInfoDTO;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingCardExpiredRechargeType;
import com.everhomes.rest.parking.ParkingCardType;
import com.everhomes.rest.parking.ParkingLotVendor;
import com.everhomes.rest.parking.ParkingOrderType;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.parking.ParkingRechargeType;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

/*
 * 正中会 正中时代广场停车场
 * // "KEXIN_XIAOMAO"需与ParkingLotVendor.KEXIN_XIAOMAO的枚举值保持一致
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "KEXIN_XIAOMAO")
public class XiaomaoKeXingParkingVendorHandler extends DefaultParkingVendorHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XiaomaoKeXingParkingVendorHandler.class);
	
	private static final Integer OUT_DATE_TIME = 3 * 60 * 1000; //允许用户在过期后三分钟内续费
	
	@Autowired
	private ParkingProvider parkingProvider;

	private static final String HANDLER_MONTHCARD = "/park/handleMonthCard";// 新增月卡接口
	private static final String OPEN_MONTHCARD = "/park/openMonthCard";// 月卡续费接口
	private static final String GET_MONTHCARD_TYPE = "/park/getMonthCardType";// 获取月卡类型接口
	private static final String GET_MONTHCARD = "/park/getMonthCard";// 月卡查询

	@Override
	public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
		KexinXiaomaoCard entity = getCardInfo(plateNumber);
		List<ParkingCardDTO> resultList = new ArrayList<>();
		if (entity != null && entity.isSuccess()) {
			ParkingCardDTO parkingCardDTO = new ParkingCardDTO();

			// 格式yyyyMMddHHmmss
			String validEnd = entity.getEndTime();
			Long endTime = Utils.strToLong(validEnd, Utils.DateStyle.DATE_TIME);

			setCardStatus(parkingLot, endTime, parkingCardDTO);// 这里设置过期可用，正常可用

			parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
			parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
			parkingCardDTO.setParkingLotId(parkingLot.getId());

			parkingCardDTO.setPlateOwnerName(entity.getUserName());// 车主名称
			parkingCardDTO.setPlateNumber(entity.getLicenseNumber());// 车牌号
			parkingCardDTO.setEndTime(endTime);
			
			parkingCardDTO.setCardTypeId(entity.getMemberType());//月卡类型id
			parkingCardDTO.setCardType(entity.getStandardType());//月卡类型名称？
			parkingCardDTO.setCardName(entity.getStandardType());
			resultList.add(parkingCardDTO);
		}
		return resultList;
	}

	private KexinXiaomaoCard getCardInfo(String plateNumber) {
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("licenseNumber", plateNumber);
		String result = post(GET_MONTHCARD, params);
		KexinXiaomaoCard entity = JSONObject.parseObject(result, new TypeReference<KexinXiaomaoCard>(){});
		return entity;
	}

	@Override
	public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot, String plateNumber,
			String cardNo) {
		
		List<ParkingRechargeRate> parkingRechargeRateList;

		if (StringUtils.isBlank(plateNumber)) {
			parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(),
					parkingLot.getOwnerId(), parkingLot.getId(), null);
		} else {
			KexinXiaomaoCard card = getCardInfo(plateNumber);
			String cardType = card.getMemberType();
			parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(),
					parkingLot.getOwnerId(), parkingLot.getId(), cardType);
		}
		
		//如果设置有问题，这里做判空处理
		if (null == parkingRechargeRateList) {
			return new ArrayList<ParkingRechargeRateDTO>(0);
		}
		
		List<ParkingRechargeRateDTO> dtos = new ArrayList<>();
		List<KexinXiaomaoCardType> allCardTypes = listCardTypes();
		
		if (!allCardTypes.isEmpty()) {
			dtos = parkingRechargeRateList.stream().map(r -> {
				ParkingRechargeRateDTO dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);
				populaterate(allCardTypes, dto, r);
				return dto;
			}).collect(Collectors.toList());
		}
		
		return dtos;
	}

	@Override
	public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		
        if (order.getOrderType().equals(ParkingOrderType.RECHARGE.getCode())) {
            if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode())) {
                return rechargeMonthlyCard(order);
            }
        } 
        
        if(order.getOrderType().equals(ParkingOrderType.OPEN_CARD.getCode())) {
            return openMonthCard(order);
         }
        
        LOGGER.info("unknown type = " + order.getRechargeType());
        return false;
    }
	
	private boolean rechargeMonthlyCard(ParkingRechargeOrder order) {
		
		String errorDescription= null;

		do {

			KexinXiaomaoCard card = getCardInfo(order.getPlateNumber());
			if (null == card || !card.isSuccess()) {
				errorDescription = "no card info found from third";
				break;
			}

			long expireTime = Utils.strToLong(card.getEndTime(), Utils.DateStyle.DATE_TIME);
			Timestamp timestampStart = Utils.addSecond(expireTime, 1);
			if (isOutOfDate(timestampStart)) {
				errorDescription = "it is out of date for monthly recharge !";
				break;
			}
			
			Timestamp timestampEnd = getCardEndTime(expireTime, order.getMonthCount().intValue());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String validStart = sdf.format(timestampStart);
			String validEnd = sdf.format(timestampEnd);

			TreeMap<String, String> params = new TreeMap<String, String>();
			params.put("memberType", card.getMemberType());
			params.put("beginTime", validStart);
			params.put("endTime", validEnd);
			params.put("licenseNumber", card.getLicenseNumber());

			String json = post(OPEN_MONTHCARD, params);

			// 将充值信息存入订单
			order.setErrorDescriptionJson(json);
			order.setStartPeriod(timestampStart);
			order.setEndPeriod(timestampEnd);

			KexinXiaomaoJsonEntity<?> entity = JSONObject.parseObject(json, KexinXiaomaoJsonEntity.class);
			if (null == entity || !entity.isSuccess()) {
				errorDescription = "error from third msg";
				break;
			}

			return true;

		} while (false);
		
		order.setErrorDescription(errorDescription);
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
			List<KexinXiaomaoCardType> listCardTypes = listCardTypes();
			if (listCardTypes.isEmpty()) {
				break;
			}

			// 获取费率支持的卡
			List<ParkingCardType> supportCardTypes = new ArrayList<>();
			for (ParkingCardRequestType type : typeList) {
				for (KexinXiaomaoCardType cardType : listCardTypes) {
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
	}
	
	private String post(String context,TreeMap<String,String> params){
		String url = configProvider.getValue("parking.kexinxiaomao.url", "");
		String parkId = configProvider.getValue("parking.kexinxiaomao.parkId", "");
		String accessKeyId = configProvider.getValue("parking.kexinxiaomao.accessKeyId", "");
		String accessKeyValue = configProvider.getValue("parking.kexinxiaomao.accessKeyValue", "");
		
		params.put("parkId", parkId);
		params.put("accessKeyId", accessKeyId);
		params.put("sign", KexinXiaomaoSignUtil.getSign(params, accessKeyValue));
		return  Utils.post(url + context, JSONObject.parseObject(StringHelper.toJsonString(params)),
				StandardCharsets.UTF_8);
	}
	
	 /** 
	* @see com.everhomes.parking.handler.DefaultParkingVendorHandler#getOpenCardInfo(com.everhomes.rest.parking.GetOpenCardInfoCommand)   
	* @Function: XiaomaoKeXingParkingVendorHandler.java
	* @Description: 月卡审核成功后，调用该方法显示月卡缴费相关信息
	*
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年4月9日 下午4:45:30 
	*/
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
			cardInfoDTO.setExpireDate(getCardEndTime(now, requestMonthCount).getTime());

			// 根据配置设定收费标准，默认按实际天数，即ParkingCardExpiredRechargeType.ACTUAL(2)
			if (requestRechargeType == ParkingCardExpiredRechargeType.ALL.getCode()) {
				cardInfoDTO.setPayMoney(cardInfoDTO.getPrice().multiply(new BigDecimal(requestMonthCount)));
			} else {
				//正中会暂不支持此模式
//				Calendar calendar = Calendar.getInstance();
//				calendar.setTimeInMillis(now);
//				int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//				int today = calendar.get(Calendar.DAY_OF_MONTH);
//
//				BigDecimal price = cardInfoDTO.getPrice().multiply(new BigDecimal(requestMonthCount - 1))
//						.add(cardInfoDTO.getPrice().multiply(new BigDecimal(maxDay - today + 1))
//								.divide(new BigDecimal(DAY_COUNT), OPEN_CARD_RETAIN_DECIMAL, RoundingMode.HALF_UP));
//				cardInfoDTO.setPayMoney(price);
			}
			
			cardInfoDTO.setOrderType(ParkingOrderType.OPEN_CARD.getCode());

		} while (false);
		
		return cardInfoDTO;
	}
	
    private void populaterate(List<KexinXiaomaoCardType> types, ParkingRechargeRateDTO dto, ParkingRechargeRate rate) {
    	KexinXiaomaoCardType temp = null;
        for(KexinXiaomaoCardType type: types) {
            if(type.getStandardId().equals(rate.getCardType())) {
                temp = type;
                break;
            }
        }
        
        if (null == temp) {
        	return;
        }
        
        dto.setCardTypeId(temp.getStandardId());
        dto.setCardType(temp.getStandardType());
        dto.setRateToken(rate.getId().toString());
        dto.setVendorName(ParkingLotVendor.KEXIN_XIAOMAO.getCode());
    }
	
    
	/**   
	* @Function: XiaomaoKeXingParkingVendorHandler.java
	* @Description: 判断一次充值是否过期，我们假设在3分钟内充值成功不算过期
	*
	* @param:endTime 需要比较的时间
	* @return：Boolean true-过期，false-未过期
	*
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年4月9日 下午5:29:55 
	*
	*/
	private static Boolean isOutOfDate(Timestamp endTime) {

		if (null == endTime) {
			return true;
		}

		Calendar time = Calendar.getInstance();
		time.add(Calendar.MILLISECOND, -OUT_DATE_TIME); // 过期后3分钟之内支付都算成功

		Timestamp outDateTime = new Timestamp(time.getTimeInMillis());

		return endTime.before(outDateTime);
	}
	
	
    /**   
    * @Function: XiaomaoKeXingParkingVendorHandler.java
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
		Timestamp timestampEnd = getCardEndTime(nowTime, order.getMonthCount().intValue());
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

        TreeMap<String, String> param = new TreeMap<String,String>();
        param.put("standardId",request.getCardTypeId());
        param.put("beginTime",validStart);//月卡开始时间，按照现在时间来
        param.put("stopTime",validEnd);//结束时间
        param.put("licenseNumber", order.getPlateNumber());
        param.put("money", order.getPrice().setScale(2).toString());
        param.put("payType", VendorType.WEI_XIN.getCode().equals(order.getPaidType())?"wxpay" : "alipay");

        param.put("userName", request.getPlateOwnerName());
        param.put("userTel", request.getPlateOwnerPhone());
        param.put("companyName", request.getPlateOwnerEntperiseName());


        String json = post(HANDLER_MONTHCARD,param);
        order.setErrorDescriptionJson(json);

        JSONObject jsonObject = JSONObject.parseObject(json);
        Object obj = jsonObject.get("flag");
        if(null != obj ) {
            int resCode = (int) obj;
            if(resCode == 1) {
                return true;
            }
        }

        return false;
    }
    

	private final List<KexinXiaomaoCardType> listCardTypes() {

		List<KexinXiaomaoCardType> cardTypeList = new ArrayList<KexinXiaomaoCardType>(0);

		do {
			// 向第三方获取卡类型
			String result = post(GET_MONTHCARD_TYPE, new TreeMap<String, String>());
			if (StringUtils.isBlank(result)) {
				break;
			}

			// 解析
			KexinXiaomaoJsonEntity<List<KexinXiaomaoCardType>> entity = JSONObject.parseObject(result,
					new TypeReference<KexinXiaomaoJsonEntity<List<KexinXiaomaoCardType>>>() {
					});
			if (null == entity || !entity.isSuccess()) {
				break;
			}

			cardTypeList = entity.getData();

		} while (false);

		return cardTypeList;
	}
	
	
	/**   
	* @Function: XiaomaoKeXingParkingVendorHandler.java
	* @Description: 根据source时间戳增加mounth个月后的时间
	* 规则：每次都是添加下个月的最大天数
	* 	今天是1月1日，2月有28天，有效期到1月29日23点59分59秒
	*	今天是2月10日（2月有28天），3月有31天，有效期到3月13日23点59分59秒
	*   
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年4月12日 下午8:23:12 
	*
	*/
	private final static Timestamp getCardEndTime(long expireTime, int addMounthNum) {
		return Utils.getTimestampByAddDistanceMonth(expireTime, addMounthNum);
	}
	
	
	/**   
	* @Function: XiaomaoKeXingParkingVendorHandler.java
	* @Description: 获取月卡开始时间。(默认为系统当前时间戳)
	* 为了测试不同的开卡时间，可通过parking.kexinxiaomao.openCardDate参数进行配置开始时间
	*
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年4月17日 下午3:38:32 
	*
	*/
	private final long getStartTimeMillis() {
		String cardStartStr = configProvider.getValue("parking.kexinxiaomao.openCardDate", "");
		if (StringUtils.isBlank(cardStartStr)) {
			return System.currentTimeMillis();
		}
		
		return Utils.strToLong(cardStartStr, Utils.DateStyle.DATE_TIME);
	}
	
}
