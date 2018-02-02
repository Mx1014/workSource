// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.portal.ServiceModuleAppStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceModuleAppsDao;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleApps;
import com.everhomes.server.schema.tables.records.EhServiceModuleAppsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceModuleAppServiceImpl implements ServiceModuleAppService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleAppServiceImpl.class);

	@Autowired
	private ServiceModuleProvider serviceModuleProvider;

	@Autowired
	private ServiceModuleAppProvider serviceModuleAppProvider;

	@Autowired
	private PortalVersionProvider portalVersionProvider;


	@Override
	public List<ServiceModuleApp> listReleaseServiceModuleApps(Integer namespaceId) {

		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);

		List<ServiceModuleApp> serviceModuleApps = serviceModuleAppProvider.listServiceModuleApp(namespaceId, releaseVersion.getId(), null);

		return serviceModuleApps;
	}

	@Override
	public List<ServiceModuleApp> listServiceModuleApps(Integer namespaceId, Long versionId, Long moduleId, Byte actionType, String customTag, String customPath) {

		if(versionId == null){
			PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);
			if(releaseVersion != null){
				versionId = releaseVersion.getId();
			}
		}

		List<ServiceModuleApp> moduleApps = serviceModuleAppProvider.listServiceModuleApp(namespaceId, moduleId, versionId);

		return moduleApps;
	}

	@Override
	public List<ServiceModuleApp> listReleaseServiceModuleAppsByOriginIds(Integer namespaceId, List<Long> originIds) {

		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);

		List<ServiceModuleApp> moduleApps = serviceModuleAppProvider.listServiceModuleAppsByVersionIdAndOriginIds(releaseVersion.getId(), originIds);

		return moduleApps;
	}
}
