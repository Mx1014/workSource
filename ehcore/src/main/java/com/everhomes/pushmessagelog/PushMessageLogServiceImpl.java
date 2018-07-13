package com.everhomes.pushmessagelog;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.common.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.configurations.ConfigurationsServiceImpl;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.message.PushMessageToAdminAndBusinessContactsCommand;
import com.everhomes.rest.organization.pm.SendNoticeCommand;
import com.everhomes.rest.organization.pm.SendNoticeMode;
import com.everhomes.rest.pushmessagelog.PushMessageListCommand;
import com.everhomes.rest.pushmessagelog.PushMessageLogDTO;
import com.everhomes.rest.pushmessagelog.PushMessageLogReturnDTO;
import com.everhomes.rest.pushmessagelog.PushMessageStringCode;
import com.everhomes.rest.pushmessagelog.PushStatusCode;
import com.everhomes.rest.pushmessagelog.ReceiverTypeCode;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import com.everhomes.util.RuntimeErrorException;

@Component
public class PushMessageLogServiceImpl implements PushMessageLogService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationsServiceImpl.class);
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private PushMessageLogProvider pushMessageLogProvider;
	
	@Autowired
    private CommunityProvider communityProvider;
	
	@Autowired
	private UserProvider userProvider;
	
	@Autowired
	LocaleStringService localeStringService ;

	@Override
	public PushMessageLogReturnDTO listPushMessageLogByNamespaceIdAndOperator(PushMessageListCommand cmd) {
		//如果传参对象为空，抛出异常
				if(cmd == null ) {
					String msg = "cmd  cannot be null.";
					throwSelfDefNullException(msg);
				}
				//若前台无每页数量传来则取默认配置的
				int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
				//创建锚点分页所需的对象
				CrossShardListingLocator locator = new CrossShardListingLocator();
				locator.setAnchor(cmd.getPageAnchor());
				//主要用于对传入的 nameSpaceId 为空时的处理，为空则从当前环境中获取
				Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
				//Provider 层传进行查询并返回对象
				List<PushMessageLog> boList = pushMessageLogProvider
						    .listPushMessageLogByNamespaceIdAndOperator(namespaceId, cmd.getOperatorId(), pageSize, locator);
				 //对象转换
				PushMessageLogReturnDTO returnDto = new PushMessageLogReturnDTO ();

				returnDto.setDtos(boList.stream().map(r->{
					//copy 相同属性下的值
					PushMessageLogDTO dto = ConvertHelper.convert(r, PushMessageLogDTO.class);
					//转换出操作人
					if(r.getOperatorId() != null){
						String name = userProvider.getNickNameByUid(r.getOperatorId()==null?null:r.getOperatorId().longValue());
						String mobile = userProvider.findMobileByUid(r.getOperatorId()==null?null:r.getOperatorId().longValue()) ;
						 if(StringUtils.isNotBlank(name)){
							dto.setOperator(name );
						}
						 if(StringUtils.isNotBlank(mobile)){
							dto.setPhone(mobile);
						}
						/*else{
							dto.setOperator("Not Found");
						}*/
					}
					//转换推送对象
					if(StringUtils.isNotBlank(r.getReceivers())){
						String[] receiver = r.getReceivers().split(",");
						//asList方法在参数为空的情况下会报指针异常
						if(receiver != null){
							dto.setReceivers(Arrays.asList(receiver));
						}					
					}
						return dto;
					}).collect(Collectors.toList()));
				
				//设置下一页开始锚点
				returnDto.setNextPageAnchor(locator.getAnchor());
				return returnDto;
	}

	@Override
	public PushMessageLog getPushMessageLogById(Integer id) {
		
		return pushMessageLogProvider.getPushMessageLogById(id);
	}

	@Override
	public Long crteatePushMessageLog(PushMessageLog bo) {
		return pushMessageLogProvider.crteatePushMessageLog(bo);

	}

	@Override
	public void updatePushMessageLog(PushMessageLog bo) {
		pushMessageLogProvider.updatePushMessageLog(bo);

	}

	@Override
	public void deletePushMessageLog(PushMessageLog bo) {
		pushMessageLogProvider.deletePushMessageLog(bo);

	}
	
	/**
	 * 抛出对象或其ID属性为空的异常
	 * @param msg 报错信息
	 */
	private void throwSelfDefNullException(String msg ){
		LOGGER.error(msg);
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,msg);
	}

	public void updatePushStatus(Long id , Byte status){
		if(id == null){
			LOGGER.error(" update pushstatus fail, because of the id is null");
			return ;
		}
		PushMessageLog bo = pushMessageLogProvider.getPushMessageLogById(id.intValue());
		if(bo == null){
			LOGGER.error(" update pushstatus fail,because can not find record for id = {} ",id);
			return ;
		}
		bo.setPushStatus(status==null?null:status.intValue());
		pushMessageLogProvider.updatePushMessageLog(bo);
	}
	
	public Long createfromSendNotice(SendNoticeCommand cmd ,Byte pushTypeCode){
		if(cmd == null) return null ;
		
		PushMessageLog  bo = new PushMessageLog(); 
        bo.setContent(cmd.getMessage());
        Timestamp createTime=DateUtils.currentTimestamp();
        bo.setCreateTime(createTime);
        bo.setNamespaceId(cmd.getNamespaceId());
        bo.setOperatorId(UserContext.currentUserId()==null?null:UserContext.currentUserId().intValue());
        bo.setPushStatus(new Byte(PushStatusCode.WAITING.getCode()).intValue());
        bo.setPushType(pushTypeCode==null?null:pushTypeCode.intValue());
        if(cmd.getSendMode() != null ){
	        if(SendNoticeMode.NAMESPACE.getCode().equals(cmd.getSendMode())){//所有人
	        	bo.setReceiverType(new Byte(ReceiverTypeCode.ALL.getCode()).intValue());
	        	
	        }else if(SendNoticeMode.MOBILE.getCode().equals(cmd.getSendMode())){//按手机号
	        	bo.setReceiverType(new Byte(ReceiverTypeCode.PHONE.getCode()).intValue());
	        	List<String> receiverList = cmd.getMobilePhones();
	        	//组装推送对象
	 	        if(receiverList !=null && receiverList.size()>0){
	 	        	StringBuffer receivers = new StringBuffer();
	 	        	if(receiverList !=null && receiverList.size()>0){
	 	        		for(String str : receiverList){
	 	        			receivers.append(str).append(",");
	 	        		}
	 	        	}
	 	        	if(receivers.toString().length()>0){
		        		bo.setReceivers(receivers.toString().substring(0, receivers.toString().length()-1));
		        	}
	 	        }
	        	
	        }else if(SendNoticeMode.COMMUNITY.getCode().equals(cmd.getSendMode())){//按项目
	        	bo.setReceiverType(new Byte(ReceiverTypeCode.COMMUNITY.getCode()).intValue());
	        	//组装推送对象
	        	List<Long> communityIdList = cmd.getCommunityIds();
	        	StringBuffer receivers = new StringBuffer();
	        	for(Long id : communityIdList){
	        		Community com = communityProvider.findCommunityById(id);
	        		if(com != null && StringUtils.isNotBlank(com.getName())){
	        			receivers.append(com.getName()).append(",");
	        		}
	        	}
	        	if(receivers.toString().length()>0){
	        		bo.setReceivers(receivers.toString().substring(0, receivers.toString().length()-1));
	        	}
	        	
	        }	                    	
        }
        return  pushMessageLogProvider.crteatePushMessageLog(bo);
        
	}
	
	/**
	 * 短信方式推送时创建消息记录
	 * @param cmd
	 * @return
	 */
	@Override
	public Long createfromSendNotice(PushMessageToAdminAndBusinessContactsCommand cmd ,Byte pushTypeCode ){
		if(cmd == null) return null ;
		
		PushMessageLog  bo = new PushMessageLog(); 
        bo.setContent(cmd.getContent());
        Timestamp createTime=DateUtils.currentTimestamp();
        bo.setCreateTime(createTime);
        bo.setNamespaceId(UserContext.getCurrentNamespaceId());
        bo.setOperatorId(UserContext.currentUserId()==null?null:UserContext.currentUserId().intValue());
        bo.setPushStatus(new Byte(PushStatusCode.WAITING.getCode()).intValue());
        bo.setPushType(pushTypeCode==null?null:pushTypeCode.intValue());
        bo.setReceiverType(new Byte(ReceiverTypeCode.COMMUNITY.getCode()).intValue());
        if(cmd.getCommunityId() != null){
        	StringBuffer receivers = new StringBuffer();
        	if(cmd.getAdminFlag() == 1){
        		String tex = localeStringService.getLocalizedString(PushMessageStringCode.SCOPE , PushMessageStringCode.ADMIN+"","zh_CN","");
        		receivers.append(tex);
        	}
        	if(cmd.getBusinessContactFlag() == 1){
        		if(receivers !=null && receivers.length()>0){
        			receivers.append("、");
        		}
        		String tex = localeStringService.getLocalizedString(PushMessageStringCode.SCOPE , PushMessageStringCode.BUSINESS+"","zh_CN","");
        		receivers.append(tex);
        	}
        	Community com = communityProvider.findCommunityById(cmd.getCommunityId());
    		if(com != null && StringUtils.isNotBlank(com.getName())){
    			receivers.append("（").append(com.getName()).append("）");
    		}
    		bo.setReceivers(receivers.toString());
        }
        
        return  pushMessageLogProvider.crteatePushMessageLog(bo);
        
	}

}
