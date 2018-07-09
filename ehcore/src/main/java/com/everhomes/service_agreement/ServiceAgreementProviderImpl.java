package com.everhomes.service_agreement;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.service_agreement.ServiceAgreementDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceAgreementDao;
import com.everhomes.server.schema.tables.pojos.EhServiceAgreement;
import com.everhomes.util.ConvertHelper;

/**
 * 服务协议 ProviderImpl
 * @author huanglm
 *
 */
@Component
public class ServiceAgreementProviderImpl implements ServiceAgreementProvider {

	//private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAgreementProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Override
	public ServiceAgreementDTO getServiceAgreementByNamespaceId(Integer namespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = null ;
		//默认域空间ID是不能为空的，若可为空该逻辑得改		
		condition = Tables.EH_SERVICE_AGREEMENT.NAMESPACE_ID.eq(namespaceId);				
		//创建返回对象
		List<ServiceAgreement> result  = new ArrayList<ServiceAgreement>();
		
		SelectJoinStep<Record> query = context.select().from(Tables.EH_SERVICE_AGREEMENT);
		query.where(condition);
		//默认ID倒序（更容易看到新增的）
		query.orderBy(Tables.EH_CONFIGURATIONS.ID.desc());						
		query.fetch().map((r) -> {	
			ServiceAgreement bo = ConvertHelper.convert(r, ServiceAgreement.class);			
			result.add(bo);
			return null;
		});		
		if(result != null && result.size()>0){
			return ConvertHelper.convert(result.get(0), ServiceAgreementDTO.class);
		}
		return null;
	}

	@Override
	public void crteateServiceAgreement(ServiceAgreement bo) {
		//获取主键序列
		Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAgreement.class));
		bo.setId(id.intValue());
						
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceAgreementDao dao = new EhServiceAgreementDao(context.configuration());
		dao.insert(bo);
				
		//广播插入数据事件
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceAgreement.class, null);

	}

	@Override
	public void updateServiceAgreement(ServiceAgreement bo) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceAgreementDao dao = new EhServiceAgreementDao(context.configuration());
		dao.update(bo);
		
		//广播更新数据事件
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceAgreement.class, bo.getId().longValue());

	}

}
