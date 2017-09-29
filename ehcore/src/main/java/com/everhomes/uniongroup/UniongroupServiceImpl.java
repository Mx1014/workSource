// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.organization.*;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.uniongroup.*;
import com.everhomes.search.UniongroupSearcher;
import com.everhomes.server.schema.tables.pojos.EhUniongroupMemberDetails;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UniongroupServiceImpl implements UniongroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniongroupServiceImpl.class);

    @Autowired
    private UniongroupConfigureProvider uniongroupConfigureProvider;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CoordinationProvider coordinationProvider ;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private UniongroupSearcher uniongroupSearcher;

    private Integer DEFAULT_VERSION_CODE = 0;

    @Override
    public void saveUniongroupConfigures(SaveUniongroupConfiguresCommand cmd){
        this.saveUniongroupConfigures(cmd,null);
    }

    @Override
    public void saveUniongroupConfigures(SaveUniongroupConfiguresCommand cmd, SaveUniongroupCallBack callBack) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Integer versionCode = cmd.getVersionCode() != null ? cmd.getVersionCode() : DEFAULT_VERSION_CODE;

        dbProvider.execute((TransactionStatus status) -> {
            UnionPolicyObject unionPolicyObject = new UnionPolicyObject();
            if(callBack != null){
                unionPolicyObject = this.originPolicyAlgorithm(cmd);
            }else{
                unionPolicyObject = callBack.policyProcess(cmd);
            }

            //4.保存
            UnionPolicyObject finalUnionPolicyObject = unionPolicyObject; //拷贝变量

            this.coordinationProvider.getNamedLock(CoordinationLocks.UNION_GROUP_LOCK.getCode()).enter(() -> {

                //--------------------------1.保存配置表--------------------------
                if (finalUnionPolicyObject.getConfigureList().size() > 0) {
                    finalUnionPolicyObject.getConfigureList().stream().map(r -> {
                        this.uniongroupConfigureProvider.createUniongroupConfigures(r);
                        //2保存关系表
                        return null;
                    }).collect(Collectors.toList());
                }
                if (finalUnionPolicyObject.getUnionDetailsList().size() > 0) {
                    //--------------------------2.保存关系表--------------------------
                    this.uniongroupConfigureProvider.deleteUniongroupMemberDetailsByDetailIds(new ArrayList(finalUnionPolicyObject.getDetailIds()), cmd.getGroupType(), versionCode);
                    //后保存
                    this.uniongroupConfigureProvider.batchCreateUniongroupMemberDetail(finalUnionPolicyObject.getUnionDetailsList());
                }

                return null;
            });
            return null;
        });


        //5.同步搜索引擎
        this.uniongroupSearcher.deleteAll();
        this.uniongroupSearcher.syncUniongroupDetailsAtOrg(checkOrganization(cmd.getEnterpriseId()), cmd.getGroupType());
        this.uniongroupSearcher.refresh();
    }

    @Override
    public List<UniongroupConfiguresDTO> getConfiguresListByGroupId(GetUniongroupConfiguresCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<UniongroupConfigures> configures = this.uniongroupConfigureProvider.listUniongroupConfiguresByGroupId(namespaceId, cmd.getGroupId());
        if (configures != null) {
            return configures.stream().map(r -> {
                return ConvertHelper.convert(r, UniongroupConfiguresDTO.class);
            }).collect(Collectors.toList());
        }
        return null;
    }

    //  将 pojo 转换为 DTO 对象
    private UniongroupMemberDetailsDTO convertUniongroupMemberToDTO(UniongroupMemberDetail detail){
        UniongroupMemberDetailsDTO dto = ConvertHelper.convert(detail,UniongroupMemberDetailsDTO.class);
        String departments = "";
        String jobPositions = "";
        if(detail.getDepartment()!=null){
            for(Long key : detail.getDepartment().keySet()){
                departments += detail.getDepartment().get(key) + ",";
            }
            departments = departments.substring(0,departments.length()-1);
        }
        if(detail.getJobPosition()!=null){
            for(Long key : detail.getJobPosition().keySet()){
                jobPositions += detail.getJobPosition().get(key) + ",";
            }
            jobPositions = jobPositions.substring(0,jobPositions.length()-1);
        }
        dto.setDepartment(departments);
        dto.setJobposition(jobPositions);
        return dto;
    }

    @Override
    public List<UniongroupMemberDetailsDTO> listUniongroupMemberDetailsByGroupId(ListUniongroupMemberDetailsCommand cmd) {

        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<UniongroupMemberDetail> details = this.uniongroupConfigureProvider.listUniongroupMemberDetail(namespaceId, cmd.getGroupId(), cmd.getOwnerId());
        if (details != null) {
            return details.stream().map(r -> {
                UniongroupMemberDetailsDTO dto = convertUniongroupMemberToDTO(r);
                return dto;
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<UniongroupMemberDetailsDTO> listUniongroupMemberDetailsByGroupId(Long groupId) {

        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<UniongroupMemberDetail> details = this.uniongroupConfigureProvider.listUniongroupMemberDetail(groupId);
        if (details != null) {
            return details.stream().map(r -> {
                UniongroupMemberDetailsDTO dto = convertUniongroupMemberToDTO(r);
                return dto;
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void deleteUniongroupConfigures(UniongroupConfigures uniongroupConfigure) {
        this.uniongroupConfigureProvider.deleteUniongroupConfigres(uniongroupConfigure);
    }

    @Override
    public ListUniongroupMemberDetailResponse listUniongroupMemberDetailsWithCondition(ListUniongroupMemberDetailsWithConditionCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        SearchUniongroupDetailCommand search_cmd = new SearchUniongroupDetailCommand();

        if (cmd.getDepartmentId() != null)
            search_cmd.setDepartmentId(cmd.getDepartmentId());
        search_cmd.setEnterpriseId(cmd.getOwnerId());
        if (cmd.getKeywords() != null)
            search_cmd.setKeyword(cmd.getKeywords());
        if (cmd.getGroupId() != null)
            search_cmd.setGroupId(cmd.getGroupId());
        if(cmd.getIsNormal() !=null )
            search_cmd.setIsNormal(String.valueOf(cmd.getIsNormal()));

        search_cmd.setNamespaceId(namespaceId);
        search_cmd.setPageAnchor(cmd.getPageAnchor());
        search_cmd.setPageSize(cmd.getPageSize());
        List<UniongroupMemberDetail> list = uniongroupSearcher.query(search_cmd);


        ListUniongroupMemberDetailResponse response = new ListUniongroupMemberDetailResponse();

        if (list != null && list.size() > 0) {
            if (search_cmd.getPageAnchor().longValue() != 0L)
                response.setPageAnchor(search_cmd.getPageAnchor());
            response.setPageSize(search_cmd.getPageSize());
            response.setUniongroupMemberDetailList(list);
            return response;
        }
        return null;
    }

    @Override
    public List listDetailNotInUniongroup(ListDetailsNotInUniongroupsCommand cmd) {
        return this.uniongroupConfigureProvider.listDetailNotInUniongroup(cmd.getNamespaceId(), cmd.getOrganizaitonId());
    }

    private Organization checkOrganization(Long orgId) {
        Organization org = organizationProvider.findOrganizationById(orgId);
        if (org == null) {
            LOGGER.error("Unable to find the organization.organizationId=" + orgId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unable to find the organization.");
        }
        return org;
    }

    private List<String> checkUnderOrganizationsPathAtConfigures(String groupPath, List<Organization> orgs) {
        List<String> underOrgPaths = new ArrayList<>();
        orgs.stream().map(r -> {
            //本次保存的groupPath在该org的path中，证明该本次保存部门是该org的父部门
            if (r.getPath().indexOf(groupPath) > -1 && !r.getPath().equals(groupPath)) {
                underOrgPaths.add(r.getPath());
            }
            return null;
        }).collect(Collectors.toList());
        return underOrgPaths;
    }

    private List<Long> checkUnderOrganizationIdsAtConfigures(String groupPath, List<Organization> orgs) {
        List<Long> underOrgIds = new ArrayList<>();
        orgs.stream().map(r -> {
            //本次保存的groupPath在该org的path中，证明该本次保存部门是该org的父部门
            if (r.getPath().indexOf(groupPath) > -1 && !r.getPath().equals(groupPath)) {
                underOrgIds.add(r.getId());
            }
            return null;
        }).collect(Collectors.toList());
        return underOrgIds;
    }

    @Override
    public Integer countUnionGroupMemberDetailsByOrgId(Integer namespaceId, Long ownerId) {
        return this.uniongroupConfigureProvider.countUnionGroupMemberDetailsByOrgId(namespaceId, ownerId);
    }

    @Override
    public List<Object[]> listUniongroupMemberCount(Integer namespaceId, List<Long> groupIds, Long ownerId) {
        return this.uniongroupConfigureProvider.listUniongroupMemberDetailsCount(namespaceId, groupIds, ownerId);
    }

    @Override
    public List<Object[]> listUniongroupMemberDetailsInfo(Integer namespaceId, Long salaryGroupIds, Long ownerId) {
        return this.uniongroupConfigureProvider.listUniongroupMemberDetailsInfo(namespaceId, salaryGroupIds, ownerId);
    }

    @Override
    public void deleteUniongroupConfigresByGroupId(Long groupId, Long organizationId) {
        this.uniongroupConfigureProvider.deleteUniongroupConfigresByGroupId(groupId, organizationId);
    }

    @Override
    public void deleteUniongroupMemberDetailByGroupId(Long groupId, Long organizationId) {
        this.uniongroupConfigureProvider.deleteUniongroupMemberDetailByGroupId(groupId, organizationId);
    }

    @Override
    public List<Object[]> listUniongroupMemberGroupIds(Integer namespaceId, Long ownerId) {
        return this.uniongroupConfigureProvider.listUniongroupMemberGroupIds(namespaceId, ownerId);
    }


    @Override
    public void reallocatedUnion(Long enterpriseId, List<Long> departmentIds, OrganizationMember organizationMember) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        //根据层级关系将departmentIds排序
        Organization organization = checkOrganization(enterpriseId);
        List<Organization> departments = this.organizationProvider.listOrganizationsByIds(departmentIds);
        //按层级退化
//        Collections.sort(departments, new Comparator<Organization>() {
//            @Override
//            public int compare(Organization o1, Organization o2) {
//                if(o1.getPath().split("/").length > o2.getPath().split("/").length){
//                    return -1;
//                }
//                return 1;
//            }
//        });
        Long groupId = 0L;
        String groupType = "";
        /**判断departmentIds是否已经被分配薪酬组**/
        for (int i = departmentIds.size() - 1; i >= 0; i--) {
            UniongroupConfigures uniongroupConfigures = this.uniongroupConfigureProvider.findUniongroupConfiguresByCurrentId(namespaceId, departmentIds.get(i), null, DEFAULT_VERSION_CODE);
            if (uniongroupConfigures != null) {
                groupId = uniongroupConfigures.getGroupId();
                groupType = uniongroupConfigures.getGroupType();
                break;
            }
        }

        /**如果没有查询到被分配的薪酬组,则向上遍历**/
        if (groupId == 0) {
            //1 进化成独立无包含关系的集合
            List<Organization> departs_one = new ArrayList<>();
            List<String> pathList = new ArrayList<>();
            departments.stream().map(r -> {
                if (!checkMaxPathOrganizationList(r.getPath(), pathList)) {
                    pathList.add(r.getPath());
                    departs_one.add(r);
                }
                return null;
            }).collect(Collectors.toList());

            //2 从该集合最后一项开始找起，直到找到应有的薪酬组
            UniongroupConfigures unc = null;
            for (int i = departs_one.size() - 1; i >= 0; i--) {
                Organization _org = departs_one.get(i);
                while (unc == null) {
                    //判断是否在配置表中
                    unc = this.uniongroupConfigureProvider.findUniongroupConfiguresByCurrentId(namespaceId, _org.getId(), null, DEFAULT_VERSION_CODE);
                    _org = this.organizationProvider.findOrganizationById(_org.getParentId());
                    if (_org == null || unc != null) {
                        break;
                    }
                }
                if (unc != null) {
                    break;
                }
            }
            //3 如果循环后找到薪酬组
            if (unc != null) {
                groupId = unc.getGroupId();
                groupType = unc.getGroupType();
            }
        }

        //找到可以使用的薪酬组id
        if (groupId != 0) {
            EhUniongroupMemberDetails uniongroupMemberDetails = new EhUniongroupMemberDetails();
            uniongroupMemberDetails.setGroupId(groupId);
            uniongroupMemberDetails.setGroupType(groupType);
            uniongroupMemberDetails.setDetailId(organizationMember.getDetailId());
            uniongroupMemberDetails.setEnterpriseId(enterpriseId);
            uniongroupMemberDetails.setTargetType(organizationMember.getTargetType());
            uniongroupMemberDetails.setTargetId(organizationMember.getTargetId());
            uniongroupMemberDetails.setNamespaceId(namespaceId);
            uniongroupMemberDetails.setContactName(organizationMember.getContactName());
            uniongroupMemberDetails.setContactToken(organizationMember.getContactToken());

            String finalGroupType = groupType;
            dbProvider.execute((TransactionStatus status) -> {
                //先删除
                this.uniongroupConfigureProvider.deleteUniongroupMemberDetailsByDetailIds(Collections.singletonList(organizationMember.getDetailId()), finalGroupType);
                //后保存
                this.uniongroupConfigureProvider.batchCreateUniongroupMemberDetail(Collections.singletonList(uniongroupMemberDetails));
                return null;
            });
        }
    }

    @Override
    public void syncUniongroupAfterLeaveTheJob(Long detailId) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        //1.删除配置表和关系表中的数据(删除时应该删除所有薪酬组的数据)
        this.uniongroupConfigureProvider.deleteUniongroupConfigresByCurrentIdAndGroupTypeAndVersion(detailId, null, null);
        List<UniongroupMemberDetail> uds = this.uniongroupConfigureProvider.findUniongroupMemberDetailByDetailIdWithoutGroupType(namespaceId, detailId);
        this.uniongroupConfigureProvider.deleteUniongroupMemberDetailsByDetailIds(Collections.singletonList(detailId), null, null);
        //2.删除搜索引擎中的失效索引
        uds.forEach(r -> {
            this.uniongroupSearcher.deleteById(r.getId());
        });
    }

    @Override
    public void distributionUniongroupToDetail(Long organiztionId, Long detailId, Long groupId){
        this.distributionUniongroupToDetail(organiztionId, detailId, groupId, DEFAULT_VERSION_CODE);
    }

    @Override
    public void distributionUniongroupToDetail(Long organiztionId, Long detailId, Long groupId, Integer versionCode) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        dbProvider.execute((TransactionStatus status) -> {
            //获取策略组的信息
            Organization policyOrg = this.organizationProvider.findOrganizationById(groupId);
            String groupType = policyOrg.getGroupType();
            OrganizationMemberDetails memberDetail = this.organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
            //配置表
            UniongroupConfigures unc = this.uniongroupConfigureProvider.findUniongroupConfiguresByCurrentId(memberDetail.getNamespaceId(), detailId, groupType, versionCode);
            if (unc != null)
                this.uniongroupConfigureProvider.deleteUniongroupConfigres(unc);
            UniongroupConfigures uc = new UniongroupConfigures();
            uc.setNamespaceId(memberDetail.getNamespaceId());
            uc.setEnterpriseId(organiztionId);
            uc.setGroupType(groupType);
            uc.setGroupId(groupId);
            uc.setCurrentType(UniongroupTargetType.MEMBERDETAIL.getCode());
            uc.setCurrentId(detailId);
            uc.setCurrentName(memberDetail.getContactName());
            uc.setVersionCode(versionCode);
            this.uniongroupConfigureProvider.createUniongroupConfigures(uc);

            //关系表
            UniongroupMemberDetail old_detail = this.uniongroupConfigureProvider.findUniongroupMemberDetailByDetailId(namespaceId, detailId, groupType, versionCode);
            this.uniongroupConfigureProvider.deleteUniongroupMemberDetails(old_detail);
//            this.uniongroupConfigureProvider.deleteUniongroupMemberDetailsByDetailIds(Collections.singletonList(detailId));
            UniongroupMemberDetail uniongroupMemberDetails = new UniongroupMemberDetail();
            uniongroupMemberDetails.setGroupId(groupId);
            uniongroupMemberDetails.setGroupType(groupType);
            uniongroupMemberDetails.setDetailId(detailId);
            uniongroupMemberDetails.setEnterpriseId(organiztionId);
            uniongroupMemberDetails.setTargetType(memberDetail.getTargetType());
            uniongroupMemberDetails.setTargetId(memberDetail.getTargetId());
            uniongroupMemberDetails.setNamespaceId(memberDetail.getNamespaceId());
            uniongroupMemberDetails.setContactName(memberDetail.getContactName());
            uniongroupMemberDetails.setContactToken(memberDetail.getContactToken());
            uniongroupMemberDetails.setVersionCode(versionCode);
            this.uniongroupConfigureProvider.createUniongroupMemberDetail(uniongroupMemberDetails);


            //同步搜索引擎
            if (old_detail != null)
                this.uniongroupSearcher.deleteById(old_detail.getId());
            this.uniongroupSearcher.feedDoc(uniongroupMemberDetails);
            return null;
        });
    }

    /**
     * 校验path是否被pathList中的任意项包含
     **/
    private boolean checkMaxPathOrganizationList(String path, List<String> pathList) {
        if (pathList.size() > 0) {
            for (String pl : pathList) {
                //如果该path已经被包含
                if (pl.indexOf(path) > -1) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public UniongroupMemberDetailsDTO findUniongroupMemberDetailByDetailId(Integer namespaceId, Long detailId, String groupType) {
        //  查找用户
        UniongroupMemberDetail detail = this.uniongroupConfigureProvider.findUniongroupMemberDetailByDetailId(namespaceId, detailId, groupType);

        //  转换对象
        if (detail != null) {
            UniongroupMemberDetailsDTO dto = convertUniongroupMemberToDTO(detail);
            return dto;
        } else
            return null;
    }

	@Override
	public Object distributionUniongroupToDetail(DistributionUniongroupToDetailCommand cmd) {
		if(null != cmd.getDetailIds()){
			for(Long detailId : cmd.getDetailIds()){
				distributionUniongroupToDetail(cmd.getOwnerId(), detailId, cmd.getOrganizationGroupId());
			}
		}
		return null;
	}

	//策略算法一：源初算法
	private UnionPolicyObject originPolicyAlgorithm(SaveUniongroupConfiguresCommand cmd){
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Integer versionCode = cmd.getVersionCode() != null ? cmd.getVersionCode() : DEFAULT_VERSION_CODE;

        /**刪除本次指定的groupId的配置表信息和关系表信息**/
        this.uniongroupConfigureProvider.deleteUniongroupConfigresByGroupId(cmd.getGroupId(), cmd.getEnterpriseId(), versionCode);
        this.uniongroupConfigureProvider.deleteUniongroupMemberDetailByGroupId(cmd.getGroupId(), cmd.getEnterpriseId(), versionCode);

        /**处理配置表**/
        UniongroupType uniongroupType = UniongroupType.fromCode(cmd.getGroupType());
        List<UniongroupConfigures> configureList = new ArrayList<>();
        List<UniongroupTarget> targets = cmd.getTargets();
        if (targets != null) {
            targets.stream().filter(r -> {
                return r.getId() != null && r.getType() != null;
            }).map(r -> {
                //------------------------------重复项过滤规则：后更新的规则覆盖先更新的规则------------------------------
                UniongroupConfigures old_uc = this.uniongroupConfigureProvider.findUniongroupConfiguresByCurrentId(namespaceId, r.getId(), cmd.getGroupType(), versionCode);
                if (old_uc != null) {
                    //如果有重复的配置项，则删除前一个配置项
                    this.uniongroupConfigureProvider.deleteUniongroupConfigres(old_uc);
                }
                UniongroupConfigures uc = new UniongroupConfigures();
                uc.setNamespaceId(namespaceId);
                uc.setEnterpriseId(cmd.getEnterpriseId());
                uc.setGroupType(uniongroupType.getCode());
                uc.setGroupId(cmd.getGroupId());
                uc.setCurrentType(UniongroupTargetType.fromCode(r.getType()).getCode());
                uc.setCurrentId(r.getId());
                uc.setCurrentName(r.getName());
                uc.setVersionCode(versionCode);
                configureList.add(uc);
                return null;
            }).collect(Collectors.toList());
        }

        /**处理关系表**/
        //1.查询本次保存中所有勾选部门的所有人员detailId集合detailIds
        List<Long> orgIds = cmd.getTargets().stream().filter((r) -> {
            //控制organization只能是部门
            return r.getType().equals(UniongroupTargetType.ORGANIZATION.getCode()) && (this.organizationProvider.findOrganizationById(r.getId()).getGroupType() == OrganizationGroupType.DEPARTMENT.getCode());
        }).map((r) -> {
            return r.getId();
        }).collect(Collectors.toList());
        //------------------------------包含关系过滤规则：小范围规则覆盖大范围规则------------------------------


        //已存在（即已分配薪酬组的）的部门集合
        List<Long> old_ids = this.uniongroupConfigureProvider.listOrgCurrentIdsOfUniongroupConfigures(namespaceId, cmd.getEnterpriseId(), cmd.getGroupType(), versionCode);
        List<Organization> old_orgs = this.organizationProvider.listOrganizationsByIds(old_ids);
        //找出已存在（即已分配薪酬组的）的个人
        List<Long> old_detail_ids = this.uniongroupConfigureProvider.listDetailCurrentIdsOfUniongroupConfigures(namespaceId, cmd.getEnterpriseId(), cmd.getGroupType(), versionCode);

//        //重新获取已存在（即已分配薪酬组的）的部门集合
//        old_ids.clear();
//        old_ids.addAll(this.uniongroupConfigureProvider.listOrgCurrentIdsOfUniongroupConfigures(namespaceId, cmd.getEnterpriseId()));
//        old_orgs.clear();
//        old_orgs.addAll(this.organizationProvider.listOrganizationsByIds(old_ids));

        //获得每个org的groupPath，并找寻是否存在其子部门的记录
        Set<Long> detailIds = new HashSet<>();
        orgIds.stream().map(r -> {
            if (null == r) {
                return null;
            }
            Organization org = checkOrganization(r);
            if (org != null) {
                //对每一个已存在的部门找到其下的子部门集合
                List<String> underOrgPaths = checkUnderOrganizationsPathAtConfigures(org.getPath(), old_orgs);
                //如果存在子部门
                if (underOrgPaths.size() > 0) {
                    //找到在部门下但并不在这些子部门下（即已分配薪酬组的）的人
                    Set<Long> memberIds = this.organizationProvider.listMemberDetailIdWithExclude(namespaceId, org.getPath(), underOrgPaths);
                    if (memberIds == null) {
                        LOGGER.error("memberIds is not found。namespaceId = {}, orgPath = {}", namespaceId, org.getPath());
//                        throw RuntimeErrorException.errorWith(UniongroupErrorCode.SCOPE, UniongroupErrorCode.ERROR_INVALID_PARAMETER,
//                                "memberIds is not found。");
                    }
                    //去掉配置表中单独勾选的人员id
                    memberIds.removeAll(old_detail_ids);

                    detailIds.addAll(memberIds);
                } else {//如果不存在子部门,则找到这个部门下的人
                    Set<Long> memberIds = this.organizationProvider.listMemberDetailIdWithExclude(namespaceId, org.getPath(), null);
                    if (memberIds == null) {
                        LOGGER.error("memberIds is not found。namespaceId = {}, orgPath = {}", namespaceId, org.getPath());
//                        throw RuntimeErrorException.errorWith(UniongroupErrorCode.SCOPE, UniongroupErrorCode.ERROR_INVALID_PARAMETER,
//                                "memberIds is not found。");
                    }
                    //去掉配置表中单独勾选的人员id
                    if (null != old_detail_ids) {
                        memberIds.removeAll(old_detail_ids);
                    }

                    detailIds.addAll(memberIds);
                }
            }
            return null;
        }).collect(Collectors.toList());

        //2.查询本次保存中所有勾选个人加入集合detailIds
        cmd.getTargets().stream().filter((r) -> {
            return r.getType().equals(UniongroupTargetType.MEMBERDETAIL.getCode());
        }).map((r) -> {
            detailIds.add(r.getId());
            return null;
        }).collect(Collectors.toList());

        //3.组装uniongroupMemberDetail对象
        List<EhUniongroupMemberDetails> unionDetailsList = new ArrayList<>();
        detailIds.stream().map(r -> {
            OrganizationMemberDetails detail = this.organizationProvider.findOrganizationMemberDetailsByDetailId(r);
            if (detail != null) {
                EhUniongroupMemberDetails uniongroupMemberDetails = new EhUniongroupMemberDetails();
                uniongroupMemberDetails.setGroupId(cmd.getGroupId());
                uniongroupMemberDetails.setGroupType(uniongroupType.getCode());
                uniongroupMemberDetails.setDetailId(r);
                uniongroupMemberDetails.setEnterpriseId(cmd.getEnterpriseId());
                uniongroupMemberDetails.setTargetType(detail.getTargetType());
                uniongroupMemberDetails.setTargetId(detail.getTargetId());
                uniongroupMemberDetails.setNamespaceId(detail.getNamespaceId());
                uniongroupMemberDetails.setContactName(detail.getContactName());
                uniongroupMemberDetails.setContactToken(detail.getContactToken());
                uniongroupMemberDetails.setVersionCode(versionCode);
                unionDetailsList.add(uniongroupMemberDetails);
            }
            return null;
        }).collect(Collectors.toList());

        UnionPolicyObject unionPolicyObject = new UnionPolicyObject();
        if(configureList != null && configureList.size() > 0)
            unionPolicyObject.setConfigureList(configureList);
        if(unionDetailsList != null && unionDetailsList.size() > 0)
            unionPolicyObject.setUnionDetailsList(unionDetailsList);
        if(detailIds != null && detailIds.size() > 0)
            unionPolicyObject.setDetailIds(detailIds);

        return unionPolicyObject;
    }

}
