package com.everhomes.rest.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.general_approval.PostApprovalFormItem;

/**
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>communityId: 园区id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/17.
 */
public class AddContractTemplateCommand {
	
	private Long id;
	private Integer namespaceId;
	private Long communityId;
	private String name;
	private Byte contracttemplateType;
	private Long categoryId;
    private Long orgId;
	
	
	

}
