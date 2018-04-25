package com.everhomes.rest.enterpriseApproval;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralApprovalScopeMapDTO;

import java.util.List;

/**
 * <ul>
 * <li>approvalId : 审批id</li>
 * <li>approvalGroupId : 审批所属组id</li>
 * <li>approvalName : 审批名称</li>
 * <li>approvalRemark : 审批备注</li>
 * <li>scopes : 可见范围 参考{@link com.everhomes.rest.general_approval.GeneralApprovalScopeMapDTO}</li>
 * </ul>
 * @author ryan
 *
 */
public class UpdateEnterpriseApprovalCommand {

    private Long approvalId;

    private Long approvalGroupId;

    private	String approvalName;

    private String approvalRemark;

    @ItemType(GeneralApprovalScopeMapDTO.class)
    private List<GeneralApprovalScopeMapDTO> scopes;
}
