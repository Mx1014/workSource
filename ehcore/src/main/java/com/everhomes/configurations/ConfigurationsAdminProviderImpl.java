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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configurations_record_change.ConfigurationsRecordChange;
import com.everhomes.configurations_record_change.ConfigurationsRecordChangeProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.configurations.admin.ConfigurationsErrorCode;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhConfigurationsDao;
import com.everhomes.server.schema.tables.pojos.EhConfigurations;
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
	
	/**
	 * 只读
	 */
	private static final int READONLY = 1;
	/**
	 * 非只读(可维护)
	 */
	private static final int NOTREADONLY = 0;

	@Override
	public List<Configurations> listConfigurations(Integer namespaceId, String name,
			String value, Integer pageSize, CrossShardListingLocator locator,Boolean likeSearch) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = null ;
		//默认域空间ID是不能为空的，若可为空该逻辑得改		
		condition = Tables.EH_CONFIGURATIONS.NAMESPACE_ID.eq(namespaceId);
		//不为空才拼接条件
		if( !StringUtils.isBlank(name)&& likeSearch){
			condition  = condition.and(Tables.EH_CONFIGURATIONS.NAME.like("%" + name + "%"));
		}
		if( !StringUtils.isBlank(name)&& !likeSearch){
			condition  = condition.and(Tables.EH_CONFIGURATIONS.NAME.eq(name));
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
		
		if(pageSize != null){
			//限制每页查询量
			pageSize = pageSize + 1;
			query.limit(pageSize);
		}
		
		query.fetch().map((r) -> {	
			Configurations bo = ConvertHelper.convert(r, Configurations.class);
			//将非 1 值的的展示出来时都是展示0
			if(bo.getIsReadonly()==null || bo.getIsReadonly() !=READONLY){
				bo.setIsReadonly(NOTREADONLY);
			}
			result.add(bo);
			return null;
		});
		
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
		//将非 1 值的的展示出来时都是展示0
		if(ehBo !=null &&( ehBo.getIsReadonly()==null || ehBo.getIsReadonly() !=READONLY)){
			ehBo.setIsReadonly(NOTREADONLY);
		}
		return ConvertHelper.convert(ehBo, Configurations.class);
	}

	@Override
	@Caching(evict = {@CacheEvict(value = {"Configuration2"},key = "{#bo.namespaceId, #bo.name}"),
			          @CacheEvict(value = "Configuration2", allEntries = true),
			          @CacheEvict(value = {"Configuration2-List"},key = "#bo.namespaceId")} )
	public void crteateConfiguration(Configurations bo) {
		
		//重复校验
		checkMultiple(null,bo.getNamespaceId(),bo.getName());
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
	@Caching(evict = {@CacheEvict(value = {"Configuration2"},key = "{#bo.namespaceId, #bo.name}"),
			          @CacheEvict(value = "Configuration2", allEntries = true),
	                  @CacheEvict(value = {"Configuration2-List"},key = "#bo.namespaceId")} )
	public void updateConfiguration(Configurations bo) {
		
		//修改前先查询出来
		Configurations preBo = getConfigurationById(bo.getId(),null);
		//是否只读校验，只读数据不能修改
		if(preBo.getIsReadonly() !=null && READONLY == preBo.getIsReadonly().intValue()){
			throwReadonlyException("the data is read-only,  not allowed to modify.");
		}
		//重复校验
		checkMultiple(bo.getId(),bo.getNamespaceId(),bo.getName());
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhConfigurationsDao dao = new EhConfigurationsDao(context.configuration());
		dao.update(bo);
		
		//广播更新数据事件
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhConfigurations.class, bo.getId().longValue());
		
		//保存日志
		saveChageRecord(preBo,bo,UPDATE);
		
	}

	@Override
	@Caching(evict = {@CacheEvict(value = {"Configuration2"},key = "{#bo.namespaceId, #bo.name}"),
			          @CacheEvict(value = "Configuration2", allEntries = true),
	                  @CacheEvict(value = {"Configuration2-List"},key = "#bo.namespaceId")} )
	public void deleteConfiguration(Configurations bo) {
		
		
		//是否只读校验，只读数据不能删除
		if(bo.getIsReadonly() !=null && READONLY == bo.getIsReadonly().intValue()){
			throwReadonlyException("the data is read-only,  not allowed to modify.");
		}
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhConfigurationsDao dao = new EhConfigurationsDao(context.configuration());
		dao.deleteById(bo.getId());
		
		//广播删除数据事件
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhConfigurations.class, bo.getId().longValue());
		
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
		recordBo.setOperateTime(operatorTime);
		//获取操作者的IP地址,目前并未获取
		recordBo.setOperatorIp(null);
		configurationsRecordChangeProvider.crteateConfiguration(recordBo);
		
	}
	
	/**
	 * 检查新增或修改时该条信息是否与表中数据存在重复（不允许重复的）
	 * 新增:不能传ID，靠域空间ID与name 查询配置项表，查出数据说明即将新增的数据为重复数据
	 * 修改：靠域空间ID与name 查询配置项表，存在2条及以上数据说明重复了。查出一条则判断该条是不是将要修改
	 * 的本身这条，不是则说明重复了。
	 * @param id 主键
	 * @param nemespaceId 域空间ID
	 * @param name 配置信息键值对中的“键”
	 */
	private void checkMultiple(Integer id , Integer nemespaceId , String name ){
		//靠nemespaceId与name查询配置表看是否有存在数据
		List<Configurations> resultList = listConfigurations(nemespaceId, name, null, null, null,false);
		//ID 为空，说明是新增数据，否则为修改数据
		if(id == null ){			
			if(resultList !=null && resultList.size() >0){
				throwRepeatNameException("A nemespace is not allowed to repeat name .");
			}
		}else { 
			//同一域空间内name 相同的有两个及以上是肯定有问题的
			if(resultList !=null && resultList.size() >1){
				throwRepeatNameException("A nemespace is not allowed to repeat name .");
			}else if(resultList !=null && resultList.size() == 1){
				//如果只查出一条，判断该 条是否自己本身，不是则要抛出异常
				Configurations bo = resultList.get(0);
				if(bo  != null && id.intValue() != bo.getId().intValue() ){
					throwRepeatNameException("A nemespace is not allowed to repeat name .");
				}
			}
		}
	}
	
	/**
	 * 抛出运行时repeat name异常，异常信息参数传递过来
	 * @param msg 报错信息
	 */
	private void throwRepeatNameException(String msg ){
		LOGGER.error(msg);
		throw RuntimeErrorException.errorWith(ConfigurationsErrorCode.SCOPE, ConfigurationsErrorCode.ERROR_CODE_MULTIPLE,msg);
	}
	
	/**
	 * 抛出运行时read-only异常，异常信息参数传递过来
	 * @param msg 报错信息
	 */
	private void throwReadonlyException(String msg ){
		LOGGER.error(msg);
		throw RuntimeErrorException.errorWith(ConfigurationsErrorCode.SCOPE, ConfigurationsErrorCode.ERROR_CODE_READONLY,msg);
	}
}
