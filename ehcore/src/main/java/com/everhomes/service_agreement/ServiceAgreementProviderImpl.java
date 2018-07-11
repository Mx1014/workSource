package com.everhomes.service_agreement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleTemplate;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.organization.OrganizationNotificationTemplateCode;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.service_agreement.ServiceAgreementDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceAgreementDao;
import com.everhomes.server.schema.tables.pojos.EhServiceAgreement;
import com.everhomes.util.ConvertHelper;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

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
	
	@Autowired
    private NamespaceProvider namespaceProvider;
	
	 @Autowired
	 private OrganizationProvider organizationProvider;
	
	@Override
	public ServiceAgreementDTO getByNamespaceIdAndDef2Null(Integer namespaceId) {
		ServiceAgreementDTO resultdto = getServiceAgreementByNamespaceId(namespaceId);	
		if(resultdto != null){
			return resultdto;
		}else{
			//如果用户没有配置，我们将返回默认的协议（默认协议放在0域空间）
			String agreementContent = getDefaultServiceAgreement(namespaceId) ;
			ServiceAgreementDTO dto = new ServiceAgreementDTO(); 
			dto.setAgreementContent(agreementContent);
			return dto ;
		}

	}
	
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
		query.orderBy(Tables.EH_SERVICE_AGREEMENT.ID.desc());						
		query.fetch().map((r) -> {	
			ServiceAgreement bo = ConvertHelper.convert(r, ServiceAgreement.class);			
			result.add(bo);
			return null;
		});		
		if(result != null && result.size()>0){
			return ConvertHelper.convert(result.get(0), ServiceAgreementDTO.class);
		}
		return null ;

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

	/**
	 * 取默认协议模板
	 * @param namespaceId用户域空间
	 * @return
	 */
	private String getDefaultServiceAgreement(Integer namespaceId){
		
		ServiceAgreementDTO resultdto = getServiceAgreementByNamespaceId(Namespace.DEFAULT_NAMESPACE);	
		if(resultdto != null){
			//查域空间名
			Namespace namespace = namespaceProvider.findNamespaceById(namespaceId);
			String namespaceName = "Not Found";
			String management = "Not Found";
			String managementFull = "Not Found";
            if(namespace != null) {
            	namespaceName = namespace.getName();    
            }
            //查询管理公司信息
            List<Organization> organizations = 
            		organizationProvider.listOrganizations(OrganizationType.PM.getCode(), namespaceId, 0L, null, null);
            if(organizations != null && organizations.size() >0){
            	management = organizations.get(0).getName();
            	managementFull = organizations.get(0).getName();
            }			
			String templateText = resultdto.getAgreementContent();

			//替换模板中的变量 
	        templateText = templateText.replace("${namespaceName}", namespaceName);
	        templateText = templateText.replace("${management}", management);
	        templateText = templateText.replace("${managementFull}", managementFull);
	       
	        return templateText ;
		}else{
			//如果用户没有配置
			return "there is no Default serviceAgreement template .";
		}
	}
	
	
}
