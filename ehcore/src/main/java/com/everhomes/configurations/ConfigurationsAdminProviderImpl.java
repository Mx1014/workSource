package com.everhomes.configurations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.common.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configurations_record_change.ConfigurationsRecordChange;
import com.everhomes.configurations_record_change.ConfigurationsRecordChangeProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.schema.Tables;
import com.everhomes.schema.tables.daos.EhConfigurationsDao;
import com.everhomes.schema.tables.pojos.EhConfigurations;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import com.everhomes.util.RuntimeErrorException;

/**
 * configurations management Provider
 * @author huanglm
 *
 */
@Component
public class ConfigurationsAdminProviderImpl implements ConfigurationsProvider{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationsAdminProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Autowired
	private ConfigurationsRecordChangeProvider configurationsRecordChangeProvider ;
	/**
	 * 新增
	 */
	private  static final int CREATE = 0;
	/**
	 * 修改
	 */
	private  static final int UPDATE = 1;
	/**
	 * 删除
	 */
	private  static final int DELETE = 3;

	@Override
	public List<Configurations> listConfigurations(Integer namespaceId, String name,
			String value, Integer pageSize, CrossShardListingLocator locator) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = null ;
		//默认域空间ID是不能为空的，若可为空该逻辑得改		
		condition = Tables.EH_CONFIGURATIONS.NAMESPACE_ID.eq(namespaceId);
		//不为空才拼接条件
		if( !StringUtils.isBlank(name)){
			condition  = condition.and(Tables.EH_CONFIGURATIONS.NAME.like("%" + name + "%"));
		}
		//不为空才拼接条件
		if(!StringUtils.isBlank(value)){
			condition  = condition.and(Tables.EH_CONFIGURATIONS.VALUE.like("%" + value + "%"));
		}
		//分页起点
		if(null != locator && null != locator.getAnchor()){
			condition  = condition.and(Tables.EH_CONFIGURATIONS.ID.lt(locator.getAnchor().intValue()));
		}
		
		//创建返回对象
		List<Configurations> result  = new ArrayList<Configurations>();
		
		SelectJoinStep<Record> query = context.select().from(Tables.EH_CONFIGURATIONS);
		query.where(condition);
		//默认ID倒序（更容易看到新增的）
		query.orderBy(Tables.EH_CONFIGURATIONS.ID.desc());
		//限制每页查询量
		pageSize = pageSize + 1;
		query.limit(pageSize);
		result = query.fetch().map((r) -> ConvertHelper.convert(r, Configurations.class));
		
		if(locator != null ){
			locator.setAnchor(null);//重置分页锚点类
			//因为在开始时查询数量增加了1，用于判断是否有后续页
			if(result.size() >= pageSize){
				result.remove(result.size() - 1);
				locator.setAnchor(result.get(result.size() - 1).getId().longValue());
			}
		}
		
		return result;
	}

	@Override
	public Configurations getConfigurationById(Integer id, Integer namespaceId) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhConfigurationsDao dao = new EhConfigurationsDao(context.configuration());
		EhConfigurations ehBo = dao.findById(id);
		
		return ConvertHelper.convert(ehBo, Configurations.class);
	}

	@Override
	public void crteateConfiguration(Configurations bo) {
		
		//获取主键序列
		Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhConfigurations.class));
		bo.setId(id.intValue());
				
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhConfigurationsDao dao = new EhConfigurationsDao(context.configuration());
		dao.insert(bo);
		
		//广播插入数据事件
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhConfigurations.class, null);
		
		//保存日志
		saveChageRecord(null,bo,CREATE);
		
	}

	@Override
	public void updateConfiguration(Configurations bo) {
		
		//修改前先查询出来
		Configurations preBo = getConfigurationById(bo.getId(),null);
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhConfigurationsDao dao = new EhConfigurationsDao(context.configuration());
		dao.update(bo);
		
		//广播更新数据事件
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhConfigurations.class, bo.getId().longValue());
		
		//保存日志
		saveChageRecord(preBo,bo,UPDATE);
		
	}

	@Override
	public void deleteConfiguration(Integer id) {
		
		//删除前先查询出来
		Configurations bo = getConfigurationById(id,null);
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhConfigurationsDao dao = new EhConfigurationsDao(context.configuration());
		dao.deleteById(id);
		
		//广播删除数据事件
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhConfigurations.class, id.longValue());
		
		//保存日志		
		saveChageRecord(bo,null,DELETE);
	}
	

	/**
	 * 保存日志调用方法
	 * @param prebo 变动前BO
	 * @param aftbo 变动后BO
	 * @param type 变动类型：0，新增；1，修改；3，删除
	 */
	private void saveChageRecord(Configurations prebo ,Configurations aftbo ,int type){
		ConfigurationsRecordChange recordBo = new ConfigurationsRecordChange();
		
		//为保存变动信息字段设值
		if(CREATE == type){
			if(aftbo == null){
				LOGGER.error("aftbo is null,there will not save create-change record.");
				return;
			}			
			String aftbostr = JSONObject.toJSONString(aftbo);
			recordBo.setConfAftJson(aftbostr);
			
			recordBo.setRecordChangeType(CREATE);
						
		}else if(UPDATE == type){
			if(prebo == null){
				LOGGER.error("prebo is null,there will not save update-change record.");
				return;
			}
			if(aftbo == null){
				LOGGER.error("aftbo is null,there will not save update-change record.");
				return;
			}
			
			String prebostr = JSONObject.toJSONString(prebo);
			recordBo.setConfPreJson(prebostr);
			
		
			String aftbostr = JSONObject.toJSONString(aftbo);
			recordBo.setConfAftJson(aftbostr);
			
			recordBo.setRecordChangeType(UPDATE);
			
		}else if(DELETE == type){
			if(prebo == null){
				LOGGER.error("prebo is null,there will not save delete-change record.");
				return;
			}
			String prebostr = JSONObject.toJSONString(prebo);
			recordBo.setConfPreJson(prebostr);
			
			recordBo.setRecordChangeType(DELETE);
		}
		//为其他字段设值
		//当前环境中获取域空间ID
		Integer namespaceId = UserContext.getCurrentNamespaceId(null);
		recordBo.setNamespaceId(namespaceId);//域空间ID
		//当前环境中获取userId
		Long uid = UserContext.currentUserId();
		recordBo.setOperatorUid(uid);
		//操作时间
		Timestamp operatorTime = DateUtils.currentTimestamp();
		recordBo.setOperatorTime(operatorTime);
		//获取操作者的IP地址,目前并未获取
		recordBo.setOperatorIp(null);
		configurationsRecordChangeProvider.crteateConfiguration(recordBo);
		
	}
	
	private boolean checkMultiple(Integer id , Integer nemespaceId , String name ){
		//靠nemespaceId与name查询配置表看是否有存在数据
		List<Configurations> resultList = listConfigurations(nemespaceId, name, null, null, null);
		//ID 为空，说明是新增数据，
		if(id == null ){			
			if(resultList !=null && resultList.size() >0){
				throwSelfDefNullException("Multiple name is not allow in one nemespace.");
			}
		}else {
			
			if(resultList !=null && resultList.size() >1){
				throwSelfDefNullException("Multiple name is not allow in one nemespace.");
			}
		}
		getConfigurationById(id , nemespaceId);
		return true;
	}
	
	/**
	 * 抛出运行时异常，异常信息参数传递过来
	 * @param msg 报错信息
	 */
	private void throwSelfDefNullException(String msg ){
		LOGGER.error(msg);
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,msg);
	}
}
