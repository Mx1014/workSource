package com.everhomes.chatrecord;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.message.MessageProvider;
import com.everhomes.message.MessageRecord;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.message.MessageRecordSenderTag;
import com.everhomes.rest.message.MessageRecordStatus;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.servicehotline.ChatGroupDTO;
import com.everhomes.rest.servicehotline.ChatMessageType;
import com.everhomes.rest.servicehotline.ChatRecordDTO;
import com.everhomes.rest.servicehotline.GetChatGroupListCommand;
import com.everhomes.rest.servicehotline.GetChatGroupListResponse;
import com.everhomes.rest.servicehotline.GetChatRecordListCommand;
import com.everhomes.rest.servicehotline.GetChatRecordListResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhMessageRecords;
import com.everhomes.server.schema.tables.EhUserIdentifiers;
import com.everhomes.server.schema.tables.EhUsers;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.NumberUtils;
import com.everhomes.util.TextUtils;

/**
 * 提供一个公共的客服记录获取算法
 * 简化代码
 * @author huangmingbo
 *
 */
public abstract class AbstractChatRecordService implements ChatRecordService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractChatRecordService.class);
	
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private UserProvider userProvider;
	
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private MessageProvider messageProvider;
	
	//获取客服列表需要子类实现如下方法
	protected abstract TargetDTO getSingleServicer(Integer namespaceId, String ownerType, Long ownerId, Long servicerId, String extrParam);
	protected abstract List<TargetDTO> getAllServicers(Integer namespaceId, String ownerType, Long ownerId, String extrParam);
	protected abstract void checkOnlineServicePrivilege(Long currentOrgId, Long appId, Long checkCommunityId);
	
	/*
	 * 功能：获取会话列表
	 * 根据条件获取会话列表。 相同的两个人的会话定义为一个会话。 
	 * 例： 
	 * 客服A与用户A的有100条聊天记录，归为 “客服A-用户A”一个会话
	 * 客服A与用户B的有1条聊天记录，归为 “客服A-用户B”一个会话 
	 * 客服A与用户C的无聊天记录，不属于会话。 
	 * 注意：锚点的意义——下次查找时起始id
	 */
	public GetChatGroupListResponse getChatGroupList(GetChatGroupListCommand cmd) {

		// 校验权限
		checkOnlineServicePrivilege(cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());

		// 获取namespaceId
		Integer namespaceId = getValidNamespaceId(cmd.getNamespaceId());

		// 获取页码数据
		int pageSize = getValidPageSize(cmd.getPageSize());

		// 设置锚点
		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		// 获取所有客服id
		List<TargetDTO> servicerDtos = null;
		if (cmd.getServicerId() == null) {
			// 未传客服id时，认为是查询全部客服，包括被删除的客服
			servicerDtos = getAllServicers(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), cmd.getExtraParam());
		} else {
			// 传了客服id，认为是指定客服
			TargetDTO servicer = getSingleServicer(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(),
					cmd.getServicerId(), cmd.getExtraParam());
			if (null != servicer) {
				servicerDtos = new ArrayList<>(1);
				servicerDtos.add(servicer);
			}
		}

		// 未找到相应客服，直接返回
		if (CollectionUtils.isEmpty(servicerDtos)) {
			return new GetChatGroupListResponse();
		}

		// 根据keywork获得用户
		List<TargetDTO> customerDtos = null;
		if (!StringUtils.isBlank(cmd.getKeyword())) {
			customerDtos = findUserByTokenOrNickName(cmd.getKeyword(), namespaceId);
			if (CollectionUtils.isEmpty(customerDtos)) {
				return new GetChatGroupListResponse();
			}
		}

		// 获取消息
		List<ChatGroupDTO> dtoList = getChatGroupList(namespaceId, pageSize, locator, servicerDtos, customerDtos);

		// 设置返回数据
		GetChatGroupListResponse rsp = new GetChatGroupListResponse();
		rsp.setNextPageAnchor(locator.getAnchor());
		rsp.setChatGroupList(dtoList);

		return rsp;
	}
	
	/*
	 * 根据客服id与用户id查询聊天记录
	 * 
	 */
	@Override
	public GetChatRecordListResponse getChatRecordList(GetChatRecordListCommand cmd) {

		// 检查权限
		checkOnlineServicePrivilege(cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());

		// 判空
		if (null == cmd.getServicerId() || null == cmd.getCustomerId()) {
			return new GetChatRecordListResponse();
		}

		// 获取namespaceId
		Integer namespaceId = getValidNamespaceId(cmd.getNamespaceId());

		// 获取页码数据
		int pageSize = getValidPageSize(cmd.getPageSize());

		// 获取客服和用户
		TargetDTO servicerDto = getSingleServicer(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getServicerId(), cmd.getExtraParam());
		if (null == servicerDto) {
			return new GetChatRecordListResponse();
		}

		TargetDTO customerDto = findUserNameIdentifierById(cmd.getCustomerId());
		if (null == customerDto) {
			return new GetChatRecordListResponse();
		}

		// 获取聊天记录
		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<ChatRecordDTO> chatRecordList = getChatRecordList(pageSize, locator, cmd.getNamespaceId(), servicerDto,
				customerDto);

		// 设置返回数据
		GetChatRecordListResponse rsp = new GetChatRecordListResponse();
		rsp.setNextPageAnchor(locator.getAnchor());
		rsp.setChatRecordList(chatRecordList);
		return rsp;
	}

	/**
	 * 根据客服id与用户id导出聊天记录
	 */
	@Override
	public void exportChatRecordList(GetChatRecordListCommand cmd, HttpServletResponse httpResponse) {

		// 检查权限
		checkOnlineServicePrivilege(cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());

		// 获取记录
		if (null == cmd.getServicerId() || null == cmd.getCustomerId()) {
			return;
		}

		// 获取页码数据
		int pageSize = getValidPageSize(cmd.getPageSize());

		// 获取客服和用户
		TargetDTO servicerDto = getSingleServicer(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getServicerId(), cmd.getExtraParam());
		if (null == servicerDto) {
			return;
		}

		TargetDTO customerDto = findUserNameIdentifierById(cmd.getCustomerId());
		if (null == customerDto) {
			return;
		}

		// 获取聊天记录
		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<ChatRecordDTO> chatRecordList = getChatRecordList(pageSize, locator, cmd.getNamespaceId(), servicerDto,
				customerDto);
		if (CollectionUtils.isEmpty(chatRecordList)) {
			return;
		}

		// 获取拼接内容
		List<String> dataList = createSingleChatGroupCotent(chatRecordList, servicerDto, customerDto);

		String fileName = servicerDto.getTargetName() + "与" + customerDto.getTargetName() + "（"
				+ customerDto.getUserIdentifier() + "）的消息记录.txt";

		// 输出文档
		exportTxt(httpResponse, dataList, fileName);

		return;
	}


	/**
	 * 根据条件导出相应聊天记录
	 */
	public void exportMultiChatRecordList(GetChatGroupListCommand cmd, HttpServletResponse httpResponse) {

		// 检查权限
		checkOnlineServicePrivilege(cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());

		// 获取namespaceId
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();

		// 获取页码数据
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		// 设置锚点
		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		// 获取所有客服id
		List<TargetDTO> servicerDtos = null;
		if (cmd.getServicerId() == null) {
			// 未传客服id时，认为是查询全部客服，包括被删除的客服
			servicerDtos = getAllServicers(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), cmd.getExtraParam());
		} else {
			// 传了客服id，认为是指定客服
			TargetDTO servicer = getSingleServicer(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(),
					cmd.getServicerId(), cmd.getExtraParam());
			if (null != servicer) {
				servicerDtos = new ArrayList<>(1);
				servicerDtos.add(servicer);
			}
		}

		// 未找到相应客服，直接返回
		if (CollectionUtils.isEmpty(servicerDtos)) {
			return;
		}

		// 根据keywork获得用户
		List<TargetDTO> customerDtos = null;
		if (!StringUtils.isBlank(cmd.getKeyword())) {
			customerDtos = findUserByTokenOrNickName(cmd.getKeyword(), namespaceId);
			if (CollectionUtils.isEmpty(customerDtos)) {
				return;
			}
		}

		// 获取消息
		List<ChatGroupDTO> dtoList = getChatGroupList(namespaceId, pageSize, locator, servicerDtos, customerDtos);
		if (CollectionUtils.isEmpty(dtoList)) {
			return;
		}

		List<String> dataList = new ArrayList<String>(1000);
		for (ChatGroupDTO group : dtoList) {
			// 获取聊天记录
			List<ChatRecordDTO> chatRecordList = getChatRecordList(pageSize, new ListingLocator(), namespaceId,
					group.getServicer(), group.getCustomer());
			if (CollectionUtils.isEmpty(chatRecordList)) {
				continue;
			}

			// 获取拼接内容
			List<String> tmpList = createSingleChatGroupCotent(chatRecordList, group.getServicer(),
					group.getCustomer());
			if (CollectionUtils.isEmpty(chatRecordList)) {
				continue;
			}

			dataList.addAll(tmpList);
		}

		// 获取文件名
		Community community = communityProvider.findCommunityById(cmd.getOwnerId());
		String communityName = null == community ? "" : community.getName();
		String fileName = communityName + "-消息记录.txt";

		// 导出
		exportTxt(httpResponse, dataList, fileName);
		return;
	}

	
	
	
	/**
	 * 用于查询会话列表的实际逻辑
	 */
	private List<ChatGroupDTO> getChatGroupList(Integer namespaceId, Integer pageSize, ListingLocator locator,
			List<TargetDTO> servicerDtos, List<TargetDTO> customerDtos) {

		// 1.必须有客服做筛选条件
		if (CollectionUtils.isEmpty(servicerDtos)) {
			return null;
		}

		// 2.1将客服转成id列表
		List<Long> servicerIds = new ArrayList<>(10);
		if (!CollectionUtils.isEmpty(servicerDtos)) {
			servicerDtos.forEach(r -> {
				servicerIds.add(r.getTargetId());
			});
		}

		// 2.2将用户转成id列表
		List<Long> customerIds = new ArrayList<>(10);
		if (!CollectionUtils.isEmpty(customerDtos)) {
			customerDtos.forEach(r -> {
				customerIds.add(r.getTargetId());
			});
		}

		// 3.获取会话列表
		List<MessageRecord> msgRecList = messageProvider.listMessageRecords(pageSize, locator, (l, q) -> {
			return buildChatGroupQuery(l, q, pageSize, servicerIds, customerIds);
		});

		List<ChatGroupDTO> dtoList = new ArrayList<>(10);
		ChatGroupDTO chatGroup = null;
		for (MessageRecord msg : msgRecList) {
			chatGroup = new ChatGroupDTO();
			setChatGroup(chatGroup, msg, servicerDtos, customerDtos);
			dtoList.add(chatGroup);
		}

		return dtoList;
	}

	/**
	 * 获取聊天记录的逻辑处理
	 */
	private List<ChatRecordDTO> getChatRecordList(Integer inputPageSize, ListingLocator locator, Integer namespaceId, TargetDTO servicerDto,
			TargetDTO customerDto) {

		// 判空
		if (null == servicerDto || null == customerDto || null == servicerDto.getTargetId()
				|| null == customerDto.getTargetId()) {
			return null;
		}

		// 获取客服发送列表
		Long anchor = locator.getAnchor();
		int pageSize = null == inputPageSize ? 0 : inputPageSize;
		List<MessageRecord> servicerList = messageProvider.listMessageRecords(pageSize + 1, locator, (l, q) -> {
			return buildChatRecordQuery(l, q, pageSize + 1, namespaceId, servicerDto.getTargetId(),
					customerDto.getTargetId());
		});

		// 获取用户发送列表
		List<MessageRecord> customerList = null;
		locator.setAnchor(anchor);
		if (!servicerDto.getTargetId().equals(customerDto.getTargetId())) {
			customerList = messageProvider.listMessageRecords(pageSize + 1, locator, (l, q) -> {
				return buildChatRecordQuery(l, q, pageSize + 1, namespaceId, customerDto.getTargetId(),
						servicerDto.getTargetId());
			});
		}

		// 合并记录
		List<MessageRecord> totalMsgList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(servicerList)) {
			totalMsgList.addAll(servicerList);
		}

		if (!CollectionUtils.isEmpty(customerList)) {
			totalMsgList.addAll(customerList);
		}

		// 判空
		if (CollectionUtils.isEmpty(totalMsgList)) {
			return null;
		}

		// 以index_id倒叙排
		Collections.sort(totalMsgList, (cmpA, cmpB) -> {
			return (int) (cmpB.getIndexId() - cmpA.getIndexId());
		});

		// 设置Anchor
		int finalMsgSize = totalMsgList.size();
		if (pageSize > 0 && totalMsgList.size() > pageSize) {
			locator.setAnchor(totalMsgList.get(pageSize).getId());
			finalMsgSize = pageSize;
		} else {
			locator.setAnchor(null);
		}

		ChatRecordDTO dto = null;
		List<ChatRecordDTO> chatRecordDtos = new ArrayList<>(50);
		for (int i = 0; i < finalMsgSize; i++) {
			dto = new ChatRecordDTO();

			// 填充查询结果
			setChatRecord(dto, totalMsgList.get(i), servicerDto, customerDto);
			chatRecordDtos.add(dto);
		}

		return chatRecordDtos;
	}

	
	/**
	 * 聊天记录的sql逻辑。只要发送了，就算是聊天记录。 即 status = 'CORE_HANDLE' sender_tag = 'ROUTE
	 * MESSAGE' 注：index_id相同的表示同一条消息。且数字越大消息越新
	 * 
	 * @param locator
	 *            分页器
	 * @param query
	 *            查询handler
	 * @param servicerId
	 *            客服id
	 * @param customerId
	 *            用户id
	 * @return
	 */
	private SelectQuery<? extends Record> buildChatRecordQuery(ListingLocator locator,
			SelectQuery<? extends Record> query, Integer pageSize, Integer namespaceId, Long senderUserId,
			Long receiverUserId) {

		final String TYPE_USER = "user";

		// 对当前表取别名
		EhMessageRecords records = Tables.EH_MESSAGE_RECORDS;

		// 增加域空间条件
		if (null != namespaceId) {
			query.addConditions(records.NAMESPACE_ID.eq(namespaceId));
		}

		// 构建发送者和接收者条件
		Condition sendCon = records.SENDER_UID.eq(senderUserId)
				.and(records.DST_CHANNEL_TYPE.eq(TYPE_USER).and(records.DST_CHANNEL_TOKEN.eq("" + receiverUserId)));

		// 固定条件: status = 'CORE_HANDLE', sender_tag = 'ROUTE MESSAGE'
		Condition fixedCond = records.STATUS.eq(MessageRecordStatus.CORE_HANDLE.getCode())
				.and(records.SENDER_TAG.eq(MessageRecordSenderTag.ROUTE_MESSAGE.getCode()));

		// 添加条件
		query.addConditions(sendCon);
		query.addConditions(fixedCond);

		// 做分组，排序，分页
		query.addGroupBy(records.INDEX_ID);
		query.addOrderBy(records.INDEX_ID.desc());

		return query;
	}

	/**
	 * 获取会话列表的sql逻辑 即 status = 'CORE_HANDLE' sender_tag = 'ROUTE MESSAGE'
	 * 注：这种消息可能有多条，index_id为唯一标识
	 * 
	 * @param locator
	 *            分页器
	 * @param query
	 *            查询handler
	 * @param servicerId
	 *            客服id
	 * @param customerId
	 *            用户id
	 * @return
	 */
	private SelectQuery<? extends Record> buildChatGroupQuery(ListingLocator locator,
			SelectQuery<? extends Record> query, Integer pageSize, List<Long> servicerIds, List<Long> customerIds) {

		final String TYPE_USER = "user";

		// 对当前表取别名
		EhMessageRecords records = Tables.EH_MESSAGE_RECORDS;

		// 客服发送
		Condition sendByServicer = records.SENDER_UID.in(servicerIds);

		// 客服接收
		Condition sendByCustomer = records.DST_CHANNEL_TYPE.eq(TYPE_USER)
				.and(records.DST_CHANNEL_TOKEN.in(CollectionUtils.collect(servicerIds, (i) -> {
					return i + "";
				})));

		if (null != customerIds && !customerIds.isEmpty()) {
			// 增加
			sendByServicer = sendByServicer.and(records.DST_CHANNEL_TYPE.eq(TYPE_USER)
					.and(records.DST_CHANNEL_TOKEN.in(CollectionUtils.collect(customerIds, (i) -> {
						return i + "";
					}))));
			sendByCustomer = sendByCustomer.and(records.SENDER_UID.in(customerIds));
		} else {
			// 去除系统用户 坑啊!
			sendByCustomer = sendByCustomer.and(records.SENDER_UID.greaterThan(User.MAX_SYSTEM_USER_ID));
			
			// 有可能是群信息 缺陷 #31090
			sendByServicer = sendByServicer.and(records.DST_CHANNEL_TYPE.eq(TYPE_USER));
		}

		// 条件3 固定条件: status = 'CORE_HANDLE', sender_tag = 'ROUTE MESSAGE'
		Condition fixedCond = records.STATUS.eq(MessageRecordStatus.CORE_HANDLE.getCode())
				.and(records.SENDER_TAG.eq(MessageRecordSenderTag.ROUTE_MESSAGE.getCode()));

		// 添加条件
		query.addConditions(sendByServicer.or(sendByCustomer));
		query.addConditions(fixedCond);

		// 做分组，排序，分页
		query.addGroupBy(DSL.decode()
				.when(records.SENDER_UID.gt(records.DST_CHANNEL_TOKEN.cast(Long.class)),
						DSL.concat(records.DST_CHANNEL_TOKEN, DSL.field("'_'", String.class), records.SENDER_UID))
				.otherwise(DSL.concat(records.SENDER_UID, DSL.field("'_'", String.class), records.DST_CHANNEL_TOKEN)));

		query.addOrderBy(records.INDEX_ID.desc());

		return query;
	}
	
	
	/**
	 * 根据传入的手机号或用户名获取用户列表
	 * 
	 * @param keyword
	 *            必填
	 * @param namespaceId
	 *            必填
	 * @return
	 */
	private List<TargetDTO> findUserByTokenOrNickName(String inputKeyword, int namespaceId) {

		if (StringUtils.isBlank(inputKeyword)) {
			return null;
		}
		
		String keyword = inputKeyword.trim();

		// 1.只要是数字，就先进行手机号搜索
		if (NumberUtils.isNumber(keyword)) {
			TargetDTO target = userProvider.findUserByToken(keyword, namespaceId);
			if (null != target) {
				List<TargetDTO> targetList = new ArrayList<TargetDTO>(1);
				targetList.add(target);
				return targetList;
			}
		}

		// 2.如果不是手机号，或者手机号未找到。都需要通过nickName搜索。
		// 因为有人可能把昵称设置成数字号码了
		return findUserByNickName(keyword, namespaceId);
	}
	
	
	/**
	 * 根据用户id获取到姓名和电话号码
	 * 姓名从eh_users的nick_name获取。
	 * 号码从eh_user_identifiers中identifier_token获取
	 * @param userId
	 * @return
	 */
	private TargetDTO findUserNameIdentifierById(Long userId) {
		return userProvider.findUserTargetById(userId);
	}
	
	
	/**
	 * 根据用户id获取到姓名和电话号码
	 * 姓名从eh_users的nick_name获取。
	 * 号码从eh_user_identifiers中identifier_token获取
	 * @param userId
	 * @return
	 */
	private List<TargetDTO> findUserByNickName(String nickName, Integer namespaceId) {
		
		if (StringUtils.isBlank(nickName) || null == namespaceId) {
			return null;
		}

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhUserIdentifiers token = Tables.EH_USER_IDENTIFIERS.as("token");
		EhUsers user = Tables.EH_USERS.as("user");

		List<TargetDTO> dtoList = new ArrayList<>(10);
		context.select(token.IDENTIFIER_TOKEN, user.ID, user.NICK_NAME).from(user).leftOuterJoin(token)
				.on(token.OWNER_UID.eq(user.ID))
				.where(user.NICK_NAME.eq(nickName).and(user.NAMESPACE_ID.eq(namespaceId))).fetch().map(r -> {
					TargetDTO dto = new TargetDTO();
					dto.setTargetName(r.getValue(user.NICK_NAME));
					dto.setTargetType("eh_user");
					dto.setTargetId(r.getValue(user.ID));
					dto.setUserIdentifier(r.getValue(token.IDENTIFIER_TOKEN));
					dtoList.add(dto);
					return null;
				});

		return dtoList;
	}
	
	/**
	 * 返回有效的pageSize防止pageSize过大或为空
	 * @param pageSize
	 * @return
	 */
	private Integer getValidPageSize(Integer pageSize) {
		return PaginationConfigHelper.getPageSize(configProvider, pageSize);
	}

	/**
	 * 返回有效的namespaceId
	 * @param pageSize
	 * @return
	 */
	private Integer getValidNamespaceId(Integer namespaceId) {
		return  namespaceId == null ? UserContext.getCurrentNamespaceId() : namespaceId;
	}
	

	/**
	 * @param chatDtos
	 * @return
	 */
	private List<String> createSingleChatGroupCotent(List<ChatRecordDTO> chatDtos, TargetDTO servicerDto, TargetDTO customerDto) {
		
		/*
		 * 	====================================
			客服名称:客服A
			====================================
			用户姓名:刘亦菲   联系电话：15306070607
			====================================
						
			2018-02-27 14:41:38 客服A（这是客服名称）
			Hello there!
			
			2018-02-27 14:45:32 刘亦菲（这是用户姓名）
			[图片]
			
		 * */
		List<String> content = new ArrayList<>(1000);
		content.add("=======================================");
		content.add("客服名称:"+servicerDto.getTargetName());
		content.add("=======================================");
		content.add("用户名称:"+customerDto.getTargetName()+"   联系电话:"+customerDto.getUserIdentifier());
		content.add("=======================================");
		content.add("\n");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (ChatRecordDTO dto : chatDtos) {
			content.add(sdf.format(dto.getSendTime()) + " " + dto.getSenderName());

			if (ChatMessageType.IMAGE.getCode().equals(dto.getMessageType())) {
				content.add("[图片]");
			}
			else if (ChatMessageType.AUDIO.getCode().equals(dto.getMessageType())) {
				content.add("[语音]");
			}
			else {
				//这里做表情过滤功能
				String filterMsg = ChatRecordUtils.filterEmoji(dto.getMessage(), "[表情]"); 
				content.add(filterMsg);
			}
			content.add("\n");
		}
		
		return content;
	}
	
	private void exportTxt(HttpServletResponse httpResponse, List<String> dataList, String fileName) {

		try {
			httpResponse.setContentType("multipart/form-data");
			httpResponse.setHeader("Content-Disposition",
					"attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
			TextUtils.exportTxtByOutputStream(httpResponse.getOutputStream(), dataList, null);
		} catch (IOException e) {
			LOGGER.error("Export Txt =" + e.getMessage());
			return;
		}
	}
	
	/*
	 * 设置会话（ChatGroupDTO）的servicer，customer字段
	 * 
	 * @param chatGroup 需要设置的会话
	 * 
	 * @param msgRecord 数据库中的消息记录
	 */
	private final void setChatGroup(ChatGroupDTO chatGroup, MessageRecord msgRecord, List<TargetDTO> servicerDtos,
			List<TargetDTO> customerDtos) {

		// 1.获取发送方和接收方id
		Long senderUid = msgRecord.getSenderUid();
		Long acceptUid = Long.parseLong(msgRecord.getDstChannelToken());


		// 2.未指定客户
		Long customerId = null;
		for (int i = 0; i < servicerDtos.size(); i++) {
			if (senderUid.equals(servicerDtos.get(i).getTargetId())) {
				chatGroup.setServicer(servicerDtos.get(i));
				customerId = acceptUid;
				break;
			}

			if (acceptUid.equals(servicerDtos.get(i).getTargetId())) {
				chatGroup.setServicer(servicerDtos.get(i));
				customerId = senderUid;
				break;
			}
		}
		
		// 3.做异常判断，通常不会到这一步
		if (null == customerId) {
			return;
		}

		// 4.根据入参获得客户
		if (null != customerDtos) {
			for (TargetDTO dto : customerDtos) {
				if (customerId.equals(dto.getTargetId())) {
					chatGroup.setCustomer(dto);
					return;
				}
			}
		}
		
		// 5.如果无入参，则直接从数据库获取
		TargetDTO targetDto = findUserNameIdentifierById(customerId);
		chatGroup.setCustomer(targetDto);
		return;
	}
	
	/**
	 * 设置聊天记录ChatRecordDTO字段
	 * 
	 * @param chatRecord
	 *            需要设置的聊天记录
	 * @param msgRecord
	 *            数据库中的消息记录
	 */
	private final void setChatRecord(ChatRecordDTO chatRecord, MessageRecord msgRecord, TargetDTO servicerDto,
			TargetDTO customerDto) {

		// 1.设置发送者名字，客服名或用户名
		if (msgRecord.getSenderUid().equals(servicerDto.getTargetId())) {
			chatRecord.setSenderName(servicerDto.getTargetName());
			chatRecord.setIsServicer(TrueOrFalseFlag.TRUE.getCode());
		} else {
			chatRecord.setSenderName(customerDto.getTargetName());
			chatRecord.setIsServicer(TrueOrFalseFlag.FALSE.getCode());
		}

		// 2.设置发送时间
		chatRecord.setSendTime(msgRecord.getCreateTime());

		// 3.设置消息内容和消息类型
		// 3.1 IMAGE body类型为ImageBody，这里不做解析，直接使用url字段
		if (MessageBodyType.IMAGE == MessageBodyType.fromCode(msgRecord.getBodyType())) {
			/*
			 * body格式 { "fileSize":16541, "filename":"9f2f070828381fx63.jpg",
			 * "format":"image/jpeg",
			 * "url":"http://xxxx:5000/image/axxxRQ?token=6Gxxl",
			 * "uri":"cs://1/image/aW1xxxxUQ", "height":405, "width":616 }
			 */
			JSONObject json = JSONObject.parseObject(msgRecord.getBody());
			chatRecord.setMessage(json.getString("url"));
			chatRecord.setMessageType(ChatMessageType.IMAGE.getCode());
			return;
		}

		// 3.2 AUDIO类型，方法同IMAGE,但语音内容不需要显示，置为null
		if (MessageBodyType.AUDIO == MessageBodyType.fromCode(msgRecord.getBodyType())) {

			/*
			 * body格式 { "duration":"2", "fileSize":0,
			 * "filename":"1524571892706.m4a", "format":"audio/m4a",
			 * "uri":"cs://1/audio/aW1xxxxUQ",
			 * "url":"http://xxxx:5000/audio/axxxRQ?token=6Gxxl" }
			 */
			// JSONObject json = JSONObject.parseObject(msgRecord.getBody());
			// chatRecord.setMessage(json.getString("url"));
			chatRecord.setMessageType(ChatMessageType.AUDIO.getCode());
			return;
		}

		// 3.3 其他都默认为text类型
		chatRecord.setMessageType(ChatMessageType.TEXT.getCode());
		chatRecord.setMessage(msgRecord.getBody());
	}
	

	
}
