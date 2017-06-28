// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.uniongroup.SaveUniongroupConfiguresCommand;
import com.everhomes.rest.uniongroup.UniongroupTarget;
import com.everhomes.rest.uniongroup.UniongroupTargetType;
import com.everhomes.rest.uniongroup.UniongroupType;
import com.everhomes.server.schema.tables.pojos.EhUniongroupMemberDetails;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    @Override
    public void saveUniongroupConfigures(SaveUniongroupConfiguresCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
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
                uc.setGroupType(uniongroupType.getCode());
                uc.setGroupid(cmd.getGroupId());
                uc.setTargetType(UniongroupTargetType.fromCode(r.getType()).getCode());
                uc.setTargetid(r.getId());
                configureList.add(uc);
                return null;
            }).collect(Collectors.toList());
        }

        /**保存配置表**/
        if (configureList.size() > 0) {
            dbProvider.execute((TransactionStatus status) -> {
                configureList.stream().map(r -> {
                    this.uniongroupConfigureProvider.createUniongroupConfigures(r);
                    return null;
                }).collect(Collectors.toList());
                return null;
            });
        }

        /**保存关系表**/
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
                //如果之前的记录存在子部门,则排除掉子部门含有的member的detailId
                detailIds.addAll(this.organizationProvider.listMemberDetailIdWithExclude(namespaceId, org.getPath(), underOrgPaths));
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
                uniongroupMemberDetails.setGroupid(cmd.getGroupId());
                uniongroupMemberDetails.setGroupType(uniongroupType.getCode());
                uniongroupMemberDetails.setDetailid(r);
                uniongroupMemberDetails.setOrganizationId(detail.getOrganizationId());
                uniongroupMemberDetails.setTargetType(detail.getTargetType());
                uniongroupMemberDetails.setTargetId(detail.getTargetId());
                uniongroupMemberDetails.setNamespaceId(detail.getNamespaceId());
                uniongroupMemberDetails.setContactName(detail.getContactName());
                uniongroupMemberDetails.setContactToken(detail.getContactToken());
                unionDetailsList.add(uniongroupMemberDetails);
            }
            return null;
        }).collect(Collectors.toList());
        //4.保存关系表
        if (unionDetailsList.size() > 0) {
            dbProvider.execute((TransactionStatus status) -> {
                //先删除
                this.uniongroupConfigureProvider.deleteUniongroupMemberDetailsByDetailIds(namespaceId, new ArrayList(detailIds));
                //后保存
                this.uniongroupConfigureProvider.batchCreateUniongroupMemberDetail(unionDetailsList);
                return null;
            });
        }
    }

    @Override
    public List getConfiguresListByGroupId(Long groupId) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        return this.uniongroupConfigureProvider.listUniongroupConfiguresByGroupId(namespaceId, groupId);
    }

    @Override
    public List getUniongroupMemberDetailsByGroupId(Long groupId) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        return this.uniongroupConfigureProvider.listUniongroupMemberDetail(namespaceId,groupId);
    }

    @Override
    public void deleteUniongroupConfigures(UniongroupConfigures uniongroupConfigure) {
        this.uniongroupConfigureProvider.deleteUniongroupConfigres(uniongroupConfigure);
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