package com.everhomes.customer;

import com.everhomes.address.AddressProvider;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contract.ContractService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.openapi.*;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.common.SyncDataResponse;
import com.everhomes.rest.common.SyncDataResultLog;
import com.everhomes.rest.contract.*;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.openapi.shenzhou.DataType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2018/1/13.
 */
@Component
public class SyncDataTaskServiceImpl implements SyncDataTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncDataTaskServiceImpl.class);

    @Autowired
    private SyncDataTaskProvider syncDataTaskProvider;

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ZjSyncdataBackupProvider zjSyncdataBackupProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private BigCollectionProvider bigCollectionProvider;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Autowired
    private AddressProvider addressProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;


    private static final Long EXPIRE_TIME_DURATION = 1800000L;


    @Override
    public SyncDataTask executeTask(ExecuteSyncTaskCallback callback, SyncDataTask task) {
        task.setStatus(SyncDataTaskStatus.EXECUTING.getCode());
        syncDataTaskProvider.createSyncDataTask(task);
        User user = UserContext.current().getUser();
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    LOGGER.debug("SyncDataTask RUN");
                    UserContext.setCurrentUser(user);
                    // this code used to ensure that only one thread executing at the same time
                    Boolean runningFlag = true;
                    if (StringUtils.isNotBlank(task.getLockKey())) {
                        runningFlag = requireSyncDataLock(task.getLockKey());
                    }
                    if (runningFlag) {

                        callback.syncData();
                    }
                    task.setStatus(SyncDataTaskStatus.FINISH.getCode());
                    task.setResult("同步成功");
                } catch (Exception e) {
                    LOGGER.error("executor task error. error: {}", e);
                    task.setStatus(SyncDataTaskStatus.EXCEPTION.getCode());
                    task.setResult(e.toString());
                } finally {
                    LOGGER.debug("SyncDataTask task: {}", StringHelper.toJsonString(task));
                    syncDataTaskProvider.updateSyncDataTask(task);
                    if (StringUtils.isNotBlank(task.getLockKey())) {
                        releaseSyncLock(task.getLockKey());
                    }
                }

            }
        });
        return task;
    }

    @Override
    public void releaseSyncLock(String lockKey) {
        Accessor accessor = bigCollectionProvider.getMapAccessor(lockKey, "");
        RedisTemplate redisTemplate = accessor.getTemplate(new StringRedisSerializer());
        redisTemplate.delete(lockKey);
    }

    private StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    @Override
    public Boolean requireSyncDataLock(String lockKey) {
        // may occur exception when setting expire time which may cause deadLock,spring data redis 2.1X can resolve this
        Accessor accessor = bigCollectionProvider.getMapAccessor(lockKey, "");
        RedisTemplate redisTemplate = accessor.getTemplate(new StringRedisSerializer());
        Boolean absent = redisTemplate.opsForValue().setIfAbsent(lockKey, String.valueOf(System.currentTimeMillis() + EXPIRE_TIME_DURATION));
        if (absent) {
            return true;
        } else {
            String lockValue = (String) redisTemplate.opsForValue().get(lockKey);
            if (StringUtils.isNotBlank(lockValue) && Long.parseLong(lockValue) < System.currentTimeMillis()) {
                String periodLockValue = (String) redisTemplate.opsForValue().getAndSet(lockKey, String.valueOf(System.currentTimeMillis() + EXPIRE_TIME_DURATION));
                if (periodLockValue.equals(lockValue) && StringUtils.isNotBlank(periodLockValue)) {
                    return true;
                }
            }
        }
//        Expiration expiration = Expiration.from(3, TimeUnit.HOURS);
//        redisTemplate.execute(redisConnection -> {
//            redisConnection.set(stringRedisSerializer.serialize(lockKey), stringRedisSerializer.serialize("executing"), expiration, RedisStringCommands.SetOption.ifAbsent());
//            return null;
//        },true);
//
//        return true;
//      redisConnection.execute("set", lockKey.getBytes(),"executing".getBytes(),"EX".getBytes(),"100".getBytes()), true,true);
        return false;
    }

    @Override
    public SyncDataResponse getSyncDataResult(Long taskId) {
        User user = UserContext.current().getUser();

        SyncDataResponse response = new SyncDataResponse();

        SyncDataTask task = syncDataTaskProvider.findSyncDataTaskById(taskId);

        if (null != task) {
            if (SyncDataTaskStatus.FINISH == SyncDataTaskStatus.fromCode(task.getStatus())) {
                response = (SyncDataResponse) StringHelper.fromJsonString(task.getResult(), SyncDataResponse.class);
                List<SyncDataResultLog> logs = response.getLogs();
                if (logs != null) {
                    for (SyncDataResultLog log : logs) {
                        log.setErrorDescription(localeStringService.getLocalizedString(log.getScope(), log.getCode().toString(), user.getLocale(), ""));
                    }
                }
            }
            response.setSyncStatus(task.getStatus());
        }

        return response;
    }

    @Override
    public String syncHasViewed(Long communityId, String syncType) {
        Integer notViewedCount = syncDataTaskProvider.countNotViewedSyncResult(communityId, syncType);
        if (notViewedCount == 0) {
            return String.valueOf(SyncResultViewedFlag.VIEWED.getCode());
        }
        return String.valueOf(SyncResultViewedFlag.NOT_VIEWED.getCode());
    }

    @Override
    public ListCommunitySyncResultResponse listCommunitySyncResult(Long communityId, String syncType, Integer pageSize, Long pageAnchor) {
        Community community = communityProvider.findCommunityById(communityId);
        if (community == null) {
            return null;
        }
        ListCommunitySyncResultResponse response = new ListCommunitySyncResultResponse();
        List<SyncDataTask> tasks = syncDataTaskProvider.listCommunitySyncResult(communityId, syncType, pageSize + 1, pageAnchor);
        if (tasks != null && tasks.size() > 0) {
            List<SyncDataResult> results = new ArrayList<>();
            for (SyncDataTask task : tasks) {
                SyncDataResult result = new SyncDataResult();
                result.setStartTime(task.getCreateTime());
                result.setEndTime(task.getUpdateTime());
                result.setStatus(task.getStatus());
                if (SyncDataTaskStatus.FINISH.equals(SyncDataTaskStatus.fromCode(task.getStatus()))
                        || SyncDataTaskStatus.EXCEPTION.equals(SyncDataTaskStatus.fromCode(task.getStatus()))) {
                    result.setEndTime(task.getUpdateTime());
                    result.setResult(task.getResult());
                } else if (SyncDataTaskStatus.EXECUTING.equals(SyncDataTaskStatus.fromCode(task.getStatus()))) {
                    result.setRateOfProgress(calculateProgress(community, syncType));
                }
                result.setId(task.getId());

                if (task.getCreatorUid() == null || task.getCreatorUid() == 0L) {
                    result.setManualFlag((byte) 0);
                } else {
                    result.setManualFlag((byte) 1);

                }
                results.add(result);

                task.setViewFlag(SyncResultViewedFlag.VIEWED.getCode());
                syncDataTaskProvider.updateSyncDataTask(task, false);
            }

            if (tasks.size() > pageSize) {
                results.remove(results.size() - 1);
                response.setNextPageAnchor(tasks.get(tasks.size() - 1).getId());
            }
            response.setResults(results);
        }
        return response;
    }

    private Double calculateProgress(Community community, String syncType) {
        Byte dataType = (byte) 0;
        if (SyncDataTaskType.CONTRACT.getCode().equals(syncType)) {
            dataType = DataType.CONTRACT.getCode();
        } else if (SyncDataTaskType.CUSTOMER.getCode().equals(syncType)) {
            dataType = DataType.ENTERPRISE.getCode();
        } else if (SyncDataTaskType.INDIVIDUAL.getCode().equals(syncType)) {
            dataType = DataType.INDIVIDUAL.getCode();
        }
        List<ZjSyncdataBackup> backups = zjSyncdataBackupProvider.listZjSyncdataBackupByParam(community.getNamespaceId(),
                community.getNamespaceCommunityToken(), dataType);
        if (backups != null && backups.size() > 0) {
            int i = 0;
            for (ZjSyncdataBackup backup : backups) {
                if (backup.getStatus() == CommonStatus.INACTIVE.getCode()) {
                    i++;
                }
            }
            if (i == 0) {
                return 0.5 - ((double) 1 / ((double) backups.size() * 2));
            }
            return 0.5 + ((double) i / ((double) backups.size() * 2));
        }

        return 1.0;

    }


    @Override
    public void createSyncErrorMsg(Integer namespaceId, Long taskId) {
        List<Contract> contractList = contractProvider.listContractByNamespaceId(namespaceId);
        for (Contract contract : contractList) {
            ContractBuildingMapping mapping = addressProvider.findContractBuildingMappingByContractId(contract.getId());
            if (contract.getCustomerId() == null || StringUtils.isBlank(contract.getCustomerName())) {
                syncDataTaskProvider.createSyncErrorMsg(
                        namespaceId, "contract", contract.getId(), "contract-customer", "该合同无关联的客户，请确认合同和客户信息", taskId);
            } else if (mapping.getAddressId() == null || StringUtils.isBlank(mapping.getBuildingName()) || StringUtils.isBlank(mapping.getApartmentName())) {
                syncDataTaskProvider.createSyncErrorMsg(
                        namespaceId, "contract", contract.getId(), "contract-address", "该合同无关联的门牌，请确认合同和资产信息", taskId);
            }
        }

        List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceId(namespaceId);
        for (EnterpriseCustomer customer : customers) {
            List<CustomerEntryInfo> infos = enterpriseCustomerProvider.listCustomerEntryInfos(customer.getId());
            if (infos != null && infos.size() > 0) {
                for (int i = 0; i < infos.size(); i++) {
                    if (infos.get(i).getAddressId() == null || infos.get(i).getAddressId() == 0 || infos.get(i).getBuildingId() == null || infos.get(i).getBuildingId() == 0) {
                        if (i == infos.size() - 1) {
                            syncDataTaskProvider.createSyncErrorMsg(
                                    namespaceId, "customer", customer.getId(), "customer-address", "该客户无关联的门牌，请确认客户和资产信息", taskId);
                        }
                    } else {
                        break;
                    }
                }
            }
        }

    }

    @Override
    public List<ContractDTO> listContractErrorMsg(SearchContractCommand cmd) {
        List<ContractDTO> contracts = new ArrayList<>();
        List<SyncDataError> syncDataErrors = syncDataTaskProvider.listSyncErrorMsgByTaskId(cmd.getTaskId(), "contract", null, 999999);
        for (SyncDataError syncDataError : syncDataErrors) {
            Contract contract = contractProvider.findContractById(syncDataError.getOwnerId());
            ContractDTO contractDTO = ConvertHelper.convert(contract, ContractDTO.class);
            contractDTO.setSyncErrorMsg(syncDataError.getErrorMessage());
            contracts.add(contractDTO);
        }
        return contracts;
    }

    @Override
    public ListSyncDataErrorMsgResponse listSyncErrorMsg(ListSyncErrorMsgCommand cmd) {
        ListSyncDataErrorMsgResponse response = new ListSyncDataErrorMsgResponse();

        Long anchor = 9999999l;
        if (cmd.getNextPageAnchor() != null) {
            anchor = cmd.getNextPageAnchor();
        }
        if (cmd.getSyncType().equals("customer")) {
            cmd.setTaskId(cmd.getTaskId() + 1);
        }
        List<SyncDataError> syncDataErrors = syncDataTaskProvider.listSyncErrorMsgByTaskId(cmd.getTaskId(), cmd.getSyncType(), anchor, cmd.getPageSize() + 1);
        List<SyncDataErrorDTO> results = syncDataErrors.stream().map(r -> {
            return ConvertHelper.convert(r, SyncDataErrorDTO.class);
        }).collect(Collectors.toList());
        if (syncDataErrors.size() > cmd.getPageSize()) {
            syncDataErrors.remove(syncDataErrors.size() - 1);
            SyncDataError last = syncDataErrors.get(syncDataErrors.size() - 1);
            response.setNextPageAnchor(last.getId());
        }

        if (cmd.getSyncType().equals("contract")) {
            for (SyncDataErrorDTO error : results) {
                String handler = configurationProvider.getValue(cmd.getNamespaceId(), "contractService", "");
                ContractService contractService = PlatformContext.getComponent(ContractService.CONTRACT_PREFIX + handler);
                FindContractCommand cmdc = new FindContractCommand();
                cmdc.setCommunityId(cmd.getCommunityId());
                cmdc.setId(error.getOwnerId());
                cmdc.setNamespaceId(cmd.getNamespaceId());
                cmdc.setCategoryId(cmd.getCategoryId());
                ContractDetailDTO contractDTO = contractService.findContract(cmdc);
                error.setContract(contractDTO);
            }
        } else if (cmd.getSyncType().equals("customer")) {
            for (SyncDataErrorDTO error : results) {
                EnterpriseCustomerDTO customer = ConvertHelper.convert(enterpriseCustomerProvider.findById(error.getOwnerId()), EnterpriseCustomerDTO.class);
                error.setCustomerDTO(customer);
            }
        }
        response.setResults(results);
        return response;
    }


}
