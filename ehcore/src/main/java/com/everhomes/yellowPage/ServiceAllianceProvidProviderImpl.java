package com.everhomes.yellowPage;

import java.sql.Timestamp;
import java.util.List;

import org.elasticsearch.common.lang3.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.FlowEvaluate;
import com.everhomes.flow.FlowEvaluateItem;
import com.everhomes.flow.FlowEvaluateItemProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceAllianceProvidersDao;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceProviders;
import com.everhomes.server.schema.tables.records.EhServiceAllianceProvidersRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;

@Component
public class ServiceAllianceProvidProviderImpl implements ServiceAllianceProviderProvider{
	
	@Autowired
	DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Autowired
	private FlowEvaluateItemProvider evaluateItemProvider;
	
	com.everhomes.server.schema.tables.EhServiceAllianceProviders table = Tables.EH_SERVICE_ALLIANCE_PROVIDERS;
	
    //评分需要保留小数位数
    public static final int EVALUATION_RETAIN_DECIMAL = 1;
    
    //已删除
    public static final byte PROVIDER_STATUS_DELETED = 0;
    //正常
    public static final byte PROVIDER_STATUS_ACTIVE = 1;

	@Override
	public void createServiceAllianceProvid(ServiceAllianceProvid serviceAllianceProvider) {
		// 获取jooq上下文
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		
		// 设置动态属性 如id，createTime
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceProviders.class));
		serviceAllianceProvider.setId(id);
		serviceAllianceProvider.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		serviceAllianceProvider.setCreateUid(UserContext.currentUserId());
		
		// 使用dao方法
		EhServiceAllianceProvidersDao dao = new EhServiceAllianceProvidersDao(context.configuration());
		dao.insert(serviceAllianceProvider);
		
		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceAllianceProviders.class, null);
	}

	@Override
	public void updateServiceAllianceProvid(ServiceAllianceProvid serviceAllianceProvider) {
		
		// 获取jooq上下文
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		// 使用dao方法
		EhServiceAllianceProvidersDao dao = new EhServiceAllianceProvidersDao(context.configuration());
		dao.update(serviceAllianceProvider);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceAllianceProviders.class, null);
	}

	@Override
	public void deleteServiceAllianceProvid(ServiceAllianceProvid serviceAllianceProvider) {
		serviceAllianceProvider.setStatus(PROVIDER_STATUS_DELETED);
		updateServiceAllianceProvid(serviceAllianceProvider);
	}

	@Override
	public ServiceAllianceProvid findServiceAllianceProvidById(Long id) {

		// 获取jooq上下文
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());


		return context.select()
				.from(table)
				.where(table.STATUS.eq(PROVIDER_STATUS_ACTIVE).and(table.ID.eq(id)))
				.fetchOneInto(ServiceAllianceProvid.class);
	}

	@Override
	public List<ServiceAllianceProvid> listServiceAllianceProviders(ListingLocator locator, Integer pageSize,
			Integer namespaceId, String ownerType, Long ownerId, Long type, Long categoryId, String keyword) {
		
		
		return listServiceAllianceProviders(locator, pageSize, (l, query) -> {

			// 对当前表取别名
			com.everhomes.server.schema.tables.EhServiceAllianceProviders t = Tables.EH_SERVICE_ALLIANCE_PROVIDERS;

			// 构建固定条件
			query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
			query.addConditions(t.OWNER_TYPE.eq(ownerType));
			query.addConditions(t.OWNER_ID.eq(ownerId));
			query.addConditions(t.TYPE.eq(type));

			// 服务类型条件
			if (null != categoryId) {
				query.addConditions(t.CATEGORY_ID.eq(categoryId));
			}

			// keyword值
			if (!StringUtils.isBlank(keyword)) {
				query.addConditions(t.NAME.eq(keyword).or(t.CONTACT_NAME.eq(keyword)).or(t.CONTACT_NUMBER.eq(keyword)));
			}

			return query;
		});
		
		
	}
	
	/**
	 * 获取符合条件的服务商
	 * @param locator 锚点,必传
	 * @param pageSize 单页记录条数
	 * @param callback 实际sql执行方法
	 * @return
	 */
	
	public List<ServiceAllianceProvid> listServiceAllianceProviders(ListingLocator locator, Integer pageSize, 
			ListingQueryBuilderCallback callback) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		SelectQuery<EhServiceAllianceProvidersRecord> query = context.selectQuery(table);
		if (callback != null) {
			callback.buildCondition(locator, query);
		}

		if (locator.getAnchor() != null) {
			query.addConditions(table.ID.ge(locator.getAnchor()));
		}
		
		//必须是未被删除的才可以获取
		query.addConditions(table.STATUS.eq(PROVIDER_STATUS_ACTIVE));

		if (null != pageSize && pageSize > 0) {
			query.addLimit(pageSize + 1);
		}
		
		query.addOrderBy(table.ID.asc());

		// 转化成ServiceAllianceProvid
		List<ServiceAllianceProvid> list = query.fetchInto(ServiceAllianceProvid.class);

		// 设置锚点
		if (null != pageSize && pageSize > 0 && list.size() > pageSize) {
			locator.setAnchor(list.get(list.size() - 1).getId());
			list.remove(list.size() - 1);
		} else {
			locator.setAnchor(null);
		}

		return list;
	}

	@Override
	public void updateScoreByEvaluation(Long flowCaseId, FlowEvaluate evaluate) {
		
		// 获取evaluateItem
		FlowEvaluateItem item = evaluateItemProvider.getFlowEvaluateItemById(evaluate.getEvaluateItemId());
		if (null == item) {
			return;
		}

		// 判断是否为新建事件的服务商
		if (!Tables.EH_SERVICE_ALLIANCE_PROVIDERS.getClass().getSimpleName().equals(item.getStringTag6())) {
			return;
		}

		// 获取相应的供应商id
		Long providerId = item.getIntegralTag6();
		ServiceAllianceProvid provider = findServiceAllianceProvidById(providerId);
		if (null == provider || flowCaseId.equals(provider.getScoreFlowCaseId())) {
			// 如果已经在该工作流更新过了，则不进行更新
			// 这种情况出现于网络异常，重复请求时的评价
			return;
		}

		// 获取评分
		Long currentScore = evaluate.getStar().longValue() ;

		// 更新该供应商的评价
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		context.update(table).set(table.TOTAL_SCORE, table.TOTAL_SCORE.add(currentScore))
				.set(table.SCORE_TIMES, table.SCORE_TIMES.add(1)).set(table.SCORE_FLOW_CASE_ID, flowCaseId)
				.where(table.SCORE_FLOW_CASE_ID.ne(flowCaseId).and(table.ID.eq(providerId)).and(table.STATUS.eq(PROVIDER_STATUS_ACTIVE))).execute();
	}


}
