package com.everhomes.customer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.openapi.ZjSyncdataBackup;
import com.everhomes.openapi.ZjSyncdataBackupProvider;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.community.NamespaceCommunityType;
import com.everhomes.rest.customer.EbeiCustomer;
import com.everhomes.rest.customer.EbeiJsonEntity;
import com.everhomes.rest.customer.NamespaceCustomerType;
import com.everhomes.rest.openapi.shenzhou.DataType;
import com.everhomes.rest.openapi.shenzhou.SyncFlag;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/11/21.
 */
@Component(CustomerHandle.CUSTOMER_PREFIX + 999983)
public class EbeiCustomerHandle implements CustomerHandle {
    private final static Logger LOGGER = LoggerFactory.getLogger(EbeiCustomerHandle.class);

    private static final String PAGE_SIZE = "20";
    private static final Integer SUCCESS_CODE = 200;
    private static final Integer NAMESPACE_ID = 999983;

    private static final String SYNC_ENTERPRISES = "/rest/LeaseContractChargeInfo/getLeaseContractOwnerInfo";

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private OrganizationSearcher organizationSearcher;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EnterpriseCustomerSearcher enterpriseCustomerSearcher;

    @Autowired
    private ZjSyncdataBackupProvider zjSyncdataBackupProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Override
    public void syncEnterprises(String pageOffset, String version, String communityIdentifier) {
        Map<String, String> params= new HashMap<String,String>();
        if(communityIdentifier == null) {
            communityIdentifier = "";
        }
        params.put("projectId", communityIdentifier);
        if(pageOffset == null || "".equals(pageOffset)) {
            pageOffset = "1";
        }
        params.put("currentPage", pageOffset);
        params.put("pageSize", PAGE_SIZE);

        if(version == null || "".equals(version)) {
            version = "0";
        }
        params.put("version", version);

        String enterprises = null;
        String url = configurationProvider.getValue("ebei.url", "");
        try {
            enterprises = HttpUtils.get(url+SYNC_ENTERPRISES, params);
        } catch (IOException e) {
            LOGGER.error("sync customer from ebei error: {}", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "sync customer from ebei error");
        }


        EbeiJsonEntity<List<EbeiCustomer>> entity = JSONObject.parseObject(enterprises, new TypeReference<EbeiJsonEntity<List<EbeiCustomer>>>(){});

        if(SUCCESS_CODE.equals(entity.getResponseCode())) {
            List<EbeiCustomer> dtos = entity.getData();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.ENTERPRISE.getCode(), communityIdentifier);

                //数据有下一页则继续请求
                if("1".equals(entity.getHasNextPag())) {
                    syncEnterprises(String.valueOf(entity.getCurrentPage()+1), version, communityIdentifier);
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if("0".equals(entity.getHasNextPag())) {
                syncDataToDb(DataType.ENTERPRISE.getCode());
            }
        }
    }

    @Override
    public void syncIndividuals(String pageOffset, String version, String communityIdentifier) {

    }

    private void syncData(EbeiJsonEntity entity, Byte dataType, String communityIdentifier) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("ebei syncData: dataType: {}, communityIdentifier: {}", dataType, communityIdentifier);
        }

        ZjSyncdataBackup backup = new ZjSyncdataBackup();
        backup.setNamespaceId(NAMESPACE_ID);
        backup.setDataType(dataType);
        if("1".equals(entity.getHasNextPag())) {
            backup.setNextPageOffset(entity.getCurrentPage()+1);
        }

        backup.setData(StringHelper.toJsonString(entity.getData()));
        backup.setStatus(CommonStatus.ACTIVE.getCode());
        backup.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        backup.setCreatorUid(1L);
        backup.setUpdateTime(backup.getCreateTime());
        backup.setAllFlag(SyncFlag.PART.getCode());
        backup.setUpdateCommunity(communityIdentifier);

        zjSyncdataBackupProvider.createZjSyncdataBackup(backup);
    }

    private void syncDataToDb(Byte dataType) {
//        queueThreadPool.execute(()->{
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("dataType {} enter into thread=================", dataType);
        }

        List<ZjSyncdataBackup> backupList = zjSyncdataBackupProvider.listZjSyncdataBackupByParam(NAMESPACE_ID, dataType);

        if (backupList == null || backupList.isEmpty()) {
            LOGGER.debug("syncDataToDb backupList is empty, NAMESPACE_ID: {}, dataType: {}", NAMESPACE_ID, dataType);
            return ;
        }
        try {
            LOGGER.debug("syncDataToDb backupList size：{}", backupList.size());
            updateAllData(dataType, NAMESPACE_ID , backupList);
        } finally {
            zjSyncdataBackupProvider.updateZjSyncdataBackupInactive(backupList);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("dataType {} get out thread=================", dataType);
        }
//        });
    }

    private void updateAllData(Byte dataType, Integer namespaceId, List<ZjSyncdataBackup> backupList) {
        DataType ebeiDataType = DataType.fromCode(dataType);
        LOGGER.debug("ebei DataType : {}", ebeiDataType);
        switch (ebeiDataType) {
            case ENTERPRISE:
                LOGGER.debug("syncDataToDb SYNC ENTERPRISE");
                syncAllEnterprises(namespaceId, backupList);
                break;

            default:
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "error data type");
        }
    }

    private void syncAllEnterprises(Integer namespaceId, List<ZjSyncdataBackup> backupList) {
        //必须按照namespaceType来查询，否则，有些数据可能本来就是我们系统独有的，不是他们同步过来的，这部分数据不能删除
        //allFlag为part时，仅更新单个特定的项目数据即可
        List<EnterpriseCustomer> myEnterpriseCustomerList = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceType(namespaceId, NamespaceCustomerType.SHENZHOU.getCode(), specialCommunityId);

        List<EbeiCustomer> mergeEnterpriseList = mergeBackupList(backupList, EbeiCustomer.class);
        List<EbeiCustomer> theirEnterpriseList = new ArrayList<EbeiCustomer>();

        LOGGER.debug("syncDataToDb namespaceId: {}, myEnterpriseCustomerList size: {}, theirEnterpriseList size: {}",
                namespaceId, myEnterpriseCustomerList.size(), theirEnterpriseList.size());
        dbProvider.execute(s->{
            syncAllEnterprises(namespaceId, myEnterpriseCustomerList, theirEnterpriseList);
            return true;
        });
    }

    //ebei同步数据规则：两次之间所有的改动会同步过来，所以以一碑同步过来数据作为参照：
    // state为0的数据删除，state为1的数据：我们有，更新；我们没有则新增
    private void syncAllEnterprises(Integer namespaceId, List<EnterpriseCustomer> myEnterpriseCustomerList, List<EbeiCustomer> theirEnterpriseList) {

    }

    private <T> List<T> mergeBackupList(List<ZjSyncdataBackup> backupList, Class<T> targetClz) {
        List<T> resultList = new ArrayList<>();
        for (ZjSyncdataBackup syncdataBackup : backupList) {
            if (StringUtils.isNotBlank(syncdataBackup.getData())) {
                List<T> list = JSONObject.parseArray(syncdataBackup.getData(), targetClz);
                if (list != null) {
                    resultList.addAll(list);
                }
            }
        }
        return resultList;
    }

}
