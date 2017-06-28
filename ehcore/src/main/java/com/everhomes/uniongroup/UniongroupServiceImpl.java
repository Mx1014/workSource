// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.*;
import com.everhomes.rest.uniongroup.SaveUniongroupConfiguresCommand;
import com.everhomes.rest.uniongroup.UniongroupTarget;
import com.everhomes.rest.uniongroup.UniongroupTargetType;
import com.everhomes.rest.uniongroup.UniongroupType;
import com.everhomes.server.schema.tables.pojos.EhUniongroupMemberDetails;
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
        UniongroupType uniongroupType = UniongroupType.fromCode(cmd.getGroupType());
        List<UniongroupConfigures> configureList = new ArrayList<>();
        List<UniongroupTarget> targets = cmd.getTargets();
        if (targets != null) {
            targets.stream().map(r -> {
                UniongroupConfigures uc = new UniongroupConfigures();
                uc.setGroupType(uniongroupType.getCode());
                uc.setGroupid(cmd.getGroupId());
                uc.setTargetType(UniongroupTargetType.fromCode(r.getType()).getCode());
                uc.setTargetid(r.getId());
                configureList.add(uc);
                return null;
            }).collect(Collectors.toList());
        }

        /**重复项过滤规则：后更新的规则覆盖先更新的规则**/

        /**包含关系过滤规则：小范围规则覆盖大范围规则**/


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
        List<OrganizationMember> organizationMembers = this.organizationProvider.listOrganizationMemberByOrganizationIds(new CrossShardListingLocator(), 1000000, null, orgIds);
        Set<Long> detailIds = new HashSet<>();
        organizationMembers.stream().map(r -> {
            detailIds.add(r.getDetailId());
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
            if(detail != null){
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
                this.uniongroupConfigureProvider.batchCreateUniongroupMemberDetail(unionDetailsList);
                return null;
            });
        }
    }

    @Override
    public List getConfiguresListByGroupId(Long groupId) {
        return this.uniongroupConfigureProvider.listUniongroupConfiguresByGroupId(groupId);
    }

    @Override
    public void deleteUniongroupConfigures(UniongroupConfigures uniongroupConfigure) {
        this.uniongroupConfigureProvider.deleteUniongroupConfigres(uniongroupConfigure);
    }
}