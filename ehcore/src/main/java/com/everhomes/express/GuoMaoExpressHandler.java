// @formatter:off
package com.everhomes.express;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.express.guomao.GuoMaoChinaPostResponse;
import com.everhomes.express.guomao.GuoMaoChinaPostResponseEntity;
import com.everhomes.express.guomao.rit.model.ArrayOfMail;
import com.everhomes.express.guomao.rit.model.Mail;
import com.everhomes.express.guomao.rit.service.MailTtServiceGn;
import com.everhomes.express.guomao.rit.service.MailTtServiceGnPortType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.parking.handler.Utils;
import com.everhomes.rest.express.*;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//后面的4为表eh_express_companies中父id为0的行的id 国贸物业酒店管理有限公司
@Component(ExpressHandler.EXPRESS_HANDLER_PREFIX+"4")
public class GuoMaoExpressHandler implements ExpressHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(GuoMaoExpressHandler.class);


	//java8新加的格式化时间类，是线程安全的
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.systemDefault());

	@Autowired
	private ExpressOrderProvider expressOrderProvider;
	
	@Autowired
	private ExpressService expressService;
	
    @Autowired
	private FlowService flowService;

    @Autowired
    private ConfigurationProvider configProvider;

	@Override
	public String getBillNo(ExpressOrder expressOrder) {
		return null;
	}

	@Override
	public GetExpressLogisticsDetailResponse getExpressLogisticsDetail(ExpressCompany expressCompany, String billNo) {
		return null;
	}

	@Override
	public void createOrder(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		Flow flow = flowService.getEnabledFlow(expressOrder.getNamespaceId(), ExpressServiceErrorCode.MODULE_CODE,
				FlowModuleType.NO_MODULE.getCode(), expressOrder.getOwnerId(), expressOrder.getOwnerType());
		//当没有设置工作流的时候，表示是禁用模式
		if(null == flow) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.EMPTY_WORK_FLOW,
					"未启用工作流");
		}
		User user = UserContext.current().getUser();
		CreateFlowCaseCommand cmd21 = new CreateFlowCaseCommand();
		cmd21.setApplyUserId(user==null?null:user.getId());
		cmd21.setReferType(FlowReferType.EXPRESS.getCode());
		cmd21.setReferId(expressOrder.getId());
		cmd21.setProjectType(expressOrder.getOwnerType());
		cmd21.setProjectId(expressOrder.getOwnerId());
		cmd21.setTitle("国贸快递");
		cmd21.setFlowMainId(flow.getFlowMainId());
		cmd21.setFlowVersion(flow.getFlowVersion());
		FlowCase flowCase = flowService.createFlowCase(cmd21);
		if(flowCase==null || flowCase.getId()==null){
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.ERROR_CREATE_FLOWCASE,
					"创建工作流失败");
		}
		expressOrder.setFlowCaseId(flowCase.getId());
		expressOrder.setStatus(ExpressOrderStatus.PROCESSING.getCode());
	}

	@Override
	public void updateOrderStatus(ExpressOrder expressOrder, ExpressCompany expressCompany) {

	}

	@Override
	public void getOrderStatus(ExpressOrder expressOrder, ExpressCompany expressCompany) {

	}

	@Override
	public void orderStatusCallback(ExpressOrder expressOrder, ExpressCompany expressCompany, Map<String, String> params) {

	}
}
