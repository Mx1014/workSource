// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
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
    private OrganizationProvider organizationProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private UniongroupSearcher uniongroupSearcher;

    @Override
    public void saveUniongroupConfigures(SaveUniongroupConfiguresCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        /**处理配置表**/
        UniongroupType uniongroupType = UniongroupType.fromCode(cmd.getGroupType());
        List<UniongroupConfigures> configureList = new ArrayList<>();
        List<UniongroupTarget> targets = cmd.getTargets();
        if (targets != null) {
            targets.stream().map(r -> {
                //------------------------------重复项过滤规则：后更新的规则覆盖先更新的规则------------------------------
                UniongroupConfigures old_uc = this.uniongroupConfigureProvider.findUniongroupConfiguresByTargetId(namespaceId, r.getId());
                if (old_uc != null) {
                    this.uniongroupConfigureProvider.deleteUniongroupConfigres(old_uc);
                }
                UniongroupConfigures uc = new UniongroupConfigures();
                uc.setNamespaceId(namespaceId);
                uc.setEnterpriseId(cmd.getEnterpriseId());
                uc.setGroupType(uniongroupType.getCode());
                uc.setGroupId(cmd.getGroupId());
                uc.setTargetType(UniongroupTargetType.fromCode(r.getType()).getCode());
                uc.setTargetId(r.getId());
                configureList.add(uc);
                return null;
            }).collect(Collectors.toList());
        }

        /**处理关系表**/
        //1.查询本次保存中所有勾选部门的所有人员detailId集合detailIds
        List<Long> orgIds = cmd.getTargets().stream().filter((r) -> {
            return r.getType().equals(UniongroupTargetType.fromCode("ORGANIZATION").getCode());
        }).map((r) -> {
            return r.getId();
        }).collect(Collectors.toList());
        //------------------------------包含关系过滤规则：小范围规则覆盖大范围规则------------------------------

        List<Long> old_ids = this.uniongroupConfigureProvider.listOrgTargetIdsOfUniongroupConfigures(namespaceId);
        List<Organization> old_orgs = this.organizationProvider.listOrganizationsByIds(old_ids);

        //获得每个org的groupPath，并找寻是否存在其子部门的记录
        Set<Long> detailIds = new HashSet<>();
        orgIds.stream().map(r -> {
            Organization org = checkOrganization(r);
            if (org != null) {
                List<String> underOrgPaths = checkUnderOrganizationsPathAsTargets(org.getPath(), old_orgs);
                //如果存在子部门
                if (underOrgPaths.size() > 0) {
                    Set<Long> memberIds = this.organizationProvider.listMemberDetailIdWithExclude(namespaceId, org.getPath(), underOrgPaths);
                    if (memberIds == null) {
                        LOGGER.error("memberIds is not found。namespaceId = {}, orgPath = {}", namespaceId, org.getPath());
                        throw RuntimeErrorException.errorWith(UniongroupErrorCode.SCOPE, UniongroupErrorCode.ERROR_INVALID_PARAMETER,
                                "memberIds is not found。");
                    }
                    detailIds.addAll(memberIds);
                } else {//如果不存在子部门
                    Set<Long> memberIds = this.organizationProvider.listMemberDetailIdWithExclude(namespaceId, org.getPath(), null);
                    if (memberIds == null) {
                        LOGGER.error("memberIds is not found。namespaceId = {}, orgPath = {}", namespaceId, org.getPath());
                        throw RuntimeErrorException.errorWith(UniongroupErrorCode.SCOPE, UniongroupErrorCode.ERROR_INVALID_PARAMETER,
                                "memberIds is not found。");
                    }
                    detailIds.addAll(memberIds);
                }
            }
            return null;
        }).collect(Collectors.toList());

        //2.查询本次保存中所有勾选个人加入集合detailIds
        cmd.getTargets().stream().filter((r) -> {
            return r.getType().equals(UniongroupTargetType.fromCode("MEMBERDETAIL").getCode());
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
                unionDetailsList.add(uniongroupMemberDetails);
            }
            return null;
        }).collect(Collectors.toList());

        //4.保存
        if (configureList.size() > 0 && unionDetailsList.size() > 0) {
            dbProvider.execute((TransactionStatus status) -> {
                //--------------------------1.保存配置表--------------------------
                configureList.stream().map(r -> {
                    this.uniongroupConfigureProvider.createUniongroupConfigures(r);
                    //2保存关系表
                    return null;
                }).collect(Collectors.toList());
                //--------------------------2.保存关系表--------------------------
                this.uniongroupConfigureProvider.deleteUniongroupMemberDetailsByDetailIds(namespaceId, new ArrayList(detailIds));
                //后保存
                this.uniongroupConfigureProvider.batchCreateUniongroupMemberDetail(unionDetailsList);
                return null;
            });
        }
    }

    @Override
    public List<UniongroupConfiguresDTO> getConfiguresListByGroupId(GetUniongroupConfiguresCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<UniongroupConfigures> configures = this.uniongroupConfigureProvider.listUniongroupConfiguresByGroupId(namespaceId, cmd.getGroupId());
        return configures.stream().map(r -> {
            return ConvertHelper.convert(r, UniongroupConfiguresDTO.class);
        }).collect(Collectors.toList());
    }

    @Override
    public List<UniongroupMemberDetailsDTO> listUniongroupMemberDetailsByGroupId(ListUniongroupMemberDetailsCommand cmd) {

        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<UniongroupMemberDetail> details = this.uniongroupConfigureProvider.listUniongroupMemberDetail(namespaceId, cmd.getGroupId(), cmd.getOwnerId());
        if (details != null) {
            return details.stream().map(r -> {
                return ConvertHelper.convert(r, UniongroupMemberDetailsDTO.class);
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void deleteUniongroupConfigures(UniongroupConfigures uniongroupConfigure) {
        this.uniongroupConfigureProvider.deleteUniongroupConfigres(uniongroupConfigure);
    }

    @Override
    public List listUniongroupMemberDetailsWithCondition(ListUniongroupMemberDetailsWithConditionCommand cmd) {
//        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Integer namespaceId = 1000000;

        SearchUniongroupDetailCommand search_cmd = new SearchUniongroupDetailCommand();
        search_cmd.setNamespaceId(namespaceId);
        search_cmd.setDepartmentId(cmd.getDepartmentId());
        search_cmd.setEnterpriseId(cmd.getOwnerId());
        search_cmd.setKeyword(cmd.getKeywords());
        uniongroupSearcher.query(search_cmd);


        List<UniongroupMemberDetail> details = this.uniongroupConfigureProvider.listUniongroupMemberDetailByGroupType(namespaceId, cmd.getOwnerId(), cmd.getGroupId(), UniongroupType.fromCode(cmd.getGroupType()).getCode());
        //查询部门和岗位
        for (UniongroupMemberDetail detail : details) {
            Map depart_map = this.organizationProvider.listOrganizationsOfDetail(namespaceId, detail.getDetailId(), OrganizationGroupType.DEPARTMENT.getCode());
            if (depart_map != null)
                detail.setDepartment(depart_map);
            Map jobp_map = this.organizationProvider.listOrganizationsOfDetail(namespaceId, detail.getDetailId(), OrganizationGroupType.JOB_POSITION.getCode());
            if (jobp_map != null) {
                detail.setJob_position(jobp_map);
            }
        }
        uniongroupSearcher.bulkUpdate(details);
        return details;
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

    private List<String> checkUnderOrganizationsPathAsTargets(String groupPath, List<Organization> orgs) {
        List<String> underOrgPaths = new ArrayList<>();
        orgs.stream().map(r -> {
            //本次保存的groupPath在该org的path中，证明该本次保存部门是该org的父部门
            if (r.getPath().indexOf(groupPath) > 0) {
                underOrgPaths.add(r.getPath());
            }
            return null;
        }).collect(Collectors.toList());
        return underOrgPaths;
    }
}
